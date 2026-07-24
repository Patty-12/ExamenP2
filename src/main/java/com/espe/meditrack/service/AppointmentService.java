package com.espe.meditrack.service;

import com.espe.meditrack.model.Appointment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AppointmentService {

    private final Flux<Appointment> appointments;

    
    public AppointmentService() {
        this.appointments = Flux.just(
                new Appointment(
                        "A1",
                        "Ana Torres",
                        "Cardiología",
                        45.00,
                        List.of("ana@gmail.com")
                ),
                new Appointment(
                        "A2",
                        "Carlos Mendoza",
                        "Pediatría",
                        30.00,
                        List.of("carlos@gmail.com", "familiar@gmail.com")
                ),
                new Appointment(
                        "A3",
                        "María López",
                        "Dermatología",
                        40.00,
                        List.of("maria@gmail.com")
                ),
                new Appointment(
                        "A4",
                        "José Pérez",
                        "Medicina General",
                        0.00,
                        List.of("jose@gmail.com")
                ),
                new Appointment(
                        "A5",
                        "Lucía Andrade",
                        "Neurología",
                        60.00,
                        Collections.emptyList()
                )
        );
    }

    
    public AppointmentService(List<Appointment> appointments) {
        this.appointments = Flux.fromIterable(appointments);
    }

    
    public Flux<Appointment> getValidAppointments() {
        return appointments

                
                .filter(appointment ->
                        appointment.getCostUsd() != null
                                && appointment.getCostUsd() > 0
                                && appointment.getNotifyEmails() != null
                                && !appointment.getNotifyEmails().isEmpty()
                )

                
                .map(appointment -> new Appointment(
                        appointment.getId(),
                        appointment.getPatientName(),
                        appointment.getSpecialty().toUpperCase(Locale.ROOT),
                        appointment.getCostUsd(),
                        appointment.getNotifyEmails()
                ))

                
                .defaultIfEmpty(createDefaultAppointment());
    }

    
    public Mono<Appointment> findById(String id) {
        return getValidAppointments()

                
                .filter(appointment -> appointment.getId().equalsIgnoreCase(id))

                
                .next()

                
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException(
                                "No existe una cita con el id: " + id
                        )
                ));
    }

    private Appointment createDefaultAppointment() {
        return new Appointment(
                "DEFAULT",
                "Paciente no disponible",
                "SIN ESPECIALIDAD",
                1.00,
                List.of("notificaciones@meditrack.com")
        );
    }
}