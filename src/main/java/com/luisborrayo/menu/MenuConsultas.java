package com.luisborrayo.menu;

import com.luisborrayo.models.Cita;
import com.luisborrayo.models.HistorialMedico;
import com.luisborrayo.models.Medico;
import com.luisborrayo.models.Pacientes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuConsultas {
    public static void menuConsultas(Scanner sc) {
        int opcion = 0;
        do {
            System.out.println("-- Consultas --");
            System.out.println("1 Listar pacientes con sus citas");
            System.out.println("2 Listar médicos con próximas citas");
            System.out.println("3 Buscar citas por rango de fechas");
            System.out.println("4 Ver historial médico de un paciente");
            System.out.println("5. Volver al menú principal");

            System.out.print("Seleccione una opción: ");
            String input = sc.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch (opcion) {
                case 1 -> listarPacientesConCitas();
                case 2 -> listarMedicosConProximasCitas();
                case 3 -> buscarCitasPorRango(sc);
                case 4 -> verHistorialPaciente(sc);
                case 5 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    private static void listarPacientesConCitas() {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            TypedQuery<Pacientes> queryPacientes = em.createQuery("SELECT p FROM Pacientes p", Pacientes.class);
            List<Pacientes> pacientes = queryPacientes.getResultList();

            for (Pacientes p : pacientes) {
                System.out.println("Paciente: " + p.getNombre() + " | DPI: " + p.getDpi());

                TypedQuery<Cita> queryCitas = em.createQuery(
                        "SELECT c FROM Cita c WHERE c.paciente.id = :pacienteId ORDER BY c.fechaHora",
                        Cita.class
                );
                queryCitas.setParameter("pacienteId", p.getId());
                List<Cita> citas = queryCitas.getResultList();

                if (citas.isEmpty()) {
                    System.out.println("  Sin citas");
                } else {
                    for (Cita c : citas) {
                        System.out.println("  Cita con Dr. " + c.getMedico().getNombre() +
                                " en " + c.getFechaHora() +
                                " | Estado: " + c.getEstado());
                    }
                }
            }
        } finally {
            em.close();
        }
    }

    private static void listarMedicosConProximasCitas() {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            LocalDateTime ahora = LocalDateTime.now();

            TypedQuery<Medico> queryMedicos = em.createQuery("SELECT m FROM Medico m", Medico.class);
            List<Medico> medicos = queryMedicos.getResultList();

            for (Medico m : medicos) {
                System.out.println("Médico: " + m.getNombre() + " | Colegiado: " + m.getColegiado());

                TypedQuery<Cita> queryCitas = em.createQuery(
                        "SELECT c FROM Cita c WHERE c.medico.id = :medicoId AND c.fechaHora >= :ahora ORDER BY c.fechaHora",
                        Cita.class
                );
                queryCitas.setParameter("medicoId", m.getId());
                queryCitas.setParameter("ahora", ahora);
                List<Cita> citasProximas = queryCitas.getResultList();

                if (citasProximas.isEmpty()) {
                    System.out.println("  Sin próximas citas");
                } else {
                    for (Cita c : citasProximas) {
                        System.out.println("  Cita con paciente: " + c.getPaciente().getNombre() +
                                " en " + c.getFechaHora() +
                                " | Estado: " + c.getEstado());
                    }
                }
            }
        } finally {
            em.close();
        }
    }

    private static void buscarCitasPorRango(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            LocalDateTime inicio;
            while (true) {
                System.out.print("Fecha inicio (YYYY-MM-DDTHH:MM): ");
                String fechaTexto = sc.nextLine().trim();
                if (fechaTexto.isEmpty()) {
                    System.out.println("Fecha no puede estar vacía.");
                    continue;
                }
                try {
                    inicio = LocalDateTime.parse(fechaTexto, fmt);
                    break;
                } catch (Exception e) {
                    System.out.println("Formato inválido. Use YYYY-MM-DDTHH:MM");
                }
            }

            LocalDateTime fin;
            while (true) {
                System.out.print("Fecha fin (YYYY-MM-DDTHH:MM): ");
                String fechaTexto = sc.nextLine().trim();
                if (fechaTexto.isEmpty()) {
                    System.out.println("Fecha no puede estar vacía.");
                    continue;
                }
                try {
                    fin = LocalDateTime.parse(fechaTexto, fmt);
                    if (fin.isBefore(inicio)) {
                        System.out.println("Fecha fin debe ser posterior a fecha inicio.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Formato inválido. Use YYYY-MM-DDTHH:MM");
                }
            }

            TypedQuery<Cita> query = em.createQuery(
                    "SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :inicio AND :fin ORDER BY c.fechaHora",
                    Cita.class
            );
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);

            List<Cita> citas = query.getResultList();
            if (citas.isEmpty()) {
                System.out.println("No se encontraron citas en ese rango.");
            } else {
                for (Cita c : citas) {
                    System.out.println("Cita: Paciente=" + c.getPaciente().getNombre() +
                            " | Médico=" + c.getMedico().getNombre() +
                            " | Fecha=" + c.getFechaHora() +
                            " | Estado=" + c.getEstado());
                }
            }
        } finally {
            em.close();
        }
    }

    private static void verHistorialPaciente(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            String dpi;
            while (true) {
                System.out.print("Ingrese DPI del paciente: ");
                dpi = sc.nextLine().trim();
                if (!dpi.isEmpty()) break;
                System.out.println("DPI no puede estar vacío.");
            }

            TypedQuery<HistorialMedico> query = em.createQuery(
                    "SELECT h FROM HistorialMedico h WHERE h.paciente.dpi = :dpi",
                    HistorialMedico.class
            );
            query.setParameter("dpi", dpi);

            HistorialMedico h = query.getResultStream().findFirst().orElse(null);
            if (h == null) {
                System.out.println("No se encontró historial para el paciente con DPI " + dpi);
            } else {
                System.out.println("Paciente: " + h.getPaciente().getNombre());
                System.out.println("Alergias: " + h.getAlergias());
                System.out.println("Antecedentes: " + h.getAntecedentes());
                System.out.println("Observaciones: " + h.getObservaciones());
            }
        } finally {
            em.close();
        }
    }
}