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
            System.out.println("6.1 Listar pacientes con sus citas");
            System.out.println("6.2 Listar médicos con próximas citas");
            System.out.println("6.3 Buscar citas por rango de fechas");
            System.out.println("6.4 Ver historial médico de un paciente");
            System.out.println("5. Volver al menú principal");

            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

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
            TypedQuery<Pacientes> query = em.createQuery("SELECT DISTINCT P FROM Pacientes P LEFT JOIN FETCH p.citas", Pacientes.class);
        List<Pacientes> pacientes = query.getResultList();
            for (Pacientes p : pacientes) {
                System.out.println("Paciente: " + p.getNombre() + " | DPI: " + p.getDpi());
                if (p.getCitas().isEmpty()) {
                    System.out.println("  Sin citas");
                } else {
                    for (Cita c : p.getCitas()) {
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
            TypedQuery<Medico> query = em.createQuery(
                    "SELECT DISTINCT m FROM Medico m LEFT JOIN FETCH m.citas c WHERE c.fechaHora >= :ahora OR c IS NULL",
                    Medico.class
            );
            query.setParameter("ahora", ahora);
            List<Medico> medicos = query.getResultList();

            for (Medico m : medicos) {
                System.out.println("Médico: " + m.getNombre() + " | Colegiado: " + m.getColegiado());
                if (m.getCitas().isEmpty()) {
                    System.out.println("  Sin próximas citas");
                } else {
                    for (Cita c : m.getCitas()) {
                        if (c.getFechaHora().isAfter(ahora)) {
                            System.out.println("  Cita con paciente: " + c.getPaciente().getNombre() +
                                    " en " + c.getFechaHora() +
                                    " | Estado: " + c.getEstado());
                        }
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
            System.out.print("Fecha inicio (YYYY-MM-DDTHH:MM): ");
            LocalDateTime inicio = LocalDateTime.parse(sc.nextLine().trim(), fmt);
            System.out.print("Fecha fin (YYYY-MM-DDTHH:MM): ");
            LocalDateTime fin = LocalDateTime.parse(sc.nextLine().trim(), fmt);

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
            System.out.print("Ingrese DPI del paciente: ");
            String dpi = sc.nextLine().trim();

            TypedQuery<HistorialMedico> query = em.createQuery(
                    "SELECT h FROM HistorialMedico h WHERE h.paciente.dpi = :dpi", HistorialMedico.class
            );
            query.setParameter("dpi", Long.parseLong(dpi));

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