package centros_universitarios;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeMap;


public class Alumno extends Persona {

	/* ATRIBUTOS */
	private GregorianCalendar fechaIngreso;
	private TreeMap<Integer, Grupo> docenciaRecibida;//docencia recibida
	private TreeMap <Integer, NotaFinal> asignaturasAprobadas;//asignaturas aprobadas
	private String[] arrayAsignaturasSuperadas;
	private TreeMap<Integer, Asignatura> asignaturasMatriculadas;
	private String[] arrayDocenciaRecibida;
	
	/* METODOS */
	@Override
	public String toString(){ //Metodo toString sobreescrito reciclando el metodo de la clase padre.
		return super.toString() + " " + fechaIngreso.get(Calendar.DATE) + "/" + fechaIngreso.get(Calendar.MONTH)+ "/" + fechaIngreso.get(Calendar.YEAR);
	}


	/* GETTERS & SETTERS */
	public GregorianCalendar getFechaIngreso(){
		return this.fechaIngreso;
	}
	public void setFechaIngreso(GregorianCalendar fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public TreeMap<Integer, Grupo> getDocenciaRecibida() {
		return docenciaRecibida;
	}
	public void setDocenciaRecibida(TreeMap<Integer, Grupo> docenciaRecibida) {
		this.docenciaRecibida = docenciaRecibida;
	}

	public TreeMap<Integer, NotaFinal> getAsignaturasAprobadas() {
		return asignaturasAprobadas;
	}
	public void setAsignaturasAprobadas(TreeMap<Integer, NotaFinal> asignaturasAprobadas) {
		this.asignaturasAprobadas = asignaturasAprobadas;
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


	/* CONSTRUCTORES */
	public Alumno (String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento, GregorianCalendar fechaIngreso,TreeMap<Integer, Grupo> docenciaRecibida, TreeMap <Integer, NotaFinal> asignaturasAprobadas, String[] arrayAsignaturasSuperadas, TreeMap <Integer, Asignatura> asignaturasMatriculadas, String[] arrayDocenciaRecibida){ //Constructor.
		super(dni, nombre, apellidos, fechaNacimiento); //Llamada al metodo constructor de la clase padre.
		this.fechaIngreso = fechaIngreso;
		this.docenciaRecibida=docenciaRecibida;
		this.asignaturasAprobadas=asignaturasAprobadas;
		this.arrayAsignaturasSuperadas=arrayAsignaturasSuperadas;
		this.asignaturasMatriculadas= asignaturasMatriculadas;
		this.arrayDocenciaRecibida=arrayDocenciaRecibida;
	}



}