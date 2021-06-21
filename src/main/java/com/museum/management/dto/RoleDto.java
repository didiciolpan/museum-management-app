package com.museum.management.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class RoleDto {

	private Integer id;

	@NotEmpty
	private String name;

	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
