package com.luisborrayo.services;

import com.luisborrayo.models.Cita;
import jakarta.persistence.EntityManager;
import java.util.Scanner;

public class CambiarEstadoCita {

    public static void cambiarEstadoConsola(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            Integer id;
            while (true) {
                System.out.print("Id de la cita: ");
                String idTexto = sc.nextLine().trim();
                if (idTexto.isEmpty()) {
                    System.out.println("Id no puede estar vacío.");
                    continue;
                }

                try {
                    id = Integer.parseInt(idTexto);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Id debe ser un número válido.");
                }
            }

            Cita cita = em.find(Cita.class, id);
            if (cita == null) {
                System.out.println("Cita no encontrada");
                return;
            }

            System.out.println("Estado actual: " + cita.getEstado());

            Cita.EstadoCita estado;
            while (true) {
                System.out.print("Nuevo estado (PROGRAMADA, ATENDIDA, CANCELADA): ");
                String nuevo = sc.nextLine().trim();
                if (nuevo.isEmpty()) {
                    System.out.println("Estado no puede estar vacío.");
                    continue;
                }

                try {
                    estado = Cita.EstadoCita.valueOf(nuevo);
                    break;
                } catch (Exception e) {
                    System.out.println("Estado inválido. Use: PROGRAMADA, ATENDIDA, CANCELADA");
                }
            }

            em.getTransaction().begin();
            cita.setEstado(estado);
            em.getTransaction().commit();
            System.out.println("Estado actualizado.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al cambiar estado: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}