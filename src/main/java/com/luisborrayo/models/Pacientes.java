package com.luisborrayo.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "paciente",
        indexes = {
                @Index(name = "ix_paciente_email", columnList = "email")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_paciente_email", columnNames = "email")
        }
)
public class Pacientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String dpi;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private HistorialMedico historialMedico;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Cita> citas = new ArrayList<>();

    public Pacientes() {}

    public Pacientes(String nombre, String dpi, LocalDate fechaNacimiento, String telefono, String email) {
        this.nombre = nombre;
        this.dpi = dpi;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
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

    public String getDpi() {
        return dpi;
    }
    public void setDpi(String dpi) {
        this.dpi = dpi;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HistorialMedico getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(HistorialMedico historialMedico) {
        this.historialMedico = historialMedico;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public void addCita(Cita cita) {
        citas.add(cita);
        cita.setPaciente(this);
    }

    public void removeCita(Cita cita) {
        citas.remove(cita);
        cita.setPaciente(null);
    }
}

