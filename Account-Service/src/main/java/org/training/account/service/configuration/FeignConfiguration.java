package org.training.account.service.configuration;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Feign clients in the Account Service.
 * 
 * <p>This configuration extends the default Feign client configuration and provides
 * custom error handling for inter-service communication. It registers a custom
 * error decoder to properly handle and translate errors from external services.</p>
 * 
 * @author Training Team
 * @version 1.0
 * @see org.training.account.service.configuration.FeignClientErrorDecoder
 */
@Configuration
public class FeignConfiguration extends FeignClientProperties.FeignClientConfiguration {

    /**
     * Returns a new instance of ErrorDecoder that is used to decode errors from a Feign client.
     *
     * @return a new instance of ErrorDecoder
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }
}
