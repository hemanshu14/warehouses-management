package com.fulfilment.application.monolith.warehouses.domain.ports;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

import java.util.List;

public interface WarehouseStore {

  List<Warehouse> getAllWarehouses();

  Warehouse create(Warehouse warehouse);

  void update(Warehouse warehouse);

  Warehouse findByWarehouseId(String id);

  void archive(String id);

  void remove(Warehouse warehouse);

  Warehouse findByBusinessUnitCode(String buCode);

  boolean existsByBusinessUnitCode(String businessUnitCode);

  int countByLocation(String locationId);
}
