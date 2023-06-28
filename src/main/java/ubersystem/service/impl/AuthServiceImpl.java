package ubersystem.service.impl;

import org.springframework.stereotype.Component;
import ubersystem.pojo.User;
import ubersystem.service.AuthService;

@Component
public class AuthServiceImpl implements AuthService {
    @Override
    public String createToken(User user) {
        return null;
    }

    @Override
    public boolean verifyToken(String token, Long uid) {
        return false;
    }

    @Override
    public Long decodeToken(String token) {
        return null;
    }
}
