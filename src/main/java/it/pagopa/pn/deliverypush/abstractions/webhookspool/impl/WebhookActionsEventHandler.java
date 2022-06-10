package it.pagopa.pn.deliverypush.abstractions.webhookspool.impl;


import it.pagopa.pn.commons.exceptions.PnInternalException;
import it.pagopa.pn.deliverypush.abstractions.webhookspool.WebhookAction;
import it.pagopa.pn.deliverypush.abstractions.webhookspool.WebhookEventType;
import it.pagopa.pn.deliverypush.service.WebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebhookActionsEventHandler {

    private final WebhookService webhookService;

    public WebhookActionsEventHandler(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    public void handleEvent(WebhookAction evt ) {
        log.info( "Received WEBHOOK-ACTION actionType={}", evt.getType());
        try {
            if (evt.getType() == WebhookEventType.REGISTER_EVENT)
                doHandleRegisterEvent(evt);
            else
                doHandlePurgeEvent(evt);
        } catch (Exception e) {
            log.error("error handling event", e);
            throw new PnInternalException("Error handling webhook event", e);
        }

    }

    private void doHandlePurgeEvent(WebhookAction evt) {
        log.debug("[enter] doHandlePurgeEvent evt={}", evt);
        webhookService
            .purgeEvents(evt.getStreamId(), evt.getEventId(), evt.getType() == WebhookEventType.PURGE_STREAM_OLDER_THAN)
                .block();
        log.debug("[exit] doHandlePurgeEvent evt={}", evt);
    }

    private void doHandleRegisterEvent(WebhookAction evt) {
        log.debug("[enter] doHandleRegisterEvent evt={}", evt);
        webhookService
            .saveEvent(evt.getPaId(), evt.getEventId(), evt.getIun(), evt.getTimestamp(),
                    evt.getOldStatus(), evt.getNewStatus(), evt.getTimelineEventCategory())
                .block();
        log.debug("[exit] doHandleRegisterEvent evt={}", evt);
    }

}
