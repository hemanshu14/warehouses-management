package com.fulfilment.application.monolith.mapper;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.time.ZoneId;

public class WarehouseMapper {

    public static Warehouse mapToWarehouse(DbWarehouse dbWarehouse) {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = dbWarehouse.businessUnitCode;
        warehouse.location = dbWarehouse.location;
        warehouse.capacity = dbWarehouse.capacity;
        warehouse.stock = dbWarehouse.stock;
        if (dbWarehouse.createdAt != null) {
            warehouse.creationAt = dbWarehouse.createdAt.atZone(ZoneId.systemDefault());
        }

        if (dbWarehouse.archivedAt != null) {
            warehouse.archivedAt = dbWarehouse.archivedAt.atZone(ZoneId.systemDefault());
        }
        return warehouse;
    }
}
