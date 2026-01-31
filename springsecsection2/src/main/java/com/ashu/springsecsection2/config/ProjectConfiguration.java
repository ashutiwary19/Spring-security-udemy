package com.ashu.springsecsection2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.autoconfigure.security.SecurityProperties;

@Configuration
public class ProjectConfiguration {

	@Bean
	@Order(SecurityProperties.DEFAULT_FILTER_ORDER)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests(requests -> requests.anyRequest().denyAll());
//		http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
//		http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
		http.authorizeHttpRequests(
				requests -> requests.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
						.requestMatchers("/notices", "/contact", "/error").permitAll());
		http.formLogin(withDefaults());
//		http.formLogin(flc -> flc.disable());
		http.httpBasic(withDefaults());
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
		// Udemy@ss1234.com
		UserDetails user = User.withUsername("ashu_tiwary")
				.password("{bcrypt}$2a$12$Bv/sbW0vmdaiS8z7JYiR1ufOjXWWnkcR2j/7xr/JHPxZ58D9zK52S").authorities("read")
				.build();
		UserDetails admin = User.withUsername("admin").password("{noop}Demo@123").authorities("admin").build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CompromisedPasswordChecker compromisedPasswordChecker() {
		return new HaveIBeenPwnedRestApiPasswordChecker();
	}

}
