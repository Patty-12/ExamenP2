package com.espe.meditrack.service;

import com.espe.meditrack.model.Appointment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

public class AppointmentServiceTest {

    @Test
    public void getValidAppointments_cincoCitas_debeEmitirSoloLasTresValidas() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getValidAppointments_todasLasCitasInvalidas_debeEmitirCitaDefault() {
        // Arrange
        List<Appointment> invalidAppointments = List.of(
                new Appointment(
                        "I1",
                        "Paciente inválido uno",
                        "Cardiología",
                        0.00,
                        List.of("paciente@gmail.com")
                ),
                new Appointment(
                        "I2",
                        "Paciente inválido dos",
                        "Pediatría",
                        25.00,
                        Collections.emptyList()
                )
        );

        AppointmentService service =
                new AppointmentService(invalidAppointments);

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextMatches(appointment ->
                        "DEFAULT".equals(appointment.getId())
                                && "Paciente no disponible"
                                .equals(appointment.getPatientName())
                )
                .verifyComplete();
    }

    @Test
    public void findById_idInexistente_debeTerminarConError() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        var resultado = service.findById("A999");

        // Assert
        StepVerifier.create(resultado)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void findById_idExistente_debeEmitirLaCitaSolicitada() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        var resultado = service.findById("A1");

        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(appointment ->
                        "A1".equals(appointment.getId())
                                && "CARDIOLOGÍA"
                                .equals(appointment.getSpecialty())
                )
                .verifyComplete();
    }
}