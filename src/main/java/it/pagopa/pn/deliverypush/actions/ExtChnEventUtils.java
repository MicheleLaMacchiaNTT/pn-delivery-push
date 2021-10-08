package it.pagopa.pn.deliverypush.actions;

import java.time.Instant;

import it.pagopa.pn.api.dto.events.*;
import org.springframework.stereotype.Component;

import it.pagopa.pn.api.dto.notification.Notification;
import it.pagopa.pn.api.dto.notification.NotificationRecipient;
import it.pagopa.pn.api.dto.notification.address.DigitalAddress;
import it.pagopa.pn.deliverypush.abstractions.actionspool.Action;

@Component
public class ExtChnEventUtils {

	public PnExtChnEmailEvent buildSendEmailRequest(
			Action action,
			Notification notification,
			NotificationRecipient recipient,
			int courtesyAddressIdx,
			DigitalAddress emailAddress
	) {
		return PnExtChnEmailEvent.builder()
				.header(StandardEventHeader.builder()
						.iun(action.getIun())
						.eventId(action.getActionId() + "_" + courtesyAddressIdx)
						.eventType(EventType.SEND_COURTESY_EMAIL.name())
						.publisher(EventPublisher.DELIVERY_PUSH.name())
						.createdAt(Instant.now())
						.build()
				)
				.payload(PnExtChnEmailEventPayload.builder()
						.iun(notification.getIun())
						.senderId(notification.getSender().getPaId())
						.senderDenomination(notification.getSender().getPaId())
						.senderEmailAddress("Not required")
						.recipientDenomination(recipient.getDenomination())
						.recipientTaxId(recipient.getTaxId())
						.emailAddress(emailAddress.getAddress())
						.shipmentDate(notification.getSentAt())
						.build()
				)
				.build();
	}

	public PnExtChnPaperEvent buildSendPaperRequest (Action action, Notification notification, CommunicationType communicationType, ServiceLevelType serviceLevelType) {
		NotificationRecipient recipient = notification.getRecipients().get( action.getRecipientIndex() );
				
		return PnExtChnPaperEvent.builder()
		        .header( StandardEventHeader.builder()
		        			.iun( action.getIun() )
		        			.eventId( action.getActionId() )
		        			.eventType( EventType.SEND_PAPER_REQUEST.name() )
		        			.publisher( EventPublisher.DELIVERY_PUSH.name() )
		        			.createdAt( Instant.now() )
		        			.build()
		        )
		        .payload( PnExtChnPaperEventPayload.builder()
		        			.iun( action.getIun() )
		        			.requestCorrelationId( action.getActionId() )
		        			.destinationAddress( recipient.getPhysicalAddress() )
		        			.recipientDenomination( recipient.getDenomination() )
		        			.communicationType( communicationType )
		        			.serviceLevel( serviceLevelType )
		        			.senderDenomination( notification.getSender().getPaId() )
		    				.build()
		        )
		        .build();
	}
	
	public PnExtChnPecEvent buildSendPecRequest(Action action, Notification notification, 
			NotificationRecipient recipient, DigitalAddress address) {
		return PnExtChnPecEvent.builder()
		        .header( StandardEventHeader.builder()
		                .iun( action.getIun() )
		                .eventId( action.getActionId() )
		                .eventType( EventType.SEND_PEC_REQUEST.name() )
		                .publisher( EventPublisher.DELIVERY_PUSH.name() )
		                .createdAt( Instant.now() )
		                .build()
		            )
		        .payload( PnExtChnPecEventPayload.builder()
		                .iun( notification.getIun() )
		                .requestCorrelationId( action.getActionId() )
		                .recipientTaxId( recipient.getTaxId() )
		                .recipientDenomination( recipient.getDenomination() )
		                .senderId( notification.getSender().getPaId() )
		                .senderDenomination( notification.getSender().getPaId() )
		                .senderPecAddress("Not required")
		                .pecAddress( address.getAddress() )
		                .shipmentDate( notification.getSentAt() )
		                .build()
		            )
		        .build();
	}
}
