package ca.sheridancollege.hammadshaikh.controllers;

import ca.sheridancollege.hammadshaikh.beans.Book;
import ca.sheridancollege.hammadshaikh.beans.Order;
import ca.sheridancollege.hammadshaikh.beans.User;
import ca.sheridancollege.hammadshaikh.database.DatabaseAccess;
import ca.sheridancollege.hammadshaikh.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class UserController {
    @Autowired
    private DatabaseAccess da2;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SecurityConfig securityConfig;
    @GetMapping("/logon")
    public String logon(Model model) {
        return "logon";
    }
    /*@PostMapping("/logon")
    public String logon(@RequestParam String username, @RequestParam String password) {
        // Authenticate the user
        try {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if (user.getPassword().equals(password)) {
                // Authentication successful, redirect to a protected page
                BookController.ordersList = new CopyOnWriteArrayList<>();
                return "redirect:secure/index";
            }
        } catch (UsernameNotFoundException e) {
            // Handle authentication failure, e.g., show an error message
        }

        // Authentication failed, return to the login page
        return "logon";
    }*/
    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }
    /*@GetMapping("/error")
    public String error(Model model) {
        return "error";
    }*/
    /*@PostMapping("/login")
    public String login(Model model, @RequestParam String username, @RequestParam String password) {
        if(da2.validateUser(username, password)) {
            BookController.currentUser = da2.getUserByUsername(username).get(0);
            return "redirect:/books";
        }
        return "index";
    }
    */
    @PostMapping("/insertUser")
    public String insertUser(Model model, @ModelAttribute User user){


        if (da2.findUserAccount(user.getEmail()) == null) {
            String encrypted = securityConfig.encoder().encode(user.getEncryptedPassword());
            da2.insertUser(user.getEmail(), encrypted);
        } else {
            model.addAttribute("error_message", "User already exists!");
            //System.out.println("User: " + securityConfig.getCurrentUser().getUsername());
            return "createUser";
        }
        model.addAttribute("user", new User());
        return "logon";
    }
    @GetMapping("/permission-denied")
    public String permissionDenied() {
        System.out.println("User: " + securityConfig.getCurrentUser().getAuthorities());
        System.out.println("User: " + securityConfig.isAuthenticated());
        return "/permission-denied";
    }
}
