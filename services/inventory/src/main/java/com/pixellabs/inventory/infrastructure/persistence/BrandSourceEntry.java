package com.pixellabs.inventory.infrastructure.persistence;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandSourceEntry {
	private String name;
    private List<ModelSourceEntry> models;
}
