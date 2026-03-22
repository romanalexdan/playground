package rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.example.models.Kyc;
import org.example.models.KycStatus;
import org.example.models.Transaction;
import org.example.models.TransactionResponse;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Slf4j
public class GetApiTest extends BaseAPITest {

    private final List<Transaction> firstRequestsTransactions = new ArrayList<>();

    @BeforeTest
    public void testDataSetup() {
        firstRequestsTransactions.add(Transaction.builder().id("tx_1").amount(new BigDecimal("100.05")).build());
        firstRequestsTransactions.add(Transaction.builder().id("tx_2").amount(new BigDecimal("50.00")).build());
        log.info("Test data setup complete");
    }

    @Test
    public void test_GetFirstRequest() {
        final Response response = RestAssured.get("/v1/transactions?limit=2");
        log.info("Get transactions API call successful");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");

        final TransactionResponse transactions = response.getBody().as(TransactionResponse.class);
        log.info("Body extraction successful");

        Assert.assertFalse(transactions.nextCursor.isEmpty(), "Response should contain a next cursor.");
        Assert.assertEquals(firstRequestsTransactions.size(), transactions.data.size(), "Received number of transaction should match");
        Assert.assertTrue(firstRequestsTransactions.containsAll(transactions.data), "Received the expected data in transactions");
    }

    @Test
    public void test_GetWithNextCursor() {
        final Response response = RestAssured.get("/v1/transactions?limit=2");
        log.info("Get firt transactions API call successful");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");

        final TransactionResponse transactions = response.getBody().as(TransactionResponse.class);
        System.out.println(response.getBody().asString());

        log.info("Body extraction for first request successful");
        final Response secondResponse = RestAssured.get("/v1/transactions?limit=2&cursor="+transactions.nextCursor);

        Assert.assertEquals(secondResponse.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        Assert.assertFalse(secondResponse.getBody().asString().isEmpty(), "Should not return empty body");

        final TransactionResponse secondTransactions = secondResponse.getBody().as(TransactionResponse.class);
        log.info("Body extraction for second request successful");

        final Response lastResponse = RestAssured.get("/v1/transactions?limit=2&cursor="+secondTransactions.nextCursor);
        final TransactionResponse lastTransactions = lastResponse.getBody().as(TransactionResponse.class);
        System.out.println(lastTransactions.data);
        System.out.println(lastResponse.getBody().asString());
        Assert.assertEquals(lastResponse.statusCode(), HttpStatus.SC_OK, "Should get " + HttpStatus.SC_OK);
        Assert.assertTrue(lastTransactions.data.isEmpty(), "Should return empty body");
        Assert.assertTrue(lastTransactions.nextCursor.isEmpty(), "There should be no cursor");
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
                    return kycToWait.status == KycStatus.APPROVED;
                });
        log.info("Pooling successful");
    }
}
