package test;
import org.junit.runner.JUnitCore;

public class TestRunner {
    public static void main(String[] args) {
        // Create JUnitCore runner and add the custom listener
        JUnitCore runner = new JUnitCore();
        runner.addListener(new TestResultListner()); // Attach the listener

        // Run multiple test classes
        runner.run( logoutSrv_Test.class,
        			login_Srv_Test.class,
        			DemandServiceTest.class,
        			CartServiceTest.class,
        			AddtoCart_Serv_Test.class,
        			AddProduct_Srv_Test.class,
        			ProductServiceTest.class, 
        			UserServiceTest.class,
        			TransServiceTest.class,
        			OrderSserviceTest.class,
        			RegisterSrv_Test.class,
        			RemoveProduct_Srv_Test.class,
        			FansMessageTest.class,
        			UpdateToCartSrv_Test.class);
        	
    }
}
