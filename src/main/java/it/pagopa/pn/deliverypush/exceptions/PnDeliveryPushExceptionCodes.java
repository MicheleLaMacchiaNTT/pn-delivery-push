package it.pagopa.pn.deliverypush.exceptions;

import it.pagopa.pn.commons.exceptions.PnExceptionsCodes;

public class PnDeliveryPushExceptionCodes extends PnExceptionsCodes {

    // raccolgo qui tutti i codici di errore di delivery push
    public static final String ERROR_CODE_DELIVERYPUSH_NOTFOUND = "PN_DELIVERYPUSH_NOTFOUND";
    public static final String ERROR_CODE_DELIVERYPUSH_GETFILEERROR = "PN_DELIVERYPUSH_GETFILEERROR";
    public static final String ERROR_CODE_DELIVERYPUSH_UPLOADFILEERROR = "PN_DELIVERYPUSH_UPLOADFILEERROR";
    public static final String ERROR_CODE_DELIVERYPUSH_UPDATEMETAFILEERROR = "PN_DELIVERYPUSH_UPDATEMETAFILEERROR";
    public static final String ERROR_CODE_DELIVERYPUSH_ERRORCOMPUTECHECKSUM = "PN_DELIVERYPUSH_ERRORCOMPUTECHECKSUM";
    public static final String ERROR_CODE_DELIVERYPUSH_ATTACHMENTCHANGESTATUSFAILED = "PN_DELIVERYPUSH_ATTACHMENTCHANGESTATUSFAILED";
    public static final String ERROR_CODE_DELIVERYPUSH_INVALIDEVENTCODE = "PN_DELIVERYPUSH_INVALIDEVENTCODE";
    public static final String ERROR_CODE_DELIVERYPUSH_INVALIDATTEMPT = "PN_DELIVERYPUSH_INVALIDATTEMPT";
    public static final String ERROR_CODE_DELIVERYPUSH_INVALIDADDRESSSOURCE = "PN_DELIVERYPUSH_INVALIDADDRESSSOURCE";
    public static final String ERROR_CODE_DELIVERYPUSH_SENDDIGITALTIMELINEEVENTNOTFOUND = "PN_DELIVERYPUSH_SENDDIGITALTIMELINEEVENTNOTFOUND";
    public static final String ERROR_CODE_DELIVERYPUSH_DIGITALPROGRESSTIMELINEEVENTNOTFOUND = "PN_DELIVERYPUSH_DIGITALPROGRESSTIMELINEEVENTNOTFOUND";
    public static final String ERROR_CODE_DELIVERYPUSH_SCHEDULEDDIGITALTIMELINEEVENTNOTFOUND = "PN_DELIVERYPUSH_SCHEDULEDDIGITALTIMELINEEVENTNOTFOUND";
    public static final String ERROR_CODE_DELIVERYPUSH_LASTADDRESSATTEMPTNOTFOUND = "PN_DELIVERYPUSH_LASTADDRESSATTEMPTNOTFOUND";
    public static final String ERROR_CODE_DELIVERYPUSH_ERRORCOURTESY = "PN_DELIVERYPUSH_ERRORCOURTESY";
    public static final String ERROR_CODE_DELIVERYPUSH_ERRORCOURTESYIO = "PN_DELIVERYPUSH_ERRORCOURTESYIO";
    public static final String ERROR_CODE_WEBHOOK_UPDATEEVENTSTREAM = "PN_WEBHOOK_UPDATEEVENTSTREAM";
    public static final String ERROR_CODE_WEBHOOK_CONSUMEEVENTSTREAM = "PN_WEBHOOK_CONSUMEEVENTSTREAM";
    public static final String ERROR_CODE_GENERATE_PDF_FAILED = "ERROR_CODE_GENERATE_PDF_FAILED";
    public static final String ERROR_CODE_NOTIFICATIONFAILED = "PN_DELIVERYPUSH_NOTIFICATIONFAILED";
}
