package ubersystem.logger;

import jakarta.persistence.*;
import lombok.*;
import ubersystem.Enums.LogLevel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    private Long id;
    private LocalDateTime timestamp;
    private String sourceModule;
//    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;
    private String logContent;

}

