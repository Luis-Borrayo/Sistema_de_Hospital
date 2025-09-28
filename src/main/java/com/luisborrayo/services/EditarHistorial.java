package com.luisborrayo.services;

import com.luisborrayo.models.HistorialMedico;
import com.luisborrayo.models.Pacientes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;
import java.util.Scanner;

public class EditarHistorial {

    public static void editarHistorialConsola(Scanner sc) {
        System.out.println("DEBUG: Iniciando edición de historial...");

        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            System.out.print("Ingrese DPI del paciente para editar historial: ");
            String dpi = sc.nextLine().trim();

            while (dpi.isEmpty()) {
                System.out.print("DPI no puede estar vacío. Ingrese DPI del paciente: ");
                dpi = sc.nextLine().trim();
            }

            TypedQuery<Pacientes> q = em.createQuery(
                    "SELECT p FROM Pacientes p LEFT JOIN FETCH p.historialMedico WHERE p.dpi = :dpi",
                    Pacientes.class
            );
            q.setParameter("dpi", dpi);

            Pacientes paciente;
            try {
                paciente = q.getSingleResult();
                System.out.println("Paciente encontrado: " + paciente.getNombre());
            } catch (NoResultException e) {
                System.out.println("Paciente no encontrado con DPI: " + dpi);
                return;
            }

            em.getTransaction().begin();

            HistorialMedico historial = paciente.getHistorialMedico();
            boolean esNuevo = false;

            if (historial == null) {
                System.out.println("Creando nuevo historial médico...");
                historial = new HistorialMedico();
                historial.setPaciente(paciente);
                esNuevo = true;
            } else {
                System.out.println("Historial médico existente:");
                System.out.println("   Alergias: " + (historial.getAlergias() != null ? historial.getAlergias() : "No especificadas"));
                System.out.println("   Antecedentes: " + (historial.getAntecedentes() != null ? historial.getAntecedentes() : "No especificados"));
                System.out.println("   Observaciones: " + (historial.getObservaciones() != null ? historial.getObservaciones() : "No especificadas"));
            }

            // Capturar datos del usuario
            System.out.print("\nAlergias (enter para mantener actual): ");
            String alergias = sc.nextLine().trim();
            if (!alergias.isEmpty()) {
                historial.setAlergias(alergias);
            }

            System.out.print("Antecedentes (enter para mantener actual): ");
            String antecedentes = sc.nextLine().trim();
            if (!antecedentes.isEmpty()) {
                historial.setAntecedentes(antecedentes);
            }

            System.out.print("Observaciones (enter para mantener actual): ");
            String observaciones = sc.nextLine().trim();
            if (!observaciones.isEmpty()) {
                historial.setObservaciones(observaciones);
            }

            // Persistir o actualizar
            if (esNuevo) {
                em.persist(historial);
                // Establecer la relación bidireccional
                paciente.setHistorialMedico(historial);
                em.merge(paciente);
            } else {
                em.merge(historial);
            }

            em.getTransaction().commit();

            System.out.println("Historial médico " + (esNuevo ? "creado" : "actualizado") + " exitosamente para: " + paciente.getNombre());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al procesar historial médico: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}