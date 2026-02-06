package servicio;

import dominio.Cita;
import dominio.Doctor;
import dominio.Paciente;

public interface IServicioClinica {
    boolean loginAdmin(String id, String password);

    boolean altaDoctor(Doctor doctor);
    boolean altaPaciente(Paciente paciente);
    boolean crearCita(Cita cita);

    void listarDoctores();
    void listarPacientes();
    void listarCitas();
}
