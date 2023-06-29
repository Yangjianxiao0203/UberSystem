package ubersystem.pojo.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    Long uid;
    String platform;
    @JsonProperty("trade_no")
    String platformSerialNumber;
}
