package ubersystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;
import ubersystem.mapper.UserMapper;
import ubersystem.pojo.User;
import ubersystem.service.LogService;
import ubersystem.service.UserService;

@Service
public class UserServiceImplBasic implements UserService {
    private UserMapper userMapper;
    private LogService logService;

    @Autowired
    public UserServiceImplBasic(UserMapper userMapper,LogService logService) {
        this.userMapper = userMapper;
        this.logService = logService;
    }

    @Override
    public boolean registerUser(User user) {
        boolean isSucceed = userMapper.registerUser(user);
        logService.createLog("UserServiceImplBasic", LogLevel.valueOf("INFO"), "register user "+isSucceed);
        return isSucceed;
    }

}
