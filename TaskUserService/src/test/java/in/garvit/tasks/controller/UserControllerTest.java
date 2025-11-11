package in.garvit.tasks.controller;

import in.garvit.tasks.exception.UserException;
import in.garvit.tasks.response.UserResponse;
import in.garvit.tasks.service.UserService;
import in.garvit.tasks.usermodel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserProfile() throws UserException {
        // Arrange
        String jwt = "mockJWT";
        User mockUser = new User();
        mockUser.setEmail("mock@example.com");
        // Set up mock behavior
        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        // Act
        ResponseEntity<UserResponse> responseEntity = userController.getUserProfile(jwt);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(mockUser.getEmail(), responseEntity.getBody().email());
    }

    @Test
    void testFindUserById() throws UserException {
        // Arrange
        String userId = "mockUserId";
        String jwt = "mockJWT";
        User mockUser = new User();
        mockUser.setId(userId);
        // Set up mock behavior
        when(userService.findUserById(userId)).thenReturn(mockUser);

        // Act
        ResponseEntity<UserResponse> responseEntity = userController.findUserById(userId, jwt);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(mockUser.getId(), responseEntity.getBody().id());
    }

    @Test
    void testFindAllUsers() {
        // Arrange
        String jwt = "mockJWT";
        List<User> mockUsers = new ArrayList<>();
        // Set up mock behavior
        when(userService.findAllUsers()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<UserResponse>> responseEntity = userController.findAllUsers(jwt);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(mockUsers.size(), responseEntity.getBody().size());
    }
}

