package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.exception.WarehousePersistenceException;
import com.fulfilment.application.monolith.mapper.WarehouseMapper;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;

import static com.fulfilment.application.monolith.mapper.WarehouseMapper.mapToDBWarehouse;
import static com.fulfilment.application.monolith.mapper.WarehouseMapper.mapToWarehouse;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

    private static final Logger LOG = Logger.getLogger(WarehouseRepository.class);

    @Override
    public List<Warehouse> getAllWarehouses() {
        return listAll().stream().map(WarehouseMapper::mapToWarehouse).toList();
    }

    @Override
    @Transactional
    public Warehouse create(Warehouse warehouse) {
        try {
            DbWarehouse entity = mapToDBWarehouse(warehouse);
            persist(entity);
            warehouse.setWareHouseId(entity.getId());
            return warehouse;
        } catch (PersistenceException e) {
            LOG.error("Failed to create warehouse", e);
            throw new WarehousePersistenceException("Unable to create warehouse");

        }
    }

    @Override
    @Transactional
    public void update(Warehouse warehouse) {
        try {
            if (warehouse.getBusinessUnitCode() == null) {
                throw new IllegalArgumentException("Warehouse ID must not be null");
            }

            DbWarehouse entity = findById(warehouse.getWareHouseId());

            if (entity == null) {
                throw new WarehouseNotFoundException("Warehouse not found with id: " + warehouse.getWareHouseId());
            }

            entity.setBusinessUnitCode(warehouse.getBusinessUnitCode());
            entity.setLocation(warehouse.getLocation());
            entity.setCapacity(warehouse.getCapacity());
            entity.setStock(warehouse.getStock());
            if (warehouse.getCreationAt() != null) {
                entity.setCreatedAt(LocalDateTime.from(warehouse.getCreationAt()));
            }

            if (warehouse.getArchivedAt() != null) {
                entity.setArchivedAt(LocalDateTime.from(warehouse.getArchivedAt()));
            }

        } catch (EntityNotFoundException e) {
            LOG.warn(e.getMessage());
            throw e;

        } catch (PersistenceException e) {
            LOG.error("Failed to update warehouse", e);
            throw new WarehousePersistenceException("Unable to update warehouse with id: " + warehouse.getWareHouseId());

        } catch (Exception e) {
            LOG.error("Unexpected error during warehouse update");
            throw new WarehousePersistenceException("Unexpected error while updating warehouse");
        }
    }

    @Override
    public Warehouse findByWarehouseId(String id) {

        DbWarehouse entity = findById(Long.getLong(id));

        if (entity == null) {
            throw new WarehouseNotFoundException("Warehouse not found with id: " + id);
        }

        return mapToWarehouse(entity);
    }

    @Override
    @Transactional
    public void archive(String id) {
        DbWarehouse entity = findById(Long.getLong(id));

        if (entity == null) {
            throw new WarehouseNotFoundException("Warehouse not found with id: " + id);
        }
        entity.setArchivedAt(LocalDateTime.now());
    }

    @Override
    public void remove(Warehouse warehouse) {
        try {
            if (warehouse.getWareHouseId() == null) {
                throw new IllegalArgumentException("Warehouse ID must not be null");
            }

            boolean deleted = deleteById(warehouse.getWareHouseId());

            if (!deleted) {
                throw new WarehouseNotFoundException("Warehouse not found with id: " + warehouse.getWareHouseId());
            }

        } catch (EntityNotFoundException e) {
            LOG.warn(e.getMessage());
            throw e;

        } catch (PersistenceException e) {
            LOG.error("Failed to remove warehouse", e);
            throw new WarehousePersistenceException("Unable to delete warehouse with id: " + warehouse.getWareHouseId());

        } catch (Exception e) {
            LOG.error("Unexpected error during warehouse removal", e);
            throw new WarehousePersistenceException("Unexpected error while deleting warehouse");
        }
    }

    @Override
    public Warehouse findByBusinessUnitCode(String buCode) {
        return find("businessUnitCode", buCode).firstResultOptional().map(WarehouseMapper::mapToWarehouse).orElse(null);
    }

    @Override
    public boolean existsByBusinessUnitCode(String buCode) {
        return find("businessUnitCode", buCode).firstResultOptional().isPresent();
    }

    @Override
    public int countByLocation(String locationId) {
        return Math.toIntExact(count("location", locationId));
    }
}
