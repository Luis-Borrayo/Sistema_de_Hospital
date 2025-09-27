package com.luisborrayo.models;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(
            name = "paciente_id",
            foreignKey = @ForeignKey(name = "fk_historial_paciente")
    )
    private Pacientes paciente;

    @Column(length = 500)
    private String alergias;

    @Column(length = 500)
    private String antecedentes;

    @Column(length = 500)
    private String observaciones;
    public HistorialMedico() {}

    public HistorialMedico(Pacientes paciente, String alergias, String antecedentes, String Observaciones) {
        this.paciente = paciente;
        this.alergias = alergias;
        this.antecedentes = antecedentes;
        this.observaciones = Observaciones;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Pacientes getPaciente() {
        return paciente;
    }
    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }
    public String getAlergias() {
        return alergias;
    }
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }
    public String getAntecedentes() {
        return antecedentes;
    }
    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}
