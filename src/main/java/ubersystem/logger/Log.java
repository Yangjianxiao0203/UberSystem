package ubersystem.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ubersystem.Enums.LogLevel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Log {
    Long id;
    LocalDateTime timestamp;
    String sourceModule;
    LogLevel logLevel;
    String logContent;

    @Override
    public String toString() {
        return "log level: " + logLevel + " | time:  " + timestamp + " | source: " + sourceModule + " | content: " + logContent + "\n";
    }
}
