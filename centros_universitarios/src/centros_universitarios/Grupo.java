package centros_universitarios;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Grupo {
 /* ATRIBUTOS */
	private char tipoGrupo;
	private Integer idGrupo;
	private char dia;
	private GregorianCalendar horaInicio;
	private GregorianCalendar horaFin;
	private Asignatura asignatura;//asignatura
	
	/* METODOS */
	public String toString() {
		return tipoGrupo + " " + idGrupo + " " + dia + " " + horaInicio.get(Calendar.HOUR) + ":" + horaInicio.get(Calendar.MINUTE)  + "-" + horaFin.get(Calendar.HOUR) + ":" + horaFin.get(Calendar.MINUTE);
	}
	
	
	/* GETTERS & SETTERS */
	public char getTipoGrupo() {
		return tipoGrupo;
	}
	public void setTipoGrupo(char tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}
	
	public Integer getIdGrupo() {
		return idGrupo;
	}
	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	public char getDia() {
		return dia;
	}
	public void setDia(char dia) {
		this.dia = dia;
	}
	
	public GregorianCalendar getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(GregorianCalendar horaInicio) {
		this.horaInicio = horaInicio;
	}
	
	public GregorianCalendar getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(GregorianCalendar horaFin) {
		this.horaFin = horaFin;
	}

	
	public Asignatura getAsignatura() {
		return asignatura;
	}
	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}


	/* CONSTRUCTORES */
	public Grupo(char tipoGrupo, Integer idGrupo, char dia, GregorianCalendar horaInicio, GregorianCalendar horaFin, Asignatura asignatura) {
		super();
		this.tipoGrupo = tipoGrupo;
		this.idGrupo = idGrupo;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.asignatura = asignatura;
	}
	
	
	
}
