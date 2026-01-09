package org.training.fundtransfer.configuration;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Feign clients in the Fund Transfer Service.
 * 
 * <p>This configuration provides custom error handling for Feign client
 * calls to other microservices (Account Service, Transaction Service).</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Configuration
public class FeignClientConfiguration extends FeignClientProperties.FeignClientConfiguration {

    /**
     * Returns the error decoder for the Feign client.
     *
     * @return the error decoder
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignClientErrorDecoder();
    }
}
