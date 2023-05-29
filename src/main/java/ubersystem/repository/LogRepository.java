package ubersystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubersystem.logger.Log;

@Repository
public interface LogRepository {
    public Log save(Log log);
    public Log findById(Long id);
}
