package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.validation.WarehouseValidator;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

    private final WarehouseStore warehouseStore;
    private final WarehouseValidator validator;

    public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, WarehouseValidator validator) {
        this.warehouseStore = warehouseStore;
        this.validator = validator;
    }

    @Override
    public void replace(Warehouse newWarehouse) {
        validator.validateForReplaceWarehouse(newWarehouse);
        warehouseStore.update(newWarehouse);
    }
}
