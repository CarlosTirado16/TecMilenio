package servicio;

import dominio.Admin;
import dominio.Cita;
import dominio.Doctor;
import dominio.Paciente;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ServicioClinica implements IServicioClinica{
    private final HashMap<String, Doctor> doctores = new HashMap<>();
    private final HashMap<String, Paciente> pacientes = new HashMap<>();
    private final List<Cita> citas = new ArrayList<>();
    private final HashMap<String, String> admins = new HashMap<>();

    // Archivos
    private final String ARCH_DOCTORES = "Doctores.txt";
    private final String ARCH_PACIENTES = "Pacientes.txt";
    private final String ARCH_CITAS = "Citas.txt";
    private final String ARCH_ADMINS = "Admins.txt";

    // Separador
    private final String SEP = "|";

    // Validador de fecha
    private final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ServicioClinica() {
        crearSiNoExiste(ARCH_DOCTORES);
        crearSiNoExiste(ARCH_PACIENTES);
        crearSiNoExiste(ARCH_CITAS);
        crearSiNoExiste(ARCH_ADMINS);

        cargarTodo();

        // Admin por defecto si no hay
        if (admins.isEmpty()) {
            Admin adminDefault = new Admin("admin", "1234");
            admins.put(normalizarId(adminDefault.getId()), adminDefault.getPassword());
            reescribirAdmins();
            System.out.println("Admin por defecto creado: admin / 1234");
        }
    }

    // ----------------- HELPERS PRO -----------------
    private String normalizarId(String s) {
        if (s == null) return "";
        return s.trim().toUpperCase();
    }

    private String normalizarTexto(String s) {
        if (s == null) return "";
        return s.trim();
    }

    // Para guardar texto sin romper separador |
    private String escape(String s) {
        // Reemplaza el separador y saltos de línea por espacios seguros
        return normalizarTexto(s)
                .replace(SEP, "/")
                .replace("\n", " ")
                .replace("\r", " ");
    }

    private void crearSiNoExiste(String nombreArchivo) {
        try {
            File f = new File(nombreArchivo);
            if (!f.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                    // crea vacío
                }
            }
        } catch (Exception e) {
            System.out.println("Error creando archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    private List<String> leerLineas(String archivo) {
        try {
            return Files.readAllLines(Path.of(archivo));
        } catch (Exception e) {
            System.out.println("Error leyendo " + archivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void escribirLineas(String archivo, List<String> lineas) {
        try (PrintWriter out = new PrintWriter(new FileWriter(archivo, false))) {
            for (String l : lineas) out.println(l);
        } catch (Exception e) {
            System.out.println("Error escribiendo " + archivo + ": " + e.getMessage());
        }
    }

    private void cargarTodo() {
        // Limpia y carga
        doctores.clear();
        pacientes.clear();
        citas.clear();
        admins.clear();

        cargarAdmins();
        cargarDoctores();
        cargarPacientes();
        cargarCitas();
    }

    private void cargarAdmins() {
        for (String l : leerLineas(ARCH_ADMINS)) {
            if (l == null || l.trim().isEmpty()) continue;
            String[] p = l.split("\\|", -1);
            if (p.length < 2) continue;
            String id = normalizarId(p[0]);
            String pass = p[1]; // password se respeta tal cual
            if (!id.isEmpty()) admins.put(id, pass);
        }
    }

    private void cargarDoctores() {
        for (String l : leerLineas(ARCH_DOCTORES)) {
            if (l == null || l.trim().isEmpty()) continue;
            var p = l.split("\\|", -1);
            if (p.length < 3) continue;

            String id = normalizarId(p[0]);
            String nombre = normalizarTexto(p[1]);
            String esp = normalizarTexto(p[2]);

            if (!id.isEmpty()) doctores.put(id, new Doctor(id, nombre, esp));
        }
    }

    private void cargarPacientes() {
        for (String l : leerLineas(ARCH_PACIENTES)) {
            if (l == null || l.trim().isEmpty()) continue;
            String[] p = l.split("\\|", -1);
            if (p.length < 3) continue;

            String id = normalizarId(p[0]);
            String nombre = normalizarTexto(p[1]);
            String tel = normalizarTexto(p[2]);

            if (!id.isEmpty()) pacientes.put(id, new Paciente(id, nombre, tel));
        }
    }

    private void cargarCitas() {
        for (String l : leerLineas(ARCH_CITAS)) {
            if (l == null || l.trim().isEmpty()) continue;
            String[] p = l.split("\\|", -1);
            if (p.length < 5) continue;

            String idCita = normalizarId(p[0]);
            String fechaHora = normalizarTexto(p[1]);
            String idDoc = normalizarId(p[2]);
            String idPac = normalizarId(p[3]);
            String motivo = normalizarTexto(p[4]);

            if (!idCita.isEmpty()) citas.add(new Cita(idCita, fechaHora, idDoc, idPac, motivo));
        }
    }

    // ----------------- REESCRITURA -----------------
    private void reescribirAdmins() {
        List<String> out = new ArrayList<>();
        for (var e : admins.entrySet()) {
            out.add(e.getKey() + SEP + e.getValue());
        }
        escribirLineas(ARCH_ADMINS, out);
    }

    private void reescribirDoctores() {
        List<String> out = new ArrayList<>();
        for (var e : doctores.entrySet()) {
            Doctor d = e.getValue();
            out.add(normalizarId(d.getId()) + SEP + escape(d.getNombre()) + SEP + escape(d.getEspecialidad()));
        }
        escribirLineas(ARCH_DOCTORES, out);
    }

    private void reescribirPacientes() {
        List<String> out = new ArrayList<>();
        for (var e : pacientes.entrySet()) {
            Paciente p = e.getValue();
            out.add(normalizarId(p.getId()) + SEP + escape(p.getNombre()) + SEP + escape(p.getTelefono()));
        }
        escribirLineas(ARCH_PACIENTES, out);
    }

    private void reescribirCitas() {
        List<String> out = new ArrayList<>();
        for (Cita c : citas) {
            out.add(
                    normalizarId(c.getIdCita()) + SEP +
                            escape(c.getFechaHora()) + SEP +
                            normalizarId(c.getIdDoctor()) + SEP +
                            normalizarId(c.getIdPaciente()) + SEP +
                            escape(c.getMotivo())
            );
        }
        escribirLineas(ARCH_CITAS, out);
    }

    // ----------------- LOGIN -----------------
    @Override
    public boolean loginAdmin(String id, String password) {
        String key = normalizarId(id);
        String pass = admins.get(key);
        return pass != null && pass.equals(password);
    }

    // ----------------- ALTAS -----------------
    @Override
    public boolean altaDoctor(Doctor doctor) {
        String id = normalizarId(doctor.getId());
        if (id.isEmpty()) return false;

        Doctor d = new Doctor(id, normalizarTexto(doctor.getNombre()), normalizarTexto(doctor.getEspecialidad()));
        doctores.put(id, d);

        // Reescribe para evitar duplicados
        reescribirDoctores();
        return true;
    }

    @Override
    public boolean altaPaciente(Paciente paciente) {
        String id = normalizarId(paciente.getId());
        if (id.isEmpty()) return false;

        Paciente p = new Paciente(id, normalizarTexto(paciente.getNombre()), normalizarTexto(paciente.getTelefono()));
        pacientes.put(id, p);

        // Reescribe para evitar duplicados
        reescribirPacientes();
        return true;
    }

    // ----------------- CITAS  -----------------
    private boolean validarFechaHora(String fechaHora) {
        try {
            LocalDateTime.parse(fechaHora, FMT);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    @Override
    public boolean crearCita(Cita cita) {
        String idCita = normalizarId(cita.getIdCita());
        String fechaHora = normalizarTexto(cita.getFechaHora());
        String idDoc = normalizarId(cita.getIdDoctor());
        String idPac = normalizarId(cita.getIdPaciente());
        String motivo = normalizarTexto(cita.getMotivo());

        if (idCita.isEmpty()) return false;

        // Evitar ID de cita duplicado
        for (Cita c : citas) {
            if (normalizarId(c.getIdCita()).equals(idCita)) return false;
        }

        // Validar que existan doctor y paciente
        if (!doctores.containsKey(idDoc)) return false;
        if (!pacientes.containsKey(idPac)) return false;

        // Validar formato de fecha/hora
        if (!validarFechaHora(fechaHora)) return false;

        citas.add(new Cita(idCita, fechaHora, idDoc, idPac, motivo));

        // Reescribe para evitar duplicados y mantener consistencia
        reescribirCitas();
        return true;
    }

    // ----------------- LISTADOS -----------------
    @Override
    public void listarDoctores() {
        System.out.println("--- DOCTORES ---");
        if (doctores.isEmpty()) {
            System.out.println("No hay doctores registrados.\n");
            return;
        }
        doctores.forEach((id, d) ->
                System.out.println("ID: " + id + " | " + d.getNombre() + " | " + d.getEspecialidad()));
        System.out.println();
    }

    @Override
    public void listarPacientes() {
        System.out.println("--- PACIENTES ---");
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.\n");
            return;
        }
        pacientes.forEach((id, p) ->
                System.out.println("ID: " + id + " | " + p.getNombre() + " | Tel: " + p.getTelefono()));
        System.out.println();
    }

    @Override
    public void listarCitas() {
        System.out.println("--- CITAS ---");
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.\n");
            return;
        }

        for (Cita c : citas) {
            Doctor d = doctores.get(normalizarId(c.getIdDoctor()));
            Paciente p = pacientes.get(normalizarId(c.getIdPaciente()));

            String nomDoc = (d != null) ? d.getNombre() : "Desconocido";
            String nomPac = (p != null) ? p.getNombre() : "Desconocido";

            System.out.println("Cita: " + c.getIdCita()
                    + " | " + c.getFechaHora()
                    + " | Doctor: " + nomDoc
                    + " | Paciente: " + nomPac
                    + " | Motivo: " + c.getMotivo());
        }
        System.out.println();
    }
}
