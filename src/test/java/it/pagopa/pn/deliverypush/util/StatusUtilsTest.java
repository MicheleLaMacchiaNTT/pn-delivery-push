package it.pagopa.pn.deliverypush.util;

import it.pagopa.pn.api.dto.notification.status.NotificationStatus;
import it.pagopa.pn.api.dto.notification.status.NotificationStatusHistoryElement;
import it.pagopa.pn.api.dto.notification.timeline.TimelineElement;
import it.pagopa.pn.api.dto.notification.timeline.TimelineElementCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

class StatusUtilsTest {

    private StatusUtils statusUtils;

    @BeforeEach
    public void setup() {
        this.statusUtils = new StatusUtils();
    }

    @Test
    void getTimelineHistoryTest() {

        // GIVEN a timeline
        TimelineElement timelineElement1 = TimelineElement.builder()
                .elementId("el1")
                .timestamp(Instant.parse("2021-09-16T15:24:00.00Z"))
                .category(TimelineElementCategory.REQUEST_ACCEPTED)
                .build();
        TimelineElement timelineElement2 = TimelineElement.builder()
                .elementId("el2")
                .timestamp(Instant.parse("2021-09-16T15:25:00.00Z"))
                .category(TimelineElementCategory.NOTIFICATION_PATH_CHOOSE)
                .build();
        TimelineElement timelineElement3 = TimelineElement.builder()
                .elementId("el3")
                .timestamp(Instant.parse("2021-09-16T15:26:00.00Z"))
                .category(TimelineElementCategory.SEND_DIGITAL_DOMICILE)
                .build();
        TimelineElement timelineElement4 = TimelineElement.builder()
                .elementId("el4")
                .timestamp(Instant.parse("2021-09-16T15:27:00.00Z"))
                .category(TimelineElementCategory.SEND_DIGITAL_DOMICILE_FEEDBACK)
                .build();
        TimelineElement timelineElement5 = TimelineElement.builder()
                .elementId("el5")
                .timestamp(Instant.parse("2021-09-16T15:28:00.00Z"))
                .category(TimelineElementCategory.END_OF_DIGITAL_DELIVERY_WORKFLOW)
                .build();
        TimelineElement timelineElement6 = TimelineElement.builder()
                .elementId("el6")
                .timestamp(Instant.parse("2021-09-16T17:00:00.00Z"))
                .category(TimelineElementCategory.NOTIFICATION_VIEWED)
                .build();
        TimelineElement timelineElement7 = TimelineElement.builder()
                .elementId("el7")
                .timestamp(Instant.parse("2021-09-16T17:30:00.00Z"))
                .category(TimelineElementCategory.PAYMENT)
                .build();

        Set<TimelineElement> timelineElementList = Set.of(timelineElement1, timelineElement2, timelineElement3,
                timelineElement4, timelineElement5, timelineElement6, timelineElement7);


        // WHEN ask for status history
        List<NotificationStatusHistoryElement> actualStatusHistory = statusUtils.getStatusHistory(
                timelineElementList, 1,
                Instant.parse("2021-09-16T15:20:00.00Z")
        );

        // THEN status histories have same length
        Assertions.assertEquals( 6, actualStatusHistory.size(), "Check length");

        //  ... 1st initial status
        Assertions.assertEquals( NotificationStatusHistoryElement.builder()
                        .status(NotificationStatus.IN_VALIDATION)
                        .activeFrom(Instant.parse("2021-09-16T15:20:00.00Z"))
                        .relatedTimelineElements( Arrays.asList( ))
                        .build(),
                actualStatusHistory.get( 0 ),
                "1st status wrong"
        );

        //  ... 2nd initial status
        Assertions.assertEquals( NotificationStatusHistoryElement.builder()
                        .status(NotificationStatus.ACCEPTED)
                        .activeFrom(Instant.parse("2021-09-16T15:24:00.00Z"))
                        .relatedTimelineElements( Arrays.asList( "el1" ))
                        .build(),
                actualStatusHistory.get( 1 ),
                "2nd status wrong"
        );

        //  ... 3rd initial status
        Assertions.assertEquals( NotificationStatusHistoryElement.builder()
                        .status(NotificationStatus.DELIVERING)
                        .activeFrom(Instant.parse("2021-09-16T15:25:00.00Z"))
                        .relatedTimelineElements( Arrays.asList( "el2", "el3", "el4" ))
                        .build(),
                actualStatusHistory.get( 2 ),
                "3rd status wrong"
        );

        //  ... 4th initial status
        Assertions.assertEquals( NotificationStatusHistoryElement.builder()
                        .status(NotificationStatus.DELIVERED)
                        .activeFrom(Instant.parse("2021-09-16T15:28:00.00Z"))
                        .relatedTimelineElements( Arrays.asList( "el5" ))
                        .build(),
                actualStatusHistory.get( 3 ),
                "4th status wrong"
        );

        //  ... 5th initial status
        Assertions.assertEquals( NotificationStatusHistoryElement.builder()
                        .status(NotificationStatus.VIEWED)
                        .activeFrom(Instant.parse("2021-09-16T17:00:00.00Z"))
                        .relatedTimelineElements( Arrays.asList( "el6" ))
                        .build(),
                actualStatusHistory.get( 4 ),
                "2nd status wrong"
        );

        //  ... 6th initial status
        Assertions.assertEquals( NotificationStatusHistoryElement.builder()
                        .status(NotificationStatus.PAID)
                        .activeFrom(Instant.parse("2021-09-16T17:30:00.00Z"))
                        .relatedTimelineElements( Arrays.asList( "el7" ))
                        .build(),
                actualStatusHistory.get( 5 ),
                "6th status wrong"
        );

    }

