package ubersystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import ubersystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(phone_number, identity, secret_key, user_name, car_number, car_type, total_ride_number, province, city) VALUES(#{phoneNumber}, #{identity}, #{secretKey}, #{userName}, #{carNumber}, #{carType}, #{totalRideLength}, #{province}, #{city})")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    boolean registerUser(User user);

    @Select("SELECT * from user where uid=#{uid}")
    User getUserByUid(Long uid);
}
