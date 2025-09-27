package com.luisborrayo.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cita",
        indexes = {
                @Index(name = "ix_cita_paciente", columnList = "paciente_id"),
                @Index(name = "ix_cita_medico", columnList = "medico_id"),
                @Index(name = "ix_cita_fecha", columnList = "fechaHora")},
        uniqueConstraints = {@UniqueConstraint(
                        name = "uk_medico_fecha",
                        columnNames = {"medico_id", "fechaHora"})})
public class Cita {
    public enum EstadoCita {
        PROGRAMADA,
        ATENDIDA,
        CANCELADA
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    private String motivo;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Pacientes paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    public Cita() {}

    public Cita(LocalDateTime fechaHora, EstadoCita estado, String motivo, Pacientes paciente, Medico medico) {
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.motivo = motivo;
        this.paciente = paciente;
        this.medico = medico;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    public EstadoCita getEstado() {
        return estado;
    }
    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
    public String getMotivo() {
        return motivo;
    }
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Pacientes getPaciente() {
        return paciente;
    }
    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }
    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

}
