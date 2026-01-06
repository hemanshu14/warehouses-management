package com.fulfilment.application.monolith.stores.listener;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.event.StoreCreateEvent;
import com.fulfilment.application.monolith.stores.event.StoreUpdateEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StoreLegacyListener {

    private static final Logger LOGGER = Logger.getLogger(StoreLegacyListener.class.getName());

    @Inject
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    public void onStoreCreate(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreCreateEvent storeCreateEvent) {
        LOGGER.infof("Syncing store creation to legacy system [id=%d]", storeCreateEvent.store().id);

        try {
            legacyStoreManagerGateway.createStoreOnLegacySystem(storeCreateEvent.store());
        } catch (Exception exception) {
            LOGGER.errorf(exception, "Failed to sync store creation to legacy system [id=%d]", storeCreateEvent.store().id);
            throw exception;
        }
    }

    public void onStoreUpdate(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreUpdateEvent storeUpdateEvent) {
        LOGGER.infof("Syncing store update to legacy system [id=%d]", storeUpdateEvent.store().id);

        try {
            legacyStoreManagerGateway.updateStoreOnLegacySystem(storeUpdateEvent.store());
        } catch (Exception exception) {
            LOGGER.errorf(exception, "Failed to sync store update to legacy system [id=%d]", storeUpdateEvent.store().id);
            throw exception;
        }
    }
}
