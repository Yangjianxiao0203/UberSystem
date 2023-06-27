package ubersystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

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

}
