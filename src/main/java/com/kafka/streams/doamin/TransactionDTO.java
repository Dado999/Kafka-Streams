package com.kafka.streams.doamin;

import lombok.Data;

@Data
public class TransactionDTO {
    Integer id;
    Integer userId;
    String createdDate;
    Integer fromAccountId;
    Integer toAccountId;
    Integer amount;
    String origin;
}
