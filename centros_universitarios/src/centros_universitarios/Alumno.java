package centros_universitarios;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;

/**
 * Clase con los atributos, metodos y constructores para trabajar con los alumnos.
 * @see Persona clase padre
 * @author Pablo Rodriguez Perez, Martin Puga Egea.
 */

public class Alumno extends Persona {

	/* ATRIBUTOS */
	private GregorianCalendar fechaIngreso;
	private TreeMap<Integer, Grupo> docenciaRecibidaA;
	private TreeMap<Integer, Grupo> docenciaRecibidaB;
	private TreeMap<Integer, NotaFinal> asignaturasSuperadas;
	private String[] arrayAsignaturasSuperadas;
	private TreeMap<Integer, Asignatura> asignaturasMatriculadas;
	private String[] arrayDocenciaRecibida;
	private TreeMap<Integer, Asignatura> asignaturasSinGrupo;
	private Float notaMedia;

	/* METODOS */
	@Override
	public String toString() {
		return super.toString() + " " + fechaIngreso.get(Calendar.DATE) + "/" + fechaIngreso.get(Calendar.MONTH) + "/"
				+ fechaIngreso.get(Calendar.YEAR);
	}

	/* GETTERS & SETTERS */
	public GregorianCalendar getFechaIngreso() {
		return this.fechaIngreso;
	}

	public void setFechaIngreso(GregorianCalendar fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public TreeMap<Integer, Grupo> getDocenciaRecibidaA() {
		return docenciaRecibidaA;
	}

	public void setDocenciaRecibidaA(TreeMap<Integer, Grupo> docenciaRecibidaA) {
		this.docenciaRecibidaA = docenciaRecibidaA;
	}

	public TreeMap<Integer, NotaFinal> getAsignaturasSuperadas() {
		return asignaturasSuperadas;
	}

	public void setAsignaturasSuperadas(TreeMap<Integer, NotaFinal> asignaturasSuperadas) {
		this.asignaturasSuperadas = asignaturasSuperadas;
	}

	public String[] getArrayAsignaturasSuperadas() {
		return arrayAsignaturasSuperadas;
	}

	public void setArrayAsignaturasSuperadas(String[] arrayAsignaturasSuperadas) {
		this.arrayAsignaturasSuperadas = arrayAsignaturasSuperadas;
	}

	public TreeMap<Integer, Asignatura> getAsignaturasMatriculadas() {
		return asignaturasMatriculadas;
	}

	public void setAsignaturasMatriculadas(TreeMap<Integer, Asignatura> asignaturasMatriculadas) {
		this.asignaturasMatriculadas = asignaturasMatriculadas;
	}

	public String[] getArrayDocenciaRecibida() {
		return arrayDocenciaRecibida;
	}

	public void setArrayDocenciaRecibida(String[] arrayDocenciaRecibida) {
		this.arrayDocenciaRecibida = arrayDocenciaRecibida;
	}

	public TreeMap<Integer, Grupo> getDocenciaRecibidaB() {
		return docenciaRecibidaB;
	}

	public void setDocenciaRecibidaB(TreeMap<Integer, Grupo> docenciaRecibidaB) {
		this.docenciaRecibidaB = docenciaRecibidaB;
	}

	public TreeMap<Integer, Asignatura> getAsignaturasSinGrupo() {
		return asignaturasSinGrupo;
	}

	public void setAsignaturasSinGrupo(TreeMap<Integer, Asignatura> asignaturasSinGrupo) {
		this.asignaturasSinGrupo = asignaturasSinGrupo;
	}

	public Float getNotaMedia() {
		return notaMedia;
	}

	public void setNotaMedia(Float notaMedia) {
		this.notaMedia = notaMedia;
	}

	/* CONSTRUCTORES */

	public Alumno() {
	}

	/**
	 * Constructor para funcion cargarAlumno();
	 * @param dni
	 * @param nombre
	 * @param apellidos
	 * @param fechaNacimiento
	 * @param fechaIngreso
	 * @param docenciaRecibidaA
	 * @param docenciaRecibidaB
	 * @param asignaturasSuperadas
	 * @param arrayAsignaturasSuperadas
	 * @param asignaturasMatriculadas
	 * @param arrayDocenciaRecibida
	 */

	public Alumno(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, GregorianCalendar fechaIngreso,
			TreeMap<Integer, Grupo> docenciaRecibidaA, TreeMap<Integer, Grupo> docenciaRecibidaB,
			TreeMap<Integer, NotaFinal> asignaturasSuperadas, String[] arrayAsignaturasSuperadas,
			TreeMap<Integer, Asignatura> asignaturasMatriculadas, String[] arrayDocenciaRecibida) { // Constructor.
		super(dni, nombre, apellidos, fechaNacimiento); // Llamada al metodo constructor de la clase padre.
		this.fechaIngreso = fechaIngreso;
		this.docenciaRecibidaA = docenciaRecibidaA;
		this.docenciaRecibidaB = docenciaRecibidaB;
		this.asignaturasSuperadas = asignaturasSuperadas;
		this.arrayAsignaturasSuperadas = arrayAsignaturasSuperadas;
		this.asignaturasMatriculadas = asignaturasMatriculadas;
		this.arrayDocenciaRecibida = arrayDocenciaRecibida;
	}

	/**
	 * Constructor para la funcion insertarPersona();
	 * @param dni
	 * @param nombre
	 * @param apellidos
	 * @param fechaNacimiento
	 * @param fechaIngreso
	 */

	public Alumno(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, GregorianCalendar fechaIngreso) {
		super(dni, nombre, apellidos, fechaNacimiento);
		this.fechaIngreso = fechaIngreso;
		this.docenciaRecibidaA = new TreeMap<Integer, Grupo>();
		this.docenciaRecibidaB = new TreeMap<Integer, Grupo>();
		this.asignaturasSuperadas = new TreeMap<Integer, NotaFinal>();
		this.asignaturasMatriculadas = new TreeMap<Integer, Asignatura>();
	}
}