    @Test
    void getTimelineHistoryMoreRecipientTest() {
        // GIVEN a timeline
        TimelineElement timelineElement1 = TimelineElement.builder()
                .elementId("el1")
                .timestamp(Instant.parse("2021-09-16T15:24:00.00Z"))
                .category(TimelineElementCategory.REQUEST_ACCEPTED)
                .build();
        TimelineElement timelineElement2 = TimelineElement.builder()
                .elementId("el2")
                .timestamp(Instant.parse("2021-09-16T15:25:00.00Z"))
                .category(TimelineElementCategory.NOTIFICATION_PATH_CHOOSE)
                .build();
        TimelineElement timelineElement3 = TimelineElement.builder()
                .elementId("el3")
                .timestamp(Instant.parse("2021-09-16T15:26:00.00Z"))
                .category(TimelineElementCategory.SEND_DIGITAL_DOMICILE)
                .build();
        TimelineElement timelineElement4 = TimelineElement.builder()
                .elementId("el4")
                .timestamp(Instant.parse("2021-09-16T15:27:00.00Z"))
                .category(TimelineElementCategory.SEND_DIGITAL_DOMICILE_FEEDBACK)
                .build();
        TimelineElement timelineElement5 = TimelineElement.builder()
                .elementId("el5")
                .timestamp(Instant.parse("2021-09-16T15:28:00.00Z"))
                .category(TimelineElementCategory.END_OF_DIGITAL_DELIVERY_WORKFLOW)
                .build();
        TimelineElement timelineElement3_1 = TimelineElement.builder()
                .elementId("el6")
                .timestamp(Instant.parse("2021-09-16T15:29:00.00Z"))
                .category(TimelineElementCategory.SEND_DIGITAL_DOMICILE)
                .build();
        TimelineElement timelineElement4_1 = TimelineElement.builder()
                .elementId("el7")
                .timestamp(Instant.parse("2021-09-16T15:30:00.00Z"))
                .category(TimelineElementCategory.SEND_DIGITAL_DOMICILE_FEEDBACK)
                .build();
        TimelineElement timelineElement5_1 = TimelineElement.builder()
                .elementId("el8")
                .timestamp(Instant.parse("2021-09-16T15:31:00.00Z"))
                .category(TimelineElementCategory.END_OF_DIGITAL_DELIVERY_WORKFLOW)
                .build();
        TimelineElement timelineElement6 = TimelineElement.builder()
                .elementId("el9")
                .timestamp(Instant.parse("2021-09-16T17:00:00.00Z"))
                .category(TimelineElementCategory.NOTIFICATION_VIEWED)
                .build();
        TimelineElement timelineElement7 = TimelineElement.builder()
                .elementId("el10")
                .timestamp(Instant.parse("2021-09-16T17:30:00.00Z"))
                .category(TimelineElementCategory.PAYMENT)
                .build();

        Set<TimelineElement> timelineElementList = Set.of(timelineElement1, timelineElement2,
                timelineElement3, timelineElement4, timelineElement5, timelineElement3_1, timelineElement4_1,
                timelineElement5_1, timelineElement6, timelineElement7);


        // creare List<NotificationStatusHistoryElement>
        NotificationStatusHistoryElement historyElement = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.IN_VALIDATION)
                .activeFrom(Instant.parse("2021-09-16T15:20:00.00Z"))
                .relatedTimelineElements( Arrays.asList(  ))
                .build();

