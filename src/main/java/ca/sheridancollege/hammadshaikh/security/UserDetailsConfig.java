/*package ca.sheridancollege.hammadshaikh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("frank@frank.com")
                .password(passwordEncoder.encode("123"))
                .roles("USER").build());
        manager.createUser(User.withUsername("fahad.jan@sheridancollege.ca")
                .password(passwordEncoder.encode("12345"))
                .roles("USER", "ADMIN").build());
        return manager;
    }
}*/
