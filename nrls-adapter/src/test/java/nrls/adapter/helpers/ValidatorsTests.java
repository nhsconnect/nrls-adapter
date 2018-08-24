package nrls.adapter.helpers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ValidatorsTests {
	
	String[] validNhsNumbers = {"9462205957","9434765919","0463391764","8187722142","4005237193"};
	String[] inValidNhsNumbers = {"946220595733","946-220-5957","94622O5957","946r205i57","8462205957"};
	
	@Test
	public void testValidNhsNumbers() {
		for(String validNhsNumber : validNhsNumbers) {
			assertEquals(true, Validators.nhsNumberValid(validNhsNumber));
		}
		
	}
	
	@Test
	public void testInValidNhsNumbers() {
		for (String inValidNhsNumber : inValidNhsNumbers) {
			assertEquals(false, Validators.nhsNumberValid(inValidNhsNumber));
		}
	}

}
