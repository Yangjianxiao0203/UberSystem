package ubersystem.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: MQTT 推送客户端
 */

@Slf4j
@Component
public class MqttPushClient {

    @Autowired
    private MqttClient client;

    /**
     * 发布，默认qos为0，非持久化
     *
     * @param topic 主题名
     * @param pushMessage 消息
     */
    public void publish(String topic, String pushMessage) {

        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     *
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = client.getTopic(topic);
        if (null == mTopic) {
            log.error("主题不存在:{}",mTopic);
        }
        try {
            mTopic.publish(message);
        } catch (Exception e) {
            log.error("mqtt发送消息异常:",e);
        }
    }
}
