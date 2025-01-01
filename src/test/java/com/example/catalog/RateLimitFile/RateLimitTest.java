package com.example.catalog.RateLimitFile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String API_ENDPOINT = "/";
    private static final String INTERNAL_ENDPOINT = "/internal";
    private static final String XRateLimitRetryAfterSecondsHeader = "X-Rate-Limit-Retry-After-Seconds";
    private static final String XRateLimitRemaining = "X-Rate-Limit-Remaining";

    @Test
    public void testRateLimiterEnforcesLimits() throws InterruptedException {
        int allowedRequests = 10;
        int extraRequests = 5;

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)), "Expected status code to be 200 for the first 10 requests");

            String remainingRequests = String.valueOf(allowedRequests - (i + 1));
            assertEquals(remainingRequests, response.getHeaders().get(XRateLimitRemaining).get(0), "Expected " + XRateLimitRemaining + " header to be " + remainingRequests + " after " + i + 1 + " requests");
        }

        for (int i = 0; i < extraRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(429)));
            int retryAfter = Integer.parseInt(response.getHeaders().get(XRateLimitRetryAfterSecondsHeader).get(0));
            assertTrue(retryAfter > 0);
        }
    }

    @Test
    public void testRateLimiterBypassesInternalEndpoint() {
        int totalRequests = 15;

        for (int i = 0; i < totalRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(INTERNAL_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)));
            assertFalse(response.getHeaders().containsKey(XRateLimitRemaining));
        }
    }
//********************************************************************************************************************//

    @Test
    public void testFixedRateLimiterBypassesAPIEndpoint1() {
        int totalRequests = 15;

        for (int i = 0; i < totalRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            if (i<10)
                assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)));
            else
                assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(429)));

        }
    }
    @Test
    public void testFixedRateLimiterWithPause() throws InterruptedException {
        int allowedRequests = 10,totalRequests = 15 ,pauseAfterRequests = 5 , pauseDurationMillis = 30000;

        for (int i = 0; i < totalRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);

            if (i < allowedRequests) {
                assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                        "Expected status code to be 200 for allowed requests");

                String remainingRequests = String.valueOf(allowedRequests - (i + 1));
                assertEquals(remainingRequests, response.getHeaders().getFirst(XRateLimitRemaining),
                        "Expected remaining requests to decrease with each allowed request");
            } else {
                assertEquals(HttpStatusCode.valueOf(429), response.getStatusCode(),
                        "Expected status code to be 429 for blocked requests");

                int retryAfter = Integer.parseInt(response.getHeaders().getFirst(XRateLimitRetryAfterSecondsHeader));
                assertTrue(retryAfter > 0, "Retry-After header should have a positive value");
            }

            if (i == pauseAfterRequests) {
                System.out.println("Pausing for 30 seconds...");
                Thread.sleep(pauseDurationMillis);
            }
        }
    }

    @Test
    public void testFixedRateLimiterFullWindowExpiry() throws InterruptedException {
        int allowedRequests = 10 ,windowDurationMillis = 60000;

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests");
        }

        ResponseEntity<String> blockedResponse = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
        assertEquals(HttpStatusCode.valueOf(429), blockedResponse.getStatusCode(),
                "Expected status code to be 429 for blocked requests");

        System.out.println("Waiting for the fixed window to expire...");
        Thread.sleep(windowDurationMillis);

        ResponseEntity<String> responseAfterExpiry = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
        assertEquals(HttpStatusCode.valueOf(200), responseAfterExpiry.getStatusCode(),
                "Expected status code to be 200 after the fixed window expires");
    }



    @Test
    public void testFixedRateLimiterMidWindowPause() throws InterruptedException {
        int allowedRequests = 10;
        int pauseAfterRequests = 5;
        int pauseDurationMillis = 20000; // 20 seconds pause
        int windowDurationMillis = 60000;

        for (int i = 0; i < pauseAfterRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests");
        }

        System.out.println("Pausing for 20 seconds...");
        Thread.sleep(pauseDurationMillis);

        for (int i = pauseAfterRequests; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests after pause");
        }

        ResponseEntity<String> blockedResponse = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
        assertEquals(HttpStatusCode.valueOf(429), blockedResponse.getStatusCode(),
                "Expected status code to be 429 for blocked requests");

        Thread.sleep(windowDurationMillis - pauseDurationMillis);

        ResponseEntity<String> responseAfterExpiry = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
        assertEquals(HttpStatusCode.valueOf(200), responseAfterExpiry.getStatusCode(),
                "Expected status code to be 200 after the fixed window expires");
    }

    @Test
    public void testFixedRateLimiterDisabled() {
        for (int i = 0; i < 20; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed&enabled=false", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 when the rate limiter is disabled");
        }
    }

    @Test
    public void testFixedRateLimiterRetryAfterHeader() throws InterruptedException {
        int allowedRequests = 10;

        // Simulate maximum allowed requests
        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests");
        }

        // Request exceeding the limit
        ResponseEntity<String> blockedResponse = restTemplate.getForEntity(API_ENDPOINT + "?algo=fixed", String.class);
        assertEquals(HttpStatusCode.valueOf(429), blockedResponse.getStatusCode(),
                "Expected status code to be 429 for blocked requests");

        String retryAfter = blockedResponse.getHeaders().getFirst(XRateLimitRetryAfterSecondsHeader);
        assertNotNull(retryAfter, "Retry-After header should be present when rate limit is exceeded");
        int retryAfterSeconds = Integer.parseInt(retryAfter);
        assertTrue(retryAfterSeconds > 0, "Retry-After value should be positive");
    }

//********************************************************************************************************************//

    @Test
    public void testMovingRateLimiterWithinLimit() {
        int allowedRequests = 10; // Maximum allowed requests in the moving window

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests");

            String remainingRequests = String.valueOf(allowedRequests - (i + 1));
            assertEquals(remainingRequests, response.getHeaders().getFirst(XRateLimitRemaining),
                    "Expected remaining requests to decrease with each allowed request");
        }
    }

    @Test
    public void testMovingRateLimiterExceedsLimit() {
        int allowedRequests = 10; // Maximum allowed requests in the moving window

        // Simulate maximum allowed requests
        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests");
        }

        // Exceed the limit
        ResponseEntity<String> blockedResponse = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
        assertEquals(HttpStatusCode.valueOf(429), blockedResponse.getStatusCode(),
                "Expected status code to be 429 for blocked requests");

        String retryAfter = blockedResponse.getHeaders().getFirst(XRateLimitRetryAfterSecondsHeader);
        assertNotNull(retryAfter, "Retry-After header should be present when rate limit is exceeded");
        assertTrue(Integer.parseInt(retryAfter) > 0, "Retry-After header value should be positive");
    }

    @Test
    public void testMovingRateLimiterAfterTimePasses() throws InterruptedException {
        int allowedRequests = 10,windowDurationMillis = 60000 ;

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed requests");
        }

        System.out.println("Pausing for the window duration...");
        Thread.sleep(windowDurationMillis);

        for (int i = 0; i < allowedRequests / 2; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for new requests after part of the window shifts");
        }
    }

    @Test
    public void testMovingRateLimiterHighFrequencyRequests() {
        int allowedRequests = 10; // Maximum allowed requests in the moving window

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode(),
                    "Expected status code to be 200 for allowed high-frequency requests");
        }

        ResponseEntity<String> blockedResponse = restTemplate.getForEntity(API_ENDPOINT + "?algo=moving", String.class);
        assertEquals(HttpStatusCode.valueOf(429), blockedResponse.getStatusCode(),
                "Expected status code to be 429 for blocked high-frequency requests");
    }


}