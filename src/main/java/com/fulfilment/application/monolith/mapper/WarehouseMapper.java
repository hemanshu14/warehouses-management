package com.fulfilment.application.monolith.mapper;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class WarehouseMapper {

    public static Warehouse mapToWarehouse(DbWarehouse dbWarehouse) {
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode(dbWarehouse.getBusinessUnitCode());
        warehouse.setLocation(dbWarehouse.getLocation());
        warehouse.setCapacity(dbWarehouse.getCapacity());
        warehouse.setStock(dbWarehouse.getStock());
        if (dbWarehouse.getCreatedAt() != null) {
            warehouse.setCreationAt(dbWarehouse.getCreatedAt().atZone(ZoneId.systemDefault()));
        }

        if (dbWarehouse.getArchivedAt() != null) {
            warehouse.setArchivedAt(dbWarehouse.getArchivedAt().atZone(ZoneId.systemDefault()));
        } return warehouse;
    }

    public static com.warehouse.api.beans.Warehouse mapToWarehouseBean(Warehouse warehouse) {
        com.warehouse.api.beans.Warehouse warehouseBean = new com.warehouse.api.beans.Warehouse();
        warehouseBean.setId(warehouse.getWareHouseId().toString());
        warehouseBean.setLocation(warehouse.getLocation());
        warehouseBean.setCapacity(warehouse.getCapacity());
        warehouseBean.setStock(warehouse.getStock());
         return warehouseBean;
    }

    public static Warehouse mapToWarehouse(com.warehouse.api.beans.Warehouse warehouseBean) {
        Warehouse warehouse = new Warehouse();
        warehouse.setLocation(warehouseBean.getLocation());
        warehouse.setCapacity(warehouseBean.getCapacity());
        warehouse.setStock(warehouseBean.getStock());
        warehouse.setCreationAt(ZonedDateTime.now());
        return warehouse;
    }

    public static DbWarehouse mapToDBWarehouse(Warehouse warehouse) {
        DbWarehouse dbWarehouse = new DbWarehouse();
        dbWarehouse.setBusinessUnitCode(warehouse.getBusinessUnitCode());
        dbWarehouse.setLocation(warehouse.getLocation());
        dbWarehouse.setCapacity(warehouse.getCapacity());
        dbWarehouse.setStock(warehouse.getStock());
        if (warehouse.getCreationAt() != null) {
            dbWarehouse.setCreatedAt(LocalDateTime.from(warehouse.getCreationAt()));
        }

        if (warehouse.getArchivedAt() != null) {
            dbWarehouse.setArchivedAt(LocalDateTime.from(warehouse.getArchivedAt()));
        }
        return dbWarehouse;
    }
}
