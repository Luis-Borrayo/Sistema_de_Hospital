package com.luisborrayo.services;

import com.luisborrayo.models.Medico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RegistrarMedico {

    private static final Pattern EMAIL_SIMPLE = Pattern.compile("^\\S+@\\S+\\.\\S+$");

    public static Medico crearMedicoConsola(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            String nombre;
            while (true) {
                System.out.print("Nombre del médico: ");
                nombre = sc.nextLine().trim();
                if (!nombre.isEmpty()) {
                    break;
                } else {
                    System.out.println("El nombre no puede estar vacío. Intente nuevamente.");
                }
            }

            String colegiado;
            while (true) {
                System.out.print("Número de colegiado (único): ");
                colegiado = sc.nextLine().trim();

                if (colegiado.isEmpty()) {
                    System.out.println("El número de colegiado no puede estar vacío.");
                    continue;
                }

                if (existeColegiado(em, colegiado)) {
                    System.out.println("Ya existe un médico con ese número de colegiado. Use otro.");
                } else {
                    break;
                }
            }

            System.out.println("\nEspecialidades disponibles:");
            for (Medico.Especialidad e : Medico.Especialidad.values()) {
                System.out.println("   - " + e.name());
            }

            Medico.Especialidad especialidad;
            while (true) {
                System.out.print("Especialidad: ");
                String esp = sc.nextLine().trim().toUpperCase();

                if (esp.isEmpty()) {
                    System.out.println("Debe seleccionar una especialidad.");
                    continue;
                }

                try {
                    especialidad = Medico.Especialidad.valueOf(esp);
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("Especialidad no válida. Use una de las opciones mostradas.");
                }
            }

            System.out.print("Email (opcional - presione Enter para omitir): ");
            String email = sc.nextLine().trim();

            if (!email.isEmpty()) {
                if (!EMAIL_SIMPLE.matcher(email).matches()) {
                    System.out.println("Formato de email inválido, se omitirá el email.");
                    email = null;
                } else {
                    // Verificar si el email ya existe
                    if (existeEmail(em, email)) {
                        System.out.println("Ya existe un médico con ese email, se omitirá.");
                        email = null;
                    }
                }
            } else {
                email = null;
            }

            Medico medico = new Medico();
            medico.setNombre(nombre);
            medico.setColegiado(colegiado);
            medico.setEspecialidad(especialidad);
            medico.setEmail(email);

            em.getTransaction().begin();
            em.persist(medico);
            em.getTransaction().commit();

            System.out.println("\n Médico registrado exitosamente:");
            System.out.println("   ID: " + medico.getId());
            System.out.println("   Nombre: " + medico.getNombre());
            System.out.println("   Colegiado: " + medico.getColegiado());
            System.out.println("   Especialidad: " + medico.getEspecialidad());
            System.out.println("   Email: " + (medico.getEmail() != null ? medico.getEmail() : "No especificado"));

            return medico;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error al registrar médico: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    private static boolean existeColegiado(EntityManager em, String colegiado) {
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(m) FROM Medico m WHERE m.colegiado = :colegiado",
                Long.class
        );
        q.setParameter("colegiado", colegiado);
        Long count = q.getSingleResult();
        return count != null && count > 0;
    }

    private static boolean existeEmail(EntityManager em, String email) {
        if (email == null || email.isEmpty()) return false;

        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(m) FROM Medico m WHERE m.email = :email",
                Long.class
        );
        q.setParameter("email", email);
        Long count = q.getSingleResult();
        return count != null && count > 0;
    }
}