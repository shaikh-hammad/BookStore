package ca.sheridancollege.hammadshaikh.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private InMemoryUserDetailsManager userDetailsManager;
    Authentication authentication;
    @Autowired
    @Lazy
    public SecurityConfig(PasswordEncoder passwordEncoder, InMemoryUserDetailsManager userDetailsManager) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector
            introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(mvc.pattern("/secure/**")).hasRole("USER")
                                .requestMatchers(mvc.pattern("/secure/insertBook")).hasRole("ADMIN")
                                .requestMatchers(mvc.pattern("/secure/modify")).hasRole("ADMIN")
                                .requestMatchers(mvc.pattern("/secure/manageUsers")).hasRole("ADMIN")
                                .requestMatchers(mvc.pattern("/secure/toggleAdmin/**")).hasRole("ADMIN")
                                .requestMatchers(mvc.pattern("/")).permitAll()
                                .requestMatchers(mvc.pattern("/createUser")).permitAll()
                                .requestMatchers(mvc.pattern("/insertUser")).permitAll()
                                .requestMatchers(mvc.pattern("/js/**")).permitAll()
                                .requestMatchers(mvc.pattern("/css/**")).permitAll()
                                .requestMatchers(mvc.pattern("/images/**")).permitAll()
                                .requestMatchers(mvc.pattern("/permission-denied")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                //.requestMatchers(mvc.pattern("/**")).denyAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2- console/**")).disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .formLogin(form -> form.loginPage("/login").permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
                .logout(logout -> logout.permitAll());
        return http.build();
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    //Get current authenticated user information
    public UserDetails getCurrentUser() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No user is authenticated
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }
    //Quickly check for authentication
    public boolean isAuthenticated() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }
}
