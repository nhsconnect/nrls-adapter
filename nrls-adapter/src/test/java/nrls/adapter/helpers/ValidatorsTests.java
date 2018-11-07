/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

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
