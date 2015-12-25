package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Gestion {

	/* MAIN */

	public static void main(String args[]) {

		Funcionalidades funcionalidad = new Funcionalidades(); // Composicion de Funcionalidades.
		ComprobacionErrores comprobacion = new ComprobacionErrores(); // Composicion de ComprobacionErrores.

		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>();
		// cargarFicheroPersonas(); //NOTA IMPORTANTE:No se hace en un metodo directamente, hay que hacerlo por partes para poder cargar todos los datos sin problemas. No se puede cargar toda la info de golpe, ya que se necesita relacionar las clases.
		profesores = cargarProfesores(); // Carga toda la informacioon de los profesores excepto la docencia impartida, que requiere que existan los grupos relacionados.
		alumnos = cargarAlumnos(); // Carga toda la informacion de los alumnos excepto las asignaturas aprobadas y la docencia recibida.
		asignaturas = cargarAsignaturas(profesores); // Carga toda la info de las asignaturas en dos fases. En la primera fase carga los datos básico y en la segunda, los prerrequisitos.
		cargarAsignaturasSuperadas(alumnos, asignaturas); // Actualiza la informacion de las asignaturas superadas de los alumnos.
		cargarDocenciaImpartida(profesores, asignaturas); // Actualiza la informacion de la docencia impartida por los profesores.
		cargarDocenciaRecibida(alumnos, asignaturas); // Actualiza la informacion de la docencia recibida por los alumnos.
	}

	/* METODOS */

	public static TreeMap<String, Profesor> cargarProfesores() {

		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (linea.contains("profesor")) { // Se recogen los datos del profesor.
				String dni = entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
						Integer.parseInt(fecha[0]));
				String categoria = entrada.nextLine();
				String departamento = entrada.nextLine();
				Integer horasDocenciaAsignables = Integer.parseInt(entrada.nextLine());
				String[] arrayDocenciaImpartida = entrada.nextLine().split("; ");// Carga de docencia impartida por el profesor.
				TreeMap<Integer, Grupo> docenciaImpartida = new TreeMap<Integer, Grupo>(); // VACIO
				TreeMap<Integer, Asignatura> asignaturasCoordinadas = new TreeMap<Integer, Asignatura>(); // VACIO. En principio vacio, luego se completa al cargar las asignaturas.
				Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento, categoria, departamento, horasDocenciaAsignables,
						docenciaImpartida, asignaturasCoordinadas, arrayDocenciaImpartida);
				profesores.put(dni, profesor);
			} else { // Se salta el bloque del alumno.
				int i;
				for (i = 0; i < 7; i++)
					linea = entrada.nextLine();
			}
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();
		return profesores;
	}

	public static TreeMap<String, Alumno> cargarAlumnos() {

		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (linea.contains("alumno")) { // Se recogen los datos del alumno.
				String dni = entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
						Integer.parseInt(fecha[0]));
				linea = entrada.nextLine();
				fecha = linea.split("/");
				GregorianCalendar fechaIngreso = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
						Integer.parseInt(fecha[0]));
				String[] arrayAsignaturasSuperadas = entrada.nextLine().split("; ");// OMISION de asignaturas superadas del alumno.
				TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>();
				String[] arrayDocenciaRecibida = entrada.nextLine().split("; ");// Carga de la docencia recibida por el alumno.
				TreeMap<Integer, Grupo> docenciaRecibida = new TreeMap<Integer, Grupo>();
				TreeMap<Integer, Asignatura> asignaturasMatriculadas = new TreeMap<Integer, Asignatura>();
				Alumno alumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, fechaIngreso, docenciaRecibida, asignaturasSuperadas,
						arrayAsignaturasSuperadas, asignaturasMatriculadas, arrayDocenciaRecibida);
				alumnos.put(dni, alumno);
			} else { // Se salta el bloque del profesor.
				int i;
				for (i = 0; i < 8; i++)
					linea = entrada.nextLine();
			}
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();
		return alumnos;
	}

	public static TreeMap<Integer, Asignatura> cargarAsignaturas(TreeMap<String, Profesor> profesores) {
		// PRIMERA FASE.
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>(); // TreeMap que contendrá las asignaturas
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("asignaturas.txt"); // Se crea un flujo de datos al fichero.
		} catch (FileNotFoundException NoExisteFichero) { // Si el fichero no existe, salta excepcion y se muestra mensaje por pantalla.
			System.out.println("Fichero \"asignaturas.txt\" inexistente");
			System.exit(-1); // Mostrar error en el fichero Avisos.txt ----- ???
		}
		Scanner entrada = new Scanner(flujo_entrada); // Se crea un objeto para escanear la linea del fichero
		String linea = null; // Variable que contendra la informacion escaneada del fichero
		while (entrada.hasNextLine()) {
			Integer idAsignatura = Integer.parseInt(entrada.nextLine());
			String nombre = entrada.nextLine();
			String siglas = entrada.nextLine();
			Integer curso = Integer.parseInt(entrada.nextLine());
			String dniCoordinador = entrada.nextLine();// Carga del coordinador de la asignatura.
			String caracterVacio = "";
			String[] arrayPrerrequisitos = entrada.nextLine().split(", ");
			TreeMap<Integer, Asignatura> prerrequisitos = new TreeMap<Integer, Asignatura>();
			Asignatura asignatura = new Asignatura(idAsignatura, nombre, siglas, curso, new Profesor(), prerrequisitos,
					new TreeMap<Integer, Grupo>(), new TreeMap<Integer, Grupo>(), arrayPrerrequisitos);// AÑADIR posteriormente los grupos A y B.
			if (dniCoordinador.compareTo(caracterVacio) != 0) {
				Profesor coordinador = profesores.get(dniCoordinador);
				asignatura.setCoordinador(coordinador);// Se le asigna un coordinador a la asignatura.
				profesores.get(dniCoordinador).getAsignaturasCoordinadas().put(idAsignatura, asignatura); // AÑADE asignatura coordinada al profesor.
			}
			TreeMap<Integer, Grupo> gruposA = new TreeMap<Integer, Grupo>(); // CARGAR gruposA
			linea = entrada.nextLine(); // Formato: ID_grupo dia horaini horafin
			String[] arrayGruposA = linea.split("; ");
			int i;
			for (i = 0; i < arrayGruposA.length; i++) {
				String[] grupo = arrayGruposA[i].split(" ");
				Integer idGrupo = Integer.parseInt(grupo[0]);
				String dia = grupo[1];
				Integer horaInicio = Integer.parseInt(grupo[2]);
				Integer horaFin = Integer.parseInt(grupo[3]);
				Grupo grupoA = new Grupo("A", idGrupo, dia, horaInicio, horaFin, asignatura);// AÑADIR asignatura posteriormente.
				gruposA.put(idGrupo, grupoA);// Se añade el grupo al Treemap de grupos A de la asignatura.
			}
			TreeMap<Integer, Grupo> gruposB = new TreeMap<Integer, Grupo>(); // CARGAR gruposB.
			linea = entrada.nextLine(); // Formato: ID_grupo dia horaini horafin
			String[] arrayGruposB = linea.split("; ");
			for (i = 0; i < arrayGruposB.length; i++) {
				String[] grupo = arrayGruposB[i].split(" ");
				Integer idGrupo = Integer.parseInt(grupo[0]);
				String dia = grupo[1];
				Integer horaInicio = Integer.parseInt(grupo[2]);
				Integer horaFin = Integer.parseInt(grupo[3]);
				Grupo grupoA = new Grupo("B", idGrupo, dia, horaInicio, horaFin, asignatura);// AÑADIR asignatura posteriormente.
				gruposB.put(idGrupo, grupoA);// Se añade el grupo al Treemap de grupos B de la asignatura.
			}
			asignatura.setGruposA(gruposA);
			asignatura.setGruposB(gruposB);
			asignaturas.put(asignatura.getIdAsignatura(), asignatura); // Se añade la asignatura al TreeMap de asignaturas.
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();

		// SEGUNDA FASE - ACTUALIZACION DE PRERREQUISITOS
		Set<Integer> setAsignaturas = asignaturas.keySet(); // CREACION de un Set con las claves de las asignaturas en el TreeMap asignaturas.
		Iterator<Integer> it = setAsignaturas.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Asignatura asignatura = asignaturas.get(it.next()); // Se recoge la asignatura del TreeMap mediante la key
			String caracterVacio = "";
			if (asignatura.getArrayPrerrequisitos()[0].compareTo(caracterVacio) != 0) { // Se cargan los prerrequisitos solo en caso de existir alguna dependencia.
				TreeMap<Integer, Asignatura> nuevosPrerrequisitos = new TreeMap<Integer, Asignatura>(); // Nuevo TreeMap donde se guardaran los nuevos prerrequisitos, para posteriormente añadirlos a la asignatura mediante un setPrerrequisitos().
				int i;
				for (i = 0; i < asignatura.getArrayPrerrequisitos().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					nuevosPrerrequisitos.put(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i]),
							asignaturas.get(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i])));
				}
				asignatura.setPrerrequisitos(nuevosPrerrequisitos);
			}
		}
		return asignaturas;
	}

	public static void cargarAsignaturasSuperadas(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		Set<String> setAlumnos = alumnos.keySet(); // CREACION de un Set con las claves de los alumnos en el TreeMap alumnos.
		Iterator<String> it = setAlumnos.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Alumno alumno = alumnos.get(it.next()); // Se recoge el alumno del TreeMap mediante la key
			String caracterVacio = "";
			if (alumno.getArrayAsignaturasSuperadas()[0].compareTo(caracterVacio) != 0) { // Se cargan los prerrequisitos solo en caso de existir alguna dependencia.
				TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>(); // Nuevo TreeMap donde se guardaran las asignaturas superadas, para posteriormente añadirlos al alumno mediante un set()..
				int i;
				for (i = 0; i < alumno.getArrayAsignaturasSuperadas().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					String[] campos = alumno.getArrayAsignaturasSuperadas()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					String cursoAcademico = campos[1];
					Float nota = Float.parseFloat(campos[2]);
					asignaturasSuperadas.put(idAsignatura,
							new NotaFinal(idAsignatura, cursoAcademico, nota, asignaturas.get(idAsignatura)));
				}
				alumno.setAsignaturasSuperadas(asignaturasSuperadas);
			}
		}
	}

	public static void cargarDocenciaImpartida(TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {

		Set<String> setProfesores = profesores.keySet(); // CREACION de un Set con las claves de los profesores en el TreeMap profesores.
		Iterator<String> it = setProfesores.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Profesor profesor = profesores.get(it.next()); // Se recoge el profesor del TreeMap mediante la key
			String caracterVacio = "";
			if (profesor.getArrayDocenciaImpartida()[0].compareTo(caracterVacio) != 0) {
				TreeMap<Integer, Grupo> docenciaImpartida = new TreeMap<Integer, Grupo>(); // Nuevo TreeMap donde se guardaran los grupos impartidos por el profesor, para posteriormente añadirlos al profesor mediante un set()..
				int i;
				for (i = 0; i < profesor.getArrayDocenciaImpartida().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					String[] campos = profesor.getArrayDocenciaImpartida()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					String tipoGrupo = campos[1];
					Integer idGrupo = Integer.parseInt(campos[2]);
					if (tipoGrupo.contains("A"))
						docenciaImpartida.put(idGrupo, asignaturas.get(idAsignatura).getGruposA().get(idGrupo));
					else
						docenciaImpartida.put(idGrupo, asignaturas.get(idAsignatura).getGruposB().get(idGrupo));
				}
				profesor.setDocenciaImpartida(docenciaImpartida);
			}
		}
	}

	public static void cargarDocenciaRecibida(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
		Set<String> setAlumnos = alumnos.keySet(); // CREACION de un Set con las claves de los alumnos en el TreeMap alumnos.
		Iterator<String> it = setAlumnos.iterator(); // Se linkea un Iterator al Set para navegarlo.
		while (it.hasNext()) { // Se navega sobre cada elemento del set para extraer la key.
			Alumno alumno = alumnos.get(it.next()); // Se recoge el alumno del TreeMap mediante la key
			TreeMap<Integer, Grupo> docenciaRecibida = new TreeMap<Integer, Grupo>(); // Nuevo TreeMap donde se guardaran los grupos a los que asiste el alumno, para posteriormente añadirlos al alumno mediante un set().
			int i;
			String caracterVacio = "";
			if (alumno.getArrayDocenciaRecibida()[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < alumno.getArrayDocenciaRecibida().length; i++) { // Bucle en el que se accede a la info de las asignaturas prerrequisito y se añaden estas al TreeMap de nuevosPrerrequisitos para posteriormente hacer un set().
					String[] campos = alumno.getArrayDocenciaRecibida()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					if (campos.length > 1) {
						String tipoGrupo = campos[1];
						Integer idGrupo = Integer.parseInt(campos[2]);
						if (tipoGrupo.contains("A"))
							docenciaRecibida.put(idGrupo, asignaturas.get(idAsignatura).getGruposA().get(idGrupo));
						else
							docenciaRecibida.put(idGrupo, asignaturas.get(idAsignatura).getGruposB().get(idGrupo));
					}
					alumno.getAsignaturasMatriculadas().put(idAsignatura, asignaturas.get(idAsignatura));
				}
			}
			alumno.setDocenciaRecibida(docenciaRecibida);
		}
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
