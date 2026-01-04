package com.cinema_package.cinema_project;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.cinema_package.cinema_project.kafka.BookingConfirmedEvent;

@TestConfiguration
public class TestKafkaConfig {

    @Bean
    public KafkaTemplate<String, BookingConfirmedEvent> kafkaTemplate() {
        KafkaTemplate<String, BookingConfirmedEvent> template = Mockito.mock(KafkaTemplate.class);
        // Ensure send(...) returns a completed future so production code can attach whenComplete without NPE
        Mockito.when(template.send(Mockito.anyString(), Mockito.any(BookingConfirmedEvent.class)))
               .thenReturn(java.util.concurrent.CompletableFuture.completedFuture(null));
        return template;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingConfirmedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookingConfirmedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(Mockito.mock(ConsumerFactory.class));
        return factory;
    }
}
