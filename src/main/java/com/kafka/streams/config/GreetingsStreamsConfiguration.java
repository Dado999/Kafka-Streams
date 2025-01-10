package com.kafka.streams.config;

import com.kafka.streams.topology.GreetingsTopology;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class GreetingsStreamsConfiguration {

    @Bean
    public NewTopic greetings(){
        return TopicBuilder.name(GreetingsTopology.TRANSACTIONS)
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic greetingsUppercase(){
        return TopicBuilder.name(GreetingsTopology.PROCESSED_TRANSACTION)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
