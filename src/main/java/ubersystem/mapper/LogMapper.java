package ubersystem.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import ubersystem.logger.Log;

@Mapper
public interface LogMapper {

    @Insert("insert into logs (timestamp, source_module, log_level, log_content) values (#{log.timestamp}, #{log.sourceModule}, #{log.logLevel}, #{log.logContent})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insert(@Param("log") Log log);
}
