package centros_universitarios;


public class NotaFinal extends Asignatura {

	/* ATRIBUTOS */
	private Integer cursoAcademico; //Gregorian calendar???
	private Integer nota;
	private Asignatura asignatura;


	/*METODOS */

	/* GETTERS & SETTERS */
	public Integer getCursoAcademico() {
		return cursoAcademico;
	}
	public void setCursoAcademico(Integer cursoAcademico) {
		this.cursoAcademico = cursoAcademico;
	}

	public Integer getNota() {
		return nota;
	}
	public void setNota(Integer nota) {
		this.nota = nota;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}
	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}
	
	/* CONSTRUCTORES */
	public NotaFinal(Integer idAsignatura, String nombre, String siglas, Integer curso, Integer cursoAcademico, Integer nota) {
		super(idAsignatura, nombre, siglas, curso);
		this.cursoAcademico=cursoAcademico;
		this.nota=nota;
	}



}
