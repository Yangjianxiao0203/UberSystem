package ubersystem.repository.impl;

import org.springframework.stereotype.Repository;
import ubersystem.logger.Log;
import ubersystem.repository.LogRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LogRepositoryImpl implements LogRepository {
    private AtomicLong idGenerator = new AtomicLong(0);
    private ConcurrentHashMap<Long, Log> logs = new ConcurrentHashMap<>();

    @Override
    public Log save(Log log) {
        if (log.getId() == null) {
            log.setId(idGenerator.incrementAndGet());
        }

        logs.put(log.getId(), log);
        return log;
    }

    @Override
    public Log findById(Long id) {
        return logs.get(id);
    }

}
