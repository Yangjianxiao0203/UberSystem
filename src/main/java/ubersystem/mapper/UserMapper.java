package ubersystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;
import ubersystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(uid, phone_number, identity, secret_key, user_name, car_number, car_type, total_ride_number, province, city) " +
            "VALUES (#{uid}, #{phoneNumber}, #{identity}, #{secretKey}, #{userName}, #{carNumber}, #{carType}, #{totalRideLength}, #{province}, #{city})")
    @Options(useGeneratedKeys = true, keyProperty = "uid", keyColumn = "uid")
    int insertUser(@Param("user") User user);

    @Select("SELECT * FROM user WHERE phone_number = #{phoneNumber} LIMIT 1")
    User getUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
