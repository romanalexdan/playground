package simple;

import org.junit.jupiter.api.*;

public class SimpleJunitTest {

    @BeforeAll
    public static void setUp() {
        System.out.println("Setting up tests.");
    }

    @AfterAll
    public static void clean() {
        System.out.println("Cleaning up tests.");
    }

    @BeforeEach
    public void before() {
        System.out.println("Setup before each test.");
    }


    @AfterEach
    public void after(){
        System.out.println("After each test.");
    }

    @Test
    public void test() {
        Assertions.assertEquals(4 + 4, 8, "Test sum is 8");
    }

    @Test
    public void test2() {
        Assertions.assertEquals(5+5, 10, "test 2");
    }
}
