package io.siesta.demos;

import io.siesta.demos.utils.PropertyUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.stream.IntStream;

public class ProducerDemoKeys {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.info("I am a kafka producer with keys!");

        Properties properties = PropertyUtil.getProperties();

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            IntStream.range(0, 2).forEach(j -> {
                        IntStream.range(0, 10).forEach(i -> {
                            String topic = "demo_java";
                            String key = "id_" + i;
                            String value = "Hello World " + i;
                            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

                            producer.send(producerRecord, (metadata, exception) -> {
                                if (Objects.isNull(exception)) {
                                    LOGGER.info("Key: {} | Partition: {}", key, metadata.partition());
                                } else {
                                    LOGGER.error("Error while producing", exception);
                                }
                            });
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            producer.flush();
        }

    }
}
