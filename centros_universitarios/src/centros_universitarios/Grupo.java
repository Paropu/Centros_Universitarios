package centros_universitarios;

public class Grupo {
	/* ATRIBUTOS */
	private String tipoGrupo;
	private Integer idGrupo;
	private String dia;
	private Integer horaInicio;
	private Integer horaFin;
	private Asignatura asignatura;// asignatura

	/* METODOS */

	@Override
	public String toString() {
		return dia + ";\t\t" + horaInicio + ";\t\t" + asignatura.getSiglas() + ";\t" + tipoGrupo + ";\t\t\t" + idGrupo;
	}

	public String toString2() {
		return dia + ";\t\t" + horaInicio + ";\t\t" + asignatura.getSiglas() + ";\t\t" + tipoGrupo + ";\t\t\t" + idGrupo;
	}

	public String toString3() {
		return dia + ";\t\t" + horaInicio + ";\t\t" + asignatura.getSiglas() + ";\t\t\t" + tipoGrupo + ";\t\t\t" + idGrupo;
	}


	/* GETTERS & SETTERS */
	public String getTipoGrupo() {
		return tipoGrupo;
	}

	public void setTipoGrupo(String tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	public Integer getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public Integer getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Integer horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Integer getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Integer horaFin) {
		this.horaFin = horaFin;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	/* CONSTRUCTORES */
	public Grupo(String tipoGrupo, Integer idGrupo, String dia, Integer horaInicio, Integer horaFin, Asignatura asignatura) {
		super();
		this.tipoGrupo = tipoGrupo;
		this.idGrupo = idGrupo;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.asignatura = asignatura;
	}

}
