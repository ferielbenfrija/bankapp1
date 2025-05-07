package com.banking.first.entities;

import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "DEPOSIT" ou "WITHDRAWAL"

    private Double amount;

    private LocalDateTime date;

    @ManyToOne
    private AppUser user;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;

	}

	public Transaction() {
		super();
	}

	public Transaction(AppUser user,String type, Double amount) {
		super();
		this.type = type;
		this.amount = amount;
		this.user = user;
        this.date = LocalDateTime.now();

	}

	public Transaction(String type, Double amount, AppUser user) {
		super();
		this.type = type;
		this.amount = amount;
		this.user = user;
        this.date = LocalDateTime.now();

	}

}

