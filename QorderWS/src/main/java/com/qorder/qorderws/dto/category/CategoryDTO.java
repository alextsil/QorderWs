package com.qorder.qorderws.dto.category;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.qorder.qorderws.utils.providers.ReferenceProvider;

@JsonIgnoreProperties("context")
public class CategoryDTO {
	
	private Long id;
	private String name;
	private final String uri = ReferenceProvider.INSTANCE.getURIfor("category");
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + " id " + id;
	}
	
	public String getUri() {
		return uri + id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
}
