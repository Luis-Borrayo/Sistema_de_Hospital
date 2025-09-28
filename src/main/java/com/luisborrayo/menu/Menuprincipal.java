package com.luisborrayo.menu;

import com.luisborrayo.services.*;
import java.util.Scanner;

public class Menuprincipal {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int opcion = 0;
        do{
            System.out.println("Bienvenido al sistema de gestion del Hospital La Cumbre");
            System.out.println("Ingrese el numero de la gestión a realizar...");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Crear/editar historial médico de un paciente");
            System.out.println("3. Registrar médico");
            System.out.println("4. Agendar cita");
            System.out.println("5. Cambiar estado de una cita");
            System.out.println("6. Consultas");
            System.out.println("7. Eliminar");
            System.out.println("8. Semilla de datos");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");

            String input = in.nextLine().trim();
            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            switch(opcion){
                case 1 -> RegistrarPaciente.crearPacienteConsola(in);
                case 2 -> EditarHistorial.editarHistorialConsola(in);
                case 3 -> RegistrarMedico.crearMedicoConsola(in);
                case 4 -> AgendarCita.agendarConsola(in);
                case 5 -> CambiarEstadoCita.cambiarEstadoConsola(in);
                case 6 -> MenuConsultas.menuConsultas(in);
                case 7 -> MenuEliminar.menueliminar(in);
                case 8 -> com.luisborrayo.services.CargarSemilla.cargar();
                case 9 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 9);

        in.close();
    }
}