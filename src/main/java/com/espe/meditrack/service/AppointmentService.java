package com.espe.meditrack.service;

import com.espe.meditrack.model.Appointment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AppointmentService {

    private final Flux<Appointment> appointments;

    /**
     * Constructor principal.
     *
     * Genera cinco citas en memoria:
     * - Tres válidas.
     * - Una inválida con costo igual a cero.
     * - Una inválida sin correos de notificación.
     */
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

    /**
     * Constructor utilizado principalmente para pruebas.
     *
     * Permite suministrar una colección diferente de citas y comprobar
     * el comportamiento de defaultIfEmpty cuando todas son inválidas.
     */
    public AppointmentService(List<Appointment> appointments) {
        this.appointments = Flux.fromIterable(appointments);
    }

    /**
     * Devuelve únicamente las citas válidas mediante un flujo reactivo.
     */
    public Flux<Appointment> getValidAppointments() {
        return appointments

                /*
                 * filter permite descartar las citas inválidas sin bloquear
                 * el flujo. Una cita es válida cuando su costo es mayor que
                 * cero y posee al menos un correo de notificación.
                 */
                .filter(appointment ->
                        appointment.getCostUsd() != null
                                && appointment.getCostUsd() > 0
                                && appointment.getNotifyEmails() != null
                                && !appointment.getNotifyEmails().isEmpty()
                )

                /*
                 * map transforma cada elemento emitido.
                 * En este caso crea una nueva cita inmutable colocando
                 * la especialidad en mayúsculas.
                 */
                .map(appointment -> new Appointment(
                        appointment.getId(),
                        appointment.getPatientName(),
                        appointment.getSpecialty().toUpperCase(Locale.ROOT),
                        appointment.getCostUsd(),
                        appointment.getNotifyEmails()
                ))

                /*
                 * defaultIfEmpty emite una cita genérica cuando ninguna
                 * cita cumple la regla de negocio y el flujo queda vacío.
                 */
                .defaultIfEmpty(createDefaultAppointment());
    }

    /**
     * Busca una cita válida por su identificador.
     */
    public Mono<Appointment> findById(String id) {
        return getValidAppointments()

                /*
                 * filter conserva únicamente la cita cuyo identificador
                 * coincide con el recibido.
                 */
                .filter(appointment -> appointment.getId().equalsIgnoreCase(id))

                /*
                 * next convierte el primer elemento del Flux en un Mono.
                 * Si no encuentra elementos, devuelve un Mono vacío.
                 */
                .next()

                /*
                 * switchIfEmpty reemplaza el Mono vacío por un Mono de error,
                 * sin utilizar block() ni lógica imperativa bloqueante.
                 */
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