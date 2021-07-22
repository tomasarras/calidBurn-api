package com.calidBurn.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@Column
	private String username;
	
	@Column
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Column
	@JsonIgnore
	private boolean admin;
	
	@OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Product> published;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Product> purchased;
	
	public void postProduct(Product product) {
		this.published.add(product);
	}
	
	public void purchaseProduct(Product product) {
		this.purchased.add(product);
	}
}
