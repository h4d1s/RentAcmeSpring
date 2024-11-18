package com.pixellabs.inventory.domain.variantAggregate;

import java.util.UUID;

import org.springframework.data.domain.AbstractAggregateRoot;

import com.pixellabs.inventory.domain.exceptions.InventoryDomainException;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.vehicleAggregate.Vehicle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Variant extends AbstractAggregateRoot<Variant> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	private Gearbox gearbox;
	private FuelType fuelType;
	public int power;
	public int engineSize;
	
	@ManyToOne
	@JoinColumn(name = "model_id")
	private Model model;
	
	@ManyToOne
    @JoinColumn(name = "vehicle_id")
	private Vehicle vehicle;

	public Variant(String name, Gearbox gearbox, FuelType fuelType, int power, int engineSize, Model model, Vehicle vehicle) throws InventoryDomainException {
		super();
		this.name = name;
		this.gearbox = gearbox;
		this.fuelType = fuelType;
		
		if(power <= 0) {
			throw new InventoryDomainException("Power must be grater than 0");
		}
		
		this.power = power;
		this.engineSize = engineSize;
		
		if(engineSize <= 0) {
			throw new InventoryDomainException("Engine size must be grater than 0");
		}
				
		this.model = model;
		this.vehicle = vehicle;
	}
}
