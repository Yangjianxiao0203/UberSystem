package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;
import ubersystem.logger.Log;

@Service
public interface LogService {
    public void log(String sourceModule, LogLevel logLevel, String logContent);

    public void logToDB(Log log);
}
