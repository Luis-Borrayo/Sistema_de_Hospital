package com.luisborrayo.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "paciente",
        uniqueConstraints = { @UniqueConstraint(name = "uk_paciente_dpi", columnNames = {"dpi"}) })
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String dpi;

    private LocalDate fechaNacimiento;

    private String telefono;

    private String email;

    // OneToOne inverso (no dueño)
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private HistorialMedico historial;

    // OneToMany (no cascade REMOVE by default to protect citas)
    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<Cita> citas;

    // getters y setters (omito por brevedad, pero agrégalos)
    public Paciente() {}
    // ... getters & setters ...
    // include standard getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public HistorialMedico getHistorial() { return historial; }
    public void setHistorial(HistorialMedico historial) { this.historial = historial; }
    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}
