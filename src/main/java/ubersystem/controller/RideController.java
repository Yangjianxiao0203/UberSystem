package ubersystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.pojo.*;
import ubersystem.Result.Result;
import ubersystem.pojo.request.distribution.CancelRequest;
import ubersystem.pojo.request.distribution.DriverAcceptOrderRequest;
import ubersystem.pojo.request.distribution.OrderCreationRequest;
import ubersystem.service.DistributionService;
import ubersystem.service.RideService;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/ride")
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    DistributionService distributionService;

    @PutMapping("/{rid}")
    public Result<String> driverAcceptsOrder(@RequestBody DriverAcceptOrderRequest request, @PathVariable("rid") Long rid) {
        log.info("driverAcceptsOrder {} by driver {}",rid, request.getDriverUid());
        try{
            String channelName = distributionService.driverAcceptsOrder(request,rid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(),ResponseStatus.SUCCESS.getMessage(), channelName);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.ORDER_ALREADY_ACCEPTED.getStatus(), ResponseStatus.ORDER_ALREADY_ACCEPTED.getMessage(), e.getMessage());
        }
    }

    /**
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

    @PutMapping("/cancel/{rid}")
    public Result<String> cancelRideAndOrder(@PathVariable("rid") Long rid, @RequestBody CancelRequest request) {
        Long uid = request.getUid();
        boolean cancel = request.isCancel();
        log.info("cancelRideAndOrder by user: {}",uid);
        try {
            String id = distributionService.cancelRideAndOrder(rid, uid, cancel);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), null);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), e.getMessage());
        }
    }

    @GetMapping("/{rid}")
    public Result<RideAndNearbyVehicles> getRideAndNearByVehicles(@RequestParam("lat") double lat, @RequestParam("long") double lon,@PathVariable("rid") Long rid) {
        log.info("getRideAndNearByVehicles by lat: {}, long: {}", lat, lon);
        try {
            RideAndNearbyVehicles rv = distributionService.getRideAndNearByVehicles(rid,lat, lon);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), rv);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }

}
