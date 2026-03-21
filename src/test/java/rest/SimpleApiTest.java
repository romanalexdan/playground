package rest;

import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.example.models.Order;
import org.example.models.OrderStatus;
import org.example.models.Transaction;
import org.example.models.TransactionResponse;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.EnvConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Slf4j
public class SimpleApiTest {

    private final List<Transaction> expectedTransactions = new ArrayList<>();

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = EnvConfig.getBaseUrl();
        RestAssured.defaultParser = Parser.JSON;
        expectedTransactions.add(Transaction.builder().id("tx_1").amount(new BigDecimal("100.05")).build());
        expectedTransactions.add(Transaction.builder().id("tx_2").amount(new BigDecimal("50.00")).build());
        log.info("API setup complete");
    }

    @AfterClass
    public void reset() {
        RestAssured.post("/__admin/scenarios/reset").then().statusCode(200);
    }

    @Test
    public void test_Get() {
        final Response response = RestAssured.get("/v1/transactions?limit=2");
        log.info("Get API call successful");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        System.out.println(response.getBody().asString());
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");

        final TransactionResponse transactions = response.getBody().as(TransactionResponse.class);
        log.info("Body extraction successful");

        Assert.assertFalse(transactions.nextCursor.isEmpty(), "Response should contain a next cursor.");
        Assert.assertEquals(expectedTransactions.size(), transactions.data.size(), "Received number of transaction should match");
        Assert.assertTrue(expectedTransactions.containsAll(transactions.data), "Received the expected data in transactions");
    }

    @Test
    public void test_GetWithBackoff() {
        final String userId = "user_123";
        final Response response = RestAssured.get("/v1/kyc/status/" + userId);
        log.info("Post API executed");
        System.out.println(response.getBody().asString());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");

        final Order order = response.getBody().as(Order.class);
        Assert.assertEquals(order.status, OrderStatus.PENDING, "Order should be in pending state");
        Assert.assertEquals(order.userId, userId, "User id should match");

        await()
                .atMost(3500, TimeUnit.MILLISECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Order orderToWait = RestAssured.get("/v1/kyc/status/" + userId).getBody().as(Order.class);
                    System.out.println("Order status is: " + orderToWait.status);
                    return orderToWait.status.compareTo(OrderStatus.APPROVED) == 0;
                });
    }

    @Test
    public void test_PostWithRateLimit() {
        Response response = RestAssured.post("/v1/transfer");
        Assert.assertEquals(response.statusCode(),429,"Status code should be 429");
        Assert.assertEquals(response.getHeader("Retry-After"),"5","Should return 5 seconds");
    }
}
