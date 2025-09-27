package com.luisborrayo.services;


import com.luisborrayo.models.*;
import com.luisborrayo.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CargarSemilla {

    public static void cargar() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Pacientes p1 = new Pacientes();
            p1.setNombre("Luis Borrayo");
            p1.setDpi("1234567890101");
            p1.setFechaNacimiento(LocalDate.of(2004, 10, 16));
            p1.setEmail("luis@gmail.com");
            p1.setTelefono("5551234");

            Pacientes p2 = new Pacientes();
            p2.setNombre("Marta");
            p2.setEmail("marta@email.com");
            p2.setDpi("1098765432101");
            p2.setTelefono("5555678");
            p2.setFechaNacimiento(LocalDate.of(1995, 10, 5));

            em.persist(p1);
            em.persist(p2);

            Medico m1 = new Medico("Dr. Juan", "C-1234", Medico.Especialidad.CARDIOLOGIA, "juan@hospital.com");
            Medico m2 = new Medico("Dra. Marta", "C-5678", Medico.Especialidad.DERMATOLOGIA, "marta@hospital.com");

            em.persist(m1);
            em.persist(m2);

            HistorialMedico h1 = new HistorialMedico();
            h1.setPaciente(p1);
            h1.setAlergias("Ninguna");
            h1.setAntecedentes("Ninguno");
            h1.setObservaciones("Paciente sano");

            HistorialMedico h2 = new HistorialMedico();
            h2.setPaciente(p2);
            h2.setAlergias("Penicilina");
            h2.setAntecedentes("Asma");
            h2.setObservaciones("Revisar presión");

            em.persist(h1);
            em.persist(h2);

            Cita c1 = new Cita();
            c1.setPaciente(p1);
            c1.setMedico(m1);
            c1.setFechaHora(LocalDateTime.now().plusDays(1));
            c1.setMotivo("Chequeo general");

            Cita c2 = new Cita();
            c2.setPaciente(p2);
            c2.setMedico(m2);
            c2.setFechaHora(LocalDateTime.now().plusDays(2));
            c2.setMotivo("Consulta de piel");

            em.persist(c1);
            em.persist(c2);

            em.getTransaction().commit();
            System.out.println("Datos semilla cargada correctamente.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
