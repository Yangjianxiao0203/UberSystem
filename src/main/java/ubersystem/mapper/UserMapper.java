package ubersystem.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;
import ubersystem.pojo.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(uid, phone_number, identity, secret_key, user_name, car_number, car_type, total_ride_number, province, city) " +
            "VALUES (#{uid}, #{phoneNumber}, #{identity}, #{secretKey}, #{userName}, #{carNumber}, #{carType}, #{totalRideLength}, #{province}, #{city})")
    @Options(useGeneratedKeys = true, keyProperty = "uid", keyColumn = "uid")
    int insertUser(@Param("user") User user);

    @Select("SELECT * FROM user WHERE phone_number = #{phoneNumber} LIMIT 1")
    User getUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Select("SELECT * FROM user WHERE uid = #{uid} LIMIT 1")
    User getUserByUid(@Param("uid") Long uid);


    @Update("update user set phone_number = #{phoneNumber}, identity = #{identity}, secret_key = #{secretKey}, user_name = #{userName}, car_number = #{carNumber}, car_type = #{carType}, total_ride_number = #{totalRideLength}, province = #{province}, city = #{city} where uid = #{uid}")
    int updateUser(User user);

}
