package roman.lazarchik.FirstSecurityApp.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import roman.lazarchik.FirstSecurityApp.services.PersonDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig{

   private final PersonDetailsService personDetailsService;

    // Настраивает аутентификацию
    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    // Конфигурируем сам Spring Security
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Установить аутентификацию (Set authentication)
        http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());

       // http.csrf(AbstractHttpConfigurer::disable);

        // Установить авторизацию (Set authentication)
       http.authorizeHttpRequests(auth -> auth
               .requestMatchers("/auth/login", "/auth/registration", "/css/**", "/error").permitAll() // Эти две страницы доступны всем.
               .anyRequest().hasAnyRole("USER", "ADMIN"));

        // Установить пользовательскую страницу входа (Set custom login page)
        http.formLogin (formLogin -> formLogin
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/hello", true)
                .failureUrl("/auth/login?error"));

        // Установить выход (Set logout)
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login"));

//        http.sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
