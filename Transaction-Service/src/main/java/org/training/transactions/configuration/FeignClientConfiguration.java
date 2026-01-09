package org.training.transactions.configuration;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Feign clients in the Transaction Service.
 * 
 * <p>This configuration provides custom error handling for Feign client
 * calls to other microservices (Account Service).</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Configuration
public class FeignClientConfiguration extends FeignClientProperties.FeignClientConfiguration {

    /**
     * Returns an instance of ErrorDecoder that will be used to decode errors returned by Feign clients.
     *
     * @return the ErrorDecoder instance
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }
}
