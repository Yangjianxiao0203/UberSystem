package ubersystem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ubersystem.mapper.OrderMapper;
import ubersystem.pojo.Order;
import ubersystem.service.OrderService;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

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
}
