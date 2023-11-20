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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class UserController {
    @Autowired
    private DatabaseAccess da2;
    @Autowired
    private SecurityConfig securityConfig;
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    @GetMapping("/createUser")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "createUser";
    }
    @GetMapping("/secure/manageUsers")
    public String manageUsers(Model model) {
        model.addAttribute("userList", da2.getUserList());
        model.addAttribute("da", da2);
        model.addAttribute("currentUser", securityConfig.getCurrentUser().getUsername());
        return "secure/manageUsers";
    }
    @GetMapping("/secure/toggleAdmin/{userId}")
    public String toggleAdmin(Model model, @PathVariable Long userId) {
        if (da2.getRolesById(userId).contains("ROLE_ADMIN")){
            da2.removeAdmin(userId);
        }
        else {
            da2.insertAdmin(userId);
        }
        model.addAttribute("userList", da2.getUserList());
        model.addAttribute("da", da2);
        model.addAttribute("currentUser", securityConfig.getCurrentUser().getUsername());
        return "secure/manageUsers";
    }
    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }
    //Handler to create new user
    @PostMapping("/insertUser")
    public String insertUser(Model model, @ModelAttribute User user){
        //If user does not exist, create and encode password
        if (da2.findUserAccount(user.getEmail()) == null) {
            String encrypted = securityConfig.encoder().encode(user.getEncryptedPassword());
            da2.insertUser(user.getEmail(), encrypted);
        } else {
            model.addAttribute("error_message", "User already exists!");
            return "createUser";
        }
        model.addAttribute("user", new User());
        return "login";
    }
    @GetMapping("/permission-denied")
    public String permissionDenied() {
        System.out.println("User: " + securityConfig.getCurrentUser().getAuthorities());
        System.out.println("User: " + securityConfig.isAuthenticated());
        return "/permission-denied";
    }
}
