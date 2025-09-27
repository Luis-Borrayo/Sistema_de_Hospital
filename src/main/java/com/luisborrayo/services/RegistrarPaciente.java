package com.luisborrayo.services;

import com.luisborrayo.models.Pacientes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RegistrarPaciente {

    private static final Pattern EMAIL_SIMPLE = Pattern.compile("^\\S+@\\S+\\.\\S+$");

    public static Pacientes crearPacienteConsola(Scanner sc) {
        EntityManager em = com.luisborrayo.utils.JpaUtil.getEntityManager();
        try {
            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();

            String dpi;
            while (true) {
                System.out.print("DPI (único): ");
                dpi = sc.nextLine().trim();
                if (dpi.isEmpty()) { System.out.println("DPI no puede quedar vacío."); continue; }
                if (existeDpi(em, dpi)) {
                    System.out.println("Ya existe un paciente con ese DPI. Ingrese otro DPI.");
                } else break;
            }

            System.out.print("Fecha de nacimiento (AAAA-MM-DD) o enter: ");
            String fn = sc.nextLine().trim();
            LocalDate fecha = null;
            if (!fn.isEmpty()) fecha = LocalDate.parse(fn);

            System.out.print("Teléfono (opcional): ");
            String tel = sc.nextLine().trim();

            System.out.print("Email (opcional): ");
            String email = sc.nextLine().trim();
            if (!email.isEmpty() && !EMAIL_SIMPLE.matcher(email).matches()) {
                System.out.println("Email con formato inválido, se dejará vacío.");
                email = null;
            }

            Pacientes p = new Pacientes();
            p.setNombre(nombre);
            p.setDpi(dpi);
            p.setFechaNacimiento(fecha);
            p.setTelefono(tel);
            p.setEmail(email);

            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();

            System.out.println("Paciente registrado con id: " + p.getId());
            return p;
        } finally {
            em.close();
        }
    }

    private static boolean existeDpi(EntityManager em, String dpi) {
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(p) FROM Pacientes p WHERE p.dpi = :dpi", Long.class);
        q.setParameter("dpi", dpi);
        Long c = q.getSingleResult();
        return c != null && c > 0;
    }
}
