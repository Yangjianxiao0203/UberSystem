package ubersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.pojo.LocalDateTimeWrapper;
import ubersystem.Result.Result;
import ubersystem.pojo.Ride;
import ubersystem.service.RideService;

import java.time.LocalDateTime;

@RestController
public class RideController {
    private RideService rideService;
    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }
    @PostMapping("/create")
    public Result<Ride> createRide(@RequestBody Ride ride) {
        Ride createdRide=rideService.createRide(ride);
        if (createdRide != null) {
            return new Result<>(ResponseStatus.SUCCESS.getCode(), "Ride created successfully", createdRide);
        } else {
            return new Result<>(ResponseStatus.FAILURE.getCode(), "Failed to create ride", null);
        }
    }

    @PutMapping("/cancel/{rideId}")
    public Result<String> cancelRide(@PathVariable("rideId") Long rideId) {
        boolean isCanceled = rideService.cancelRide(rideId, LocalDateTime.now());
        if (isCanceled) {
            return new Result<>(ResponseStatus.SUCCESS.getCode(), "Ride canceled successfully", "RideId: "+rideId);
        } else {
            return new Result<>(ResponseStatus.FAILURE.getCode(), "Failed to cancel ride", null);
        }
    }

    @PutMapping("/accept/{rideId}")
    public Result<String> driverAcceptRide(@PathVariable("rideId") Long rideId, @RequestParam("driverUid") Long driverUid) {
        if (rideService.driverAcceptRide(rideId, driverUid,LocalDateTime.now())) {
            return new Result<>(ResponseStatus.SUCCESS.getCode(), "Ride accepted by driver successfully", "Ride ID: " + rideId + ", Driver ID: " + driverUid);
        } else {
            return new Result<>(ResponseStatus.FAILURE.getCode(), "Failed to accept ride", null);
        }
    }

    @PostMapping("/rides/{id}/pick-up")
    public Result<String> pickUpPassenger(@PathVariable Long id, @RequestBody LocalDateTimeWrapper pickUpTime) {
        boolean isPicked = rideService.pickUpPassenger(id, pickUpTime.getLocalDateTime());
        if (isPicked) {
            return new Result<>(ResponseStatus.SUCCESS.getCode(), "Ride picked up successfully", "Ride ID: " + id);
        } else {
            return new Result<>(ResponseStatus.FAILURE.getCode(), "Failed to pick up ride", null);
        }
    }

    @PostMapping("/rides/{id}/arrive")
    public Result<String> arriveAtDestination(@PathVariable Long id, @RequestBody LocalDateTimeWrapper arrivalTime) {
        boolean isArrived = rideService.arriveAtDestination(id, arrivalTime.getLocalDateTime());
        if (isArrived) {
            return new Result<>(ResponseStatus.SUCCESS.getCode(), "Ride arrived successfully", "Ride ID: " + id);
        } else {
            return new Result<>(ResponseStatus.FAILURE.getCode(), "Failed to arrive at destination", null);
        }
    }
}
