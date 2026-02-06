package dominio;

public class Paciente {
    private String id;
    private String nombre;
    private String telefono;

    public Paciente(String id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
}
