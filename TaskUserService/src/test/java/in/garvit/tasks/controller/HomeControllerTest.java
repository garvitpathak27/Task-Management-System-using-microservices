package in.garvit.tasks.controller;


import in.garvit.tasks.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    void setUp() {
        homeController = new HomeController();
    }

    @Test
    void testHomeController() {
        // Act
        ResponseEntity<ApiResponse> responseEntity = homeController.homeController();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse responseBody = responseEntity.getBody();
        assertEquals("Welcome To Task Management Microservice Project", responseBody.getMessage());
        assertTrue(responseBody.isStatus());
    }

    @Test
    void testUserHomeController() {
        // Act
        ResponseEntity<ApiResponse> responseEntity = homeController.userHomeController();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse responseBody = responseEntity.getBody();
        assertEquals("Welcome To Task Management User Service", responseBody.getMessage());
        assertTrue(responseBody.isStatus());
    }
}

