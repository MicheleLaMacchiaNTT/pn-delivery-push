package it.pagopa.pn.deliverypush.middleware.timelinedao;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.pn.api.dto.notification.timeline.NotificationPathChooseDetails;
import it.pagopa.pn.api.dto.notification.timeline.ReceivedDetails;
import it.pagopa.pn.api.dto.notification.timeline.TimelineElement;
import it.pagopa.pn.api.dto.notification.timeline.TimelineElementCategory;
import it.pagopa.pn.api.dto.status.RequestUpdateStatusDto;
import it.pagopa.pn.api.dto.status.ResponseUpdateStatusDto;
import it.pagopa.pn.commons.abstractions.IdConflictException;
import it.pagopa.pn.commons_delivery.middleware.notificationdao.CassandraNotificationEntityDao;
import it.pagopa.pn.commons_delivery.model.notification.cassandra.NotificationEntity;
import it.pagopa.pn.commons_delivery.model.notification.cassandra.TimelineElementEntity;
import it.pagopa.pn.commons_delivery.model.notification.cassandra.TimelineElementEntityId;
import it.pagopa.pn.deliverypush.pnclient.delivery.PnDeliveryClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class CassandraTimelineDaoTest {

    private TimelineDao dao;
    private CassandraNotificationEntityDao notificationEntityDao;
    private PnDeliveryClient client;
    
    @BeforeEach
    void instantiateDao() {
        ObjectMapper objMapper = new ObjectMapper();
        DtoToEntityTimelineMapper dto2Entity = new DtoToEntityTimelineMapper(objMapper);
        EntityToDtoTimelineMapper entity2dto = new EntityToDtoTimelineMapper(objMapper);

        TimelineEntityDao entityDao = new TestMyTimelineEntityDao();
        notificationEntityDao = Mockito.mock(CassandraNotificationEntityDao.class);
        client = Mockito.mock(PnDeliveryClient.class);
        
        this.dao = new CassandraTimelineDao(entityDao, dto2Entity, entity2dto,  client);
    }

    @Test
    void successfullyInsertAndRetrieve() {

        // GIVEN
        String iun = "202109-eb10750e-e876-4a5a-8762-c4348d679d35";

        String id1 = "sender_ack";
        TimelineElement row1 = TimelineElement.builder()
                .iun(iun)
                .elementId(id1)
                .category(TimelineElementCategory.REQUEST_ACCEPTED)
                .details(new ReceivedDetails())
                .timestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();
        String id2 = "path_choose";
        TimelineElement row2 = TimelineElement.builder()
                .iun(iun)
                .elementId(id2)
                .category(TimelineElementCategory.NOTIFICATION_PATH_CHOOSE)
                .details(new NotificationPathChooseDetails())
                .timestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();

        // WHEN
        ResponseUpdateStatusDto dto = ResponseUpdateStatusDto.builder().build();
        ResponseEntity<ResponseUpdateStatusDto> respEntity = ResponseEntity.ok(dto);
        Mockito.when(client.updateState(Mockito.any(RequestUpdateStatusDto.class))).thenReturn(respEntity);

        Optional<NotificationEntity> notification = Optional.ofNullable(NotificationEntity.builder()
                .iun(iun)
                .recipientsOrder(Arrays.asList("CodiceFiscale"))
                .build());
        Mockito.when(notificationEntityDao.get(iun)).thenReturn(notification);
        
        dao.addTimelineElement(row1);
        dao.addTimelineElement(row2);
        

        // THEN
        // check first row
        Optional<TimelineElement> retrievedRow1 = dao.getTimelineElement(iun, id1);
        Assertions.assertTrue(retrievedRow1.isPresent());
        Assertions.assertEquals(row1, retrievedRow1.get());

        // check second row
        Optional<TimelineElement> retrievedRow2 = dao.getTimelineElement(iun, id2);
        Assertions.assertTrue(retrievedRow2.isPresent());
        Assertions.assertEquals(row2, retrievedRow2.get());

        // check full retrieve
        Set<TimelineElement> result = dao.getTimeline(iun);
        Assertions.assertEquals(Set.of(row1, row2), result);
    }

    @Test
    void successfullyDelete() {

        // GIVEN
        String iun = "iun1";

        String id1 = "sender_ack";
        TimelineElement row1 = TimelineElement.builder()
                .iun(iun)
                .elementId(id1)
                .category(TimelineElementCategory.REQUEST_ACCEPTED)
                .details(new ReceivedDetails())
                .timestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();
        String id2 = "path_choose";
        TimelineElement row2 = TimelineElement.builder()
                .iun(iun)
                .elementId(id2)
                .category(TimelineElementCategory.NOTIFICATION_PATH_CHOOSE)
                .details(new NotificationPathChooseDetails())
                .timestamp(Instant.ofEpochMilli(System.currentTimeMillis()))
                .build();

        // WHEN
        ResponseUpdateStatusDto dto = ResponseUpdateStatusDto.builder().build();
        ResponseEntity<ResponseUpdateStatusDto> respEntity = ResponseEntity.ok(dto);
        Mockito.when(client.updateState(Mockito.any(RequestUpdateStatusDto.class))).thenReturn(respEntity);

        Optional<NotificationEntity> notification = Optional.ofNullable(NotificationEntity.builder()
                .iun(iun)
                .recipientsOrder(Arrays.asList("CodiceFiscale"))
                .build());
        Mockito.when(notificationEntityDao.get(iun)).thenReturn(notification);
        dao.addTimelineElement(row1);
        dao.addTimelineElement(row2);
        dao.deleteTimeline(iun);

        // THEN
        Assertions.assertTrue(dao.getTimeline(iun).isEmpty());
    }


    private static class TestMyTimelineEntityDao implements TimelineEntityDao {

        private final Map<TimelineElementEntityId, TimelineElementEntity> store = new ConcurrentHashMap<>();

        @Override
        public void put(TimelineElementEntity timelineElementEntity) {
            this.store.put(timelineElementEntity.getId(), timelineElementEntity);
        }

        @Override
        public void putIfAbsent(TimelineElementEntity timelineElementEntity) throws IdConflictException {
            if (this.store.putIfAbsent(timelineElementEntity.getId(), timelineElementEntity) != null) {
                throw new IdConflictException(timelineElementEntity.getId());
            }
        }

        @Override
        public Optional<TimelineElementEntity> get(TimelineElementEntityId timelineElementEntityId) {
            return Optional.ofNullable(store.get(timelineElementEntityId));
        }

        @Override
        public void delete(TimelineElementEntityId timelineElementEntityId) {
            store.remove(timelineElementEntityId);
        }


        @Override
        public Set<TimelineElementEntity> findByIun(String iun) {
            return this.store.values().stream()
                    .filter(el -> iun.equals(el.getId().getIun()))
                    .collect(Collectors.toSet());
        }

        @Override
        public void deleteByIun(String iun) {
            Set<TimelineElementEntityId> toRemove = this.store.keySet().stream()
                    .filter(id -> iun.equals(id.getIun()))
                    .collect(Collectors.toSet());

            for (TimelineElementEntityId id : toRemove) {
                this.store.remove(id);
            }
        }

    }
}
