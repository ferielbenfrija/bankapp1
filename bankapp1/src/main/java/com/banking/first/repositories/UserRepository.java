package com.banking.first.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banking.first.entities.AppUser;


public interface UserRepository extends JpaRepository<AppUser,Integer>{

	public AppUser findByEmail(String email);
}
