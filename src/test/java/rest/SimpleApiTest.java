package rest;

import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.example.models.Kyc;
import org.example.models.KycStatus;
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
        if (EnvConfig.getIsMockedAPI()) {
            log.info("Reset mock scenarios");
            RestAssured.post("/__admin/scenarios/reset").then().statusCode(200);
        }
    }

    @Test
    public void test_GetWithNextCursor() {
        final Response response = RestAssured.get("/v1/transactions?limit=2");
        log.info("Get transactions API call successful");

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
        log.info("Get kyc API executed");
        System.out.println(response.getBody().asString());
        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");

        final Kyc kyc = response.getBody().as(Kyc.class);
        Assert.assertEquals(kyc.status, KycStatus.PENDING, "Order should be in pending state");
        Assert.assertEquals(kyc.userId, userId, "User id should match");

        log.info("Pool for expected status");
        await()
                .atMost(3500, TimeUnit.MILLISECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Kyc kycToWait = RestAssured.get("/v1/kyc/status/" + userId).getBody().as(Kyc.class);
                    return kycToWait.status.compareTo(KycStatus.APPROVED) == 0;
                });
        log.info("Pooling successful");
    }

    @Test
    public void test_NegativePostWithRateLimit() {
        Response response = RestAssured.post("/v1/transfer");
        log.info("Post transfer API executed");
        Assert.assertEquals(response.statusCode(), 429, "Status code should be 429");
        Assert.assertEquals(response.getHeader("Retry-After"), "5", "Should return 5 seconds");
    }
}
