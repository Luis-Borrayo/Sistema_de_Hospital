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
            String input = sc.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcion = 0;
            }

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
            Integer id;
            while (true) {
                System.out.print("Ingrese ID de la cita a eliminar: ");
                String idTexto = sc.nextLine().trim();
                if (idTexto.isEmpty()) {
                    System.out.println("ID no puede estar vacío.");
                    continue;
                }

                try {
                    id = Integer.parseInt(idTexto);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("ID debe ser un número válido.");
                }
            }

            Cita cita = em.find(Cita.class, id);
            if (cita == null) {
                System.out.println("No se encontró la cita con ID " + id);
                return;
            }

            System.out.println("Cita encontrada:");
            System.out.println("  Paciente: " + cita.getPaciente().getNombre());
            System.out.println("  Médico: " + cita.getMedico().getNombre());
            System.out.println("  Fecha: " + cita.getFechaHora());
            System.out.println("  Estado: " + cita.getEstado());

            System.out.print("¿Está seguro que desea eliminar esta cita? (s/n): ");
            String confirmacion = sc.nextLine().trim().toLowerCase();

            if (!confirmacion.equals("s") && !confirmacion.equals("si")) {
                System.out.println("Eliminación cancelada.");
                return;
            }

            em.getTransaction().begin();
            em.remove(cita);
            em.getTransaction().commit();

            System.out.println("Cita eliminada correctamente: ID " + id);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al eliminar cita: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void eliminarPaciente(Scanner sc) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String dpi;
            while (true) {
                System.out.print("Ingrese DPI del paciente a eliminar: ");
                dpi = sc.nextLine().trim();
                if (!dpi.isEmpty()) break;
                System.out.println("DPI no puede estar vacío.");
            }

            TypedQuery<Pacientes> query = em.createQuery(
                    "SELECT p FROM Pacientes p WHERE p.dpi = :dpi", Pacientes.class
            );
            query.setParameter("dpi", dpi);
            Pacientes paciente = query.getResultStream().findFirst().orElse(null);

            if (paciente == null) {
                System.out.println("No se encontró paciente con DPI " + dpi);
                return;
            }

            TypedQuery<Long> citasQuery = em.createQuery(
                    "SELECT COUNT(c) FROM Cita c WHERE c.paciente.id = :pacienteId", Long.class
            );
            citasQuery.setParameter("pacienteId", paciente.getId());
            Long numCitas = citasQuery.getSingleResult();

            System.out.println("Paciente encontrado: " + paciente.getNombre());
            System.out.println("El paciente tiene " + numCitas + " citas registradas.");

            if (numCitas > 0) {
                System.out.print("¿Desea eliminar también todas sus citas? (s/n): ");
                String resp = sc.nextLine().trim().toLowerCase();

                if (!resp.equals("s") && !resp.equals("si")) {
                    System.out.println("No se puede eliminar el paciente porque tiene citas registradas.");
                    return;
                }
            }

            System.out.print("¿Está seguro que desea eliminar este paciente? (s/n): ");
            String confirmacion = sc.nextLine().trim().toLowerCase();

            if (!confirmacion.equals("s") && !confirmacion.equals("si")) {
                System.out.println("Eliminación cancelada.");
                return;
            }

            em.getTransaction().begin();

            if (numCitas > 0) {
                em.createQuery("DELETE FROM Cita c WHERE c.paciente.id = :pacienteId")
                        .setParameter("pacienteId", paciente.getId())
                        .executeUpdate();
                System.out.println("Eliminadas " + numCitas + " citas del paciente.");
            }

            em.createQuery("DELETE FROM HistorialMedico h WHERE h.paciente.id = :pacienteId")
                    .setParameter("pacienteId", paciente.getId())
                    .executeUpdate();

            // Finalmente eliminar el paciente
            em.remove(paciente);
            em.getTransaction().commit();

            System.out.println("Paciente eliminado correctamente.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al eliminar paciente: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}