package ubersystem.mapper;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Order;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO `order`(ride_id, creation_time, total_cost, base_cost, ride_and_fuel_cost, time_cost, special_location_servicecost, dynamic_cost, status, payment_platform, payment_platform_serial_number, payment_result_from_platform) " +
            "VALUES (#{order.rideId}, #{order.creationTime}, #{order.totalCost}, #{order.baseCost}, #{order.rideAndFuelCost}, #{order.timeCost}, #{order.specialLocationServiceCost}, #{order.dynamicCost}, #{order.status}, #{order.paymentPlatform}, #{order.paymentPlatformSerialNumber}, #{order.paymentResultFromPlatform})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createOrder(@Param("order") Order order);
}
