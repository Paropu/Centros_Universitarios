package centros_universitarios;

import java.util.GregorianCalendar;
import java.util.TreeMap;

/**
 * Clase con los atributos, metodos y constructores para trabajar con los profesores.
 * @see Persona clase padre
 * @author Pablo Rodriguez Perez, Martin Puga Egea
 */

public class Profesor extends Persona {

	/* ATRIBUTOS */
	private String categoria;
	private String departamento;
	private Integer horasDocenciaAsignables;
	private TreeMap<Grupo, Grupo> docenciaImpartidaA;
	private TreeMap<Grupo, Grupo> docenciaImpartidaB;
	private TreeMap<Integer, Asignatura> asignaturasCoordinadas;
	private String[] arrayDocenciaImpartida;

	/* METODOS */
	@Override
	public String toString() {
		return super.toString() + " " + categoria + " " + departamento + " " + horasDocenciaAsignables;
	}

	/* GETTERS & SETTERS */
	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public int getHorasDocenciaAsignables() {
		return horasDocenciaAsignables;
	}

	public void setHorasDocenciaAsignables(int horasDocenciaAsignables) {
		this.horasDocenciaAsignables = horasDocenciaAsignables;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public TreeMap<Grupo, Grupo> getDocenciaImpartidaA() {
		return docenciaImpartidaA;
	}

	public void setDocenciaImpartidaA(TreeMap<Grupo, Grupo> docenciaImpartidaA) {
		this.docenciaImpartidaA = docenciaImpartidaA;
	}

	public TreeMap<Integer, Asignatura> getAsignaturasCoordinadas() {
		return asignaturasCoordinadas;
	}

	public void setAsignaturasCoordinadas(TreeMap<Integer, Asignatura> asignaturasCoordinadas) {
		this.asignaturasCoordinadas = asignaturasCoordinadas;
	}

	public String[] getArrayDocenciaImpartida() {
		return arrayDocenciaImpartida;
	}

	public void setArrayDocenciaImpartida(String[] arrayDocenciaImpartida) {
		this.arrayDocenciaImpartida = arrayDocenciaImpartida;
	}

	public TreeMap<Grupo, Grupo> getDocenciaImpartidaB() {
		return docenciaImpartidaB;
	}

	public void setDocenciaImpartidaB(TreeMap<Grupo, Grupo> docenciaImpartidaB) {
		this.docenciaImpartidaB = docenciaImpartidaB;
	}

	public void setHorasDocenciaAsignables(Integer horasDocenciaAsignables) {
		this.horasDocenciaAsignables = horasDocenciaAsignables;
	}

	/* CONSTRUCTORES */

	public Profesor() {
	}

	/**
	 * Constructor para funcion cargarAlumno();
	 * @param dni
	 * @param nombre
	 * @param apellidos
	 * @param fechaNacimiento
	 * @param categoria
	 * @param departamento
	 * @param horasDocenciaAsignables
	 * @param docenciaImpartidaA
	 * @param docenciaImpartidaB
	 * @param asignaturasCoordinadas
	 * @param arrayDocenciaImpartida
	 */

	public Profesor(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, String categoria, String departamento,
			Integer horasDocenciaAsignables, TreeMap<Grupo, Grupo> docenciaImpartidaA, TreeMap<Grupo, Grupo> docenciaImpartidaB,
			TreeMap<Integer, Asignatura> asignaturasCoordinadas, String[] arrayDocenciaImpartida) { // Constructor.
		super(dni, nombre, apellidos, fechaNacimiento);
		this.setCategoria(categoria);
		this.departamento = departamento;
		this.horasDocenciaAsignables = horasDocenciaAsignables;
		this.docenciaImpartidaA = docenciaImpartidaA;
		this.docenciaImpartidaB = docenciaImpartidaB;
		this.asignaturasCoordinadas = asignaturasCoordinadas;
		this.arrayDocenciaImpartida = arrayDocenciaImpartida;
	}

	/**
	 * Constructor para la funcion insertarPersona();
	 * @param dni
	 * @param nombre
	 * @param apellidos
	 * @param fechaNacimiento
	 * @param categoria
	 * @param departamento
	 * @param horasDocenciaAsignables
	 */

	public Profesor(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, String categoria, String departamento,
			Integer horasDocenciaAsignables) {
		super(dni, nombre, apellidos, fechaNacimiento);
		this.setCategoria(categoria);
		this.departamento = departamento;
		this.horasDocenciaAsignables = horasDocenciaAsignables;
		this.docenciaImpartidaA = new TreeMap<Grupo, Grupo>();
		this.docenciaImpartidaB = new TreeMap<Grupo, Grupo>();
		this.asignaturasCoordinadas = new TreeMap<Integer, Asignatura>(); // this.arrayDocenciaImpartida=null;
	}

}