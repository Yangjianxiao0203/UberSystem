package ubersystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import ubersystem.pojo.User;

@SpringBootTest
public class ComponentTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void listAllBeans() {
        System.out.println("**********List all beans by user**********");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            // 检查是否为用户自定义的Bean
            if (beanDefinition.getSource() != null) {
                System.out.println(beanName);
            }
        }
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testString() throws JsonProcessingException {

        User user = new User();
        user.setUserName("John");
        String json = mapper.writeValueAsString(user);
        redisTemplate.opsForValue().set("user", json);

        String jsonUser = redisTemplate.opsForValue().get("user");
        User user1 = mapper.readValue(jsonUser, User.class);
        System.out.println(user1);
    }

}
