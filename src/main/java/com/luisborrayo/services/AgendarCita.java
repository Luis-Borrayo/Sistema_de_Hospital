package com.luisborrayo.services;

import com.luisborrayo.models.Cita;
import com.luisborrayo.models.Medico;
import com.luisborrayo.models.Pacientes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AgendarCita {

    public static void agendarConsola(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            String dpi;
            while (true) {
                System.out.print("DPI paciente: ");
                dpi = sc.nextLine().trim();
                if (!dpi.isEmpty()) break;
                System.out.println("DPI no puede estar vacío.");
            }

            TypedQuery<Pacientes> q1 = em.createQuery("SELECT p FROM Pacientes p WHERE p.dpi = :dpi", Pacientes.class);
            q1.setParameter("dpi", dpi);
            List<Pacientes> lp = q1.getResultList();
            if (lp.isEmpty()) {
                System.out.println("Paciente no encontrado");
                return;
            }
            Pacientes paciente = lp.get(0);

            String coleg;
            while (true) {
                System.out.print("Colegiado del médico: ");
                coleg = sc.nextLine().trim();
                if (!coleg.isEmpty()) break;
                System.out.println("Colegiado no puede estar vacío.");
            }

            TypedQuery<Medico> q2 = em.createQuery("SELECT m FROM Medico m WHERE m.colegiado = :c", Medico.class);
            q2.setParameter("c", coleg);
            List<Medico> lm = q2.getResultList();
            if (lm.isEmpty()) {
                System.out.println("Médico no encontrado");
                return;
            }
            Medico medico = lm.get(0);

            LocalDateTime fechaHora;
            while (true) {
                System.out.print("Fecha y hora (YYYY-MM-DDTHH:MM) ejemplo 2025-09-30T14:30 : ");
                String fh = sc.nextLine().trim();
                if (fh.isEmpty()) {
                    System.out.println("Fecha y hora no puede estar vacía.");
                    continue;
                }

                try {
                    fechaHora = LocalDateTime.parse(fh);
                    break;
                } catch (Exception e) {
                    System.out.println("Formato inválido. Use YYYY-MM-DDTHH:MM");
                }
            }

            if (fechaHora.isBefore(LocalDateTime.now())) {
                System.out.println("No se puede agendar en el pasado.");
                return;
            }

            TypedQuery<Long> q3 = em.createQuery("SELECT COUNT(c) FROM Cita c WHERE c.medico = :m AND c.fechaHora = :fh", Long.class);
            q3.setParameter("m", medico);
            q3.setParameter("fh", fechaHora);
            Long count = q3.getSingleResult();
            if (count != null && count > 0) {
                System.out.println("El médico ya tiene una cita en esa fecha/hora.");
                return;
            }

            System.out.print("Motivo (opcional): ");
            String motivo = sc.nextLine();

            Cita cita = new Cita();
            cita.setPaciente(paciente);
            cita.setMedico(medico);
            cita.setFechaHora(fechaHora);
            cita.setMotivo(motivo);

            em.getTransaction().begin();
            em.persist(cita);
            em.getTransaction().commit();
            System.out.println("Cita agendada id: " + cita.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al agendar cita: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}