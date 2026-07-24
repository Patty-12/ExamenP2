package com.espe.meditrack.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AppointmentTest {

    @Test
    public void getters_datosDelConstructor_debenRetornarLosValoresRecibidos() {
        // Arrange
        List<String> emails = List.of(
                "paciente@gmail.com",
                "familiar@gmail.com"
        );

        Appointment appointment = new Appointment(
                "A1",
                "Ana Torres",
                "Cardiología",
                45.00,
                emails
        );

        // Act
        String id = appointment.getId();
        String patientName = appointment.getPatientName();
        String specialty = appointment.getSpecialty();
        Double costUsd = appointment.getCostUsd();
        List<String> notifyEmails = appointment.getNotifyEmails();

        // Assert
        assertEquals("A1", id);
        assertEquals("Ana Torres", patientName);
        assertEquals("Cardiología", specialty);
        assertEquals(Double.valueOf(45.00), costUsd);
        assertEquals(emails, notifyEmails);
    }

    @Test
    public void constructor_listaOriginalModificada_noDebeCambiarLaListaInterna() {
        // Arrange
        List<String> originalEmails = new ArrayList<>();
        originalEmails.add("paciente@gmail.com");

        Appointment appointment = new Appointment(
                "A2",
                "Carlos Mendoza",
                "Pediatría",
                30.00,
                originalEmails
        );

        // Act
        originalEmails.add("correo-agregado@gmail.com");

        List<String> emailsObtenidos = appointment.getNotifyEmails();

        // Assert
        assertEquals(1, emailsObtenidos.size());
        assertNotSame(originalEmails, emailsObtenidos);
    }

    @Test
    public void getNotifyEmails_dosInvocaciones_debeRetornarCopiasDiferentes() {
        // Arrange
        Appointment appointment = new Appointment(
                "A3",
                "María López",
                "Dermatología",
                40.00,
                List.of("maria@gmail.com")
        );

        // Act
        List<String> primeraLista = appointment.getNotifyEmails();
        List<String> segundaLista = appointment.getNotifyEmails();

        // Assert
        assertEquals(primeraLista, segundaLista);
        assertNotSame(primeraLista, segundaLista);
    }
}