package com.banking.first.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table (name="users")
public class AppUser {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private int id;
private String firstname;
private String lastname;
@Column(unique=true , nullable=false)
private String email;
private String phone;
private String address;
private String passworld;
private String role;
private Date createdAt;
@Column(nullable = false)
private double balance = 0.0; 

@OneToMany(mappedBy = "user")
private List<Transaction> transactions;
public List<Transaction> getTransactions() {
    return transactions;
}

public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
}


public double getBalance() {
	return balance;
}
public void setBalance(double balance) {
	this.balance = balance;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getFirstname() {
	return firstname;
}
public void setFirstname(String firstname) {
	this.firstname = firstname;
}
public String getLastname() {
	return lastname;
}
public void setLastname(String lastname) {
	this.lastname = lastname;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getPassworld() {
	return passworld;
}
public void setPassworld(String passworld) {
	this.passworld = passworld;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}
public Date getCreatedAt() {
	return createdAt;
}
public void setCreatedAt(Date createdAt) {
	this.createdAt = createdAt;
}

}
