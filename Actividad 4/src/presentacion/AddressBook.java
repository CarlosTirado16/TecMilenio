package presentacion;

import dominio.Contacto;
import servicio.ServicioAddressBookArchivo;
import servicio.IServicioAddressBook;


import java.util.Scanner;

public class AddressBook {
    static void main() { appAgenda();}

    public static void appAgenda() {
        var salir = false;
        var consola = new Scanner(System.in);
        //Usamos la interfaz
        IServicioAddressBook agenda = new ServicioAddressBookArchivo();
        agenda.listContactos();
        while (!salir) {
            try {
                var opcion = mostrarMenu(consola);
                salir = ejecutarOpciones(opcion, consola, agenda);
            } catch (Exception e) {
                System.out.println("Ocurrio un error " + e.getMessage());
            }
        }

    }

    private static int mostrarMenu(Scanner consola) {
        System.out.println("""
                Menu:
                1. Mostrar contactos
                2. Crear contacto
                3. Borrar contacto
                4. Salir
                Teclee una opcion:""");
        return Integer.parseInt(consola.nextLine());
    }

    private static boolean ejecutarOpciones(int opcion, Scanner consola,
                                            IServicioAddressBook agenda) {
        var salir = false;
        switch (opcion) {
            case 1 -> agenda.listContactos();
            case 2 -> {
                System.out.print("Nombre: ");
                var nombre = consola.nextLine();
                System.out.print("Telefono: ");
                var telefono = consola.nextLine();
                agenda.crearContacto(new Contacto(nombre, telefono));
                System.out.println("Contacto agregado exitosamente");
                agenda.listContactos();
            }
            case 3 -> {
                System.out.print("Nombre del contacto a borrar: ");
                var nombre = consola.nextLine();

                boolean borrado = agenda.borrarContacto(nombre);

                if (borrado) {
                    System.out.println("Contacto borrado correctamente.");
                } else {
                    System.out.println("No se encontró un contacto con ese nombre.");
                }
                agenda.listContactos();}
            case 4 -> {
                System.out.println("Gracias por usar la agenda!");
                salir = true;
            }
            default -> System.out.println("Opcion no valida.");
        }
        return salir;
    }

}
