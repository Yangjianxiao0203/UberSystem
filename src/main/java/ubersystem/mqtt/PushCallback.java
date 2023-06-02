package ubersystem.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {        // 连接丢失后，一般在这里面进行重连
        log.info("连接断开，正在重连");
        while (true) {
            try {
                new MqttConfiguration().connect();
                log.info("重连成功");
                break;
            } catch (Exception e) {
                log.info("重连失败，5秒后重试");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 订阅主题接收到消息处理方法
     * @param topic
     * @param message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + message.getQos());
        log.info("接收消息内容 : " + new String(message.getPayload()));
    }

    /**
     * 发送消息，消息到达后处理方法
     * @param token
     */

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete---------" + token.isComplete());
    }
}
