package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.LogLevel;
import ubersystem.mapper.LogMapper;
import ubersystem.logger.Log;
import ubersystem.rabbitMQ.MessagePublisher;
import ubersystem.rabbitMQ.MessageReceiver;
import ubersystem.service.LogService;

import java.time.LocalDateTime;


@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    @Autowired
    private MessagePublisher messagePublisher;
    @Override
    public void log(String sourceModule, LogLevel logLevel, String logContent) {
        Log logg = new Log();
        logg.setTimestamp(LocalDateTime.now());
        logg.setSourceModule(sourceModule);
        logg.setLogLevel(logLevel);
        logg.setLogContent(logContent);
        messagePublisher.publish("mapper-exchange", "log", logg);
    }

    @Override
    public void logToDB(Log log) {
        int res = logMapper.insert(log);
        if(res<=0) {
            throw new RuntimeException("Failed to insert log to DB");
        }
    }
}
