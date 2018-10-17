package nrls.adapter.helpers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class HeaderGenerator {

	// Header configuration
	@Value("${adapter.asid}")
	private String fromAsid;
	@Value("${spine.asid}")
	private String toAsid;
	@Value("${nrls.api.issuer}")
	private String issuer;
	@Value("${nrls.api.audience}")
	private String audience;

	public HttpHeaders generateSecurityHeaders(String scope, String odsCode, String userId) {
		HttpHeaders headers = new HttpHeaders();
		if (scope.equals("read")) {
			headers.add("Authorization", "Bearer " + generateConsumerToken(scope, odsCode, userId));
		} else {
			headers.add("Authorization", "Bearer " + generateProviderToken(scope, odsCode));
		}
		headers.add("fromASID", fromAsid);
		headers.add("toASID", toAsid);
		headers.setContentType(MediaType.parseMediaType("application/fhir+json"));
		headers.setAccept(Collections.singletonList(MediaType.parseMediaType("application/fhir+xml")));
		return headers;
	}

	// Provider Validation
	// No specific validation rules apply.
	public String generateProviderToken(String scope, String odsCode) {
		String jws = Jwts.builder().setHeaderParam("typ", "JWT").setIssuer(issuer)
				.setSubject("https://fhir.nhs.uk/Id/accredited-system|" + fromAsid)
				.setAudience(audience)
				.setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES))).setIssuedAt(new Date())
				.claim("reason_for_request", "directcare").claim("scope", "patient/DocumentReference." + scope)
				.claim("requesting_system", "https://fhir.nhs.uk/Id/accredited-system|" + fromAsid)
				.claim("requesting_organization", "https://fhir.nhs.uk/Id/ods-organization-code|" + odsCode).compact();
		return jws;
	}

	// Consumer Validation
	// In the context of a Consumer request the requesting_user claim is mandatory.
	public String generateConsumerToken(String scope, String odsCode, String userId) {
		String jws = Jwts.builder().setHeaderParam("typ", "JWT").setIssuer(issuer)
				.setSubject("https://fhir.nhs.uk/Id/sds-role-profile-id|" + userId)
				.setAudience(audience)
				.setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES))).setIssuedAt(new Date())
				.claim("reason_for_request", "directcare").claim("scope", "patient/DocumentReference." + scope)
				.claim("requesting_system", "https://fhir.nhs.uk/Id/accredited-system|" + fromAsid)
				.claim("requesting_organization", "https://fhir.nhs.uk/Id/ods-organization-code|" + odsCode)
				.claim("requesting_user", "https://fhir.nhs.uk/Id/sds-role-profile-id|" + userId).compact();
		return jws;
	}

}
