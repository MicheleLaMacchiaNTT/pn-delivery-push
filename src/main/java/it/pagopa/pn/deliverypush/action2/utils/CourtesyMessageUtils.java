package it.pagopa.pn.deliverypush.action2.utils;

import it.pagopa.pn.api.dto.notification.Notification;
import it.pagopa.pn.api.dto.notification.NotificationRecipient;
import it.pagopa.pn.api.dto.notification.address.DigitalAddress;
import it.pagopa.pn.api.dto.notification.timeline.EventId;
import it.pagopa.pn.api.dto.notification.timeline.SendCourtesyMessageDetails;
import it.pagopa.pn.api.dto.notification.timeline.TimelineEventId;
import it.pagopa.pn.deliverypush.action2.ExternalChannelSendHandler;
import it.pagopa.pn.deliverypush.external.AddressBook;
import it.pagopa.pn.deliverypush.service.TimelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CourtesyMessageUtils {
    private final AddressBook addressBook;
    private final ExternalChannelSendHandler externalChannelSendHandler;
    private final TimelineService timelineService;
    private final NotificationUtils notificationUtils;
    
    public CourtesyMessageUtils(AddressBook addressBook, ExternalChannelSendHandler externalChannelSendHandler, TimelineService timelineService, NotificationUtils notificationUtils) {
        this.addressBook = addressBook;
        this.externalChannelSendHandler = externalChannelSendHandler;
        this.timelineService = timelineService;
        this.notificationUtils = notificationUtils;
    }

    /**
     * Get recipient addresses and send courtesy messages.
     */
    public void checkAddressesForSendCourtesyMessage(Notification notification, int recIndex) {
        log.info("CheckAddressesForSendCourtesyMessage - iun {} id {} ", notification.getIun(), recIndex);
        
        NotificationRecipient recipient = notificationUtils.getRecipientFromIndex(notification,recIndex);
        
        //Vengono ottenuti tutti gli indirizzi di cortesia per il recipient ...
        addressBook.getAddresses(recipient.getTaxId(), notification.getSender())
                .ifPresent(addressBookItem -> {
                    int courtesyAddrIndex = 0;
                    if (addressBookItem.getCourtesyAddresses() != null) {
                        for (DigitalAddress courtesyAddress : addressBookItem.getCourtesyAddresses()) {
                            sendCourtesyMessage(notification, recipient, courtesyAddrIndex, courtesyAddress);
                            courtesyAddrIndex++;
                        }
                    }
                });

        log.debug("End sendCourtesyMessage - IUN {} id {}", notification.getIun(),recIndex);
    }

    private void sendCourtesyMessage(Notification notification, int recIndex, int courtesyAddrIndex, DigitalAddress courtesyAddress) {
        log.debug("Send courtesy message address index {} - iun {} id {} ", courtesyAddrIndex, notification.getIun(), recIndex);

        //... Per ogni indirizzo di cortesia ottenuto viene inviata la notifica del messaggio di cortesia tramite external channel
        String eventId = getTimelineElementId(recIndex, notification.getIun(), courtesyAddrIndex);
        externalChannelSendHandler.sendCourtesyNotification(notification, courtesyAddress, recIndex, eventId);
    }

    private String getTimelineElementId(int recIndex, String iun, int index) {
        return TimelineEventId.SEND_COURTESY_MESSAGE.buildEventId(EventId.builder()
                .iun(iun)
                .recIndex(recIndex)
                .index(index)
                .build()
        );
    }

    /**
     * Get user courtesy messages from timeline
     *
     * @param iun   Notification unique identifier
     * @param taxId User identifier
     */
    public Optional<SendCourtesyMessageDetails> getFirstSentCourtesyMessage(String iun, int recIndex) {
        String timeLineCourtesyId = getTimelineElementId(recIndex, iun, 0);
        log.debug("Get courtesy message for timelineCourtesyId {} - IUN {} id {}", timeLineCourtesyId, iun, recIndex);

        return timelineService.getTimelineElement(iun, timeLineCourtesyId, SendCourtesyMessageDetails.class);
    }

}
