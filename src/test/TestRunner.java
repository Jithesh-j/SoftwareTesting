package test;
import org.junit.runner.JUnitCore;

public class TestRunner {
    public static void main(String[] args) {
        // Create JUnitCore runner and add the custom listener
        JUnitCore runner = new JUnitCore();
        runner.addListener(new TestResultListner()); // Attach the listener

        // Run multiple test classes
        runner.run(CartServiceTest.class, ProductServiceTest.class, UserServiceTest.class,TransServiceTest.class,DemandServiceTest.class);
    }
}
