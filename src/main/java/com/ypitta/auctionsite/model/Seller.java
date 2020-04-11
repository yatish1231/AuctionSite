package com.ypitta.auctionsite.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "sellers")
@PrimaryKeyJoinColumn(name = "id")
public class Seller extends User{
	
	@Column(name = "social_id", nullable = false)
	private String social_id;
	
	@OneToMany(mappedBy = "seller", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Product> products;

	public String getSocial_id() {
		return social_id;
	}

	public void setSocial_id(String social_id) {
		this.social_id = social_id;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void setUserSuper(User user) {
		super.setFirstname(user.getFirstname());
		super.setLastname(user.getLastname());
		super.setPassword(user.getPassword());
		super.setUsername(user.getUsername());
		super.setUserroles(user.getUserroles());
		
	}
}
