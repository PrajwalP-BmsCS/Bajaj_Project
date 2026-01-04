package com.cinema_package.cinema_project.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class BookingEventConsumer {

    @KafkaListener(topics = "booking-confirmed" , groupId = "booking-group")
    public void handleBookingConfirmed(BookingConfirmedEvent event) {
        System.out.println(
            "📩 Booking confirmed for user: " + event.getUserEmail()
        );
    }
}
