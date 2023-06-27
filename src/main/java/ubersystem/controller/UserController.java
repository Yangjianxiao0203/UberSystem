package ubersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.pojo.User;
import ubersystem.service.UserService;

import java.util.UUID;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public Result<String> registerUser(@RequestBody User user) {
        if (user.getPhoneNumber() == null || user.getSecretKey() == null || user.getIdentity() == null) {
            return new Result<>(ResponseStatus.CREATE_USER_ERROR.getCode(), ResponseStatus.CREATE_USER_ERROR.getMessage(), null);
        }

        if(user.getUserName()==null) {
            user.setUserName(createRandomUserName());
        }

        int res = userService.registerUser(user);
        //check if already exist
        if (res==-1) {
            return new Result<>(ResponseStatus.USER_ALREADY_EXIST.getCode(), ResponseStatus.USER_ALREADY_EXIST.getMessage(), null);
        }
        else if(res<0) {
            return new Result<>(ResponseStatus.FAILURE.getCode(), ResponseStatus.FAILURE.getMessage(), null);
        }

        String token = userService.createToken(user);
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "Success", token);
    }

    @GetMapping("/login")
    public Result<String> login(@RequestParam String phoneNumber, @RequestParam String password) {
        User user = userService.login(phoneNumber, password);
        if (user == null) {
            return new Result<>(ResponseStatus.AUTH_ERROR.getStatus(), ResponseStatus.AUTH_ERROR.getMessage(), null);
        }
        String token = userService.createToken(user);
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "Success", token);
    }

    @PutMapping("/user/{uid}")
    public Result<String> updateUser(@PathVariable("uid") Long uid) {
//        int res = userService.updateUser(user);
        int res=0;
        if (res < 0) {
            return new Result<>(ResponseStatus.FAILURE.getCode(), ResponseStatus.FAILURE.getMessage(), null);
        }
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "Success", null);
    }

    public String createRandomUserName() {
        return "user-" + UUID.randomUUID().toString();
    }
}
