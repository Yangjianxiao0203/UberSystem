package ubersystem.dto;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Coordinate;

public interface CoordinateMapper {
    @Insert("INSERT INTO coordinate(longitude, latitude) VALUES (#{coordinate.longitude}, #{coordinate.latitude})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertCoordinate(@Param("coordinate") Coordinate coordinate);
}
