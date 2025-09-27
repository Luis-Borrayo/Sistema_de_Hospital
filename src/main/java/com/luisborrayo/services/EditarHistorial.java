package com.luisborrayo.services;

import com.luisborrayo.models.HistorialMedico;
import com.luisborrayo.models.Pacientes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Scanner;

public class EditarHistorial {

    public static void editarHistorialConsola(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            System.out.print("Ingrese DPI del paciente para editar historial: ");
            String dpi = sc.nextLine().trim();

            TypedQuery<Pacientes> q = em.createQuery("SELECT p FROM Pacientes p WHERE p.dpi = :dpi", Pacientes.class);
            q.setParameter("dpi", dpi);
            Pacientes p;
            try {
                p = q.getSingleResult();
            } catch (Exception e) {
                System.out.println("Paciente no encontrado con DPI: " + dpi);
                return;
            }

            em.getTransaction().begin();
            HistorialMedico h = p.getHistorialMedico();
            if (h == null) {
                h = new HistorialMedico();
                h.setPaciente(p);
            } else {
                System.out.println("Historial actual -> Alergias: " + h.getAlergias());
            }

            System.out.print("Alergias (enter para mantener): ");
            String alerg = sc.nextLine();
            if (!alerg.trim().isEmpty()) h.setAlergias(alerg);

            System.out.print("Antecedentes (enter para mantener): ");
            String ant = sc.nextLine();
            if (!ant.trim().isEmpty()) h.setAntecedentes(ant);

            System.out.print("Observaciones (enter para mantener): ");
            String obs = sc.nextLine();
            if (!obs.trim().isEmpty()) h.setObservaciones(obs);

            em.persist(h);
            em.getTransaction().commit();
            System.out.println("Historial guardado/actualizado para paciente id: " + p.getId());
        } finally {
            em.close();
        }
    }
}
