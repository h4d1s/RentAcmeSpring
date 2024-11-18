package com.pixellabs.inventory.infrastructure.persistence;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelSourceEntry {
	private String name;
    private String category;
    private List<VariantSourceEntry> variants;
}
