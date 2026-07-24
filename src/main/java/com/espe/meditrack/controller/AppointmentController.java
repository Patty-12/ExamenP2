package com.espe.meditrack.controller;

import com.espe.meditrack.model.Appointment;
import com.espe.meditrack.service.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    /**
     * Devuelve todas las citas válidas mediante Flux.
     */
    @GetMapping
    public Flux<Appointment> getAppointments() {
        return appointmentService.getValidAppointments();
    }

    /**
     * Devuelve una cita mediante Mono.
     */
    @GetMapping("/{id}")
    public Mono<Appointment> getAppointmentById(@PathVariable String id) {
        return appointmentService.findById(id);
    }
}