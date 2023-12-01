package io.siesta.demos;

import io.siesta.demos.utils.PropertyUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.stream.IntStream;

public class ProducerDemoWithCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("I am a kafka producer!");

        Properties properties = PropertyUtil.getProperties();

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            IntStream.range(0, 10).forEach(j -> {
                IntStream.range(0, 30).forEach(i -> {
                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "Hello World " + j + "_" + i);

                    producer.send(producerRecord, (metadata, exception) -> {
                        if (Objects.isNull(exception)) {
                            LOGGER.info("Received new metadata \nTopic: {}\nPartition: {}\nOffset: {}\nTimestamp: {}",
                                    metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
                        } else {
                            LOGGER.error("Error while producing", exception);
                        }
                    });
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            producer.flush();
        }

    }
}
