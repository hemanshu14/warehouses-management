package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.exception.InvalidLocationException;
import com.fulfilment.application.monolith.exception.WarehouseNotFoundException;
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
public class ReplaceWarehouseUseCaseTest {

    @InjectMocks
    private ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @Mock
    private DefaultWarehouseValidator validator;

    @Test
    public void testUpdatingNonExistingWarehouse() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");

        //when
        Mockito.doThrow(new WarehouseNotFoundException("WareHouse does not exist")).when(validator).validateForReplaceWarehouse(warehouse);

        //then
        assertThrows(WarehouseNotFoundException.class, () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    public void testUpdatingWarehouseAtInvalidLocation() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");

        //when
        Mockito.doThrow(new InvalidLocationException("Invalid WareHouse location")).when(validator).validateForReplaceWarehouse(warehouse);

        //then
        assertThrows(InvalidLocationException.class, () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    public void testUpdatingWarehouseCapacityMoreThanExistingStock() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");

        //when
        Mockito.doThrow(new WarehouseValidationException("New warehouse capacity can not accommodate with existing stock")).when(validator).validateForReplaceWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> replaceWarehouseUseCase.replace(warehouse));
    }

    @Test
    public void testUpdatingWarehouseWithMoreStockDuringReplacement() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");

        //when
        Mockito.doThrow(new WarehouseValidationException("New warehouse stock must match with existing stock")).when(validator).validateForReplaceWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> replaceWarehouseUseCase.replace(warehouse));
    }
}
