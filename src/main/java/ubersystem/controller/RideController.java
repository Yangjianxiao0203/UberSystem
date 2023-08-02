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
import ubersystem.utils.JwtUtils;

import java.util.List;

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
        try{
            String channelName = distributionService.driverAcceptsOrder(request,rid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(),ResponseStatus.SUCCESS.getMessage(), channelName);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.ORDER_ALREADY_ACCEPTED.getStatus(), ResponseStatus.ORDER_ALREADY_ACCEPTED.getMessage(), e.getMessage());
        }
    }

    /**
     * @param rid
     * @Description: Passenger: get track channelName by rid
     */
    @GetMapping("/track/{rid}")
    public Result<String> getTrackIdByRid(@PathVariable("rid") Long rid) {
        try{
            String channelName = distributionService.getTrackIdByRid(rid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(),ResponseStatus.SUCCESS.getMessage(), channelName);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), e.getMessage());
        }
    }

    /**
     * @param request
     * @return : ride id (if paid) or order id (if not paid)
     */
    @PostMapping("")
    public Result<String> createRideAndOrder(@RequestBody OrderCreationRequest request) {
        try {
            String id = distributionService.createOrderAndRide(request);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), id);
        } catch (Exception e) {
            // return order id
            return new Result<>(ResponseStatus.CREATE_RIDE_ERROR.getCode(), ResponseStatus.CREATE_RIDE_ERROR.getMessage(), e.getMessage());
        }
    }

    @PutMapping("/cancel/{rid}")
    public Result<String> cancelRideAndOrder(@PathVariable("rid") Long rid, @RequestBody CancelRequest request) {
        Long uid = request.getUid();
        boolean cancel = request.isCancel();
        try {
            String id = distributionService.cancelRideAndOrder(rid, uid, cancel);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), null);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), e.getMessage());
        }
    }

    @GetMapping("/{rid}")
    public Result<RideAndNearbyVehicles> getRideAndNearByVehicles(@RequestParam("lat") double lat, @RequestParam("long") double lon,@PathVariable("rid") Long rid) {
        try {
            RideAndNearbyVehicles rv = distributionService.getRideAndNearByVehicles(rid,lat, lon);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), rv);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }

    @GetMapping("/accepted")
    public Result<List<Ride>> getAcceptedRide(@RequestHeader("x-auth-token") String token) {
        Long uid = JwtUtils.getUid(token);
        try {
            List<Ride> rides = distributionService.getAcceptedRide(uid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), rides);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }

    /**
     * get all rides by uid
     */
    @GetMapping("")
    public Result<List<Ride>> getAllRides(@RequestParam("uid") Long uid) {
        try {
            List<Ride> rides = rideService.getAllRides(uid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), rides);
        } catch (Exception e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }
}
