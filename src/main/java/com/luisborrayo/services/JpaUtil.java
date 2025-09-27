package com.luisborrayo.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf;

    static {
        try {
            // "default" debe coincidir con el nombre de tu unidad de persistencia en persistence.xml
            emf = Persistence.createEntityManagerFactory("default");
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error al inicializar EntityManagerFactory: " + e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
