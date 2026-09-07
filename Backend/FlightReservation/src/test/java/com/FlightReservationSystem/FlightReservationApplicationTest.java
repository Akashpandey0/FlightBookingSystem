package com.FlightReservationSystem;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FlightReservationApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            FlightReservationApplication.main(new String[] {});
            mockedSpringApplication.verify(() -> SpringApplication.run(FlightReservationApplication.class, new String[] {}));
        }
    }
}