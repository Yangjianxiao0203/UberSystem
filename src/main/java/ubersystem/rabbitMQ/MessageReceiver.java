package ubersystem.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ubersystem.Enums.LogLevel;
import ubersystem.logger.Log;
import ubersystem.service.LogService;

@Component
@Slf4j
public class MessageReceiver {
    @Autowired
    LogService logService;

    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("log-queue"),
            exchange = @Exchange(name = "mapper-exchange",type = ExchangeTypes.DIRECT),
            key = {"log"}
    ))
    public void logReceiver(String logJson) {
        try {
            Log log = objectMapper.readValue(logJson, Log.class);
            logService.logToDB(log);
        } catch (Exception e) {
            log.error("Error while receiving message from queue", e);
        }
    }
}
