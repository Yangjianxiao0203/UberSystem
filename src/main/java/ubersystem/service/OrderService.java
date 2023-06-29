package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.pojo.Order;
import ubersystem.pojo.request.order.PaymentRequest;

@Service
public interface OrderService {
    String createBillInOrderByRideId(Long rid);
    Order getOrderByOid(Long oid);
    String createPaymentRequest(PaymentRequest request,Long oid);
    String checkPaymentRequest(PaymentRequest request,Long oid);
}