        NotificationStatusHistoryElement historyElement1 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.ACCEPTED)
                .activeFrom(Instant.parse("2021-09-16T15:24:00.00Z"))
                .relatedTimelineElements( Arrays.asList( "el1" ))
                .build();

        NotificationStatusHistoryElement historyElement2 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.DELIVERING)
                .activeFrom(Instant.parse("2021-09-16T15:25:00.00Z"))
                .relatedTimelineElements( Arrays.asList( "el2", "el3", "el4", "el5", "el6", "el7" ))
                .build();
        NotificationStatusHistoryElement historyElement4_1 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.DELIVERED)
                .activeFrom(Instant.parse("2021-09-16T15:31:00.00Z"))
                .relatedTimelineElements( Arrays.asList( "el8" ))
                .build();
        NotificationStatusHistoryElement historyElement5 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.VIEWED)
                .activeFrom(Instant.parse("2021-09-16T17:00:00.00Z"))
                .relatedTimelineElements( Arrays.asList( "el9" ))
                .build();
        NotificationStatusHistoryElement historyElement6 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.PAID)
                .activeFrom(Instant.parse("2021-09-16T17:30:00.00Z"))
                .relatedTimelineElements( Arrays.asList( "el10" ))
                .build();
        List<NotificationStatusHistoryElement> historyElementList = Arrays.asList(historyElement,historyElement1,
                historyElement2, historyElement4_1, historyElement5, historyElement6);

        // chiamare metodo di test
        List<NotificationStatusHistoryElement> resHistoryElementList = statusUtils.getStatusHistory(
                timelineElementList, 2,
                Instant.parse("2021-09-16T15:20:00.00Z")
        );
        // verificare che è risultato atteso
        Assertions.assertEquals(historyElementList, resHistoryElementList);
    }

    @Test
    void getTimelineHistoryErrorTest() {
        // creare TimelineElement
        TimelineElement timelineElement1 = TimelineElement.builder()
                .elementId("el1")
                .timestamp(Instant.parse("2021-09-16T15:24:00.00Z"))
                .category(TimelineElementCategory.REQUEST_ACCEPTED)
                .build();
        TimelineElement timelineElement2 = TimelineElement.builder()
                .elementId("el2")
                .timestamp(Instant.parse("2021-09-16T15:25:00.00Z"))
                .category(TimelineElementCategory.NOTIFICATION_VIEWED)
                .build();
        TimelineElement timelineElement3 = TimelineElement.builder()
                .elementId("el3")
                .timestamp(Instant.parse("2021-09-16T15:26:00.00Z"))
                .category(TimelineElementCategory.PAYMENT)
                .build();

        Set<TimelineElement> timelineElementList = Set.of(timelineElement1,
                timelineElement2, timelineElement3);

        // creare List<NotificationStatusHistoryElement>
        NotificationStatusHistoryElement historyElement1 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.IN_VALIDATION)
                .relatedTimelineElements( Arrays.asList( ))
                .activeFrom(Instant.parse("2021-09-16T15:23:00.00Z"))
                .build();

        NotificationStatusHistoryElement historyElement2 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.ACCEPTED)
                .activeFrom(Instant.parse("2021-09-16T15:24:00.00Z"))
                .relatedTimelineElements( Arrays.asList("el1"))
                .build();

        NotificationStatusHistoryElement historyElement3 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.VIEWED)
                .activeFrom(Instant.parse("2021-09-16T15:25:00.00Z"))
                .relatedTimelineElements( Arrays.asList("el2"))
                .build();

        NotificationStatusHistoryElement historyElement4 = NotificationStatusHistoryElement.builder()
                .status(NotificationStatus.PAID)
                .activeFrom(Instant.parse("2021-09-16T15:26:00.00Z"))
                .relatedTimelineElements( Arrays.asList("el3"))
                .build();

        List<NotificationStatusHistoryElement> historyElementList = Arrays.asList(
                historyElement1, historyElement2, historyElement3, historyElement4
        );

        // chiamare metodo di test
        List<NotificationStatusHistoryElement> resHistoryElementList = statusUtils.getStatusHistory(
                timelineElementList, 2,
                Instant.parse("2021-09-16T15:23:00.00Z")
        );
        // verificare che è risultato atteso
        Assertions.assertEquals(historyElementList, resHistoryElementList);
    }

    @Test
    void emptyTimelineInitialStateTest() {
        //
        Assertions.assertEquals(NotificationStatus.IN_VALIDATION, statusUtils.getCurrentStatus(Collections.emptyList()));
    }

    @Test
    void getCurrentStatusTest() {
        List<NotificationStatusHistoryElement>  statusHistory = new ArrayList<>();
        NotificationStatusHistoryElement statusHistoryDelivering = NotificationStatusHistoryElement.builder()
                .activeFrom(Instant.now())
                .status(NotificationStatus.DELIVERING)
                .build();
        NotificationStatusHistoryElement statusHistoryAccepted = NotificationStatusHistoryElement.builder()
                .activeFrom(Instant.now())
                .status(NotificationStatus.ACCEPTED)
                .build();
        statusHistory.add(statusHistoryDelivering);
        statusHistory.add(statusHistoryAccepted);

        Assertions.assertEquals(NotificationStatus.ACCEPTED, statusUtils.getCurrentStatus(statusHistory));
    }

}