package centros_universitarios;

import java.util.TreeMap;

/**
 * Clase con los atributos, metodos y constructores para trabajar con las asignaturas.
 * @author Pablo Rodriguez Perez, Martin Puga Egea.
 */

public class Asignatura {

	/* ATRIBUTOS */
	private Integer idAsignatura;
	private String nombre;
	private String siglas;
	private Integer curso;
	private Profesor coordinador;
	private TreeMap<Integer, Asignatura> prerrequisitos;
	private TreeMap<Integer, Grupo> gruposA;
	private TreeMap<Integer, Grupo> gruposB;
	private String[] arrayPrerrequisitos;

	/* METODOS */
	@Override
	public String toString() {
		return idAsignatura + " " + nombre + " " + siglas + " " + curso;
	}

	/* GETTERS & SETTERS */
	public Integer getIdAsignatura() {
		return idAsignatura;
	}

	public void setIdAsignatura(Integer idAsignatura) {
		this.idAsignatura = idAsignatura;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSiglas() {
		return siglas;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

	public Integer getCurso() {
		return curso;
	}

	public void setCurso(Integer curso) {
		this.curso = curso;
	}

	public Profesor getCoordinador() {
		return coordinador;
	}

	public void setCoordinador(Profesor coordinador) {
		this.coordinador = coordinador;
	}

	public TreeMap<Integer, Asignatura> getPrerrequisitos() {
		return prerrequisitos;
	}

	public void setPrerrequisitos(TreeMap<Integer, Asignatura> prerrequisitos) {
		this.prerrequisitos = prerrequisitos;
	}

	public TreeMap<Integer, Grupo> getGruposA() {
		return gruposA;
	}

	public void setGruposA(TreeMap<Integer, Grupo> gruposA) {
		this.gruposA = gruposA;
	}

	public TreeMap<Integer, Grupo> getGruposB() {
		return gruposB;
	}

	public void setGruposB(TreeMap<Integer, Grupo> gruposB) {
		this.gruposB = gruposB;
	}

	public String[] getArrayPrerrequisitos() {
		return arrayPrerrequisitos;
	}

	public void setArrayPrerrequisitos(String[] arrayPrerrequisitos) {
		this.arrayPrerrequisitos = arrayPrerrequisitos;
	}

	/* CONSTRUCTORES */
	public Asignatura() {
	}

	public Asignatura(Integer idAsignatura, String nombre, String siglas, Integer curso, Profesor coordinador,
			TreeMap<Integer, Asignatura> prerrequisitos, TreeMap<Integer, Grupo> gruposA, TreeMap<Integer, Grupo> gruposB,
			String[] arrayPrerrequisitos) {
		super();
		this.idAsignatura = idAsignatura;
		this.nombre = nombre;
		this.siglas = siglas;
		this.curso = curso;
		this.coordinador = coordinador;
		this.prerrequisitos = prerrequisitos;
		this.gruposA = gruposA;
		this.gruposB = gruposB;
		this.arrayPrerrequisitos = arrayPrerrequisitos;
	}
}
