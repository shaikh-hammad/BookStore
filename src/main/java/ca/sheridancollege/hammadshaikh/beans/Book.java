package ca.sheridancollege.hammadshaikh.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
@Data
@NoArgsConstructor
@Component
public class Book {
    private Long isbn;
    private String title;
    private String author;
    private double price;
    private String description;
}
