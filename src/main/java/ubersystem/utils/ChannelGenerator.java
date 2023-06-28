package ubersystem.utils;

public class ChannelGenerator {
    //singleton
    private static ChannelGenerator instance;

    // 私有构造函数，防止外部实例化
    private ChannelGenerator() {}

    // 公有静态方法，用于获取单例实例
    public static ChannelGenerator getInstance() {
        if (instance == null) {
            // 在第一次调用时创建实例
            instance = new ChannelGenerator();
        }
        return instance;
    }

    public static String generateTrackChannelName(Long rideId) {
        return "track-" + rideId;
    }
    public static String decodeTrackChannelName(String channelName) {
        return channelName.substring(6);
    }
}
