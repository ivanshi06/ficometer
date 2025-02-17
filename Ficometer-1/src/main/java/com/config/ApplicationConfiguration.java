package com.config;


	import java.util.Collection;
	import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.security.authentication.AuthenticationManager;
	import org.springframework.security.authentication.AuthenticationProvider;
	import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
	import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
	import org.springframework.security.core.GrantedAuthority;
	import org.springframework.security.core.authority.SimpleGrantedAuthority;
	import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
	import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.model.Customer;
import com.repo.ICustomerRepo;
	
	@Configuration
	public class ApplicationConfiguration {
	    private final ICustomerRepo customerRepository;
	    public ApplicationConfiguration(ICustomerRepo customerRepository) {
	        this.customerRepository = customerRepository;
	    }
	    @Bean
	    public UserDetailsService userDetailsService() {
	        return username -> {
	            Optional<Customer> optionalCustomer = customerRepository.findByEmail(username);
	            if (!optionalCustomer.isPresent()) {
	                throw new UsernameNotFoundException("User not found");
	            }
	            return new org.springframework.security.core.userdetails.User(
	                    optionalCustomer.get().getEmail(),
	                    optionalCustomer.get().getPassword(),
	                    List.of(new SimpleGrantedAuthority("USER"))
	            );
	        };
	    }
	    
	 

	    @Bean
	    BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	 
	 
//	    @Bean
//	    public GrantedAuthoritiesMapper authoritiesMapper() {
//	        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
//	        mapper.mapAuthorities(authorities)
//	        mapper.setConvertToUpperCase(true); // Convert authorities to uppercase
//	        mapper.setDefaultAuthority("USER"); // Default authority if none provided
//	        
//	        return mapper;
//	    }
	    @Bean
	    AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());
//	        authProvider.setAuthoritiesMapper(authoritiesMapper());
	        return authProvider;
	    }
	}
	