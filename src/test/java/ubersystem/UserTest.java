package ubersystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.controller.UserController;
import ubersystem.pojo.User;
import ubersystem.service.UserService;
import ubersystem.utils.JwtUtils;

public class UserTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    JwtUtils jwtUtils;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        Long uid = Long.valueOf(1);
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjg3ODk4MjUzfQ.vH3edpz5NOrzsBy8FoStgeOr7NTKNY_f9WFw9OA9Byo";
        User existingUser = new User();
        existingUser.setUid(uid);

        User updateUser = new User();
        updateUser.setUserName("UpdatedName");

        when(userService.getUserByUid(uid)).thenReturn(existingUser);
        when(jwtUtils.verifyToken(token,uid)).thenReturn(true);
        when(userService.updateUser(updateUser)).thenReturn(1);

        // Act
        Result<User> result = userController.updateUser(uid, token, updateUser);

        // Assert
        assertEquals(ResponseStatus.SUCCESS.getCode(), result.getStatus());
        assertEquals("Success", result.getMessage());
        assertEquals(updateUser, result.getData());
        verify(userService, times(1)).getUserByUid(uid);
        verify(userService, times(1)).updateUser(updateUser);
    }
}
