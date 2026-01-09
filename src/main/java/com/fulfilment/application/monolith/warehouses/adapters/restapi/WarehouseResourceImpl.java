package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fulfilment.application.monolith.exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.exception.WarehousePersistenceException;
import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.List;

import static com.fulfilment.application.monolith.mapper.WarehouseMapper.mapToWarehouse;
import static com.fulfilment.application.monolith.mapper.WarehouseMapper.mapToWarehouseBean;

public class WarehouseResourceImpl implements WarehouseResource {

    private static final Logger LOG = Logger.getLogger(WarehouseResourceImpl.class);

    private final WarehouseStore warehouseStore;

    public WarehouseResourceImpl(WarehouseStore warehouseStore) {
        this.warehouseStore = warehouseStore;
    }

    @Override
    public List<Warehouse> listAllWarehousesUnits() {
        return warehouseStore.getAllWarehouses().stream().map(WarehouseMapper::mapToWarehouseBean).toList();
    }

    @Override
    public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
        return mapToWarehouseBean(warehouseStore.create(mapToWarehouse(data)));
    }

    @Override
    public Warehouse getAWarehouseUnitByID(String id) {
        return mapToWarehouseBean(warehouseStore.findByWarehouseId(id));
    }

    @Override
    public void archiveAWarehouseUnitByID(String id) {
        warehouseStore.archive(id);
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOG.error("Failed to handle request", exception);

            int code = switch (exception) {
                case WarehousePersistenceException e -> 503;
                case WarehouseNotFoundException e -> 400;
                default -> 500;
            };

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code).entity(exceptionJson).build();
        }
    }
}
