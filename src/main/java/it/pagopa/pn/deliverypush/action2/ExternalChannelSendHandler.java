package it.pagopa.pn.deliverypush.action2;

import it.pagopa.pn.api.dto.events.PnExtChnEmailEvent;
import it.pagopa.pn.api.dto.events.PnExtChnPaperEvent;
import it.pagopa.pn.api.dto.events.PnExtChnPecEvent;
import it.pagopa.pn.deliverypush.action2.utils.ExternalChannelUtils;
import it.pagopa.pn.deliverypush.action2.utils.TimelineUtils;
import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationInt;
import it.pagopa.pn.deliverypush.externalclient.pnclient.externalchannel.ExternalChannelSendClient;
import it.pagopa.pn.deliverypush.generated.openapi.server.v1.dto.DigitalAddress;
import it.pagopa.pn.deliverypush.generated.openapi.server.v1.dto.DigitalAddressSource;
import it.pagopa.pn.deliverypush.generated.openapi.server.v1.dto.PhysicalAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExternalChannelSendHandler {
    private final ExternalChannelUtils externalChannelUtils;
    private final ExternalChannelSendClient externalChannel;
    private final TimelineUtils timelineUtils;
    
    public ExternalChannelSendHandler(ExternalChannelUtils externalChannelUtils, ExternalChannelSendClient externalChannel, 
                                      TimelineUtils timelineUtils) {
        this.externalChannelUtils = externalChannelUtils;
        this.externalChannel = externalChannel;
        this.timelineUtils = timelineUtils;
    }

    /**
     * Send pec notification to external channel
     */
    public void sendDigitalNotification(NotificationInt notification, DigitalAddress digitalAddress, DigitalAddressSource addressSource, Integer recIndex,
                                        int sentAttemptMade) {
        log.debug("Start sendDigitalNotification - iun {} id {}", notification.getIun(), recIndex);

        PnExtChnPecEvent pnExtChnPecEvent = externalChannelUtils.getExtChannelPecEvent(notification, digitalAddress, addressSource, recIndex, sentAttemptMade);

        externalChannelUtils.addSendDigitalNotificationToTimeline(notification, digitalAddress, addressSource, recIndex, sentAttemptMade, pnExtChnPecEvent.getHeader().getEventId());
        externalChannel.sendNotification(pnExtChnPecEvent);
    }

    /**
     * Send courtesy message to external channel
     */
    public void sendCourtesyNotification(NotificationInt notification, DigitalAddress courtesyAddress, Integer recIndex, String eventId) {
        log.debug("Start sendCourtesyNotification - iun {} id {}", notification.getIun(), recIndex);

        PnExtChnEmailEvent pnExtChnEmailEvent = externalChannelUtils.getExtChannelEmailRequest(notification, courtesyAddress, recIndex, eventId);

        externalChannelUtils.addSendCourtesyMessageToTimeline(notification, courtesyAddress, recIndex, eventId);
        externalChannel.sendNotification(pnExtChnEmailEvent);
    }

    /**
     * Send registered letter to external channel
     */
    public void sendNotificationForRegisteredLetter(NotificationInt notification, PhysicalAddress physicalAddress, Integer recIndex) {
        log.debug("Start sendNotificationForRegisteredLetter - iun {} id {}", notification.getIun(), recIndex);
        boolean isNotificationAlreadyViewed = timelineUtils.checkNotificationIsAlreadyViewed(notification.getIun(), recIndex);

        if(! isNotificationAlreadyViewed){
            PnExtChnPaperEvent pnExtChnPaperEvent = externalChannelUtils.getExtChannelPaperRequest(notification, physicalAddress, recIndex);
            externalChannelUtils.addSendSimpleRegisteredLetterToTimeline(notification, physicalAddress, recIndex, pnExtChnPaperEvent.getHeader().getEventId());
            externalChannel.sendNotification(pnExtChnPaperEvent);
            
            log.info("Registered Letter sent to externalChannel - iun {} id {}", notification.getIun(), recIndex);
        }else {
            log.info("Notification is already viewed, registered Letter isn't sent to externalChannel - iun {} id {}", notification.getIun(), recIndex);
        }
    }

    /**
     * Send paper notification to external channel
     */
    public void sendAnalogNotification(NotificationInt notification, PhysicalAddress physicalAddress, Integer recIndex, boolean investigation, int sentAttemptMade) {
        log.debug("Start sendAnalogNotification - iun {} id {}", notification.getIun(), recIndex);
        
        boolean isNotificationAlreadyViewed = timelineUtils.checkNotificationIsAlreadyViewed(notification.getIun(), recIndex);

        if(! isNotificationAlreadyViewed){
            PnExtChnPaperEvent pnExtChnPaperEvent = externalChannelUtils.getExtChannelPaperRequest(notification, physicalAddress, recIndex, investigation, sentAttemptMade);
            externalChannelUtils.addSendAnalogNotificationToTimeline(notification, physicalAddress, recIndex, investigation, sentAttemptMade, pnExtChnPaperEvent.getHeader().getEventId());
            externalChannel.sendNotification(pnExtChnPaperEvent);

            log.info("Registered Letter sent to externalChannel - iun {} id {}", notification.getIun(), recIndex);
        }else {
            log.info("Notification is already viewed, registered Letter isn't sent to externalChannel - iun {} id {}", notification.getIun(), recIndex);
        }
    }
}
