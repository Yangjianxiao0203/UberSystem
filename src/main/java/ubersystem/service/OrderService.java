package ubersystem.service;

import org.springframework.stereotype.Service;
import ubersystem.pojo.Order;

@Service
public interface OrderService {
    String createBillInOrderByRideId(Long rid);
    Order getOrderByOid(Long oid);
}
