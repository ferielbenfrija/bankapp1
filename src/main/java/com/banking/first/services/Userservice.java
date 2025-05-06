package com.banking.first.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.banking.first.entities.AppUser;
import com.banking.first.repositories.UserRepository;

@Service
public class Userservice implements UserDetailsService {
@Autowired
private UserRepository repo;
	
	@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
	AppUser appuser =repo.findByEmail(email);
	if (appuser != null) { 
		var springUser = User.withUsername (appuser.getEmail()) 
		.password(appuser.getPassworld()) 
		.roles (appuser.getRole()) 
		.build(); 
		return springUser; 
		}
	
	throw new UsernameNotFoundException("User not found with email: " + email);


}
}
