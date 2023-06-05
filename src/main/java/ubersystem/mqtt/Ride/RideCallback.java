package ubersystem.mqtt.Ride;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RideCallback implements MqttCallbackExtended {

    @Autowired
    @Lazy
    RideClient rideClient;

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("Ride Client connection completed: {}", s);
        log.info("client: {}", RideClient.getClient().getClientId());
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("Ride connection lost: {}", throwable.getMessage());

        log.info("reconnecting...");
        if(RideClient.client==null) {
            rideClient.reconnect();
        }
    }

    /**
     *
     messageArrived(String topic, MqttMessage message) 方法在订阅的主题收到消息时触发。
     当 MQTT 代理接收到发布的消息，并且订阅者订阅了该主题时，将调用此方法。
     topic参数表示收到消息的主题，mqttMessage参数表示接收到的 MQTT 消息对象。

     deliveryComplete(IMqttDeliveryToken token) 方法在成功发布消息后触发。
     当客户端成功将消息发布到 MQTT 代理时，将调用此方法。您可以在该方法中执行必要的逻辑，例如记录日志或处理发布完成事件。
     token参数表示发布的消息的传递令牌，可以用于获取有关消息传递状态的详细信息。
     */

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("Ride message received: {}", mqttMessage);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("MQTT delivery complete: {}", iMqttDeliveryToken);
    }
}
