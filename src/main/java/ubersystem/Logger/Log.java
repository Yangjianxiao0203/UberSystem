package ubersystem.Logger;

import jakarta.persistence.*;
import lombok.*;
import ubersystem.Enums.LogLevel;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "source_module")
    private String sourceModule;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_level")
    private LogLevel logLevel;

    @Column(name = "log_content", columnDefinition = "TEXT")
    private String logContent;

}

