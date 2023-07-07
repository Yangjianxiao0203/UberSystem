package ubersystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.pojo.Order;
import ubersystem.pojo.request.order.PaymentRequest;
import ubersystem.pojo.request.order.PostToPriceRequest;
import ubersystem.service.OrderService;

@RestController
@CrossOrigin
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 通过rideId找到订单 并生成相应费用
     */
    @PostMapping("")
    public Result<String> createBillInOrderByRideId(@RequestBody PostToPriceRequest request) {
        log.info("createBillInOrderByRideId: {}", request);
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
        log.info("getOrderByOid: {}", oid);
        try{
            Order order = orderService.getOrderByOid(oid);
            return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), order);
        } catch (RuntimeException e) {
            return new Result<>(ResponseStatus.FAILURE.getStatus(), ResponseStatus.FAILURE.getMessage(), null);
        }
    }

    /**
     * 创建支付请求，返回已签名的订单信息字符串（用jwt）
     * params: uid, platform, platformSerialNumber
     * path: /order/{oid}
     */
    @PutMapping("/{oid}")
    public Result<String> paymentRequest(@RequestBody PaymentRequest request,@PathVariable("oid") Long oid) {
        if(request.getPlatformSerialNumber()==null) {
            log.info("createPaymentRequest: {}", request);
            try {
                String paymentRequest = orderService.createPaymentRequest(request, oid);
                return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), paymentRequest);
            } catch (RuntimeException e) {
                return new Result<>(ResponseStatus.PAYMENT_ERROR.getStatus(), ResponseStatus.PAYMENT_ERROR.getMessage(), null);
            }
        } else {
            log.info("checkPaymentRequest: {}", request);
            try {
                String res = orderService.checkPaymentRequest(request, oid);
                return new Result<>(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), res);
            } catch (RuntimeException e) {
                return new Result<>(ResponseStatus.PAYMENT_ERROR.getStatus(), ResponseStatus.PAYMENT_ERROR.getMessage(), null);
            }
        }
    }

}
