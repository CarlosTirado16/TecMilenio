package servicio;

import dominio.Contacto;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class ServicioAddressBookArchivo implements IServicioAddressBook {
    private HashMap<String, String> agenda = new HashMap<>();
    private final String NOMBRE_ARCHIVO = "ArchivoAgenda.txt";

    public ServicioAddressBookArchivo() {
        //Creamos el archivo si no existe
        var archivo = new File(NOMBRE_ARCHIVO);
        var existe = false;
        try {
            existe = archivo.exists();
            if (existe) {
                this.agenda = obtenerContactos();
            } else {
                // Creamos el archivo
                var salida = new PrintWriter(new FileWriter(archivo));
                salida.close();
                System.out.println("Se ha creado el archivo");
            }
        } catch (Exception e) {
            System.out.println("Error al crear el archivo");
        }
    }

    private HashMap<String, String> obtenerContactos() {
        var contactos = new HashMap<String, String>();
        try {
            List<String> lineas = Files.readAllLines(Paths.get(NOMBRE_ARCHIVO));
            for (String linea : lineas) {
                String[] lineaContacto = linea.split(",");
                var nombre = lineaContacto[0];
                var telefono = lineaContacto[1];
                contactos.put(nombre, telefono);
            }

        } catch (Exception e) {
            System.out.println("Erro al leer archivo de agenda: " + e.getMessage());

        }
        return contactos;
    }

    public void crearContacto(Contacto contacto) {
        // Se gaurda en el hashmap
        this.agenda.put(contacto.getNombre(), contacto.getTelefono());
        // Lo cargamos en el archivo
        this.saveContacto(contacto);
    }

    public void saveContacto(Contacto contacto) {
        boolean anexar;
        var archivo = new File(NOMBRE_ARCHIVO);
        try {
            anexar = archivo.exists();
            var salida = new PrintWriter(new FileWriter(archivo, anexar));
            salida.println(contacto.cargarContacto());
            salida.close();
        } catch (Exception e) {
            System.out.println("Error al agregar snack " + e.getMessage());
        }

    }

    public void listContactos() {
        System.out.println("--- Contactos ---");
        this.agenda.forEach((clave, valor) -> System.out.println(valor + " -> " + clave));
        System.out.println();

    }

    public boolean borrarContacto(String nombre) {


        String eliminado = this.agenda.remove(nombre);

        if (eliminado == null) {
            return false; // no existía
        }

        // Reescribimos el archivo
        reescribirArchivo();
        return true;
    }

    private void reescribirArchivo() {
        var archivo = new File(NOMBRE_ARCHIVO);
        try (var salida = new PrintWriter(new FileWriter(archivo, false))) { // false = sobrescribir
            this.agenda.forEach((nombre, telefono) -> salida.println(nombre + "," + telefono));
        } catch (Exception e) {
            System.out.println("Error al reescribir archivo: " + e.getMessage());
        }
    }

}







