package nrls.adapter.helpers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class HeaderGeneratorTests {

	HeaderGenerator headerGenerator = new HeaderGenerator();

	@Before
	public void before() {
		ReflectionTestUtils.setField(headerGenerator, "fromAsid", "200000000117");
		ReflectionTestUtils.setField(headerGenerator, "toAsid", "999999999999");

	}

	@Test
	public void testValidProviderHeaderCreation() {
		HttpHeaders headers = headerGenerator.generateSecurityHeaders("read", "AMS01", "234234234");
		assertEquals(5, headers.size());
		assertEquals("application/fhir+json", headers.getContentType().toString());
		assertEquals(true, headers.getValuesAsList("Authorization").get(0).contains("Bearer"));
		assertEquals(true, headers.getValuesAsList("fromAsid").get(0).contains("200000000117"));
		assertEquals(true, headers.getValuesAsList("toAsid").get(0).contains("999999999999"));

		Jwt<?, ?> jwt = Jwts.parser().parse(headers.getValuesAsList("Authorization").get(0).replace("Bearer ", ""));

		assertEquals("{typ=JWT, alg=none}", jwt.getHeader().toString());
		assertEquals(true, jwt.getBody().toString().contains("iss=https://demonstrator.com"));
		assertEquals(true, jwt.getBody().toString().contains("sub=https://fhir.nhs.uk/Id/sds-role-profile-id|234234234"));
		assertEquals(true, jwt.getBody().toString().contains("aud=https://nrls.com/fhir/documentreference"));
		assertEquals(true, jwt.getBody().toString().contains("reason_for_request=directcare"));
		assertEquals(true, jwt.getBody().toString().contains("scope=patient/DocumentReference.read"));
		assertEquals(true, jwt.getBody().toString().contains("requesting_system=https://fhir.nhs.uk/Id/accredited-system|200000000117"));
		assertEquals(true, jwt.getBody().toString().contains("requesting_organisation=https://fhir.nhs.uk/Id/ods-organization-code|AMS01"));
		assertEquals(true, jwt.getBody().toString().contains("requesting_user=https://fhir.nhs.uk/Id/sds-role-profile-id|234234234"));
	}

	@Test
	public void testValidConsumerHeaderCreation() {
		HttpHeaders headers = headerGenerator.generateSecurityHeaders("write", "EXP001", null);
		assertEquals(5, headers.size());
		assertEquals("application/fhir+json", headers.getContentType().toString());
		assertEquals(true, headers.getValuesAsList("Authorization").get(0).contains("Bearer"));
		assertEquals(true, headers.getValuesAsList("fromAsid").get(0).contains("200000000117"));
		assertEquals(true, headers.getValuesAsList("toAsid").get(0).contains("999999999999"));

		Jwt<?, ?> jwt = Jwts.parser().parse(headers.getValuesAsList("Authorization").get(0).replace("Bearer ", ""));

		assertEquals("{typ=JWT, alg=none}", jwt.getHeader().toString());
		assertEquals(true, jwt.getBody().toString().contains("iss=https://demonstrator.com"));
		assertEquals(true, jwt.getBody().toString().contains("sub=https://fhir.nhs.uk/Id/accredited-system|200000000117"));
		assertEquals(true, jwt.getBody().toString().contains("aud=https://nrls.com/fhir/documentreference"));
		assertEquals(true, jwt.getBody().toString().contains("reason_for_request=directcare"));
		assertEquals(true, jwt.getBody().toString().contains("scope=patient/DocumentReference.write"));
		assertEquals(true, jwt.getBody().toString().contains("requesting_system=https://fhir.nhs.uk/Id/accredited-system|200000000117"));
		assertEquals(true, jwt.getBody().toString().contains("requesting_organisation=https://fhir.nhs.uk/Id/ods-organization-code|EXP001"));
	}

}
