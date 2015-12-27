package centros_universitarios;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;



public class Funcionalidades { // Esta clase contendra las funcionalidades que aparecen explicados en las especificaciones del proyecto y el control de errores.

	
	/*
	 * FUNCIONALIDADES INCLUIDAS EN LAS ESPECIFICACIONES DEL PROYECTO:
	 * Insertar persona
	 * Asignar coordinador
	 * Asignar carga docente
	 * Matricular alumno
	 * Asignar grupo
	 * Evaluar asignatura
	 * Obtener expediente del alumno
	 * Obtener calendario del profesor 
	 */

	
	/* METODOS */
	//===== Funcionalidades =====
	public void insertarPersona(){
		
	}
	
	public void asignarCoordinador(String linea, TreeMap<String, Profesor> profesores , TreeMap<Integer, Asignatura> asignaturas){ //Formato: AsignaCoordinador persona asignatura
		String[] campos = linea.split(" ");
		String persona = campos[1];
		String asignatura = campos [2];
		
		if(!(existeProfesor(profesores, persona))){
			guardarError("ACOORD", "Profesor inexistente");
			return;
		}
		if(!(profesorTitular(profesores, persona))){
			guardarError("ACOORD", "Profesor no titular");
			return;
		}
		if(!(existeAsignatura(asignaturas, asignatura))){
		guardarError("ACOORD", "Asignatura inexistente");
		return;
		}
		if(coordinadorDosMaterias(profesores, persona)){
			guardarError("ACOORD", "Profesor ya es coordinador de 2 materias");
			return;
		}
		Set <Integer> setAsignaturas =asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		while(it.hasNext()){
			Integer idAsignatura= it.next();
			if(asignaturas.get(idAsignatura).getSiglas().contentEquals(asignatura)){
				asignaturas.get(idAsignatura).setCoordinador(profesores.get(persona));
				profesores.get(persona).getAsignaturasCoordinadas().put(asignaturas.get(idAsignatura).getIdAsignatura(), asignaturas.get(idAsignatura));
			}
		}
		return;
	}
	
	public void asignarCargaDocente(){
		
	}
	
	public void matricularAlumno(String linea, TreeMap<String, Alumno> alumnos , TreeMap<Integer, Asignatura> asignaturas){
	
		String[] campos = linea.split(" ");
		String alumno = campos[1];
		String asignatura = campos [2];
		
	if(!(existeAlumno(alumnos, alumno))){
		guardarError("MAT", "Alumno inexistente");
		return;
	}
	if(!(existeAsignatura(asignaturas, asignatura))){
		guardarError("MAT", "Asignatura inexistente");
		return;
		}
		if(matriculaExistente(alumnos, asignaturas, alumno, asignatura)){
			guardarError("MAT", "Ya es alumno de la asignatura indicada");
			return;
		}
	if(!(cumplePrerrequisitos(alumnos, asignaturas, alumno, asignatura))){
		guardarError("MAT", "No cumple requisitos");
		return;
	}
	Set <Integer> setAsignaturas = asignaturas.keySet();
	Iterator <Integer> it =setAsignaturas.iterator();
	Integer key=0;
	while (it.hasNext()){
		key=it.next();
		Asignatura asignaturaId = asignaturas.get(key);
		if (asignaturaId.getSiglas().contentEquals(asignatura)){
			key=asignaturaId.getIdAsignatura();
			break;
		}
	}
	alumnos.get(alumno).getAsignaturasMatriculadas().put(key, asignaturas.get(key));
	alumnos.get(alumno).getAsignaturasSinGrupo().put(key, asignaturas.get(key));

	}
	
	public void asignarGrupo(){
		
	
	}
	
	public void evaluarAsignatura(){
		
	}
	
	public void obtenerExpedienteAlumno(){
		
	}
	
	public void obtenerCalendarioProfesor(){
		
	}
	
