package centros_universitarios;

import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Persona {

	/* ATRIBUTOS */
	private String dni;
	private String nombre;
	private String apellidos;
	private GregorianCalendar fechaNacimiento;



	/* METODOS */
	@Override
	public String toString(){
		String mensaje = this.dni + " " + this.nombre + " " + this.apellidos + " " + this.fechaNacimiento.get(Calendar.DATE) + "/" + this.fechaNacimiento.get(Calendar.MONTH)+ "/" + this.fechaNacimiento.get(Calendar.YEAR);
		return mensaje;
	}


	/*GETTERS & SETTERS */
	public String getDni() {
		return this.dni;
	}
	public void setDni(String dNi){
		this.dni = dNi;
	}

	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nOmbre) {
		this.nombre = nOmbre;
	}

	public String getApellidos() {
		return this.dni;
	}
	public void setApellidos(String aPellidos){
		this.apellidos = aPellidos;
	}

	public GregorianCalendar getfechaNacimiento(){
		return this.fechaNacimiento;
	}
	public void setFechaNacimiento (GregorianCalendar fEchaNacimiento){
		this.fechaNacimiento = fEchaNacimiento;
	}


	/*CONSTRUCTORES*/
	public Persona(String dNi, String nOmbre, String aPellidos, GregorianCalendar fEchaNacimiento) {
		this.dni = dNi;
		this.nombre = nOmbre;
		this.apellidos = aPellidos;
		this.fechaNacimiento = fEchaNacimiento;
	}

}