package it.pagopa.pn.deliverypush.middleware.actiondao;

import it.pagopa.pn.commons.abstractions.KeyValueStore;
import it.pagopa.pn.deliverypush.middleware.actiondao.dynamo.FutureActionEntity;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Set;

public interface FutureActionEntityDao extends KeyValueStore<Key, FutureActionEntity> {
    Set<FutureActionEntity> findByTimeSlot(String timeSlot);
}
