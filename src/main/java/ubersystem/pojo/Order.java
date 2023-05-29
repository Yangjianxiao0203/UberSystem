package ubersystem.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ubersystem.Enums.OrderStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private Long rideId;
    private LocalDateTime creationTime;
    private Double totalCost;
    private Double baseCost;
    private Double rideAndFuelCost;
    private Double timeCost;
    private Double specialLocationServiceCost; // airports, high-speed rail stations, highways
    private Double dynamicCost;
    private OrderStatus status; // 'Unpaid', 'PaidOrderComplete', 'RefundProcessing', 'Refunded'
    private String paymentPlatform;
    private String paymentPlatformSerialNumber;
    private String paymentResultFromPlatform;
}