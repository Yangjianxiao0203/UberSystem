package ubersystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;
import ubersystem.logger.Log;
import ubersystem.repository.LogRepository;
import ubersystem.service.LogService;

import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    @Override
    public void createLog(String sourceModule, LogLevel logLevel, String logContent) {
        Log log = new Log();
        log.setTimestamp(LocalDateTime.now());
        log.setSourceModule(sourceModule);
        log.setLogLevel(logLevel);
        log.setLogContent(logContent);
        logRepository.save(log);
    }
}
