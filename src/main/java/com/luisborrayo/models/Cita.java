package com.luisborrayo.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita",
        indexes = {
                @Index(name = "idx_cita_medico", columnList = "medico_id"),
                @Index(name = "idx_cita_paciente", columnList = "paciente_id"),
                @Index(name = "idx_cita_fechaHora", columnList = "fechaHora")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cita_medico_fecha", columnNames = {"medico_id", "fechaHora"})
        }
)
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Column(length = 1000)
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "paciente_id", foreignKey = @ForeignKey(name = "fk_cita_paciente"))
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", foreignKey = @ForeignKey(name = "fk_cita_medico"))
    private Medico medico;

    public Cita() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
}
