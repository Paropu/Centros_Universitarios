package centros_universitarios;

import java.util.GregorianCalendar;
import java.util.TreeMap;

public class Profesor extends Persona {

	/* ATRIBUTOS */
	private String categoria;
	private String departamento;
	private Integer horasDocenciaAsignables;
	private TreeMap<Integer, Grupo> docenciaImpartida;//docencia impartida
	private TreeMap<Integer, Asignatura> asignaturasCoordinadas;//asignaturas coordinadas


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

	public TreeMap<Integer, Grupo> getDocenciaImpartida() {
		return docenciaImpartida;
	}
public void setDocenciaImpartida(TreeMap<Integer, Grupo> docenciaImpartida) {
		this.docenciaImpartida = docenciaImpartida;
	}

	public TreeMap<Integer, Asignatura> getAsignaturasCoordinadas() {
		return asignaturasCoordinadas;
	}
public void setAsignaturasCoordinadas(TreeMap<Integer, Asignatura> asignaturasCoordinadas) {
		this.asignaturasCoordinadas = asignaturasCoordinadas;
	}


	/* CONSTRUCTORES */
public Profesor(){
}

	public Profesor(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, String categoría, String departamento, Integer horasDocenciaAsignables, TreeMap<Integer, Grupo> docenciaImpartida, TreeMap<Integer, Asignatura> asignaturasCoordinadas) { //Constructor.
		super(dni, nombre, apellidos, fechaNacimiento);
		this.setCategoria(categoría);
		this.departamento = departamento;
		this.horasDocenciaAsignables = horasDocenciaAsignables;
		this.docenciaImpartida= docenciaImpartida;
		this.asignaturasCoordinadas=asignaturasCoordinadas;
	}


}