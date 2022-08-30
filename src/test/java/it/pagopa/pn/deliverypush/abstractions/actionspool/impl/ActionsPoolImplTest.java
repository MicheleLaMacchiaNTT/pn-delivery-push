package it.pagopa.pn.deliverypush.abstractions.actionspool.impl;

import it.pagopa.pn.commons.abstractions.MomProducer;
import it.pagopa.pn.deliverypush.PnDeliveryPushConfigs;
import it.pagopa.pn.deliverypush.abstractions.actionspool.Action;
import it.pagopa.pn.deliverypush.abstractions.actionspool.ActionType;
import it.pagopa.pn.deliverypush.middleware.dao.actiondao.LastPollForFutureActionsDao;
import it.pagopa.pn.deliverypush.service.ActionService;
import net.javacrumbs.shedlock.core.LockAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ActionsPoolImplTest {

    @Mock
    private MomProducer<ActionEvent> actionsQueue;

    @Mock
    private ActionService actionService;

    @Mock
    private Clock clock;

    @Mock
    private LastPollForFutureActionsDao lastFutureActionPoolExecutionTimeDao;

    @Mock
    private PnDeliveryPushConfigs configs;

    private ActionsPoolImpl actionsPool;

    @BeforeEach
    void setup() {
        //actionsQueue = Mockito.mock(MomProducer<ActionEvent>);
        actionService = Mockito.mock(ActionService.class);
        clock = Mockito.mock(Clock.class);
        lastFutureActionPoolExecutionTimeDao = Mockito.mock(LastPollForFutureActionsDao.class);
        configs = Mockito.mock(PnDeliveryPushConfigs.class);
        actionsPool = new ActionsPoolImpl(actionsQueue, actionService, clock, lastFutureActionPoolExecutionTimeDao, configs);
    }

    @Test
    void scheduleFutureAction() {
        String PATTERN_FORMAT = "dd.MM.yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());

        Instant instant = Instant.parse("2022-09-15T18:35:24.00Z");

        Action action = Action.builder()
                .iun("01")
                .actionId("001")
                .recipientIndex(0)
                .notBefore(instant)
                .type(ActionType.ANALOG_WORKFLOW)
                .build();

        actionsPool.scheduleFutureAction(action);

        Mockito.verify(actionService, Mockito.times(1)).addAction(action, computeTimeSlot(instant));
    }

    @Test
    void loadActionById() {
        String actionId = "001";
        Action action = Action.builder()
                .iun("01")
                .actionId("001")
                .recipientIndex(0)
                .notBefore(Instant.now())
                .type(ActionType.ANALOG_WORKFLOW)
                .build();

        Mockito.when(actionService.getActionById(actionId)).thenReturn(Optional.of(action));

        Optional<Action> result = actionsPool.loadActionById(actionId);

        Assertions.assertEquals(result.get(), action);
    }

    @Test
    void pollForFutureActions() {

        ;
        Instant now = Instant.now();
        Action action = Action.builder()
                .iun("01")
                .actionId("001")
                .recipientIndex(0)
                .notBefore(Instant.now())
                .type(ActionType.ANALOG_WORKFLOW)
                .build();

        List<Action> actions = new ArrayList<>();
        actions.add(action);

       
        Mockito.when(lastFutureActionPoolExecutionTimeDao.getLastPollTime()).thenReturn(Optional.of(now));
        Mockito.when(clock.instant()).thenReturn(now);
        Mockito.when(actionService.findActionsByTimeSlot("001")).thenReturn(actions);

        actionsPool.pollForFutureActions();

        Mockito.verify(lastFutureActionPoolExecutionTimeDao, Mockito.times(1)).updateLastPollTime(now);
    }

    private String computeTimeSlot(Instant instant) {
        OffsetDateTime nowUtc = instant.atOffset(ZoneOffset.UTC);
        int year = nowUtc.get(ChronoField.YEAR_OF_ERA);
        int month = nowUtc.get(ChronoField.MONTH_OF_YEAR);
        int day = nowUtc.get(ChronoField.DAY_OF_MONTH);
        int hour = nowUtc.get(ChronoField.HOUR_OF_DAY);
        int minute = nowUtc.get(ChronoField.MINUTE_OF_HOUR);
        return String.format("%04d-%02d-%02dT%02d:%02d", year, month, day, hour, minute);
    }

}