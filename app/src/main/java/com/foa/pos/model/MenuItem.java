package com.foa.pos.model;


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
	private long discount;
	private Date createdAt;
	private Date updatedAt;
	private Date syncedAt;
	private String merchantId;
	private String status;
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

	public long getDiscount() {
		return discount;
	}

	public void setDiscount(long discount) {
		this.discount = discount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getSyncedAt() {
		return syncedAt;
	}

	public void setSyncedAt(Date syncedAt) {
		this.syncedAt = syncedAt;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
