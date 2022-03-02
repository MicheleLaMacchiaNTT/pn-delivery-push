package it.pagopa.pn.deliverypush.pnclient.externalchannel;

import it.pagopa.pn.deliverypush.PnDeliveryPushConfigs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ExternalChannelClientImpl implements ExternalChannelClient{

    public static final String EXTERNAL_CHANNEL_GET_DOWNLOAD_LINKS = "/attachments/getDownloadLinks?%s";


    private final PnDeliveryPushConfigs cfg;
    private final RestTemplate restTemplate;
    private final String externalChannelBaseUrl;


    public ExternalChannelClientImpl(PnDeliveryPushConfigs cfg, @Qualifier("withTracing") RestTemplate restTemplate) {
        this.cfg = cfg;
        this.externalChannelBaseUrl = cfg.getExternalChannelBaseUrl();
        this.restTemplate = restTemplate;
    }

    @Override
    public String[] getResponseAttachmentUrl(String[] attachmentIds) {

        String queryString = Arrays.stream( attachmentIds )
                .map( el ->  "attachmentKey=" + el)
                .collect(Collectors.joining( "&" ));
        final String baseUrl = externalChannelBaseUrl + EXTERNAL_CHANNEL_GET_DOWNLOAD_LINKS;
        String url = String.format(baseUrl, queryString);
        return restTemplate.getForObject(url, String[].class);
    }

}
