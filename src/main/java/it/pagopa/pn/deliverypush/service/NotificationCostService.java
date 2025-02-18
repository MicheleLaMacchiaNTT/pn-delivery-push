package it.pagopa.pn.deliverypush.service;

import it.pagopa.pn.deliverypush.dto.ext.delivery.NotificationCostResponseInt;
import it.pagopa.pn.deliverypush.dto.ext.delivery.notification.NotificationInt;

public interface NotificationCostService {
    Integer getNotificationCost(NotificationInt notificationInt, int recIndex);

    NotificationCostResponseInt getIunFromPaTaxIdAndNoticeCode(String paTaxId, String noticeCode);
}
