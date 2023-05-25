package ubersystem.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long rideId;
    private LocalDateTime creationTime;
    private Double totalCost;
    private Double baseCost;
    private Double rideAndFuelCost;
    private Double timeCost;
    private Double specialLocationServiceCost; // airports, high-speed rail stations, highways
    private Double dynamicCost;
    private String status; // 'Unpaid', 'PaidOrderComplete', 'RefundProcessing', 'Refunded'
    private String paymentPlatform;
    private String paymentPlatformSerialNumber;
    private String paymentResultFromPlatform;
}