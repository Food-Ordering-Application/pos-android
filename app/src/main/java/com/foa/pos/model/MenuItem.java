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
	private Date createdOn;
	private Date updatedOn;
	private Date sycnOn;
	private String createdBy;
	private String updatedBy;
	private String merchantId;
	private String status; // ready or no
	@SerializedName("imageUrl")
	private String image;
	@SerializedName("index")
	private float index;
	@SerializedName("isActive")
	private boolean isActive;

	// field for set list view selector, android only
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getSycnOn() {
		return sycnOn;
	}

	public void setSycnOn(Date sycnOn) {
		this.sycnOn = sycnOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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
}
