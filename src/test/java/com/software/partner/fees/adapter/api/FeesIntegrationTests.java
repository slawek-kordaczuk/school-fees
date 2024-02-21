package com.software.partner.fees.adapter.api;

import com.software.partner.fees.domain.dto.ParentFeesResponse;
import com.software.partner.fees.domain.dto.SchoolFeesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeesIntegrationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void testFeesBySchoolId() {
		//Given & When
		SchoolFeesResponse response = webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/fees/school/{schoolId}")
						.build("79b55533-72bb-4479-83a7-f3e403870bf0"))

				.exchange()
				.expectStatus()
				.isEqualTo(OK)
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody(SchoolFeesResponse.class)
				.returnResult()
				.getResponseBody();

		//Then
		assertEquals(new BigDecimal("600.00"), response.getSumFees());

	}

	@Test
	void testFeesByParentId() {
		//Given & When
		ParentFeesResponse response = webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/fees/parent/{parentId}")
						.build("b8fa1be2-0e04-453d-8634-c8e403609826"))

				.exchange()
				.expectStatus()
				.isEqualTo(OK)
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody(ParentFeesResponse.class)
				.returnResult()
				.getResponseBody();

		//Then
		assertEquals(new BigDecimal("600.00"), response.getSumFees());

	}

}
