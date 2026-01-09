package com.fulfilment.application.monolith.warehouses.validation;

import com.fulfilment.application.monolith.exception.InvalidLocationException;
import com.fulfilment.application.monolith.exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.exception.WarehouseValidationException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultWarehouseValidator implements WarehouseValidator {

    private final WarehouseStore warehouseStore;
    private final LocationResolver locationResolver;

    public DefaultWarehouseValidator(WarehouseStore warehouseStore, LocationResolver locationResolver) {
        this.warehouseStore = warehouseStore;
        this.locationResolver = locationResolver;
    }

    @Override
    public void validateForCreateWarehouse(Warehouse warehouse) {
        Location location = locationResolver.resolveByIdentifier(warehouse.getLocation());
        if (warehouseStore.existsByBusinessUnitCode(warehouse.getBusinessUnitCode())) {
            throw new WarehouseValidationException("Warehouse with businessUnitCode '" + warehouse.getBusinessUnitCode() + "' already exists");
        }
        if (location == null || location.identification == null) {
            throw new InvalidLocationException("Invalid warehouse location: " + warehouse.getLocation());
        }
        int existingCount = warehouseStore.countByLocation(location.identification);
        if (existingCount >= location.maxNumberOfWarehouses) {
            throw new WarehouseValidationException("Cannot create new warehouse at location '" + location.identification + "'. Maximum number of warehouses reached.");
        }
        if (warehouse.getCapacity() > location.maxCapacity) {
            throw new WarehouseValidationException("Warehouse capacity (" + warehouse.getCapacity() + ") exceeds max capacity for location (" + location.maxCapacity + ")");
        }

        if (warehouse.getStock() > warehouse.getCapacity()) {
            throw new WarehouseValidationException("Warehouse stock (" + warehouse.getStock() + ") cannot exceed its capacity (" + warehouse.getCapacity() + ")");
        }
    }


    @Override
    public void validateForReplaceWarehouse(Warehouse newWarehouse) {

        Warehouse existing = warehouseStore.findByBusinessUnitCode(newWarehouse.getBusinessUnitCode());
        if (existing == null) {
            throw new WarehouseNotFoundException("Warehouse with businessUnitCode '" + newWarehouse.getBusinessUnitCode() + "' does not exist");
        }

        Location location = locationResolver.resolveByIdentifier(newWarehouse.getLocation());
        if (location == null || location.identification == null) {
            throw new InvalidLocationException("Invalid warehouse location: " + newWarehouse.getLocation());
        }

        if (newWarehouse.getCapacity() < existing.getStock()) {
            throw new WarehouseValidationException("New warehouse capacity (" + newWarehouse.getCapacity() + ") cannot accommodate existing stock (" + existing.getStock() + ")");
        }

        if (!newWarehouse.getStock().equals(existing.getStock())) {
            throw new WarehouseValidationException("New warehouse stock (" + newWarehouse.getStock() + ") must match existing warehouse stock (" + existing.getStock() + ")");
        }
    }

    @Override
    public void validateForArchiveWarehouse(Warehouse warehouse) {
        Warehouse existing = warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode());

        if (existing == null) {
            throw new WarehouseNotFoundException("Warehouse does not exist");
        }

        if (existing.getArchivedAt() != null) {
            throw new WarehouseValidationException("Warehouse is already archived");
        }
    }
}
