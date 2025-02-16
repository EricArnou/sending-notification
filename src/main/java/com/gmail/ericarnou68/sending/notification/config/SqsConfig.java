package com.gmail.ericarnou68.sending.notification.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Value("${aws.sqs.update.status.queue.uri}")
    private String sqsQueueUrl;

    @Value("${sqs.use-localstack}")
    private boolean useLocalstack;

    @Bean
    public SqsAsyncClient sqsAsyncClient(){
        if(useLocalstack)
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create(sqsQueueUrl))
                .region(Region.US_EAST_1)
                .build();
                
        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

    }
}
