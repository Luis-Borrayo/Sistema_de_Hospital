package com.luisborrayo.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "medico",
        indexes = {
                @Index(name = "ix_medico_email", columnList = "email")
        }, uniqueConstraints = {
                @UniqueConstraint(name = "uk_medico_colegiado", columnNames = "colegiado"),
                @UniqueConstraint(name = "uk_medico_email", columnNames = "email")})
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Especialidad {
        PEDIATRIA,
        CARDIOLOGIA,
        DERMATOLOGIA,
        GINECOLOGIA,
        MEDICINA_GENERAL
    }

    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, unique = true, length = 100)
    private String colegiado;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Especialidad especialidad;
    @Column(nullable = false, length = 100)
    private String email;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Cita> citas = new ArrayList<>();
    public List<Cita> getCitas() {
        return citas;
    }
    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public Medico(){}
    public Medico(String nombre, String colegiado, Especialidad especialidad, String email) {
        this.nombre = nombre;
        this.colegiado = colegiado;
        this.especialidad = especialidad;
        this.email = email;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getColegiado() {
        return colegiado;
    }
    public void setColegiado(String colegiado) {
        this.colegiado = colegiado;
    }
    public Especialidad getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
