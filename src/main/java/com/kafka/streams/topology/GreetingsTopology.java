package com.kafka.streams.topology;

import com.kafka.streams.doamin.ProcessesTransactionDTO;
import com.kafka.streams.doamin.ProcessesTransactionSerde;
import com.kafka.streams.doamin.TransactionDTO;
import com.kafka.streams.doamin.TransactionSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GreetingsTopology {

    public static String TRANSACTIONS = "Transaction";
    public static String PROCESSED_TRANSACTION = "ProcessedTransaction";

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {

        var transactionsStream = streamsBuilder
                .stream(TRANSACTIONS,
                        Consumed.with(Serdes.String(),
                                new TransactionSerde())
                );

        Map<String, Double> cumulativeData = new HashMap<>();
        cumulativeData.put("sum", 0.0);
        cumulativeData.put("count", 0.0);

        transactionsStream.print(Printed.<String, TransactionDTO>toSysOut().withLabel("Transactions Stream"));

        var processTransactionsStream = transactionsStream
                .mapValues(transaction -> {
                    double newSum = cumulativeData.get("sum") + transaction.getAmount();
                    double newCount = cumulativeData.get("count") + 1;
                    cumulativeData.put("sum", newSum);
                    cumulativeData.put("count", newCount);
                    double average = newSum / newCount;
                    System.out.println("Current Average: " + average);
                    System.out.println("Current Total: " + newSum);
                    System.out.println("Region: " + transaction.getOrigin());
                    return new ProcessesTransactionDTO(
                            transaction,
                            String.valueOf(newSum),
                            String.valueOf(average),
                            transaction.getOrigin()
                    );
                });

        processTransactionsStream
                .to(PROCESSED_TRANSACTION,
                        Produced.with(Serdes.String(),new ProcessesTransactionSerde()));

    }
}
