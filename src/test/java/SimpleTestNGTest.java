import org.testng.Assert;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class SimpleTestNGTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTestNGTest.class);

    @BeforeClass
    public void beforeClass() {
        logger.info("In setup");
        System.out.println("Test setup");
    }

    @AfterClass
    public void afterClass() {
        logger.info("In cleanup");
        System.out.println("After class");
    }

    @BeforeTest
    public void beforeEach() {
        logger.info("Before each test");
        System.out.println("In before each");
    }

    @AfterTest
    public void afterEach() {
        logger.info("In after each");
        System.out.println("In after each");
    }

    @Test
    public void test() {
        logger.info("In test 1");
        int a = 4;
        int b = 5;
        int result = 9;
        Assert.assertEquals(a + b, result, "Test sum is "+ result);
    }

    @Test
    public void test2() {
        logger.info("In test 2");
        int a = 5;
        int b = 2;
        int result = 3;
        Assert.assertEquals(a - b, result, "Test difference is "+ result);
    }

    @Test
    public void test3() {
        logger.info("In test 3");
        BigDecimal a = new BigDecimal("10.02");
        BigDecimal b = new BigDecimal("10.020001");

        Assert.assertEquals(a.compareTo(b), -1, "Test values should be different.");
    }
}
