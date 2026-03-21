import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SimpleApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = EnvConfig.getBaseUrl();
    }

    @Test
    public void test_Get() {
        Response response = RestAssured.get("/v1/transactions?limit=2");
        Assert.assertEquals(response.statusCode(),200,"Should get 200");
        Assert.assertFalse(response.getBody().asString().isEmpty(), "Should not return empty body");
    }
}
