package it.pagopa.pn.deliverypush.dto.timeline.details;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder( toBuilder = true )
@EqualsAndHashCode
@ToString
public class NotificationViewedDetailsInt implements RecipientRelatedTimelineElementDetails {
    private int recIndex;
    private Integer notificationCost;
    private String raddType;
    private String raddTransactionId;

    public String toLog() {
        return String.format(
                "recIndex=%d",
                recIndex
        );
    }
}
