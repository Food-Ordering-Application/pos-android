package com.foa.smartpos.model;


import com.foa.smartpos.model.enums.StockState;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MenuItem {
	@SerializedName("id")
	private String id;
	@SerializedName("name")
	private String name;
	@SerializedName("menuGroupId")
	private String groupId;
	private String categoryName;
	@SerializedName("description")
	private String description;
	@SerializedName("price")
	private long price;
	@SerializedName("state")
	private StockState stockState;
	@SerializedName("imageUrl")
	private String image;
	@SerializedName("index")
	private float index;
	@SerializedName("isActive")
	private boolean isActive;

	private boolean selected = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public StockState getStockState() {
		return stockState;
	}

	public void setStockState(StockState stockState) {
		this.stockState = stockState;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public float getIndex() {
		return index;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
