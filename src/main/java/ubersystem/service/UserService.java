package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.pojo.User;

public interface UserService {

    /**
     * jwt token
     */
    public String createToken(User user);


    /**
     * register user
     */
    public int registerUser(User user);


    /**
     * login in
     */
    public User login(String phoneNumber, String token);


}
