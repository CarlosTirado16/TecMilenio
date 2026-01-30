package servicio;

import dominio.Contacto;

public interface IServicioAddressBook {
    void crearContacto(Contacto contacto);
    void listContactos();
    boolean borrarContacto(String nombre);
}
