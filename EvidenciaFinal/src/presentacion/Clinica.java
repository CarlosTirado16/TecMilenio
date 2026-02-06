package presentacion;

import dominio.Cita;
import dominio.Doctor;
import dominio.Paciente;
import servicio.IServicioClinica;
import servicio.ServicioClinica;

import java.util.Scanner;

public class Clinica {
    public static void main(String[] args) {
        appClinica();
    }

    public static void appClinica() {
        Scanner consola = new Scanner(System.in);
        IServicioClinica sistema = new ServicioClinica();

        System.out.println("=== SISTEMA DE CITAS (Administradores) ===");

        boolean acceso = false;
        for (int i = 1; i <= 3; i++) {
            System.out.print("ID Admin: ");
            String id = consola.nextLine();
            System.out.print("Password: ");
            String pass = consola.nextLine();

            if (sistema.loginAdmin(id, pass)) {
                acceso = true;
                break;
            }
            System.out.println("Credenciales incorrectas. Intento " + i + " de 3.\n");
        }

        if (!acceso) {
            System.out.println("Acceso denegado. Saliendo...");
            return;
        }

        boolean salir = false;
        while (!salir) {
            try {
                int opcion = mostrarMenu(consola);
                salir = ejecutarOpcion(opcion, consola, sistema);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: escribe un número de opción.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    private static int mostrarMenu(Scanner consola) {
        System.out.println("""
                \nMenu:
                1. Listar doctores
                2. Alta doctor
                3. Listar pacientes
                4. Alta paciente
                5. Listar citas
                6. Crear cita
                7. Salir
                Teclee una opción:""");
        return Integer.parseInt(consola.nextLine());
    }

    private static boolean ejecutarOpcion(int opcion, Scanner consola, IServicioClinica sistema) {
        switch (opcion) {
            case 1 -> sistema.listarDoctores();

            case 2 -> {
                System.out.print("ID Doctor (ej: DOC01): ");
                String id = consola.nextLine();
                System.out.print("Nombre: ");
                String nombre = consola.nextLine();
                System.out.print("Especialidad: ");
                String esp = consola.nextLine();

                boolean ok = sistema.altaDoctor(new Doctor(id, nombre, esp));
                System.out.println(ok ? "Doctor guardado.\n" : "No se pudo guardar (ID vacío).\n");
            }

            case 3 -> sistema.listarPacientes();

            case 4 -> {
                System.out.print("ID Paciente (ej: PAC01): ");
                String id = consola.nextLine();
                System.out.print("Nombre: ");
                String nombre = consola.nextLine();
                System.out.print("Teléfono: ");
                String tel = consola.nextLine();

                boolean ok = sistema.altaPaciente(new Paciente(id, nombre, tel));
                System.out.println(ok ? "Paciente guardado.\n" : "No se pudo guardar (ID vacío).\n");
            }

            case 5 -> sistema.listarCitas();

            case 6 -> {
                System.out.print("ID Cita (ej: CITA01): ");
                String idCita = consola.nextLine();

                System.out.print("Fecha y hora (yyyy-MM-dd HH:mm): ");
                String fechaHora = consola.nextLine();

                System.out.print("ID Doctor: ");
                String idDoc = consola.nextLine();

                System.out.print("ID Paciente: ");
                String idPac = consola.nextLine();

                System.out.print("Motivo: ");
                String motivo = consola.nextLine();

                boolean creada = sistema.crearCita(new Cita(idCita, fechaHora, idDoc, idPac, motivo));

                if (creada) {
                    System.out.println("Cita creada correctamente.\n");
                } else {
                    System.out.println("No se pudo crear la cita. Revisa:\n" +
                            "- Que el ID de cita no esté repetido\n" +
                            "- Que existan Doctor y Paciente\n" +
                            "- Que la fecha/hora esté en formato yyyy-MM-dd HH:mm\n");
                }
            }

            case 7 -> {
                System.out.println("Gracias por usar el sistema.");
                return true;
            }

            default -> System.out.println("Opción no válida.");
        }
        return false;
    }
}
