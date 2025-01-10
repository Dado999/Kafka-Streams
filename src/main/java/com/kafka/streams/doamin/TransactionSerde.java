package com.kafka.streams.doamin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class TransactionSerde implements Serde<TransactionDTO> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<TransactionDTO> serializer() {
        return (topic, data) -> {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize", e);
            }
        };
    }

    @Override
    public Deserializer<TransactionDTO> deserializer() {
        return (topic, data) -> {
            try {
                return objectMapper.readValue(data, TransactionDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize", e);
            }
        };
    }
}

