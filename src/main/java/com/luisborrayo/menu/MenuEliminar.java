package com.luisborrayo.menu;

import com.luisborrayo.models.Cita;
import com.luisborrayo.models.Pacientes;
import com.luisborrayo.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Scanner;

public class MenuEliminar {

    public static void menueliminar(Scanner sc) {
        int opcion = 0;
        do {
            System.out.println("-- Eliminar --");
            System.out.println("1. Eliminar cita");
            System.out.println("2. Eliminar paciente");
            System.out.println("3. Volver al menú principal");

            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> eliminarCita(sc);
                case 2 -> eliminarPaciente(sc);
                case 3 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 3);
    }

    private static void eliminarCita(Scanner sc) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            System.out.print("Ingrese ID de la cita a eliminar: ");
            Long id = sc.nextLong();
            sc.nextLine();

            Cita cita = em.find(Cita.class, id);
            if (cita == null) {
                System.out.println("No se encontró la cita con ID " + id);
                return;
            }

            em.getTransaction().begin();
            em.remove(cita);
            em.getTransaction().commit();

            System.out.println("Cita eliminada correctamente: ID " + id);
        } finally {
            em.close();
        }
    }

    private static void eliminarPaciente(Scanner sc) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            System.out.print("Ingrese DPI del paciente a eliminar: ");
            String dpi = sc.nextLine().trim();

            TypedQuery<Pacientes> query = em.createQuery(
                    "SELECT p FROM Pacientes p WHERE p.dpi = :dpi", Pacientes.class
            );
            query.setParameter("dpi", Long.parseLong(dpi));
            Pacientes paciente = query.getResultStream().findFirst().orElse(null);

            if (paciente == null) {
                System.out.println("No se encontró paciente con DPI " + dpi);
                return;
            }
            System.out.println("El paciente tiene " + paciente.getCitas().size() + " citas.");
            System.out.print("¿Desea eliminar también todas sus citas? (s/n): ");
            String resp = sc.nextLine().trim().toLowerCase();

            em.getTransaction().begin();
            if (resp.equals("s")) {
                em.remove(paciente);
                System.out.println("Paciente y sus citas eliminadas correctamente.");
            } else {
                if (!paciente.getCitas().isEmpty()) {
                    System.out.println("No se puede eliminar el paciente porque tiene citas registradas.");
                    em.getTransaction().rollback();
                    return;
                } else {
                    em.remove(paciente);
                    System.out.println("Paciente eliminado correctamente.");
                }
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
