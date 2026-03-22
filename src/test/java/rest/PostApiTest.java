package rest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Transfer;
import org.example.models.TransferStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Slf4j
public class PostApiTest extends  BaseAPITest{

    @Test
    public void test_RecoverPostWithRateLimit() {
        Response response = RestAssured.post("/v1/transfer");
        log.info("Post first transfer API executed");
        Assert.assertEquals(response.statusCode(), 429, "Status code should be 429");
        Assert.assertEquals(response.getHeader("Retry-After"), "5", "Should return 5 seconds");

        int poolingTimeMs = Integer.parseInt(response.getHeader("Retry-After")) * 1000;

        log.info("Pool for expected status");
        await()
            .atMost(poolingTimeMs * 3, TimeUnit.MILLISECONDS)
            .pollDelay(poolingTimeMs, TimeUnit.MILLISECONDS)
            .pollInterval(1, TimeUnit.SECONDS)
            .until(() -> {
                Response currentResponse = RestAssured.post("/v1/transfer");

                System.out.println(currentResponse.getBody().asString());

                if (currentResponse.getStatusCode() == 200) {
                    Transfer transfer = currentResponse.as(Transfer.class);
                    return transfer.getStatus() == TransferStatus.SUCCESS;
                }

                return false; // Keep polling if we get another 429 or other error
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
