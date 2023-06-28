package ubersystem.mapper;

import org.apache.ibatis.annotations.*;
import ubersystem.pojo.Order;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO `order`(ride_id, creation_time, total_cost, base_cost, ride_and_fuel_cost, time_cost, special_location_servicecost, dynamic_cost, status, payment_platform, payment_platform_serial_number, payment_result_from_platform) " +
            "VALUES (#{order.rideId}, #{order.creationTime}, #{order.totalCost}, #{order.baseCost}, #{order.rideAndFuelCost}, #{order.timeCost}, #{order.specialLocationServiceCost}, #{order.dynamicCost}, #{order.status}, #{order.paymentPlatform}, #{order.paymentPlatformSerialNumber}, #{order.paymentResultFromPlatform})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(@Param("order") Order order);

    @Update("UPDATE `order` SET ride_id = #{order.rideId}, creation_time = #{order.creationTime}, total_cost = #{order.totalCost}, base_cost = #{order.baseCost}, ride_and_fuel_cost = #{order.rideAndFuelCost}, time_cost = #{order.timeCost}, special_location_servicecost = #{order.specialLocationServiceCost}, dynamic_cost = #{order.dynamicCost}, status = #{order.status}, payment_platform = #{order.paymentPlatform}, payment_platform_serial_number = #{order.paymentPlatformSerialNumber}, payment_result_from_platform = #{order.paymentResultFromPlatform} WHERE id = #{order.id}")
    int update(@Param("order") Order order);

}