	//===== Control de errores =====
	public void guardarError(String abreviatura, String texto){ //Metodo que permite la escritura en el fichero "avisos.txt".
	FileWriter fichero = null;
	PrintWriter pw = null;
	try{
		fichero = new FileWriter("avisos.txt", true);
		pw = new PrintWriter(fichero);		
		pw.println(abreviatura + " -- " + texto);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		try {
			if (null != fichero)
				fichero.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
	
	public void comandoIncorrecto(){
		guardarError("", "Comando incorrecto --");
	}

	public Boolean existeProfesor(TreeMap<String, Profesor> profesores, String dni){
		if(profesores.containsKey(dni))return true;
		else return false;
	}
	
	public Boolean profesorTitular(TreeMap<String, Profesor> profesores, String dni){
		if(profesores.get(dni).getCategoria().contains("titular")) return true;
		else return false;	
	}
	
	public Boolean existeAsignatura(TreeMap<Integer, Asignatura> asignaturas, String siglas){
		Set <Integer> setAsignaturas =asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		Boolean flag=false;
		while(it.hasNext()) if(asignaturas.get(it.next()).getSiglas().contentEquals(siglas))flag=true;
			if(flag)return true;
			else return false;
	}
	
	public Boolean coordinadorDosMaterias(TreeMap<String, Profesor> profesores, String dni){
		if(profesores.get(dni).getAsignaturasCoordinadas().size()==2)return true;
		else return false;
	}
	
	public Boolean existeAlumno(TreeMap<String, Alumno> alumnos, String dni){
		if(alumnos.containsKey(dni))return true;
		else return false;
	}
	
	public Boolean matriculaExistente(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String alumno, String asignatura){
		Set<Integer> setAsignaturasMatriculadas= alumnos.get(alumno).getAsignaturasMatriculadas().keySet();
		Iterator<Integer> it=setAsignaturasMatriculadas.iterator();
		while(it.hasNext()){
			Asignatura asignaturaMatriculada = asignaturas.get(it.next());
			if(asignaturaMatriculada.getSiglas().contentEquals(asignatura))return true;	
		}
		return false;
	}
	
	public Boolean cumplePrerrequisitos(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String alumno, String asignatura){
		Set <Integer> setAsignaturas = asignaturas.keySet();
		Iterator <Integer> it =setAsignaturas.iterator();
		Integer key=0;
		while (it.hasNext()){
			key=it.next();
			Asignatura asignaturaId = asignaturas.get(key);
			if (asignaturaId.getSiglas().contentEquals(asignatura)){
				key=asignaturaId.getIdAsignatura();
				break;
			}
		}
		Set<Integer> setPrerrequisitos=asignaturas.get(key).getPrerrequisitos().keySet();//Asignatura
		Iterator<Integer> it1=setPrerrequisitos.iterator();
		Asignatura asignaturaPrerrequisito, asignaturaSuperada;
		Boolean flag=false;
		while(it1.hasNext()){
			 asignaturaPrerrequisito = asignaturas.get(it1.next());
			 Set<Integer> setAsignaturasSuperadas = alumnos.get(alumno).getAsignaturasSuperadas().keySet();
				Iterator<Integer> it0= setAsignaturasSuperadas.iterator(); 
			 while(it0.hasNext()){				
				asignaturaSuperada = asignaturas.get(it0.next());
				if(asignaturaPrerrequisito.getIdAsignatura()==asignaturaSuperada.getIdAsignatura()) flag=true;
				if(!(it0.hasNext()) && !(flag))return false;
			}
		}
	return true;
	}
	
	
	/* CONSTRUCTORES */
	public Funcionalidades() {
	}
}

/*		// PRUEBAS
FileInputStream flujo_entrada = null;
try {
	flujo_entrada = new FileInputStream("ejecucion.txt");
} catch (FileNotFoundException NoExisteFichero) {
	System.out.println("Fichero \"ejecucion.txt\" inexistente");
	System.exit(-1);
}
Scanner entrada = new Scanner(flujo_entrada);
String linea = null;
linea = entrada.nextLine();
String[] lineaDesplegada = linea.split(" ");
if (lineaDesplegada[0].compareTo("InsertaPersona") == 0) {
	if (lineaDesplegada[1].compareTo("alumno") == 0) {
		String dni = lineaDesplegada[2];
		GregorianCalendar fechaNacimiento = stringToCalendar(linea.substring(linea.indexOf("/") - 2, linea.indexOf("/") + 8));
		if (validarFecha(fechaNacimiento))
			System.out.println("ERROR 1");
		GregorianCalendar fechaIngreso = stringToCalendar(linea.substring(linea.indexOf("/") + 9, linea.indexOf("/") + 19));
		if (validarFecha(fechaIngreso))
			System.out.println("ERROR 2");
		if (validarEdad(fechaNacimiento, fechaIngreso))
			System.out.println("ERROR 3");
	}
}


public static GregorianCalendar stringToCalendar(String fechaEntrada) {
String[] aux = fechaEntrada.split("/");
GregorianCalendar fecha = new GregorianCalendar(Integer.parseInt(aux[2]), Integer.parseInt(aux[1]), Integer.parseInt(aux[0]));
return fecha;
}

public static boolean validarFecha(GregorianCalendar fecha) {
Calendar fechaMinima = new GregorianCalendar(1950, 1, 1);
Calendar fechaMaxima = new GregorianCalendar(2020, 1, 1);
fecha.setLenient(false);
try {
	fecha.getTime();
} catch (IllegalArgumentException excepcion) {
	System.out.println("ERROR: fecha introducida incorrecta");
	return true;
}
if (fecha.before(fechaMinima) || fecha.after(fechaMaxima))
	return true;
else
	return false;
}

public static boolean validarEdad(GregorianCalendar fechaNacimiento, GregorianCalendar fechaInscripcion) {
int anho1 = fechaNacimiento.get(GregorianCalendar.YEAR);
int anho2 = fechaInscripcion.get(GregorianCalendar.YEAR);
int n_years = 0;
while (anho1 < anho2) {
	n_years++;
	anho1++;
}
if (n_years < 15 || n_years > 65)
	return true;
else
	return false;
}
*/