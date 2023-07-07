package ubersystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UberSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberSystemApplication.class, args);
    }

}
