package ui;

import com.codeborne.selenide.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

@Slf4j
public class SimpleUITest {

    @BeforeClass
    public void setup() {
        final String fileName = this.getClass().getSimpleName() + "_" + System.currentTimeMillis() + ".mp4";
        Configuration.remote = System.getProperty("remote.url", "http://localhost:4444/wd/hub");
        Configuration.browser = "chrome";
        Configuration.browserVersion = System.getProperty("browser.version", "120.0");
        Configuration.timeout = 8000;

        final Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", true);
        selenoidOptions.put("name", "SauceDemo Test - " + System.currentTimeMillis()); // Optional: Name your session
        selenoidOptions.put("videoName", fileName);

        final ChromeOptions options = new ChromeOptions();
        options.setCapability("selenoid:options", selenoidOptions);

        Configuration.browserCapabilities = options;
        log.info("Browser setup complete");
    }

    @Test
    public void test_AddAndRemoveFromCart(){
        final String base = System.getProperty("ui.base", "https://www.saucedemo.com/");
        final String userName ="standard_user";
        final String password="secret_sauce";

        log.info("Open application");
        open(base);
        $("[data-test='username']").setValue(userName);
        $("[data-test='password']").setValue(password);
        $("[data-test=\"login-button\"]").click();
        log.info("Successful login");

        Assert.assertTrue($(".app_logo").is(visible),"The logo is shown on the page after login");
        Assert.assertEquals($(".app_logo").text(),"Swag Labs","The header description should match");

        var element = $$("[data-test='inventory-item']").filterBy(text("Sauce Labs Backpack")).first();
        element.$("[data-test=\"add-to-cart-sauce-labs-backpack\"]").click();
        log.info("Backpack added to cart");

        Assert.assertEquals($("[data-test=\"shopping-cart-badge\"]").text(),"1","Should match");

        $("[data-test=\"shopping-cart-link\"]").click();
        $("[data-test=\"remove-sauce-labs-backpack\"]").click();
        log.info("Backpack removed from cart");

        Assert.assertEquals($("[id=\"shopping_cart_container\"]").text(),"","Should match");
    }
}
