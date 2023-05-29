package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.pojo.User;

public interface UserService {
    /**
     * register user
     */
    public boolean registerUser(User user);



}
