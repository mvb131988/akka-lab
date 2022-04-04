package com;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

	final List<Item> items;

	@JsonCreator
	Order(@JsonProperty("items") List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

}
