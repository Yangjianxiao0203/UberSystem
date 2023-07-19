package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        order.setTotalCost(100.0);

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
    @Transactional
    public Order getOrderByRid(Long rid) {
        Order order = orderMapper.getOrderByRideId(rid);
        if(order==null) {
            throw new RuntimeException("order not found");
        }
        return order;
    }

    @Override
    public String createPaymentRequest(PaymentRequest request,Long oid) {
        // request's serial number should be null
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

        //get third party url
        String paymentUrl=null;
        switch (request.getPlatform()) {
            case "Alipay":
                paymentUrl="https://www.alipay.com";
                break;
            case "Wechat":
                paymentUrl="https://pay.weixin.qq.com";
                break;
            case "Paypal":
                paymentUrl="https://www.paypal.com";
                break;
            default:
                throw new RuntimeException("platform not supported");
        }

        order.setPaymentPlatform(request.getPlatform());

        int res = orderMapper.update(order);
        if(res<=0) {
            throw new RuntimeException("update order failed");
        }

        return paymentUrl;
    }

    @Override
    public String checkPaymentRequest(PaymentRequest request, Long oid) {
        // it return a serialNumber to update
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
        order.setStatus(OrderStatus.Paid);
        order.setPaymentPlatform(request.getPlatform());
        order.setPaymentPlatformSerialNumber(request.getPlatformSerialNumber());
        order.setPaymentResultFromPlatform("success");
        int res = orderMapper.update(order);
        if(res<=0) {
            throw new RuntimeException("update order failed");
        }

        return "success";
    }

}
