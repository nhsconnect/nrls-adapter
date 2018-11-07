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

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import nrls.adapter.model.task.Coding;

public class ValueSetValidatorTests {
	ValueSetValidator valueSetValidator = new ValueSetValidator();

	ArrayList<Coding> validCodings = new ArrayList<Coding>(0);
	ArrayList<Coding> inValidCodings = new ArrayList<Coding>(0);
	Coding validCode1 = new Coding();
	Coding inValidCode1 = new Coding();

	@Before
	public void before() {
		ReflectionTestUtils.setField(valueSetValidator, "fhirValueSetsPath", "valuesets/");
		
		validCode1.setSystem("http://snomed.info/sct");
		validCode1.setCode("736253002");
		validCode1.setDisplay("Mental health crisis plan (record artifact)");

		inValidCode1.setSystem("http://snomed.info/sct");
		inValidCode1.setCode("736253002");
		inValidCode1.setDisplay("Mental health crisis plan (record artifact) - broken");
		valueSetValidator.init();
	}

	@Test
	public void testValidCoding() throws Exception {
		assertEquals(true, valueSetValidator.validateCoding(validCode1));

	}

	@Test
	public void testInValidCoding() throws Exception {
		assertEquals(false, valueSetValidator.validateCoding(inValidCode1));
	}

}
