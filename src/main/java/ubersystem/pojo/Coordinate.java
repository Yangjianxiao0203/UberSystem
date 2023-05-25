package ubersystem.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Coordinate {
    @Id
    private Long id;
    private Double longitude;
    private Double latitude;
}
