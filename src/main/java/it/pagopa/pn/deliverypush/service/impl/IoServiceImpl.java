package it.pagopa.pn.deliverypush.service.impl;

import it.pagopa.pn.commons.exceptions.PnInternalException;
import it.pagopa.pn.commons.log.PnAuditLogBuilder;
import it.pagopa.pn.commons.log.PnAuditLogEvent;
import it.pagopa.pn.commons.log.PnAuditLogEventType;
import it.pagopa.pn.deliverypush.action.utils.NotificationUtils;
import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationInt;
import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationRecipientInt;
import it.pagopa.pn.deliverypush.middleware.externalclient.pnclient.externalregistry.PnExternalRegistryClient;
import it.pagopa.pn.deliverypush.service.IoService;
import it.pagopa.pn.externalregistry.generated.openapi.clients.externalregistry.model.SendMessageRequest;
import it.pagopa.pn.externalregistry.generated.openapi.clients.externalregistry.model.SendMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static it.pagopa.pn.externalregistry.generated.openapi.clients.externalregistry.model.SendMessageResponse.ResultEnum.*;


@Slf4j
@Service
public class IoServiceImpl implements IoService {
    private final PnExternalRegistryClient pnExternalRegistryClient;
    private final NotificationUtils notificationUtils;
    
    public IoServiceImpl(PnExternalRegistryClient pnExternalRegistryClient,
                         NotificationUtils notificationUtils) {
        this.pnExternalRegistryClient = pnExternalRegistryClient;
        this.notificationUtils = notificationUtils;
    }

    @Override
    public void sendIOMessage(NotificationInt notification, int recIndex) {
        log.info("Start send message to App IO - iun={} id={}", notification.getIun(), recIndex);

        NotificationRecipientInt recipientInt = notificationUtils.getRecipientFromIndex(notification, recIndex);

        SendMessageRequest sendMessageRequest = getSendMessageRequest(notification, recipientInt);

        PnAuditLogBuilder auditLogBuilder = new PnAuditLogBuilder();
        PnAuditLogEvent logEvent = auditLogBuilder.before(PnAuditLogEventType.AUD_AD_SEND_IO, "sendIOMessage")
                .iun(sendMessageRequest.getIun())
                .build();
        logEvent.log();
        
        try {
            ResponseEntity<SendMessageResponse> resp = pnExternalRegistryClient.sendIOMessage(sendMessageRequest);

            if (resp.getStatusCode().is2xxSuccessful()) {

                SendMessageResponse sendIoMessageResponse = resp.getBody();
                if(sendIoMessageResponse != null){
                    if( isErrorStatus( sendIoMessageResponse.getResult() ) ){
                        logEvent.generateFailure("Error in sendIoMessage, with errorStatus is {} - iun={} id={} ", sendIoMessageResponse.getResult(), notification.getIun(), recIndex).log();
                    } else {
                        logEvent.generateSuccess("Send io message success, with result={} - iun={} id={}", sendIoMessageResponse.getResult(), notification.getIun(), recIndex).log();
                    }
                }else {
                    log.error("sendIOMessage return not valid response response");
                }

            } else {
                logEvent.generateFailure("Error in sendIoMessage, httpStatus is {} - iun={} id={} ", resp.getStatusCode(), notification.getIun(), recIndex).log();
                throw new PnInternalException("sendIOMessage Failed - iun "+ notification.getIun() +" id "+ recIndex);
            }
        }catch (Exception ex){
            logEvent.generateFailure("Error in sendIoMessage for iun={} id={}, exception={}", notification.getIun(), recIndex, ex.getMessage()).log();
            throw ex;
        }
    }

    private boolean isErrorStatus(SendMessageResponse.ResultEnum result) {
        return ERROR_USER_STATUS.equals(result) || ERROR_COURTESY.equals(result) || ERROR_OPTIN.equals(result);
    }

    @NotNull
    private SendMessageRequest getSendMessageRequest(NotificationInt notification, NotificationRecipientInt recipientInt) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setAmount(notification.getAmount());
        sendMessageRequest.setDueDate(notification.getPaymentExpirationDate());
        sendMessageRequest.setRecipientTaxID(recipientInt.getTaxId());
        sendMessageRequest.setRequestAcceptedDate(notification.getSentAt());
        sendMessageRequest.setSenderDenomination(notification.getSender().getPaDenomination());
        sendMessageRequest.setIun(notification.getIun());
        
        String subject = notification.getSender().getPaDenomination() +"-"+ notification.getSubject();
        sendMessageRequest.setSubject(subject);

        return sendMessageRequest;
    }
}
