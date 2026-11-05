package com.pixellabs.inventory.infrastructure.persistence.seeding;

import tools.jackson.databind.ObjectMapper;
import com.pixellabs.inventory.domain.brandAggregate.Brand;
import com.pixellabs.inventory.domain.brandAggregate.BrandRepository;
import com.pixellabs.inventory.domain.modelAggregate.Category;
import com.pixellabs.inventory.domain.modelAggregate.Model;
import com.pixellabs.inventory.domain.variantAggregate.*;
import com.pixellabs.inventory.domain.vehicleAggregate.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryDbSeeder implements ApplicationRunner {
    private final BrandRepository brandRepository;
    private final VehicleRepository vehicleRepository;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        if (brandRepository.count() > 0) {
            log.info("Database already seeded. Skipping seeding.");
            return;
        }

        try {
            log.info("Starting inventory database seeding.");

            ClassPathResource resource =
                    new ClassPathResource("json/brands.json");

            BrandSourceEntry[] sourceBrands = objectMapper.readValue(
                resource.getInputStream(),
                BrandSourceEntry[].class
            );

            for (BrandSourceEntry brandSource : sourceBrands) {
                Brand brand = new Brand(brandSource.name());
                for (ModelSourceEntry modelSource : brandSource.models()) {
                    int year = random.nextInt(
                        Year.now().getValue() - 5,
                        Year.now().getValue() + 1
                    );
                    int seats = random.nextInt(2, 7);
                    Category category = randomEnum(Category.class);
                    Model model = new Model(
                        modelSource.name(),
                        year,
                        seats,
                        category,
                        brand
                    );

                    for (VariantSourceEntry variantSource : modelSource.variants()) {
                        Gearbox gearbox = randomEnum(Gearbox.class);
                        FuelType fuelType = randomEnum(FuelType.class);

                        int power = random.nextInt(44, 300);
                        int engineSize = random.nextInt(1000, 4000);

                        model.addVariant(
                            variantSource.name(),
                            gearbox,
                            fuelType,
                            power,
                            engineSize
                        );
                    }
                    brand.addModel(model);
                }

                Brand savedBrand = brandRepository.save(brand);

                // Create vehicles for each variant.
                for (Model model : savedBrand.getModels()) {
                    for (Variant variant : model.getVariants()) {
                        BigDecimal rentalPrice = BigDecimal.valueOf(
                            10 + random.nextDouble() * 490
                        );

                        String plates = generateRandomRegistrationPlates();
                        int mileage = random.nextInt(100, 200000);
                        Vehicle vehicle = new Vehicle(
                            rentalPrice,
                            plates,
                            VehicleStatus.AVAILABLE,
                            mileage
                        );

                        vehicle.updateVariant(variant);
                        vehicleRepository.save(vehicle);
                    }
                }
            }

            log.info("Inventory database seeding completed successfully.");
        } catch (Exception ex) {
            log.error("An error occurred while seeding the database.", ex);
            throw new IllegalStateException("Database seeding failed.", ex);
        }
    }

    private <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    private String generateRandomRegistrationPlates() {
        StringBuilder plates = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            plates.append((char) ('A' + random.nextInt(26)));
        }

        return plates.toString();
    }

    public record BrandSourceEntry(
        String name,
        List<ModelSourceEntry> models
    ) {}

    public record ModelSourceEntry(
        String name,
        List<VariantSourceEntry> variants,
        String category
    ) {}

    public record VariantSourceEntry(
        String name
    ) {}
}
