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

    /**
     * get user by uid
     */
    public User getUserByUid(Long uid);

    /**
     * get user by rid
     */
    public User getDriverByRid(Long rid);

    /**
     * update user
     */
    public int updateUser(User user);

    /**
     * delete user
     */
    public int deleteUser(Long uid);


}
