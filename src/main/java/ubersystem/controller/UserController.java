package ubersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.pojo.User;
import ubersystem.service.UserService;
import ubersystem.utils.JwtUtils;

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

    @GetMapping("/user")
    public Result<User> getMySelf(@RequestHeader("x-auth-token") String token) {
        Long uid = JwtUtils.getUid(token);
        User user = userService.getUserByUid(uid);
        if (user == null) {
            return new Result<>(ResponseStatus.AUTH_ERROR.getStatus(), ResponseStatus.AUTH_ERROR.getMessage(), null);
        }
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "Success", user);
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

    /**
     * @param uid
     * @param token
     * @param updateUser : need full user updated info, except uid
     * @return Result<User>
     */

    @PutMapping("/user/{uid}")
    public Result<User> updateUser(@PathVariable("uid") Long uid, @RequestHeader("x-auth-token") String token, @RequestBody User updateUser) {

        //first, get user, if not self or not exist, return error
        User user = userService.getUserByUid(uid);
        if (user == null ) {
            return new Result<>(ResponseStatus.AUTH_ERROR.getStatus(), ResponseStatus.AUTH_ERROR.getMessage(), null);
        }
        boolean isAuth = JwtUtils.verifyToken(token,uid);
        if(!isAuth) {
            return new Result<>(ResponseStatus.AUTH_ERROR.getStatus(), ResponseStatus.AUTH_ERROR.getMessage(), null);
        }

        updateUser.setUid(uid);

        int res = userService.updateUser(updateUser);
        if (res < 0) {
            return new Result<>(ResponseStatus.FAILURE.getCode(), ResponseStatus.FAILURE.getMessage(), null);
        }
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "Success", updateUser);
    }


    @DeleteMapping("/user")
    public Result<String> deleteUser(@RequestHeader("x-auth-token") String token) {
        Long uid = JwtUtils.getUid(token);
        int res = userService.deleteUser(uid);
        if (res < 0) {
            return new Result<>(ResponseStatus.FAILURE.getCode(), ResponseStatus.FAILURE.getMessage(), null);
        }
        return new Result<>(ResponseStatus.SUCCESS.getCode(), "Success", null);
    }

    public String createRandomUserName() {
        return "user-" + UUID.randomUUID().toString();
    }
}
