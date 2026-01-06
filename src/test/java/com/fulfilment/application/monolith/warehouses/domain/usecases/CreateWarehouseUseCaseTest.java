package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.exception.InvalidLocationException;
import com.fulfilment.application.monolith.exception.WarehouseValidationException;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.validation.DefaultWarehouseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CreateWarehouseUseCaseTest {

    @InjectMocks
    private CreateWarehouseUseCase createWarehouseUseCase;

    @Mock
    private DefaultWarehouseValidator validator;


    @Test
    public void testCreatingAlreadyExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";

        //when
        Mockito.doThrow(new WarehouseValidationException("WareHouse already exists")).when(validator).validateForCreateWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> createWarehouseUseCase.create(warehouse));
    }

    @Test
    public void testCreatingWarehouseAtInvalidLocation() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";

        //when
        Mockito.doThrow(new InvalidLocationException("Invalid WareHouse location")).when(validator).validateForCreateWarehouse(warehouse);

        //then
        assertThrows(InvalidLocationException.class, () -> createWarehouseUseCase.create(warehouse));
    }

    @Test
    public void testCreatingWarehouseAtLocationWithMaxCountReached() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";

        //when
        Mockito.doThrow(new WarehouseValidationException("Max number of warehouses reached at this location")).when(validator).validateForCreateWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> createWarehouseUseCase.create(warehouse));
    }

    @Test
    public void testCreatingWarehouseAtLocationWithMaxCapacityReached() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";

        //when
        Mockito.doThrow(new WarehouseValidationException("Max capacity of warehouses reached at this location")).when(validator).validateForCreateWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> createWarehouseUseCase.create(warehouse));
    }

    @Test
    public void testCreatingWarehouseWithMaxStockReached() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "1";

        //when
        Mockito.doThrow(new WarehouseValidationException("Warehouse stock is more than the capacity")).when(validator).validateForCreateWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> createWarehouseUseCase.create(warehouse));
    }

}
