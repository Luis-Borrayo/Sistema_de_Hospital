package com.luisborrayo.services;

import com.luisborrayo.models.*;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CargarSemilla {

    public static void seed() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Paciente p1 = new Paciente();
            p1.setNombre("Ana Perez");
            p1.setDpi("0801199012345");
            p1.setFechaNacimiento(LocalDate.of(1990,1,1));
            p1.setEmail("ana@example.com");
            em.persist(p1);

            Paciente p2 = new Paciente();
            p2.setNombre("Juan Lopez");
            p2.setDpi("0801199012346");
            p2.setFechaNacimiento(LocalDate.of(1985,5,12));
            em.persist(p2);

            Medico m1 = new Medico();
            m1.setNombre("Dr. Carlos Gomez");
            m1.setColegiado("MED-001");
            m1.setEspecialidad(Especialidad.MEDICINA_GENERAL);
            m1.setEmail("carlos.gomez@example.com");
            em.persist(m1);

            Medico m2 = new Medico();
            m2.setNombre("Dra. Marta Ruiz");
            m2.setColegiado("MED-002");
            m2.setEspecialidad(Especialidad.PEDIATRIA);
            em.persist(m2);

            Cita c1 = new Cita();
            c1.setPaciente(p1);
            c1.setMedico(m1);
            c1.setFechaHora(LocalDateTime.now().plusDays(2).withHour(9).withMinute(0));
            c1.setMotivo("Consulta general");
            em.persist(c1);

            Cita c2 = new Cita();
            c2.setPaciente(p2);
            c2.setMedico(m2);
            c2.setFechaHora(LocalDateTime.now().plusDays(3).withHour(11).withMinute(30));
            c2.setMotivo("Revisión pediátrica");
            em.persist(c2);

            em.getTransaction().commit();
            System.out.println("Semilla cargada correctamente.");
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
