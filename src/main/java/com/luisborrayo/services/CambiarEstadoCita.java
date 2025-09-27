package com.luisborrayo.services;

import com.luisborrayo.models.Cita;
import jakarta.persistence.EntityManager;
import java.util.Scanner;

public class CambiarEstadoCita {

    public static void cambiarEstadoConsola(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            System.out.print("Id de la cita: ");
            Long id = Long.parseLong(sc.nextLine().trim());

            Cita cita = em.find(Cita.class, id);
            if (cita == null) { System.out.println("Cita no encontrada"); return; }

            System.out.println("Estado actual: " + cita.getEstado());
            System.out.print("Nuevo estado (PROGRAMADA, ATENDIDA, CANCELADA): ");
            String nuevo = sc.nextLine().trim();
            Cita.EstadoCita estado;
            try { estado = Cita.EstadoCita.valueOf(nuevo); } catch (Exception e) {
                System.out.println("Estado inválido.");
                return;
            }

            em.getTransaction().begin();
            cita.setEstado(estado);
            em.getTransaction().commit();
            System.out.println("Estado actualizado.");
        } finally {
            em.close();
        }
    }
}
