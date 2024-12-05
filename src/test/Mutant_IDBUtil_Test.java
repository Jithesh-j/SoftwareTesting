package test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.shashi.utility.IDUtil;

public class Mutant_IDBUtil_Test {

    @Test
    public void testGenerateIdNotEmpty() {
        String id = IDUtil.generateId();
        
        System.out.println("Generated ID: " + id);

        assertNotNull(id);
        assertTrue("Generated ID should start with 'P'", id.startsWith("P"));
    }
	@Test
	public void testGenerateTransIdWithExceptions() {
	    String transId = IDUtil.generateTransId();
	    assertNotEquals("", transId);
	    assertTrue(transId.startsWith("T"));
	    assertTrue(transId.length() > 1);
	}

}
