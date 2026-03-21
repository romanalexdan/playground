import org.testng.Assert;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Assert.assertEquals(4 + 4, 8, "Test sum is 8");
    }

    @Test
    public void test2() {
        logger.info("In test 2");
        Assert.assertEquals(5+5, 10, "test 2");
    }
}
