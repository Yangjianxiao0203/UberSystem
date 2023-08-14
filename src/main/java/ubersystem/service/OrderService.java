package ubersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.Enums.OrderStatus;
import ubersystem.pojo.Order;
import ubersystem.pojo.Ride;
import ubersystem.pojo.request.order.PaymentRequest;

import java.util.List;

@Service
public interface OrderService {
    @Transactional
    int create(Order order);
    @Transactional
    int update(Order order);
    @Transactional
    String createBillInOrderByRideId(Long rid);
    @Transactional
    String createBillInOrderByRide(Ride ride,Double estimateTime);
    @Transactional
    Order getOrderByOid(Long oid);
    @Transactional
    Order getOrderByRid(Long rid);

    @Transactional
    List<Order> getOrdersByStatus(OrderStatus status, Long uid);
    @Transactional
    String createPaymentRequest(PaymentRequest request,Long oid);
    @Transactional
    String checkPaymentRequest(PaymentRequest request,Long oid);


}
