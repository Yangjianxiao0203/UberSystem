package ubersystem.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubersystem.pojo.DriverAcceptOrderRequest;
import ubersystem.pojo.Order;
import ubersystem.pojo.OrderCreationRequest;
import ubersystem.pojo.Ride;

@Service
public interface DistributionService {
    @Transactional
    public String driverAcceptsOrder(DriverAcceptOrderRequest request);

    public String createOrderAndRide(OrderCreationRequest request);
}
