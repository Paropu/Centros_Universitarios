package centros_universitarios;

public class NotaFinal  {

	/* ATRIBUTOS */
	private Integer idAsignatura;
	private String cursoAcademico; 
	private Float nota;
	private Asignatura asignatura;


	/*METODOS */

	
	/* GETTERS & SETTERS */
	public String getCursoAcademico() {
		return cursoAcademico;
	}
	public void setCursoAcademico(String cursoAcademico) {
		this.cursoAcademico = cursoAcademico;
	}

	public Float getNota() {
		return nota;
	}
	public void setNota(Float nota) {
		this.nota = nota;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}
	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}
	
	
	public Integer getIdAsignatura() {
		return idAsignatura;
	}
	public void setIdAsignatura(Integer idAsignatura) {
		this.idAsignatura = idAsignatura;
	}
	
	
	/* CONSTRUCTORES */
	public NotaFinal(Integer idAsignatura, String cursoAcademico, Float nota, Asignatura asignatura) {
		this.idAsignatura= idAsignatura;
		this.cursoAcademico=cursoAcademico;
		this.nota=nota;
		this.asignatura = asignatura;
	}



}
