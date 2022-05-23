package it.pagopa.pn.deliverypush.externalclient.pnclient.userattributes;

import it.pagopa.pn.userattributes.generated.openapi.clients.userattributes.model.CourtesyDigitalAddress;
import it.pagopa.pn.userattributes.generated.openapi.clients.userattributes.model.LegalDigitalAddress;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserAttributes {
    ResponseEntity<List<LegalDigitalAddress>> getLegalAddressBySender(String taxId, String senderId);

    ResponseEntity<List<CourtesyDigitalAddress>> getCourtesyAddressBySender(String taxId, String senderId);
}
