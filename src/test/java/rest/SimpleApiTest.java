package rest;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Transaction;
import org.example.models.TransactionResponse;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.EnvConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SimpleApiTest {

    private final List<Transaction> expectedTransactions = new ArrayList<>();

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = EnvConfig.getBaseUrl();
        expectedTransactions.add(Transaction.builder()
                .id("tx_1")
                .amount(new BigDecimal("100.05"))
                .build());
        expectedTransactions.add(Transaction.builder()
                .id("tx_2")
                .amount(new BigDecimal("50.00"))
                .build());
        log.info("API setup complete");
    }

    @Test
    public void test_Get() {
        Response response = RestAssured.get("/v1/transactions?limit=2");
        log.info("Get API call successful");
        Assert.assertEquals(response.statusCode(),200,"Should get 200");
        System.out.println(response.getBody().asString());
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");

        TransactionResponse transactions = response.getBody().as(TransactionResponse.class);
        log.info("Body extraction successful");

        Assert.assertFalse(transactions.nextCursor.isEmpty(), "Response should contain a next cursor.");
        Assert.assertEquals(expectedTransactions.size(), transactions.data.size(), "Received number of transaction should match");
        Assert.assertTrue(expectedTransactions.containsAll(transactions.data), "Received the expected data in transactions");
    }
}
