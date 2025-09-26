package com.luisborrayo.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "medico",
        uniqueConstraints = { @UniqueConstraint(name = "uk_medico_colegiado", columnNames = {"colegiado"}) })
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String colegiado;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    private String email;

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private List<Cita> citas;

    public Medico() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getColegiado() { return colegiado; }
    public void setColegiado(String colegiado) { this.colegiado = colegiado; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}
