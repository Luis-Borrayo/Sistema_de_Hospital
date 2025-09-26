package com.luisborrayo.models;

import javax.persistence.*;

@Entity
@Table(name = "historial_medico",
        uniqueConstraints = { @UniqueConstraint(name = "uk_historial_paciente", columnNames = {"paciente_id"}) })
public class HistorialMedico {

    @Id
    private Long id; // mapped to paciente id

    @MapsId
    @OneToOne
    @JoinColumn(name = "paciente_id", foreignKey = @ForeignKey(name = "fk_historial_paciente"), nullable = false, unique = true)
    private Paciente paciente;

    @Column(length = 1000)
    private String alergias;

    @Column(length = 2000)
    private String antecedentes;

    @Column(length = 2000)
    private String observaciones;

    public HistorialMedico() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    public String getAntecedentes() { return antecedentes; }
    public void setAntecedentes(String antecedentes) { this.antecedentes = antecedentes; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
