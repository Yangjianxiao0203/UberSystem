package ubersystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.pojo.Order;
import ubersystem.pojo.request.order.PostToPriceRequest;
import ubersystem.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 通过rideId找到订单 并生成相应费用
     */
    @PostMapping("")
    public Result<String> createBillInOrderByRideId(@RequestBody PostToPriceRequest request) {
        try{
            String oid = orderService.createBillInOrderByRideId(request.getRid());
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), oid);
        } catch (RuntimeException e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }

    /**
     * 通过oid找到订单，返回账单具体数据
     */
    @GetMapping("/{oid}")
    public Result<Order> getOrderByOid(@PathVariable("oid") Long oid) {
        try{
            Order order = orderService.getOrderByOid(oid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), order);
        } catch (RuntimeException e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }
}
