package rest;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import util.EnvConfig;

@Slf4j
public class BaseAPITest {

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = EnvConfig.getBaseUrl();
        RestAssured.defaultParser = Parser.JSON;
        log.info("API setup complete");
    }

    @AfterMethod
    public void reset() {
        if (EnvConfig.getIsMockedAPI()) {
            log.info("Reset mock scenarios");
            RestAssured.post("/__admin/scenarios/reset").then().statusCode(200);
        }
    }
}
