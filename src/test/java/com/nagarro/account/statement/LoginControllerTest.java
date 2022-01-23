package com.nagarro.account.statement;

import com.nagarro.account.statement.model.AuthenticationRequest;
import com.nagarro.account.statement.model.AuthenticationResponse;
import com.nagarro.account.statement.model.IdentityAttributes;
import com.nagarro.account.statement.repository.IdentityRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

	private final String LOGIN_URL = "/authenticate";

	@Autowired
	private TestRestTemplate testRestTemplate;

	private HttpEntity<AuthenticationRequest> requestHttpEntity;

	@MockBean
	private IdentityRepository identityRepository;


	private HttpHeaders getDefaultHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@Test
	public void testIncorrectCredentials() {
		AuthenticationRequest request = AuthenticationRequest.builder().username("admin").password("password").build();
		requestHttpEntity = new HttpEntity<>(request, getDefaultHeaders());
		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(LOGIN_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	}

	@Test
	public void testUnAuthorizedUser() {

		AuthenticationRequest request = AuthenticationRequest.builder().username("checker").password("password").build();
		requestHttpEntity = new HttpEntity<>(request, getDefaultHeaders());
		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(LOGIN_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

	}

	@Test
	public void testRedisConnectionError() {

		Mockito.when(identityRepository.findById(ArgumentMatchers.anyString()))
				.thenThrow(RuntimeException.class);
		AuthenticationRequest request = AuthenticationRequest.builder().username("admin").password("password").build();
		requestHttpEntity = new HttpEntity<>(request, getDefaultHeaders());
		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(LOGIN_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		Assert.assertNotNull(response.getBody());
	}

	@Test
	public void testSessionActiveScenario() {

		IdentityAttributes identityAttributes = IdentityAttributes.builder().authId("admin").token("sampletoken").validity(3L).build();
		Optional<IdentityAttributes> optional = Optional.of(identityAttributes);
		Mockito.when(identityRepository.findById(ArgumentMatchers.anyString())).thenReturn(optional);
		AuthenticationRequest request = AuthenticationRequest.builder().username("admin").password("admin").build();
		requestHttpEntity = new HttpEntity<>(request, getDefaultHeaders());
		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(LOGIN_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		Assert.assertNotNull(response.getBody());

	}

	@Test
	public void testSuccessfullLoginScenario() {

		AuthenticationRequest request = AuthenticationRequest.builder().username("admin").password("admin").build();
		requestHttpEntity = new HttpEntity<>(request, getDefaultHeaders());
		ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(LOGIN_URL, request,
				AuthenticationResponse.class);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertNotNull(response.getBody());

	}

}
