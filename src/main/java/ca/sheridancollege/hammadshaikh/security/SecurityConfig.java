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
    /*@Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("guest@guest.com")
                .password(passwordEncoder.encode("123"))
                .roles("GUEST").build());
        manager.createUser(User.withUsername("fahad.jan@sheridancollege.ca")
                .password(passwordEncoder.encode("123"))
                .roles("USER").build());
        manager.createUser(User.withUsername("admin@admin.com")
                .password(passwordEncoder.encode("123"))
                .roles("USER", "ADMIN").build());
        return manager;
    }

     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector
            introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        AntPathRequestMatcher logonMatcher = new AntPathRequestMatcher("/logon");
        AntPathRequestMatcher logoutMatcher = new AntPathRequestMatcher("/logout");
        AntPathRequestMatcher createMatcher = new AntPathRequestMatcher("/createUser");
        AntPathRequestMatcher h2Console = new AntPathRequestMatcher("/h2-console/**");
        AntPathRequestMatcher pDenied = new AntPathRequestMatcher("/permission-denied");
        AntPathRequestMatcher secure = new AntPathRequestMatcher("/secure/**");
        AntPathRequestMatcher index = new AntPathRequestMatcher("/");
        AntPathRequestMatcher unsecure = new AntPathRequestMatcher("/**");
        AntPathRequestMatcher images = new AntPathRequestMatcher("/images/**");
        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(mvc.pattern("/secure/**")).hasRole("USER")
                                .requestMatchers(mvc.pattern("/")).permitAll()
                                .requestMatchers(mvc.pattern("/createUser")).permitAll()
                                .requestMatchers(mvc.pattern("/insertUser")).permitAll()
                                .requestMatchers(mvc.pattern("/js/**")).permitAll()
                                .requestMatchers(mvc.pattern("/css/**")).permitAll()
                                .requestMatchers(mvc.pattern("/images/**")).permitAll()
                                .requestMatchers(mvc.pattern("/permission-denied")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                //.requestMatchers(mvc.pattern("/**")).denyAll()
                        //.requestMatchers(secure).hasRole("USER")
                        //.requestMatchers(logonMatcher, logoutMatcher, createMatcher, h2Console, pDenied,index, images).permitAll()
                        //.requestMatchers(unsecure).denyAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2- console/**")).disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .formLogin(form -> form.loginPage("/logon").permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
                .logout(logout -> logout.permitAll());
                /*
                .formLogin(formLogin -> formLogin.loginPage("/logon")).logout(logout -> logout.permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));

                 */
        return http.build();
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    public void addUser(String username, String password, String... roles) {
        userDetailsManager.createUser(
                User.withUsername(username)
                        .password(passwordEncoder.encode(password))
                        .roles(roles)
                        .build()
        );
    }
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
    public boolean isAuthenticated() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.isAuthenticated();
    }
}
