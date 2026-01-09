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
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");

        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(null);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(true);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithInvalidLocationOfWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");

        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(null);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(false);

        //then
        assertThrows(InvalidLocationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithMaxNumberOfWarehouses() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(false);
        Mockito.when(warehouseStore.countByLocation(location.identification)).thenReturn(33);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithMaxCapacityOfWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");
        warehouse.setCapacity(31);

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(false);
        Mockito.when(warehouseStore.countByLocation(location.identification)).thenReturn(33);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForCreateWarehouseWithStockOfWarehouseMoreThanItsCapacity() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");
        warehouse.setCapacity(31);
        warehouse.setStock(35);

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        Mockito.when(warehouseStore.existsByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(false);
        Mockito.when(warehouseStore.countByLocation(location.identification)).thenReturn(33);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForCreateWarehouse(warehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithNonExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");

        //when
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(null);

        //then
        assertThrows(WarehouseNotFoundException.class, () -> validator.validateForReplaceWarehouse(warehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithInvalidLocationOfWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");

        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(null);
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(warehouse);

        //then
        assertThrows(InvalidLocationException.class, () -> validator.validateForReplaceWarehouse(warehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithNewWarehouseCapacityLessThanExistingStock() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");
        warehouse.setStock(32);

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("1");
        newWarehouse.setLocation("ncr");
        newWarehouse.setCapacity(31);

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForReplaceWarehouse(newWarehouse));
    }

    @Test
    public void testValidateForReplaceWarehouseWithNewWarehouseStockNotEqualToExistingStock() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");
        warehouse.setStock(32);

        Warehouse newWarehouse = new Warehouse();
        newWarehouse.setBusinessUnitCode("1");
        newWarehouse.setLocation("ncr");
        newWarehouse.setCapacity(31);
        newWarehouse.setStock(33);

        Location location = new Location("1", 32, 30);


        //when
        Mockito.when(locationResolver.resolveByIdentifier(warehouse.getLocation())).thenReturn(location);
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForReplaceWarehouse(newWarehouse));
    }

    @Test
    public void testValidateForArchiveWarehouseWithNonExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");

        //when
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(null);

        //then
        assertThrows(WarehouseNotFoundException.class, () -> validator.validateForArchiveWarehouse(warehouse));
    }

    @Test
    public void testValidateForArchiveWarehouseWithAlreadyArchivedWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");
        warehouse.setLocation("ncr");
        warehouse.setArchivedAt(ZonedDateTime.now());

        //when
        Mockito.when(warehouseStore.findByBusinessUnitCode(warehouse.getBusinessUnitCode())).thenReturn(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> validator.validateForArchiveWarehouse(warehouse));
    }
}
