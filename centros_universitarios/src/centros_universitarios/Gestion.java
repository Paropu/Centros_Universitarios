package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.TreeMap;

public class Gestion {
	public static void main (String args[]){

		Funcionalidades funcionalidad = new Funcionalidades(); //Composicion de Funcionalidades.
		ComprobacionErrores comprobacion = new ComprobacionErrores(); //Composicion de ComprobacionErrores.

		
		TreeMap <String, Profesor> profesores = new TreeMap <String, Profesor> ();
		TreeMap <String, Alumno> alumnos = new TreeMap<String, Alumno> ();
		TreeMap <Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>();
		//cargarFicheroPersonas(); //Se carga el fichero personas.txt.
		profesores=cargarProfesores();
		alumnos=cargarAlumnos();
		asignaturas=cargarAsignaturas();
		//cargarPrerrequisitos.
		//cargarAsignaturasSuperadas.
		//cargarDocenciaRecibida.
		//cargarDocenciaImpartida.
		}

	public static TreeMap<String, Profesor> cargarProfesores(){
		
		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} 
		catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			System.out.println("Entra en el while");
			linea=entrada.nextLine();
			if(linea.contains("profesor")){ //Se recogen los datos del profesor.
				System.out.println("Es un profesor");//------------------
				String dni= entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar (Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
				String categoria= entrada.nextLine();
				String departamento= entrada.nextLine();
				Integer horasDocenciaAsignables = Integer.parseInt(entrada.nextLine());
				linea=entrada.nextLine();//OMISION de carga de docencia impartida por el profesor.
				TreeMap<Integer, Grupo> docenciaImpartida = new TreeMap<Integer, Grupo> (); //VACIO
				TreeMap<Integer, Asignatura> asignaturasCoordinadas = new TreeMap<Integer, Asignatura> (); //VACIO. En principio vacio, luego se completa al cargar las asignaturas.
				Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento,categoria, departamento, horasDocenciaAsignables, docenciaImpartida, asignaturasCoordinadas);
				profesores.put(dni, profesor);	
			}
			else { //Se salta el bloque del alumno.
				int i;
				for(i=0; i<7 ; i++) linea=entrada.nextLine();
			}
			if(profesores.containsKey("12345678A")) System.out.println("key");
			if(entrada.hasNextLine())
				linea=entrada.nextLine(); //Se recoge el "*" de separacion.
		}
		entrada.close();
		
		return profesores;
	}
	
	
	
	public static TreeMap <String, Alumno> cargarAlumnos(){
		
		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} 
		catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			System.out.println("Entra en el while");
			linea=entrada.nextLine();
			if(linea.contains("alumno")){ //Se recogenlos datos del alumno.
				System.out.println("Es un alumno"); //-------------
				String dni= entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar (Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
				linea = entrada.nextLine();
				fecha = linea.split("/");
				GregorianCalendar fechaIngreso = new GregorianCalendar (Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
				linea=entrada.nextLine();//OMISION de asignaturas superadas del alumno.
				TreeMap<Integer, NotaFinal> asignaturasAprobadas = new TreeMap<Integer, NotaFinal>(); 
				linea=entrada.nextLine();//OMISION de la docencia recibida del alumno.
				TreeMap<Integer, Grupo> docenciaRecibida = new TreeMap<Integer, Grupo>();
				Alumno alumno = new Alumno (dni, nombre,  apellidos, fechaNacimiento,  fechaIngreso, docenciaRecibida,  asignaturasAprobadas);
				alumnos.put(dni, alumno);
			}
			else { //Se salta el bloque del profesor.
				int i;
				for(i=0; i<8 ; i++) linea=entrada.nextLine();
			}
			if(entrada.hasNextLine())
				linea=entrada.nextLine(); //Se recoge el "*" de separacion.
		}
		entrada.close();
		
		return alumnos;
	}
	
	
	
	public static TreeMap<Integer, Asignatura> cargarAsignaturas(){
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("asignaturas.txt"); // Se crea un flujo de datos al fichero.
			System.out.println("Fichero asignaturas");//---------------------
		} 
		catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"asignaturas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			System.out.println("Entra en el while");
		//	linea=entrada.nextLine();
			Integer idAsignatura = Integer.parseInt(entrada.nextLine());
			String nombre = entrada.nextLine();
			String siglas = entrada.nextLine();
			Integer curso = Integer.parseInt(entrada.nextLine());
			linea=entrada.nextLine();//OMISION de carga del coordinador de la asignatura.
			Profesor coordinador;
			linea=entrada.nextLine();//OMISION de carga de los prerrequisitos de la asignatura.
			TreeMap<Integer, Asignatura> prerrequisitos = new TreeMap<Integer, Asignatura>();
			
			TreeMap<Integer, Grupo> gruposA = new TreeMap<Integer, Grupo>(); //CARGAR grupos. ---------------------------------------------------------------------------------
			linea=entrada.nextLine();
			
			
			
			
			TreeMap<Integer, Grupo> gruposB = new TreeMap<Integer, Grupo>(); //CARGAR grupos.
			linea=entrada.nextLine();
			if(entrada.hasNextLine())
				linea=entrada.nextLine(); //Se recoge el "*" de separacion.
			System.out.println(linea);//-------------
		}
		entrada.close();
		
		
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>();
		return asignaturas;
	}
}


