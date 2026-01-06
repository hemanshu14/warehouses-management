package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.validation.WarehouseValidator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArchiveWarehouseUseCase implements ArchiveWarehouseOperation {

    private final WarehouseStore warehouseStore;
    private final WarehouseValidator validator;

    public ArchiveWarehouseUseCase(WarehouseStore warehouseStore, WarehouseValidator validator) {
        this.warehouseStore = warehouseStore;
        this.validator = validator;
    }

    @Override
    public void archive(Warehouse warehouse) {
        validator.validateForArchiveWarehouse(warehouse);

        warehouseStore.update(warehouse);
    }
}
