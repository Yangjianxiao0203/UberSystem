package ubersystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.pojo.DriverAcceptOrderRequest;
import ubersystem.pojo.LocalDateTimeWrapper;
import ubersystem.Result.Result;
import ubersystem.pojo.OrderCreationRequest;
import ubersystem.pojo.Ride;
import ubersystem.service.DistributionService;
import ubersystem.service.RideService;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/ride")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    DistributionService distributionService;

    @PutMapping("/rid")
    public Result<String> driverAcceptsOrder(@RequestBody DriverAcceptOrderRequest request) {
        log.info("driverAcceptsOrder by driver: {}",request.getDriverUid());
        try{
            String channelName = distributionService.driverAcceptsOrder(request);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(),ResponseStatus.SUCCESS.getMessage(), channelName);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.ORDER_ALREADY_ACCEPTED.getStatus(), ResponseStatus.ORDER_ALREADY_ACCEPTED.getMessage(), e.getMessage());
        }
    }

    /**
     *
     * @param request
     * @return : ride id (if paid) or order id (if not paid)
     */
    @PostMapping("")
    public Result<String> createRideAndOrder(@RequestBody OrderCreationRequest request) {
        log.info("createRideAndOrder by user: {}",request.getUid());
        try {
            String id = distributionService.createOrderAndRide(request);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), id);
        } catch (Exception e) {
            // return order id
            return new Result<>(ResponseStatus.CREATE_RIDE_ERROR.getStatus(), ResponseStatus.CREATE_RIDE_ERROR.getMessage(), e.getMessage());
        }
    }


}
