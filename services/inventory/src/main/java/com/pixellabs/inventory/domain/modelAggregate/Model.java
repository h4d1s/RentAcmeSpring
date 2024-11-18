package com.pixellabs.inventory.domain.modelAggregate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.variantAggregate.FuelType;
import com.pixellabs.inventory.domain.variantAggregate.Gearbox;
import com.pixellabs.inventory.domain.variantAggregate.Variant;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Model extends AbstractAggregateRoot<Model> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	private int yearOfProduction;
	private int numberOfSeats;
	private Category category;
	
	@ManyToOne
    @JoinColumn(name = "brand_id")
	private Brand brand;
	
	@OneToMany
	private Set<Variant> variants;

	public Model(String name, int yearOfProduction, int numberOfSeats, Category category, Brand brand) throws InventoryDomainException {
		super();
		
		if(name.isEmpty()) {
			throw new InventoryDomainException("Model name cannot be empty.");
		}
		
		this.name = name;
		
		if(yearOfProduction < 1900) {
			throw new InventoryDomainException("Model year of production must be grater than 1900.");
		}
		
		if(yearOfProduction > LocalDate.now().getYear()) {
			throw new InventoryDomainException("Model year of production cannot be in the future.");
		}
		
		this.yearOfProduction = yearOfProduction;
		
		if(numberOfSeats <= 0) {
			throw new InventoryDomainException("Model number of seats must be greater than 0.");
		}
		
		this.numberOfSeats = numberOfSeats;
		
		this.category = category;
		this.brand = brand;
	}
	
	public void addVariant(String name, Gearbox gearbox, FuelType fuelType, int power, int engineSize, Model model, Vehicle vehicle) throws InventoryDomainException {
        Variant variant = new Variant(name, gearbox, fuelType, power, engineSize, model, vehicle);
        variants.add(variant);
    }

	public void addVariant(Variant variant) {
		variants.add(variant);
	}
	
    public void removeVariant(UUID id) throws InventoryDomainException {
        Optional<Variant> variantToRemove = this.variants.stream()
        		.filter(each -> each.equals(id))
        		.findAny();
        
        if (!variantToRemove.isPresent()) {
            throw new InventoryDomainException("Variant not found.");
        }

        variants.remove(variantToRemove.get());
    }
}
