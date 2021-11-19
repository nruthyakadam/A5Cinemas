package com.a5cinemas.user.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.a5cinemas.user.service.UserServiceImpl;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    private DataSource dataSource;
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }
	
    @Autowired
    private UserServiceImpl userService;

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(
                "/registration**",
                "/forgot_password**",
                "/select-movie**",
                "/verify**",
                "/reset_password**",
                "/js/**",
                "/static/css/**",
                "/img/**",
                "/webjars/**",
                "/edit/{id}",
                "/account**",
                "/save**").permitAll()
            .antMatchers("/add_new_movie").hasAnyAuthority("ADMIN")
            .antMatchers("/manage-promotions").hasAnyAuthority("ADMIN")
            .antMatchers("/schedule").hasAnyAuthority("ADMIN")
            .antMatchers("/select-time").hasAnyAuthority("USER","ADMIN")
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .rememberMe().tokenValiditySeconds(7 * 24 * 60 * 60)
            .key("AbcdefghiJklmNoPqRstUvXyz")
            .and()
            .logout()
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/login?logout")
            .permitAll();
            //.and()
           // .rememberMe().tokenRepository(persistentTokenRepository());
    }
    
  
//TODO
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//    	JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//		return tokenRepository;
//    	
//    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
