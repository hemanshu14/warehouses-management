package com.fulfilment.application.monolith.warehouses.validation;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

public interface WarehouseValidator {
    void validateForCreateWarehouse(Warehouse warehouse);

    void validateForReplaceWarehouse(Warehouse newWarehouse);

    void validateForArchiveWarehouse(Warehouse warehouse);
}
