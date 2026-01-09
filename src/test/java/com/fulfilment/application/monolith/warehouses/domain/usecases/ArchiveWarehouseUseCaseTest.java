package com.fulfilment.application.monolith.warehouses.domain.usecases;

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
public class ArchiveWarehouseUseCaseTest {

    @InjectMocks
    private ArchiveWarehouseUseCase archiveWarehouseUseCase;

    @Mock
    private DefaultWarehouseValidator validator;

    @Test
    public void testWhenWarehouseDoesNotExistAndTriedToArchiveIt() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");

        //when
        Mockito.doThrow(new WarehouseNotFoundException("WareHouse not found")).when(validator).validateForArchiveWarehouse(warehouse);

        //then
        assertThrows(WarehouseNotFoundException.class, () -> archiveWarehouseUseCase.archive(warehouse));
    }

    @Test
    public void testWhenWarehouseAlreadyArchivedAndTriedToArchiveIt() {
        //given
        Warehouse warehouse = new Warehouse();
        warehouse.setBusinessUnitCode("1");

        //when
        Mockito.doThrow(new WarehouseValidationException("Warehouse is already archived")).when(validator).validateForArchiveWarehouse(warehouse);

        //then
        assertThrows(WarehouseValidationException.class, () -> archiveWarehouseUseCase.archive(warehouse));
    }
}
