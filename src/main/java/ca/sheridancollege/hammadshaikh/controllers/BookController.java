package ca.sheridancollege.hammadshaikh.controllers;

import ca.sheridancollege.hammadshaikh.beans.Book;
import ca.sheridancollege.hammadshaikh.beans.Order;
import ca.sheridancollege.hammadshaikh.database.DatabaseAccess;
import ca.sheridancollege.hammadshaikh.security.SecurityConfig;
import ca.sheridancollege.hammadshaikh.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class BookController {
    @Autowired
    private DatabaseAccess da;
    @Autowired
    SecurityConfig securityConfig;
    //Cart
    static List<Order> ordersList = new CopyOnWriteArrayList<>();
    //Map stores user carts upon logout
    static Map<String, List<Order>> cartMap = new HashMap<>();
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", da.getBookList());
        //check if user is authenticated, restore their previous cart from Map
        if (securityConfig.isAuthenticated() && securityConfig.getCurrentUser() != null){
            if (cartMap.get(securityConfig.getCurrentUser().getUsername()) != null){
                ordersList = cartMap.get(securityConfig.getCurrentUser().getUsername());
            }
            else {
                ordersList = new CopyOnWriteArrayList<>();
            }
            return "redirect:/secure";
        }

        return "login";

    }
    //Homepage in secure folder
    @GetMapping("/secure")
    public String secureIndex(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", da.getBookList());
        model.addAttribute("cart", ordersList);
        model.addAttribute("cartMap", cartMap);
        model.addAttribute("currentUser", securityConfig.getCurrentUser().getUsername());
        return "/secure/index";
    }
    //Admin access to modifying inventory
    @GetMapping("/secure/modify")
    public String modify(Model model) {
        model.addAttribute("book", new Book());
        return "/secure/modify";
    }
    //Form handler for inventory management
    @PostMapping("/secure/insertBook")
    public String insertBook(Model model, @ModelAttribute Book book){
        List<Book> existingBooks = da.getBookByIsbn(book.getIsbn());

        if (existingBooks.isEmpty()) {
            da.insertBook(book);
        } else {
            da.updateBook(book);
        }
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", da.getBookList());
        return "secure/index";
    }
    @GetMapping("/secure/deleteBookByIsbn/{isbn}")
    public String deleteBookByIsbn(Model model, @PathVariable Long isbn) {
        model.addAttribute("book", new Book());
        da.deleteBookByIsbn(isbn);
        model.addAttribute("bookList", da.getBookList());
        return "secure/index";
    }
    @GetMapping("/secure/editBookByIsbn/{isbn}")
    public String editBookByIsbn(Model model, @PathVariable Long isbn) {

        Book book = da.getBookByIsbn(isbn).get(0);

        model.addAttribute("book", book);
        model.addAttribute("bookList", da.getBookList());

        return "secure/modify";
    }
    @GetMapping("/secure/viewBookByIsbn/{isbn}")
    public String viewBookByIsbn(Model model, @PathVariable Long isbn) {

        Book book = da.getBookByIsbn(isbn).get(0);

        model.addAttribute("book", book);
        model.addAttribute("isbn2", isbn);
        model.addAttribute("order", new Order());

        return "secure/book";
    }
    //Shopping cart controller
    @GetMapping("/secure/shoppingCart")
    public String shoppingCart(Model model) {
        //model.addAttribute("currentUser", currentUser);
        model.addAttribute("cart", ordersList);
        model.addAttribute("da", da);
        model.addAttribute("cartTotal", getCartTotal(ordersList));

        return "secure/cart";
    }
    //Logic for adding book to user cart
    @PostMapping("/secure/addBookToCart")
    public String addBookToCart(Model model, @ModelAttribute Order order){
        //Add authenticated user's email to order info
        order.setUsername(securityConfig.getCurrentUser().getUsername());
        //Calculate subtotal for each order
        order.setTotal(order.getQuantity() * da.getBookByIsbn(order.getIsbn()).get(0).getPrice());
        //Add order to cart and save to Map
        ordersList.add(order);
        saveCart();
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", da.getBookList());
        return "secure/index";
    }
    //Checkout controller
    @GetMapping("/secure/checkout")
    public String checkout(Model model){
        da.insertOrders(ordersList);    //insert orders to database
        ordersList = new CopyOnWriteArrayList<>();  //clear cart and save to Map
        saveCart();
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", da.getBookList());
        return "secure/thankyou";
    }
    public void saveCart(){
        cartMap.put(securityConfig.getCurrentUser().getUsername(), ordersList);
    }
    public double getCartTotal(List<Order> ordersList){
        double total = 0.0;
        for (Order order:ordersList
        ) {
            total += order.getQuantity() * da.getBookByIsbn(order.getIsbn()).get(0).getPrice();
        }
        return total;
    }
}
