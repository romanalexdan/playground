import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.math.BigDecimal;

@Slf4j
public class SimpleTestNGTest {

    @BeforeClass
    public void beforeClass() {
        log.info("In setup");
        System.out.println("Test setup");
    }

    @AfterClass
    public void afterClass() {
        log.info("In cleanup");
        System.out.println("After class");
    }

    @BeforeTest
    public void beforeEach() {
        log.info("Before each test");
        System.out.println("In before each");
    }

    @AfterTest
    public void afterEach() {
        log.info("In after each");
        System.out.println("In after each");
    }

    @Test
    public void test() {
        log.info("In test 1");
        int a = 4;
        int b = 5;
        int result = 9;
        Assert.assertEquals(a + b, result, "Test sum is "+ result);
    }

    @Test
    public void test2() {
        log.info("In test 2");
        int a = 5;
        int b = 2;
        int result = 3;
        Assert.assertEquals(a - b, result, "Test difference is "+ result);
    }

    @Test
    public void test3() {
        log.info("In test 3");
        BigDecimal a = new BigDecimal("10.02");
        BigDecimal b = new BigDecimal("10.020001");

        Assert.assertEquals(a.compareTo(b), -1, "Test values should be different.");
    }
}
