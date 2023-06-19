package ubersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.pojo.User;
import ubersystem.service.UserService;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<User> registerUser(@RequestBody User user) {
        boolean isSucceed = userService.registerUser(user);
        if (!isSucceed) {
            return new Result<>(ResponseStatus.CLIENT_ERROR.getCode(), "user register failed", null);
        }
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "user register success", user);
    }
}
