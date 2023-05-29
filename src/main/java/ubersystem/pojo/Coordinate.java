package ubersystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinate {
    private Long id;
    private Double longitude;
    private Double latitude;
}
