package it.pagopa.pn.deliverypush.pnclient.externalchannel;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateFactory {
    @Bean
    @Qualifier("withTracing")
    public RestTemplate restTemplate(){
        RestTemplate template = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors
                = template.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new RestTemplateHeaderModifierInterceptor());
        template.setInterceptors(interceptors);
        return template;
    }

    //TODO: interceptor per il trace verso external channel
    @Value("${pn.log.trace-id-header}")
    private String traceIdHeader;

    public class RestTemplateHeaderModifierInterceptor
            implements ClientHttpRequestInterceptor {

        @NotNull
        @Override
        public ClientHttpResponse intercept(
                @NotNull HttpRequest request,
                @NotNull byte[] body,
                @NotNull ClientHttpRequestExecution execution) throws IOException {
            String traceId = MDC.get("trace_id");
            if (traceId != null) {
                request.getHeaders().add(traceIdHeader, traceId);
            }
            return execution.execute(request, body);
        }
    }
}
