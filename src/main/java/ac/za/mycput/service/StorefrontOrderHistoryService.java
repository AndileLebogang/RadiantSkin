package ac.za.mycput.service;

import ac.za.mycput.domain.Customer;
import ac.za.mycput.dto.OrderSummary;
import ac.za.mycput.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StorefrontOrderHistoryService {
    private final OrderRepository orders;

    public StorefrontOrderHistoryService(OrderRepository orders) {
        this.orders = orders;
    }

    @Transactional(readOnly = true)
    public List<OrderSummary> forCustomer(Customer customer) {
        return orders.findByCustomerOrderByOrderDateDescOrderIdDesc(customer).stream()
                .map(OrderSummary::from)
                .toList();
    }
}
