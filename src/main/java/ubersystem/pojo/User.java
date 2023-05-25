package ubersystem.pojo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import ubersystem.Enums.UserType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(name = "phone_number")
    private String phoneNumber;
    private UserType identity; // 'Driver' or 'Passenger'
    @Column(name = "secret_key")
    private String secretKey;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "car_number")
    private String carNumber;
    @Column(name = "car_type")
    private String carType;
    @Column(name="total_ride_number")
    private Double totalRideLength;
    private String province;
    private String city;
}
