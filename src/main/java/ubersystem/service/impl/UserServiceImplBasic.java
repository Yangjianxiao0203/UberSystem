package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;
import ubersystem.mapper.UserMapper;
import ubersystem.pojo.User;
import ubersystem.service.LogService;
import ubersystem.service.UserService;
import ubersystem.utils.JwtUtils;

@Service
@Slf4j
public class UserServiceImplBasic implements UserService {
    private UserMapper userMapper;

    @Autowired
    public UserServiceImplBasic(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int registerUser(User user) {
        //if already exist return false
        if (userMapper.getUserByPhoneNumber(user.getPhoneNumber()) != null) {
            log.error("user already exist");
            return -1;
        }

        int res = userMapper.insertUser(user);
        log.info("register user in database: "+ (res>0));
        return res;
    }

    @Override
    public User login(String phoneNumber, String password) {
        User user = userMapper.getUserByPhoneNumber(phoneNumber);
        if (user == null) {
            log.error("user not exist");
            return null;
        }
        if (!user.getSecretKey().equals(password)) {
            log.error("password not match");
            return null;
        }
        return user;
    }

    @Override
    public String createToken(User user) {
        return JwtUtils.generateToken(user);
    }

    @Override
    public User getUserByUid(Long uid) {
        return userMapper.getUserByUid(uid);
    }

    @Override
    public int updateUser(User user) {
        //todo: check if data is valid

        return userMapper.updateUser(user);
    }
}
