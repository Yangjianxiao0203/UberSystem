package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;

@Service
public interface LogService {
    public void createLog(String sourceModule, LogLevel logLevel, String logContent);
}
