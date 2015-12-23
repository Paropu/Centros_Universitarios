package centros_universitarios;

import java.util.TreeMap;

public class NotaFinal extends Asignatura {

	/* ATRIBUTOS */
	private Integer cursoAcademico; //Gregorian calendar o String ???
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
	public NotaFinal(Integer idAsignatura, String nombre, String siglas, Integer curso,Profesor coordinador, TreeMap<Integer, Asignatura> prerrequisitos, TreeMap<Integer, Grupo> gruposA, TreeMap<Integer, Grupo> gruposB,String[] arrayPrerrequisitos, Integer cursoAcademico, Integer nota) {
		super(idAsignatura, nombre, siglas, curso, coordinador, prerrequisitos, gruposA, gruposB, arrayPrerrequisitos);
		this.cursoAcademico=cursoAcademico;
		this.nota=nota;
	}



}
