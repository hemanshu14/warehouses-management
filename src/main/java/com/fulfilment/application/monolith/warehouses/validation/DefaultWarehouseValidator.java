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
        Location location = locationResolver.resolveByIdentifier(warehouse.location);
        if (warehouseStore.existsByBusinessUnitCode(warehouse.businessUnitCode)) {
            throw new WarehouseValidationException("Warehouse with businessUnitCode '" + warehouse.businessUnitCode + "' already exists");
        }
        if (location == null || location.identification == null) {
            throw new InvalidLocationException("Invalid warehouse location: " + warehouse.location);
        }
        int existingCount = warehouseStore.countByLocation(location.identification);
        if (existingCount >= location.maxNumberOfWarehouses) {
            throw new WarehouseValidationException("Cannot create new warehouse at location '" + location.identification + "'. Maximum number of warehouses reached.");
        }
        if (warehouse.capacity > location.maxCapacity) {
            throw new WarehouseValidationException("Warehouse capacity (" + warehouse.capacity + ") exceeds max capacity for location (" + location.maxCapacity + ")");
        }

        if (warehouse.stock > warehouse.capacity) {
            throw new WarehouseValidationException("Warehouse stock (" + warehouse.stock + ") cannot exceed its capacity (" + warehouse.capacity + ")");
        }
    }


    @Override
    public void validateForReplaceWarehouse(Warehouse newWarehouse) {

        Warehouse existing = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
        if (existing == null) {
            throw new WarehouseNotFoundException("Warehouse with businessUnitCode '" + newWarehouse.businessUnitCode + "' does not exist");
        }

        Location location = locationResolver.resolveByIdentifier(newWarehouse.location);
        if (location == null || location.identification == null) {
            throw new InvalidLocationException("Invalid warehouse location: " + newWarehouse.location);
        }

        if (newWarehouse.capacity < existing.stock) {
            throw new WarehouseValidationException("New warehouse capacity (" + newWarehouse.capacity + ") cannot accommodate existing stock (" + existing.stock + ")");
        }

        if (!newWarehouse.stock.equals(existing.stock)) {
            throw new WarehouseValidationException("New warehouse stock (" + newWarehouse.stock + ") must match existing warehouse stock (" + existing.stock + ")");
        }
    }

    @Override
    public void validateForArchiveWarehouse(Warehouse warehouse) {
        Warehouse existing = warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode);

        if (existing == null) {
            throw new WarehouseNotFoundException("Warehouse does not exist");
        }

        if (existing.archivedAt != null) {
            throw new WarehouseValidationException("Warehouse is already archived");
        }
    }
}
