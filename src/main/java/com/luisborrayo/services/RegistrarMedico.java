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
            System.out.print("Nombre del médico: ");
            String nombre = sc.nextLine().trim();

            String colegiado;
            while (true) {
                System.out.print("Número de colegiado (único): ");
                colegiado = sc.nextLine().trim();
                if (colegiado.isEmpty()) { System.out.println("No puede quedar vacío."); continue; }
                if (existeColegiado(em, colegiado)) {
                    System.out.println("Ya existe un médico con ese número de colegiado.");
                } else break;
            }

            System.out.println("Especialidad (elige uno): ");
            for (Medico.Especialidad e : Medico.Especialidad.values()) {
                System.out.println(" - " + e.name());
            }
            System.out.print("Especialidad: ");
            String esp = sc.nextLine().trim();
            Medico.Especialidad especialidad = Medico.Especialidad.CARDIOLOGIA;
            try { especialidad = Medico.Especialidad.valueOf(esp); } catch (Exception ex) {}

            System.out.print("Email (opcional): ");
            String email = sc.nextLine().trim();
            if (!email.isEmpty() && !EMAIL_SIMPLE.matcher(email).matches()) {
                System.out.println("Email con formato inválido, se dejará vacío.");
                email = null;
            }

            Medico m = new Medico();
            m.setNombre(nombre);
            m.setColegiado(colegiado);
            m.setEspecialidad(especialidad);
            m.setEmail(email);

            em.getTransaction().begin();
            em.persist(m);
            em.getTransaction().commit();
            System.out.println("Médico registrado id: " + m.getId());
            return m;
        } finally {
            em.close();
        }
    }

    private static boolean existeColegiado(EntityManager em, String colegiado) {
        TypedQuery<Long> q = em.createQuery("SELECT COUNT(m) FROM Medico m WHERE m.colegiado = :c", Long.class);
        q.setParameter("c", colegiado);
        Long c = q.getSingleResult();
        return c != null && c > 0;
    }
}