/*public static void cargarFicheroPersonas () {
 * 
FileInputStream flujo_entrada = null;
try {
	flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
} 
catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
	System.out.println("Fichero \"personas.txt\" inexistente");
	System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
}
Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
String linea = null; // Variable que contendra la informacion escaneada del fichero
while (entrada.hasNextLine()) {
	System.out.println("Entra en el while");
	linea=entrada.nextLine();
	if(linea.contains("profesor")){ //Recogemos los datos del profesor.
		System.out.println("Es un profesor");//------------------
		String dni= entrada.nextLine();
		String nombre = entrada.nextLine();
		String apellidos = entrada.nextLine();
		linea = entrada.nextLine();
		String[] fecha = linea.split("/");
		GregorianCalendar fechaNacimiento = new GregorianCalendar (Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
		String categoria= entrada.nextLine();
		String departamento= entrada.nextLine();
		Integer horasDocenciaAsignables = Integer.parseInt(entrada.nextLine());
		linea=entrada.nextLine();//CAMBIAR. Recoger docencia impartida del profesor.
		TreeMap<Integer, Grupo> docenciaImpartida = new TreeMap<Integer, Grupo> (); //Recoger docencia impartida del profesor.
		TreeMap<Integer, Asignatura> asignaturasCoordinadas = new TreeMap<Integer, Asignatura> (); //VACIO. No se carga del fichero personas, se carga del fichero asignaturas
		Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento,categoria, departamento, horasDocenciaAsignables, docenciaImpartida, asignaturasCoordinadas);
	}
	else { //Recogemos los datos del alumno.
		System.out.println("Es un alumno"); //-------------
		String dni= entrada.nextLine();
		String nombre = entrada.nextLine();
		String apellidos = entrada.nextLine();
		linea = entrada.nextLine();
		String[] fecha = linea.split("/");
		GregorianCalendar fechaNacimiento = new GregorianCalendar (Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
		linea = entrada.nextLine();
		fecha = linea.split("/");
		GregorianCalendar fechaIngreso = new GregorianCalendar (Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
		linea=entrada.nextLine();//CAMBIAR. Recoger asignaturas superadas.
		TreeMap<Integer, NotaFinal> asignaturasAprobadas = new TreeMap<Integer, NotaFinal>(); //Recoger asignaturas superadas.
		linea=entrada.nextLine();//CAMBIAR. Recoger docencia recibida.
		TreeMap<Integer, Grupo> docenciaRecibida = new TreeMap<Integer, Grupo>();//Recoger docencia recibida;
		Alumno alumno = new Alumno (dni, nombre,  apellidos, fechaNacimiento,  fechaIngreso, docenciaRecibida,  asignaturasAprobadas);
	}
	if(entrada.hasNextLine())
		linea=entrada.nextLine(); //Se recoge el "*" de separacion.
	System.out.println(linea);//-------------
}
entrada.close();
}	*/


/*System.out.println(funcionalidad.start()); //Herramienta

Alumno alumno0 = new Alumno("44497152H", "Martin", "Puga Egea", new GregorianCalendar(1995, 01, 24), new GregorianCalendar(2000, 11, 20)); //Herramienta.
Profesor profesor0 = new Profesor("34925064R", "Gisela", "Egea Rodriguez", new GregorianCalendar(1957, 8, 23), "Titular", "Electrónica", 24);
Asignatura asignatura0 = new Asignatura(5, "Cálculo", "CALI", 1);
GregorianCalendar horaGrupo0 = new GregorianCalendar();
horaGrupo0.set(Calendar.HOUR, 11);
horaGrupo0.set(Calendar.MINUTE, 45);
GregorianCalendar horaGrupo00 = new GregorianCalendar();
horaGrupo00.set(Calendar.HOUR, 13);
horaGrupo00.set(Calendar.MINUTE, 50);

Grupo grupo0 = new Grupo ('A',15,'L', horaGrupo0, horaGrupo00);
System.out.println(alumno0.toString()); //Herramienta.
System.out.println(profesor0.toString()); //Herramienta.
System.out.println(asignatura0.toString()); //Herramienta.
System.out.println(grupo0.toString()); //Herramienta.

System.out.println(comprobacion.end()); //Herramienta.
 */
