package dominio;

public class Cita {
    private String idCita;
    private String fechaHora; // Guardamos como texto "yyyy-MM-dd HH:mm"
    private String idDoctor;
    private String idPaciente;
    private String motivo;

    public Cita(String idCita, String fechaHora, String idDoctor, String idPaciente, String motivo) {
        this.idCita = idCita;
        this.fechaHora = fechaHora;
        this.idDoctor = idDoctor;
        this.idPaciente = idPaciente;
        this.motivo = motivo;
    }

    public String getIdCita() { return idCita; }
    public String getFechaHora() { return fechaHora; }
    public String getIdDoctor() { return idDoctor; }
    public String getIdPaciente() { return idPaciente; }
    public String getMotivo() { return motivo; }
}
