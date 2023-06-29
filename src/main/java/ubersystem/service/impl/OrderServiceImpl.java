package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.Enums.OrderStatus;
import ubersystem.mapper.OrderMapper;
import ubersystem.mapper.UserMapper;
import ubersystem.pojo.Order;
import ubersystem.pojo.User;
import ubersystem.pojo.request.order.PaymentRequest;
import ubersystem.service.OrderService;
import ubersystem.utils.JwtUtils;

import java.util.Objects;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public String createBillInOrderByRideId(Long rid) {
        Order order = orderMapper.getOrderByRideId(rid);
        if(order==null) {
            throw new RuntimeException("order not found");
        }
        //todo: 通过访问 ride 和 snap 表的数据自动计算价格

        int res = orderMapper.update(order);
        if(res<=0) {
            throw new RuntimeException("update order failed");
        }

        return order.getId().toString();
    }

    @Override
    public Order getOrderByOid(Long oid) {
        Order order=orderMapper.getOrderById(oid);
        if(order==null) {
            throw new RuntimeException("order not found");
        }
        return order;
    }

    @Override
    public String createPaymentRequest(PaymentRequest request,Long oid) {
        Order order=orderMapper.getOrderById(oid);
        if(order==null) {
            throw new RuntimeException("order not found");
        }
        //check if order already been paid
        if(order.getStatus()!= OrderStatus.Unpaid) {
            throw new RuntimeException("order has already been processing");
        }

        User user=userMapper.getUserByUid(request.getUid());
        if(user==null) {
            throw new RuntimeException("user not found");
        }
        //todo: create payment and return a sign token
        String token = JwtUtils.createTokenById(oid);

        order.setPaymentPlatform(request.getPlatform());
        order.setPaymentPlatformSerialNumber(token);

        int res = orderMapper.update(order);
        if(res<=0) {
            throw new RuntimeException("update order failed");
        }

        return token;
    }

    @Override
    public String checkPaymentRequest(PaymentRequest request, Long oid) {
        Order order=orderMapper.getOrderById(oid);
        if(order==null) {
            throw new RuntimeException("order not found");
        }
        //check if order already been paid
        if(order.getStatus()!= OrderStatus.Unpaid) {
            throw new RuntimeException("order has already been processing");
        }
        //check if order token is correct
        if(!Objects.equals(order.getPaymentPlatformSerialNumber(), request.getPlatformSerialNumber())) {
            throw new RuntimeException("wrong order token");
        }

        User user=userMapper.getUserByUid(request.getUid());
        if(user==null) {
            throw new RuntimeException("user not found");
        }
        order.setStatus(OrderStatus.Paid);
        order.setPaymentResultFromPlatform("success");
        int res = orderMapper.update(order);
        if(res<=0) {
            throw new RuntimeException("update order failed");
        }

        return "success";
    }

}
