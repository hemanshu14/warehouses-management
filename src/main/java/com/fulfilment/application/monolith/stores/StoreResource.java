package com.fulfilment.application.monolith.stores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fulfilment.application.monolith.exception.InvalidStoreException;
import com.fulfilment.application.monolith.exception.StoreNotFoundException;
import com.fulfilment.application.monolith.stores.event.StoreCreateEvent;
import com.fulfilment.application.monolith.stores.event.StoreUpdateEvent;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.List;

@Path("stores")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class StoreResource {

    private static final Logger LOGGER = Logger.getLogger(StoreResource.class.getName());
    @Inject
    Event<StoreCreateEvent> storeCreateEvent;
    @Inject
    Event<StoreUpdateEvent> storeUpdateEvent;

    @GET
    public List<Store> get() {
        LOGGER.infof("Getting list of ll the store names");
        return Store.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Store getSingle(Long id) {
        LOGGER.infof("Getting store with [id=%d]", id);
        Store entity = Store.findById(id);
        if (entity == null) {
            throw new StoreNotFoundException("Store with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Store store) {
        LOGGER.infof("Creating store [name=%s]", store.name);
        if (store.id != null) {
            throw new InvalidStoreException("Id was invalidly set on request.", 422);
        }

        store.persist();

        storeCreateEvent.fire(new StoreCreateEvent(store));

        LOGGER.infof("Store created successfully [id=%d]", store.id);
        return Response.ok(store).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Store update(Long id, Store updatedStore) {
        LOGGER.infof("Updating store [name=%s]", updatedStore.name);
        if (updatedStore.name == null) {
            throw new InvalidStoreException("Store Name was not set on request.", 422);
        }

        Store entity = Store.findById(id);

        if (entity == null) {
            throw new StoreNotFoundException("Store with id of " + id + " does not exist.", 404);
        }

        entity.name = updatedStore.name;
        entity.quantityProductsInStock = updatedStore.quantityProductsInStock;

        storeUpdateEvent.fire(new StoreUpdateEvent(entity));
        LOGGER.infof("Store updated successfully [id=%d]", updatedStore.id);
        return entity;
    }

    @PATCH
    @Path("{id}")
    @Transactional
    public Store patch(Long id, Store updatedStore) {
        LOGGER.infof("Updating store [name=%s]", updatedStore.name);
        if (updatedStore.name == null) {
            throw new InvalidStoreException("Store Name was not set on request.", 422);
        }

        Store entity = Store.findById(id);

        if (entity == null) {
            throw new StoreNotFoundException("Store with id of " + id + " does not exist.", 404);
        }

        if (entity.name != null) {
            entity.name = updatedStore.name;
        }

        if (entity.quantityProductsInStock != 0) {
            entity.quantityProductsInStock = updatedStore.quantityProductsInStock;
        }

        storeUpdateEvent.fire(new StoreUpdateEvent(entity));
        LOGGER.infof("Store updated successfully [id=%d]", updatedStore.id);

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
        LOGGER.infof("Deleting store [id=%d]", id);
        Store entity = Store.findById(id);
        if (entity == null) {
            throw new StoreNotFoundException("Store with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        LOGGER.infof("Store deleted successfully [id=%d]", id);
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = switch (exception) {
                case StoreNotFoundException e -> e.getErrorCode();
                case InvalidStoreException e -> e.getErrorCode();
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
