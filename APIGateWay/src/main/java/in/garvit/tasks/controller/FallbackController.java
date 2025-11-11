package in.garvit.tasks.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Ensures circuit breaker fallbacks return a consistent payload instead of an empty 500. */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        return buildFallbackResponse("User service is temporarily unavailable. Please try again shortly.");
    }

    @GetMapping("/tasks")
    public ResponseEntity<Map<String, String>> taskServiceFallback() {
        return buildFallbackResponse("Task service is temporarily unavailable. Please try again shortly.");
    }

    @GetMapping("/submissions")
    public ResponseEntity<Map<String, String>> taskSubmissionServiceFallback() {
        return buildFallbackResponse("Task submission service is temporarily unavailable. Please try again shortly.");
    }

    private ResponseEntity<Map<String, String>> buildFallbackResponse(String message) {
        Map<String, String> body = Map.of(
            "status", "SERVICE_UNAVAILABLE",
            "message", message
        );
        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
