package com.espe.meditrack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class Appointment {

    private final String id;
    private final String patientName;
    private final String specialty;
    private final Double costUsd;
    private final List<String> notifyEmails;

    public Appointment(
            String id,
            String patientName,
            String specialty,
            Double costUsd,
            List<String> notifyEmails
    ) {
        this.id = id;
        this.patientName = patientName;
        this.specialty = specialty;
        this.costUsd = costUsd;

        
        this.notifyEmails = notifyEmails == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(notifyEmails));
    }

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public Double getCostUsd() {
        return costUsd;
    }

    public List<String> getNotifyEmails() {
        
        return Collections.unmodifiableList(new ArrayList<>(notifyEmails));
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", costUsd=" + costUsd +
                ", notifyEmails=" + notifyEmails +
                '}';
    }
}