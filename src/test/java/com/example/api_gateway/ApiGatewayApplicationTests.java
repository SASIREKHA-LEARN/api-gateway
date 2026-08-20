package com.example.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	void rateLimiterShouldReturn429() throws Exception {

		HttpClient client = HttpClient.newHttpClient();

		// Allow Redis rate limiter tokens to refill
		Thread.sleep(1500);

		for (int i = 1; i <= 3; i++) {

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("http://localhost:" + port + "/api/hello"))
					.GET()
					.build();

			HttpResponse<String> response =
					client.send(request, HttpResponse.BodyHandlers.ofString());

			System.out.println(
					"Request " + i + " -> HTTP " + response.statusCode()
			);

			if (i <= 2) {
				assertTrue(
						response.statusCode() == 200,
						"Expected request " + i + " to return HTTP 200"
				);
			} else {
				assertTrue(
						response.statusCode() == 429,
						"Expected request 3 to return HTTP 429"
				);
			}
		}
	}
	@Test
	void normalRequestShouldReturn200() throws Exception {

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/hello"))
				.GET()
				.build();

		HttpResponse<String> response =
				client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(
				"Normal request -> HTTP " + response.statusCode()
		);

		assertTrue(
				response.statusCode() == 200,
				"Expected HTTP 200 for a normal request"
		);
	}

	@Test
	void rateLimiterShouldRecoverAfterRefill() throws Exception {

		HttpClient client = HttpClient.newHttpClient();

		// Give Redis rate limiter time to refill
		Thread.sleep(1500);

		// Request 1 -> should be allowed
		HttpRequest request1 = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/hello"))
				.GET()
				.build();

		HttpResponse<String> response1 =
				client.send(request1, HttpResponse.BodyHandlers.ofString());

		System.out.println("Recovery Request 1 -> HTTP " + response1.statusCode());

		// Request 2 -> should be allowed
		HttpRequest request2 = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/hello"))
				.GET()
				.build();

		HttpResponse<String> response2 =
				client.send(request2, HttpResponse.BodyHandlers.ofString());

		System.out.println("Recovery Request 2 -> HTTP " + response2.statusCode());

		// Request 3 -> should be rate limited
		HttpRequest request3 = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/hello"))
				.GET()
				.build();

		HttpResponse<String> response3 =
				client.send(request3, HttpResponse.BodyHandlers.ofString());

		System.out.println("Recovery Request 3 -> HTTP " + response3.statusCode());

		assertTrue(response1.statusCode() == 200);
		assertTrue(response2.statusCode() == 200);
		assertTrue(response3.statusCode() == 429);

		// Wait for tokens to refill
		Thread.sleep(1000);

		// Request 4 -> should be allowed again
		HttpRequest request4 = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/api/hello"))
				.GET()
				.build();

		HttpResponse<String> response4 =
				client.send(request4, HttpResponse.BodyHandlers.ofString());

		System.out.println("Recovery Request 4 -> HTTP " + response4.statusCode());

		assertTrue(
				response4.statusCode() == 200,
				"Expected rate limiter to recover and return HTTP 200"
		);
	}
}