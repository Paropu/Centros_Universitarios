package centros_universitarios;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Clase padre con los atributos, metodos y constructores para trabajar con los elementos comunes de alumnos y profesores.
 * @see Alumno clase hija
 * @see Profesor clase hija
 * @author Pablo Rodriguez Perez, Martin Puga Egea.
 */

public abstract class Persona {

	/* ATRIBUTOS */
	private String dni;
	private String nombre;
	private String apellidos;
	private GregorianCalendar fechaNacimiento;

	/* METODOS */
	@Override
	public String toString() {
		return this.dni + " " + this.nombre + " " + this.apellidos + " " + this.fechaNacimiento.get(Calendar.DATE) + "/"
				+ this.fechaNacimiento.get(Calendar.MONTH) + "/" + this.fechaNacimiento.get(Calendar.YEAR);
	}

	/* GETTERS & SETTERS */
	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public GregorianCalendar getfechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(GregorianCalendar fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/* CONSTRUCTORES */

	public Persona() {
	}

	public Persona(String dni, String nombre, String apellidos, GregorianCalendar fechaNacimiento) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
	}

}