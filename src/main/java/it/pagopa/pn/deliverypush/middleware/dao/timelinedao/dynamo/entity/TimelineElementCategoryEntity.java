package it.pagopa.pn.deliverypush.middleware.dao.timelinedao.dynamo.entity;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum TimelineElementCategoryEntity {
    REQUEST_ACCEPTED("REQUEST_ACCEPTED"),

    SEND_COURTESY_MESSAGE("SEND_COURTESY_MESSAGE"),

    GET_ADDRESS("GET_ADDRESS"),

    PUBLIC_REGISTRY_CALL("PUBLIC_REGISTRY_CALL"),

    PUBLIC_REGISTRY_RESPONSE("PUBLIC_REGISTRY_RESPONSE"),

    SCHEDULE_ANALOG_WORKFLOW("SCHEDULE_ANALOG_WORKFLOW"),

    SCHEDULE_DIGITAL_WORKFLOW("SCHEDULE_DIGITAL_WORKFLOW"),

    SEND_DIGITAL_DOMICILE("SEND_DIGITAL_DOMICILE"),
    
    SEND_DIGITAL_PROGRESS("SEND_DIGITAL_PROGRESS"),

    SEND_DIGITAL_FEEDBACK("SEND_DIGITAL_FEEDBACK"),

    REFINEMENT("REFINEMENT"),

    SCHEDULE_REFINEMENT("SCHEDULE_REFINEMENT"),

    DIGITAL_SUCCESS_WORKFLOW("DIGITAL_SUCCESS_WORKFLOW"),

    DIGITAL_FAILURE_WORKFLOW("DIGITAL_FAILURE_WORKFLOW"),

    ANALOG_SUCCESS_WORKFLOW("ANALOG_SUCCESS_WORKFLOW"),

    ANALOG_FAILURE_WORKFLOW("ANALOG_FAILURE_WORKFLOW"),

    SEND_SIMPLE_REGISTERED_LETTER("SEND_SIMPLE_REGISTERED_LETTER"),

    NOTIFICATION_VIEWED("NOTIFICATION_VIEWED"),

    SEND_ANALOG_DOMICILE("SEND_ANALOG_DOMICILE"),

    SEND_PAPER_FEEDBACK("SEND_PAPER_FEEDBACK"),

    PAYMENT("PAYMENT"),

    COMPLETELY_UNREACHABLE("COMPLETELY_UNREACHABLE"),

    REQUEST_REFUSED("REQUEST_REFUSED"),

    AAR_GENERATION("AAR_GENERATION"),

    NOT_HANDLED("NOT_HANDLED");

    private final String value;

    TimelineElementCategoryEntity(String value) {
        this.value = value;
    }

}
