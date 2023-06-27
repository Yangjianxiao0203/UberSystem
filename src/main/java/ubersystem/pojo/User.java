package ubersystem.pojo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import ubersystem.Enums.UserType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long uid;
    private String phoneNumber;
    private UserType identity; // 'Driver' or 'Passenger'
    private String secretKey; // this should be a hashed password
    private String userName;
    private String carNumber;
    private String carType;
    private Double totalRideLength;
    private String province;
    private String city;
}
