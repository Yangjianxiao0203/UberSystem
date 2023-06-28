package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.pojo.User;

@Service
public interface AuthService {
    String createToken(User user);
    boolean verifyToken(String token,Long uid);
    Long decodeToken(String token);
}
