package it.pagopa.pn.deliverypush.sanitizers;

import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationInt;
import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationRecipientInt;
import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationSenderInt;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.pagopa.pn.deliverypush.legalfacts.LegalFactGenerator.FIELD_NOTIFICATION;

public class RequestAcceptedHtmlSanitizer extends HtmlSanitizer {


    @Override
    public Map<String, Object> sanitize(Map<String, Object> templateModelMap) {
        NotificationInt notificationInt = (NotificationInt) templateModelMap.get(FIELD_NOTIFICATION);
        NotificationInt trustedNotificationInt = sanitize(notificationInt);

        templateModelMap.put("notification", trustedNotificationInt);
        return templateModelMap;

    }

    private NotificationInt sanitize(NotificationInt notificationInt) {
        String trustedIun = sanitize(notificationInt.getIun());
        String trustedSubject = sanitize(notificationInt.getSubject());
        String trustedPaProtocolNumber = sanitize(notificationInt.getPaProtocolNumber());
        NotificationSenderInt trustedNotificationSenderInt = sanitize(notificationInt.getSender());
        List<NotificationRecipientInt> trustedRecipients;
        if(! CollectionUtils.isEmpty(notificationInt.getRecipients())  ) {
            trustedRecipients = notificationInt.getRecipients().stream().map(this::sanitize).collect(Collectors.toList());
        }
        else {
            trustedRecipients = notificationInt.getRecipients();
        }



        return NotificationInt.builder()
                .iun(trustedIun)
                .subject(trustedSubject)
                .paProtocolNumber(trustedPaProtocolNumber)
                .recipients(trustedRecipients)
                .sender(trustedNotificationSenderInt)
                .sentAt(notificationInt.getSentAt())
                .amount(notificationInt.getAmount())
                .documents(notificationInt.getDocuments())
                .physicalCommunicationType(notificationInt.getPhysicalCommunicationType())
                .build();
    }

    private NotificationSenderInt sanitize(NotificationSenderInt notificationSenderInt) {
        if(notificationSenderInt == null) {
            return null;
        }

        String trustedPaDenomination = sanitize(notificationSenderInt.getPaDenomination());
        String trustedPaId = sanitize(notificationSenderInt.getPaId());
        String trustedPaTaxId = sanitize(notificationSenderInt.getPaTaxId());

        return NotificationSenderInt.builder()
                .paId(trustedPaId)
                .paDenomination(trustedPaDenomination)
                .paTaxId(trustedPaTaxId)
                .build();
    }
}
