package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Pablo Rodriguez Perez, Martin Puga Egea
 */

public class Gestion {

	/**
	 * Contiene toda el funcionamiento del programa
	 * Comienza creando TreeMaps con la información de los ficheros de entrada
	 * Ordena toda la información creando las relaciones necesarias
	 * Ejecuta las ordenes que se le hayan introducido
	 * Guarda los cambios en los ficheros correspondientes
	 */

	public static void main(String args[]) {

		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>();
		profesores = cargarProfesores();
		alumnos = cargarAlumnos();
		asignaturas = cargarAsignaturas(profesores);
		cargarAsignaturasSuperadas(alumnos, asignaturas);
		cargarDocenciaImpartida(profesores, asignaturas);
		cargarDocenciaRecibida(alumnos, asignaturas);
		ejecucion(profesores, alumnos, asignaturas);
		guardarFicheroPersonas(profesores, alumnos, asignaturas);
		guardarFicheroAsignaturas(profesores, asignaturas);
	}

	/**
	 * Crea un TreeMap con los profesores del archivo personas.txt y le asigna su correspondiente informacion.
	 * @return TreeMap de profesores.
	 */

	public static TreeMap<String, Profesor> cargarProfesores() {

		TreeMap<String, Profesor> profesores = new TreeMap<String, Profesor>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt");
		} catch (FileNotFoundException NoExisteFichero) {
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1);
		}
		Scanner entrada = new Scanner(flujo_entrada);
		String linea = null;
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (linea.contains("profesor")) {
				String dni = entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
				String categoria = entrada.nextLine();
				String departamento = entrada.nextLine();
				Integer horasDocenciaAsignables = Integer.parseInt(entrada.nextLine());
				String[] arrayDocenciaImpartida = entrada.nextLine().split("; ");
				TreeMap<Grupo, Grupo> docenciaImpartidaA = new TreeMap<Grupo, Grupo>();
				TreeMap<Grupo, Grupo> docenciaImpartidaB = new TreeMap<Grupo, Grupo>();
				TreeMap<Integer, Asignatura> asignaturasCoordinadas = new TreeMap<Integer, Asignatura>();
				Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento, categoria, departamento, horasDocenciaAsignables, docenciaImpartidaA, docenciaImpartidaB, asignaturasCoordinadas, arrayDocenciaImpartida);
				profesores.put(dni, profesor);
			} else { // Se salta el bloque si es un alumno.
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

	/**
	 * Crea un TreeMap con los alumnos del archivo personas.txt y le asigna su correspondiente informacion.
	 * @return TreeMap de alumnos.
	 */

	public static TreeMap<String, Alumno> cargarAlumnos() {

		TreeMap<String, Alumno> alumnos = new TreeMap<String, Alumno>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("personas.txt");
		} catch (FileNotFoundException NoExisteFichero) {
			System.out.println("Fichero \"personas.txt\" inexistente");
			System.exit(-1);
		}
		Scanner entrada = new Scanner(flujo_entrada);
		String linea = null;
		while (entrada.hasNextLine()) {
			linea = entrada.nextLine();
			if (linea.contains("alumno")) {
				String dni = entrada.nextLine();
				String nombre = entrada.nextLine();
				String apellidos = entrada.nextLine();
				linea = entrada.nextLine();
				String[] fecha = linea.split("/");
				GregorianCalendar fechaNacimiento = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
				linea = entrada.nextLine();
				fecha = linea.split("/");
				GregorianCalendar fechaIngreso = new GregorianCalendar(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]) - 1, Integer.parseInt(fecha[0]));
				String[] arrayAsignaturasSuperadas = entrada.nextLine().split("; ");
				TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>();
				String[] arrayDocenciaRecibida = entrada.nextLine().split("; ");
				TreeMap<Integer, Grupo> docenciaRecibidaA = new TreeMap<Integer, Grupo>();
				TreeMap<Integer, Grupo> docenciaRecibidaB = new TreeMap<Integer, Grupo>();
				TreeMap<Integer, Asignatura> asignaturasMatriculadas = new TreeMap<Integer, Asignatura>();
				Alumno alumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, fechaIngreso, docenciaRecibidaA, docenciaRecibidaB, asignaturasSuperadas, arrayAsignaturasSuperadas, asignaturasMatriculadas, arrayDocenciaRecibida);
				alumnos.put(dni, alumno);
			} else { // Se salta el bloque si es un profesor.
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

	/**
	 * Crea un TreeMap con las asignaturas del archivo asignaturas.txt y le asigna su correspondiente informacion.
	 * @return Treemap asignaturas
	 */

	public static TreeMap<Integer, Asignatura> cargarAsignaturas(TreeMap<String, Profesor> profesores) {
		TreeMap<Integer, Asignatura> asignaturas = new TreeMap<Integer, Asignatura>();
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("asignaturas.txt");
		} catch (FileNotFoundException NoExisteFichero) {
			System.out.println("Fichero \"asignaturas.txt\" inexistente");
			System.exit(-1);
		}
		Scanner entrada = new Scanner(flujo_entrada);
		String linea = null;
		while (entrada.hasNextLine()) {
			/**
			 * Se recoge la informacion general de la asignatura
			 */
			Integer idAsignatura = Integer.parseInt(entrada.nextLine());
			String nombre = entrada.nextLine();
			String siglas = entrada.nextLine();
			Integer curso = Integer.parseInt(entrada.nextLine());
			String dniCoordinador = entrada.nextLine();
			String caracterVacio = "";
			String[] arrayPrerrequisitos = entrada.nextLine().split(", ");
			TreeMap<Integer, Asignatura> prerrequisitos = new TreeMap<Integer, Asignatura>();
			Asignatura asignatura = new Asignatura(idAsignatura, nombre, siglas, curso, new Profesor(), prerrequisitos, new TreeMap<Integer, Grupo>(), new TreeMap<Integer, Grupo>(), arrayPrerrequisitos);
			if (dniCoordinador.compareTo(caracterVacio) != 0) {
				Profesor coordinador = profesores.get(dniCoordinador);
				asignatura.setCoordinador(coordinador);
				profesores.get(dniCoordinador).getAsignaturasCoordinadas().put(idAsignatura, asignatura);
			} else {
				Profesor coordinador = null;
				asignatura.setCoordinador(coordinador);
			}
			/**
			 * Se recoge la informacion de los grupos A y B
			 */
			TreeMap<Integer, Grupo> gruposA = new TreeMap<Integer, Grupo>();
			linea = entrada.nextLine();
			String[] arrayGruposA = linea.split("; ");

			int i;
			if (arrayGruposA[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < arrayGruposA.length; i++) {
					String[] grupo = arrayGruposA[i].split(" ");
					Integer idGrupo = Integer.parseInt(grupo[0]);
					String dia = grupo[1];
					Integer horaInicio = Integer.parseInt(grupo[2]);
					Integer horaFin = Integer.parseInt(grupo[3]);
					Grupo grupoA = new Grupo("A", idGrupo, dia, horaInicio, horaFin, asignatura);
					gruposA.put(idGrupo, grupoA);
				}
			}
			TreeMap<Integer, Grupo> gruposB = new TreeMap<Integer, Grupo>();
			linea = entrada.nextLine();
			String[] arrayGruposB = linea.split("; ");
			if (arrayGruposB[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < arrayGruposB.length; i++) {
					String[] grupo = arrayGruposB[i].split(" ");
					Integer idGrupo = Integer.parseInt(grupo[0]);
					String dia = grupo[1];
					Integer horaInicio = Integer.parseInt(grupo[2]);
					Integer horaFin = Integer.parseInt(grupo[3]);
					Grupo grupoA = new Grupo("B", idGrupo, dia, horaInicio, horaFin, asignatura);
					gruposB.put(idGrupo, grupoA);
				}
			}
			asignatura.setGruposA(gruposA);
			asignatura.setGruposB(gruposB);
			asignaturas.put(asignatura.getIdAsignatura(), asignatura);
			if (entrada.hasNextLine())
				linea = entrada.nextLine(); // Se recoge el "*" de separacion.
		}
		entrada.close();

		/**
		 * Se actualizan los prerrequisitos de las asignaturas
		 */

		Set<Integer> setAsignaturas = asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		while (it.hasNext()) {
			Asignatura asignatura = asignaturas.get(it.next());
			String caracterVacio = "";
			if (asignatura.getArrayPrerrequisitos()[0].compareTo(caracterVacio) != 0) {
				TreeMap<Integer, Asignatura> nuevosPrerrequisitos = new TreeMap<Integer, Asignatura>();
				int i;
				for (i = 0; i < asignatura.getArrayPrerrequisitos().length; i++) {
					nuevosPrerrequisitos.put(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i]), asignaturas.get(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i])));
				}
				asignatura.setPrerrequisitos(nuevosPrerrequisitos);
			}
		}
		return asignaturas;
	}

	/**
	 * Actualiza la informacion de las asignaturas superadas de los alumnos.
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public static void cargarAsignaturasSuperadas(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		Set<String> setAlumnos = alumnos.keySet();
		Iterator<String> it = setAlumnos.iterator();
		while (it.hasNext()) {
			Alumno alumno = alumnos.get(it.next());
			String caracterVacio = "";
			if (alumno.getArrayAsignaturasSuperadas()[0].compareTo(caracterVacio) != 0) { // Se cargan los prerrequisitos solo en caso de existir alguna dependencia.
				TreeMap<Integer, NotaFinal> asignaturasSuperadas = new TreeMap<Integer, NotaFinal>();
				int i;
				for (i = 0; i < alumno.getArrayAsignaturasSuperadas().length; i++) {
					String[] campos = alumno.getArrayAsignaturasSuperadas()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					String cursoAcademico = campos[1];
					Float nota = Float.parseFloat(campos[2]);
					asignaturasSuperadas.put(idAsignatura, new NotaFinal(idAsignatura, cursoAcademico, nota, asignaturas.get(idAsignatura)));
				}
				alumno.setAsignaturasSuperadas(asignaturasSuperadas);
			}
		}
	}

	/**
	 * Actualiza la informacion de la docencia impartida por los profesores.
	 * @param profesores TreeMap de profesores
	 * @param asignaturas TreeMap de asignaturas
	 */

	public static void cargarDocenciaImpartida(TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {

		Set<String> setProfesores = profesores.keySet();
		Iterator<String> it = setProfesores.iterator();
		while (it.hasNext()) {
			Profesor profesor = profesores.get(it.next());
			String caracterVacio = "";
			if (profesor.getArrayDocenciaImpartida()[0].compareTo(caracterVacio) != 0) {
				TreeMap<Grupo, Grupo> docenciaImpartidaA = new TreeMap<Grupo, Grupo>();
				TreeMap<Grupo, Grupo> docenciaImpartidaB = new TreeMap<Grupo, Grupo>();
				int i;
				for (i = 0; i < profesor.getArrayDocenciaImpartida().length; i++) {
					String[] campos = profesor.getArrayDocenciaImpartida()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					String tipoGrupo = campos[1];
					Integer idGrupo = Integer.parseInt(campos[2]);
					if (tipoGrupo.contains("A"))
						docenciaImpartidaA.put(asignaturas.get(idAsignatura).getGruposA().get(idGrupo), asignaturas.get(idAsignatura).getGruposA().get(idGrupo));
					else
						docenciaImpartidaB.put(asignaturas.get(idAsignatura).getGruposB().get(idGrupo), asignaturas.get(idAsignatura).getGruposB().get(idGrupo));
				}

				profesor.setDocenciaImpartidaA(docenciaImpartidaA);
				profesor.setDocenciaImpartidaB(docenciaImpartidaB);
			}
		}
	}

	/**
	 * Actualiza la informacion de la docencia recibida por los alumnos.
	 * @param alumnos TreeMap con los alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public static void cargarDocenciaRecibida(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
		Set<String> setAlumnos = alumnos.keySet();
		Iterator<String> it = setAlumnos.iterator();
		while (it.hasNext()) {
			Alumno alumno = alumnos.get(it.next());
			TreeMap<Integer, Grupo> docenciaRecibidaA = new TreeMap<Integer, Grupo>();
			TreeMap<Integer, Grupo> docenciaRecibidaB = new TreeMap<Integer, Grupo>();
			TreeMap<Integer, Asignatura> asignaturasSinGrupo = new TreeMap<Integer, Asignatura>();
			int i;
			String caracterVacio = "";
			if (alumno.getArrayDocenciaRecibida()[0].compareTo(caracterVacio) != 0) {
				for (i = 0; i < alumno.getArrayDocenciaRecibida().length; i++) {
					String[] campos = alumno.getArrayDocenciaRecibida()[i].split(" ");
					Integer idAsignatura = Integer.parseInt(campos[0]);
					if (campos.length > 1) {
						String tipoGrupo = campos[1];
						Integer idGrupo = Integer.parseInt(campos[2]);
						if (tipoGrupo.contains("A"))
							docenciaRecibidaA.put(idAsignatura, asignaturas.get(idAsignatura).getGruposA().get(idGrupo));
						else
							docenciaRecibidaB.put(idAsignatura, asignaturas.get(idAsignatura).getGruposB().get(idGrupo));

					} else {
						asignaturasSinGrupo.put(idAsignatura, asignaturas.get(idAsignatura));// NEW
					}
					alumno.getAsignaturasMatriculadas().put(idAsignatura, asignaturas.get(idAsignatura));
				}
			}
			alumno.setDocenciaRecibidaA(docenciaRecibidaA);
			alumno.setDocenciaRecibidaB(docenciaRecibidaB);
			alumno.setAsignaturasSinGrupo(asignaturasSinGrupo);
		}
	}

	/**
	 * Guarda la información de las personas contenidos en los TreeMaps despues del funcionamiento del programa en el fichero "personas.txt".
	 * @param profesores TreeMap con los profesores
	 * @param alumnos TreeMap con los alumnos
	 * @param asignaturas TreeMap de asignaturas
	 */

	public static void guardarFicheroPersonas(TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("personas.txt");
			pw = new PrintWriter(fichero);
			/**
			 * Se guardan los profesores
			 */
			Set<String> setProfesores = profesores.keySet();
			Iterator<String> it0 = setProfesores.iterator();
			while (it0.hasNext()) {
				Profesor profesor = profesores.get(it0.next());
				pw.println("profesor");
				pw.println(profesor.getDni());
				pw.println(profesor.getNombre());
				pw.println(profesor.getApellidos());
				/**
				 * Estos if son necesario para que siempre se escriba la fecha en formato dd/mm/aaaa
				 */
				if (profesor.getfechaNacimiento().get(Calendar.DATE) < 10 && (profesor.getfechaNacimiento().get(Calendar.MONTH) + 1) < 10) {
					pw.println("0" + profesor.getfechaNacimiento().get(Calendar.DATE) + "/" + "0" + (profesor.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + profesor.getfechaNacimiento().get(Calendar.YEAR));
				} else if (profesor.getfechaNacimiento().get(Calendar.DATE) < 10) {
					pw.println("0" + profesor.getfechaNacimiento().get(Calendar.DATE) + "/" + (profesor.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + profesor.getfechaNacimiento().get(Calendar.YEAR));
				} else if ((profesor.getfechaNacimiento().get(Calendar.MONTH) + 1) < 10) {
					pw.println(profesor.getfechaNacimiento().get(Calendar.DATE) + "/" + "0" + (profesor.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + profesor.getfechaNacimiento().get(Calendar.YEAR));
				} else {
					pw.println(profesor.getfechaNacimiento().get(Calendar.DATE) + "/" + (profesor.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + profesor.getfechaNacimiento().get(Calendar.YEAR));
				}
				pw.println(profesor.getCategoria());
				pw.println(profesor.getDepartamento());
				pw.println(profesor.getHorasDocenciaAsignables());
				Set<Grupo> setDocenciaImpartidaA = profesor.getDocenciaImpartidaA().keySet();
				Iterator<Grupo> it1 = setDocenciaImpartidaA.iterator();
				Set<Grupo> setDocenciaImpartidaB = profesor.getDocenciaImpartidaB().keySet();
				Iterator<Grupo> it2 = setDocenciaImpartidaB.iterator();
				if (!setDocenciaImpartidaA.isEmpty()) {
					while (it1.hasNext()) {
						Grupo grupoA = profesor.getDocenciaImpartidaA().get(it1.next());
						pw.print(grupoA.getAsignatura().getIdAsignatura() + " " + grupoA.getTipoGrupo() + " " + grupoA.getIdGrupo());
						if (it1.hasNext())
							pw.print("; ");
						else if (setDocenciaImpartidaB.isEmpty()) {
							pw.print("");
						} else
							pw.print("; ");
					}
				}
				if (!setDocenciaImpartidaB.isEmpty()) {
					while (it2.hasNext()) {
						Grupo grupoB = profesor.getDocenciaImpartidaB().get(it2.next());
						pw.print(grupoB.getAsignatura().getIdAsignatura() + " " + grupoB.getTipoGrupo() + " " + grupoB.getIdGrupo());
						if (it2.hasNext())
							pw.print("; ");
						else
							pw.print("\n");
					}
				} else
					pw.println();
				if (it0.hasNext())
					pw.println("*");
			}
			/**
			 * Se guardan los alumnos
			 */
			if (!setProfesores.isEmpty())
				pw.println("*");
			Set<String> setAlumnos = alumnos.keySet();
			Iterator<String> it3 = setAlumnos.iterator();
			while (it3.hasNext()) {
				Alumno alumno = alumnos.get(it3.next());
				pw.println("alumno");
				pw.println(alumno.getDni());
				pw.println(alumno.getNombre());
				pw.println(alumno.getApellidos());
				/*
				 * Estos if son necesario para que siempre se escriba la fecha en formato dd/mm/aaaa
				 */
				if (alumno.getfechaNacimiento().get(Calendar.DATE) < 10 && (alumno.getfechaNacimiento().get(Calendar.MONTH) + 1) < 10) {
					pw.println("0" + alumno.getfechaNacimiento().get(Calendar.DATE) + "/" + "0" + (alumno.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + alumno.getfechaNacimiento().get(Calendar.YEAR));
				} else if (alumno.getfechaNacimiento().get(Calendar.DATE) < 10) {
					pw.println("0" + alumno.getfechaNacimiento().get(Calendar.DATE) + "/" + (alumno.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + alumno.getfechaNacimiento().get(Calendar.YEAR));
				} else if ((alumno.getfechaNacimiento().get(Calendar.MONTH) + 1) < 10) {
					pw.println(alumno.getfechaNacimiento().get(Calendar.DATE) + "/" + "0" + (alumno.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + alumno.getfechaNacimiento().get(Calendar.YEAR));
				} else {
					pw.println(alumno.getfechaNacimiento().get(Calendar.DATE) + "/" + (alumno.getfechaNacimiento().get(Calendar.MONTH) + 1) + "/" + alumno.getfechaNacimiento().get(Calendar.YEAR));
				}
				/*
				 * Estos if son necesario para que siempre se escriba la fecha en formato dd/mm/aaaa
				 */
				if (alumno.getFechaIngreso().get(Calendar.DATE) < 10 && (alumno.getFechaIngreso().get(Calendar.MONTH) + 1) < 10) {
					pw.println("0" + alumno.getFechaIngreso().get(Calendar.DATE) + "/" + "0" + (alumno.getFechaIngreso().get(Calendar.MONTH) + 1) + "/" + alumno.getFechaIngreso().get(Calendar.YEAR));
				} else if (alumno.getFechaIngreso().get(Calendar.DATE) < 10) {
					pw.println("0" + alumno.getFechaIngreso().get(Calendar.DATE) + "/" + (alumno.getFechaIngreso().get(Calendar.MONTH) + 1) + "/" + alumno.getFechaIngreso().get(Calendar.YEAR));
				} else if ((alumno.getFechaIngreso().get(Calendar.MONTH) + 1) < 10) {
					pw.println(alumno.getFechaIngreso().get(Calendar.DATE) + "/" + "0" + (alumno.getFechaIngreso().get(Calendar.MONTH) + 1) + "/" + alumno.getFechaIngreso().get(Calendar.YEAR));
				} else {
					pw.println(alumno.getFechaIngreso().get(Calendar.DATE) + "/" + (alumno.getFechaIngreso().get(Calendar.MONTH) + 1) + "/" + alumno.getFechaIngreso().get(Calendar.YEAR));
				}
				Set<Integer> setAsignaturasSuperadas = alumno.getAsignaturasSuperadas().keySet();
				Iterator<Integer> it4 = setAsignaturasSuperadas.iterator();
				if (setAsignaturasSuperadas.isEmpty())
					pw.println();
				else {
					while (it4.hasNext()) {
						NotaFinal notaFinal = alumno.getAsignaturasSuperadas().get(it4.next());
						pw.print(notaFinal.getAsignatura().getIdAsignatura() + " " + notaFinal.getCursoAcademico() + " " + notaFinal.getNota());
						if (it4.hasNext())
							pw.print("; ");
						else
							pw.println("");
					}
				}
				Set<Integer> setDocenciaRecibidaA = alumno.getDocenciaRecibidaA().keySet();
				Iterator<Integer> it5 = setDocenciaRecibidaA.iterator();
				Set<Integer> setDocenciaRecibidaB = alumno.getDocenciaRecibidaB().keySet();
				Iterator<Integer> it6 = setDocenciaRecibidaB.iterator();
				Set<Integer> setAsignaturasSinGrupo = alumno.getAsignaturasSinGrupo().keySet();
				Iterator<Integer> it7 = setAsignaturasSinGrupo.iterator();
				if (!setDocenciaRecibidaA.isEmpty()) {
					while (it5.hasNext()) {
						Grupo grupoA = alumno.getDocenciaRecibidaA().get(it5.next());
						pw.print(grupoA.getAsignatura().getIdAsignatura() + " " + grupoA.getTipoGrupo() + " " + grupoA.getIdGrupo());
						if (it5.hasNext())
							pw.print("; ");
						else {
							if (setDocenciaRecibidaB.isEmpty() & setAsignaturasSinGrupo.isEmpty()) {
								pw.print("");
							} else
								pw.print("; ");
						}
					}
				}
				if (!setDocenciaRecibidaB.isEmpty()) {
					while (it6.hasNext()) {
						Grupo grupoB = alumno.getDocenciaRecibidaB().get(it6.next());
						pw.print(grupoB.getAsignatura().getIdAsignatura() + " " + grupoB.getTipoGrupo() + " " + grupoB.getIdGrupo());
						if (it6.hasNext())
							pw.print("; ");
						else {
							if (setAsignaturasSinGrupo.isEmpty()) {
								pw.print("\n");
							} else
								pw.print("; ");
						}
					}
				} else if (setAsignaturasSinGrupo.isEmpty()) {
					pw.println();
				}
				while (it7.hasNext()) {
					Asignatura asignatura = alumno.getAsignaturasSinGrupo().get(it7.next());
					pw.print(asignatura.getIdAsignatura());
					if (it7.hasNext())
						pw.print("; ");
					else
						pw.println("");
				}
				if (it3.hasNext())
					pw.println("*");
			}
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

	/**
	 * Guarda la informacion de las asignaturas contenidas en el TreeMap despues del funcionamiento del programa en el fichero "asignaturas.txt".
	 * @param profesores TreeMap de profesores
	 * @param asignaturas TreeMap de asignaturas
	 */

	public static void guardarFicheroAsignaturas(TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("asignaturas.txt");
			pw = new PrintWriter(fichero);
			Set<Integer> setAsignaturas = asignaturas.keySet();
			Iterator<Integer> it0 = setAsignaturas.iterator();
			while (it0.hasNext()) {
				Asignatura asignatura = asignaturas.get(it0.next());
				pw.println(asignatura.getIdAsignatura());
				pw.println(asignatura.getNombre());
				pw.println(asignatura.getSiglas());
				pw.println(asignatura.getCurso());
				// if (asignatura.getCoordinador().getDni() == null)
				if (asignatura.getCoordinador() == null)
					pw.println("");
				else
					pw.println(asignatura.getCoordinador().getDni());
				Set<Integer> setPrerrequisitos = asignatura.getPrerrequisitos().keySet();
				Iterator<Integer> it1 = setPrerrequisitos.iterator();
				if (!setPrerrequisitos.isEmpty()) {

					while (it1.hasNext()) {
						Asignatura prerrequisito = asignaturas.get(it1.next());
						pw.print(prerrequisito.getIdAsignatura());
						if (it1.hasNext())
							pw.print(", ");
						else
							pw.println("");
					}
				} else
					pw.println("");
				Set<Integer> setGruposA = asignatura.getGruposA().keySet();
				Iterator<Integer> it2 = setGruposA.iterator();
				if (!setGruposA.isEmpty()) {
					while (it2.hasNext()) {
						Grupo grupo = asignatura.getGruposA().get(it2.next());
						pw.print(grupo.getIdGrupo() + " " + grupo.getDia() + " " + grupo.getHoraInicio() + " " + grupo.getHoraFin());
						if (it2.hasNext())
							pw.print("; ");
						else
							pw.println("");
					}
				} else
					pw.println("");

				Set<Integer> setGruposB = asignatura.getGruposB().keySet();
				Iterator<Integer> it3 = setGruposB.iterator();
				if (!setGruposB.isEmpty()) {
					while (it3.hasNext()) {
						Grupo grupo = asignatura.getGruposB().get(it3.next());
						pw.print(grupo.getIdGrupo() + " " + grupo.getDia() + " " + grupo.getHoraInicio() + " " + grupo.getHoraFin());
						if (it3.hasNext())
							pw.print("; ");
						else
							pw.println("");
					}
				} else
					pw.println("");
				if (it0.hasNext())
					pw.println("*");
			}
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

	/**
	 * Ejecuta los comandos del fichero "ejecucion.txt".
	 * @param profesores TreeMap profesores
	 * @param alumnos TreeMap alumnos
	 * @param asignaturas TreeMap asignaturas
	 */

	public static void ejecucion(TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		Funcionalidades funcionalidad = new Funcionalidades();

		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream("ejecucion.txt");
		} catch (FileNotFoundException NoExisteFichero) {
			funcionalidad.guardarError("", "Fichero de ejecucion no existente");
			System.exit(-1);
		}
		Scanner entrada = new Scanner(flujo_entrada);
		String linea2 = null;
		while (entrada.hasNextLine()) {
			linea2 = entrada.nextLine();
			if (!(linea2.charAt(0) == '*')) {
				String linea = linea2.replaceAll("\\s+", " "); // Contiene la informacion del fichero sin espacios duplicados
				String[] campos = linea.split(" ");
				String camposMinuscula = campos[0].toLowerCase();
				switch (camposMinuscula) {
				case "insertapersona":
					if (campos.length < 7) {
						funcionalidad.argumentosIncorrectos("IP");
						break;
					}
					funcionalidad.insertarPersona(linea, profesores, alumnos);
					break;

				case "asignacoordinador":
					if (campos.length != 3) {
						funcionalidad.argumentosIncorrectos("ACOORD");
						break;
					}
					funcionalidad.asignarCoordinador(linea, profesores, asignaturas);
					break;

				case "asignacargadocente":
					if (campos.length != 5) {
						funcionalidad.argumentosIncorrectos("ACDOC");
						break;
					}
					funcionalidad.asignarCargaDocente(linea, profesores, asignaturas);
					break;

				case "matricula":
					if (campos.length != 3) {
						funcionalidad.argumentosIncorrectos("MAT");
						break;
					}
					funcionalidad.matricularAlumno(linea, alumnos, asignaturas);
					break;

				case "asignagrupo":
					if (campos.length != 5) {
						funcionalidad.argumentosIncorrectos("AGRUPO");
						break;
					}
					funcionalidad.asignarGrupo(linea, alumnos, asignaturas);
					break;

				case "evalua":
					if (campos.length != 4) {
						funcionalidad.argumentosIncorrectos("EVALUA");
						break;
					}
					funcionalidad.evaluarAsignatura(linea, alumnos, asignaturas);
					break;

				case "expediente":
					if (campos.length != 3) {
						funcionalidad.argumentosIncorrectos("EXP");
						break;
					}
					funcionalidad.obtenerExpedienteAlumno(linea, alumnos, asignaturas);
					break;

				case "obtenercalendarioclases":
					if (campos.length != 3) {
						funcionalidad.argumentosIncorrectos("CALENP");
						break;
					}
					funcionalidad.obtenerCalendarioProfesor(campos[1], campos[2], profesores, asignaturas);
					break;

				case "ordenaalumnosxnota":
					if (campos.length != 2) {
						funcionalidad.argumentosIncorrectos("ALUMNOTA");
						break;
					}
					funcionalidad.ordenarAlumnosPorExpediente(campos[1], alumnos);
					break;

				case "crearasignatura":
					if (campos.length < 7) {
						funcionalidad.argumentosIncorrectos("CREAASIG");
						break;
					}
					funcionalidad.crearAsignatura(linea, asignaturas);

				default:
					funcionalidad.comandoIncorrecto(campos[0]);
					break;
				}
			}
		}
		entrada.close();
	}
}