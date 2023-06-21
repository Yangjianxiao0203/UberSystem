package ubersystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private Long id;
    private Double longitude;
    private Double latitude;
}
