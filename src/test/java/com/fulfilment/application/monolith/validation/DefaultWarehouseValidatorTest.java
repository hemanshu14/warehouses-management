package com.fulfilment.application.monolith.validation;

import com.fulfilment.application.monolith.exception.InvalidLocationException;
import com.fulfilment.application.monolith.exception.WarehouseNotFoundException;
import com.fulfilment.application.monolith.exception.WarehouseValidationException;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import com.fulfilment.application.monolith.warehouses.validation.DefaultWarehouseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DefaultWarehouseValidatorTest {

    @InjectMocks
    private DefaultWarehouseValidator validator;

    @Mock
    private WarehouseStore warehouseStore;

    @Mock
    private LocationResolver locationResolver;

    @Test
    public void testValidateForCreateWarehouseWithAlreadyExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";

        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(null);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(true);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithInvalidLocationOfWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";

        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(null);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(false);

        //then
        assertThrows(InvalidLocationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithMaxNumberOfWarehouses() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(location);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(false);
        Mockito.when(warehouseStore.countByLocation(location.identification)).thenReturn(33);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithMaxCapacityOfWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";
        warehouse.capacity = 31;

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(location);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(false);
        Mockito.when(warehouseStore.countByLocation(location.identification)).thenReturn(33);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithStockOfWarehouseMoreThanItsCapacity() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";
        warehouse.capacity = 31;
        warehouse.stock = 35;

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(location);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(false);
        Mockito.when(warehouseStore.countByLocation(location.identification)).thenReturn(33);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithNonExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";

        //when
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(null);

        //then
        assertThrows(WarehouseNotFoundException.class, () -> validator.validateForReplaceWarehouse(warehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithInvalidLocationOfWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";

        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(null);
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(warehouse);

        //then
        assertThrows(InvalidLocationException.class, () -> validator.validateForReplaceWarehouse(warehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithNewWarehouseCapacityLessThanExistingStock() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";
        warehouse.stock = 32;

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "1";
        newWarehouse.location = "ncr";
        newWarehouse.capacity = 31;

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(location);
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForReplaceWarehouse(newWarehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithNewWarehouseStockNotEqualToExistingStock() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";
        warehouse.stock = 32;

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.businessUnitCode = "1";
        newWarehouse.location = "ncr";
        newWarehouse.capacity = 31;
        newWarehouse.stock = 33;

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.location)).thenReturn(location);
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForReplaceWarehouse(newWarehouse));
    }

    @Test
    public void testValidateForArchiveWarehouseWithNonExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";

        //when
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(null);

        //then
        assertThrows(WarehouseNotFoundException.class, () -> validator.validateForArchiveWarehouse(warehouse));
    }

    @Test
    public void testValidateForArchiveWarehouseWithAlreadyArchivedWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";
        warehouse.location = "ncr";
        warehouse.archivedAt = ZonedDateTime.now();

        //when
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode)).thenReturn(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForArchiveWarehouse(warehouse));
    }
}
