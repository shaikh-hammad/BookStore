package ca.sheridancollege.hammadshaikh.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class Order {
    private Long id;
    private Long isbn;
    private int quantity;
    private String username;
    private double total;
}
