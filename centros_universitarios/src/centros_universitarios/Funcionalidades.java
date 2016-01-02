package centros_universitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * Esta clase contiene las funcionalidades y el control de errores que aparecen explicadas en las especificaciones del proyecto.
 * @author Pablo Rodriguez Perez, Martin Puga Egea.
 */

public class Funcionalidades {

	/**
	 * Permite introducir un nuevo alumno o profesor en el sistema.
	 * @param linea Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            Si es alumno:
	 *            InsertaPersona alumno dni nombre apellidos fechaNacimiento fechaingreso
	 *            Si es profesor:
	 *            InsertaPersona profesor dni nombre apellidos fechaNacimiento categoría departamento horasAsignables
	 * 
	 * @param profesores TreeMap de profesores.
	 * @param alumnos TreeMap de alumnos.
	 */

	public void insertarPersona(String linea, TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos) { // falta aï¿½adir treemaps
		String[] lineaDesplegadaEspacios = linea.split(" ");
		String[] lineaDesplegadaComillas = linea.split("\"");
		String[] lineaDesplegadaBarras = linea.split("/");

		if ((lineaDesplegadaEspacios[1].compareTo("alumno") == 0 && lineaDesplegadaComillas.length != 5) || (lineaDesplegadaEspacios[1].compareTo("profesor") == 0 && lineaDesplegadaComillas.length != 7)) {
			guardarError("IP", "Numero de argumentos incorrecto");
			return;
		}
		if ((lineaDesplegadaEspacios[1].compareTo("alumno") == 0 && lineaDesplegadaBarras.length != 5) || (lineaDesplegadaEspacios[1].compareTo("profesor") == 0 && lineaDesplegadaBarras.length != 3)) {
			guardarError("IP", "Numero de argumentos incorrecto");
			return;
		}

		/**
		 * Se recogen de la clase Persona
		 */

		String dni = lineaDesplegadaEspacios[2];
		if (!validarDNI(dni)) {
			guardarError("IP", "Dni incorrecto");
			return;
		}
		String nombre = nombreSinEspacios(lineaDesplegadaComillas[1].split(" "));
		String apellidos = nombreSinEspacios(lineaDesplegadaComillas[3].split(" "));
		GregorianCalendar fechaNacimiento = null;
		try {
			fechaNacimiento = stringToCalendar(linea.substring(linea.indexOf("/") - 2, linea.indexOf("/") + 8));
		} catch (NumberFormatException e) {
			guardarError("IP", "Numero de argumentos incorrecto");
			return;
		}
		if (validarFecha(fechaNacimiento)) {
			guardarError("IP", "Fecha incorrecta");
			return;
		}

		/**
		 * Se recogen de la clase Alumno
		 */

		if (lineaDesplegadaEspacios[1].compareTo("alumno") == 0) {
			GregorianCalendar fechaIngreso = null;
			try {
				fechaIngreso = stringToCalendar(linea.substring(linea.lastIndexOf("/") - 5, linea.lastIndexOf("/") + 5));
			} catch (NumberFormatException e) {
				guardarError("IP", "Numero de argumentos incorrecto");
				return;
			}

			if (validarFecha(fechaIngreso)) {
				guardarError("IP", "Fecha incorrecta");
				return;
			}
			if (!validarEdad(fechaNacimiento, fechaIngreso)) {
				guardarError("IP", "Fecha ingreso incorrecta");
				return;
			}
			if ((existeAlumno(alumnos, dni))) {
				guardarError("IP", "Alumno ya existente");
				return;
			}

			/**
			 * Introduzco el nuevo alumno en el sistema
			 */

			Alumno alumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, fechaIngreso);
			alumno.setAsignaturasSinGrupo(new TreeMap<Integer, Asignatura>());
			alumnos.put(dni, alumno);
		}

		/**
		 * Se recogen de la clase Profesor
		 */

		if (lineaDesplegadaEspacios[1].compareTo("profesor") == 0) {
			String departamento = nombreSinEspacios(lineaDesplegadaComillas[5].split(" "));
			Integer horasDocenciaAsignables = Integer.parseInt(lineaDesplegadaComillas[6].trim());
			if (horasDocenciaAsignables < 0) {
				guardarError("IP", "Numero de horas incorrecto");
				return;
			}
			String categoria = null;
			String aux = linea.substring(linea.lastIndexOf("/") + 6, linea.lastIndexOf("/") + 14);
			if (aux.equals("asociado")) {
				categoria = "asociado";
				if (horasDocenciaAsignables > 15) {
					guardarError("IP", "Numero de horas incorrecto");
					return;
				}
			}
			if (aux.equals("titular ")) {
				categoria = "titular";
				if (horasDocenciaAsignables > 20) {
					guardarError("IP", "Numero de horas incorrecto");
					return;
				}
			}
			if ((existeProfesor(profesores, dni))) {
				guardarError("IP", "Profesor ya existente");
				return;
			}

			/**
			 * Introduzco el nuevo profesor en el sistema
			 */

			Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento, categoria, departamento, horasDocenciaAsignables);
			profesores.put(dni, profesor);
		}
	}

	/**
	 * Permite asignar un profesor coordinador a una asignatura.
	 * @param linea Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            AsignaCoordinador dniProfesor siglasAsignatura
	 * @param profesores TreeMap de profesores.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void asignarCoordinador(String linea, TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) { // Formato: AsignaCoordinador persona asignatura
		String[] campos = linea.split(" ");
		String persona = campos[1];
		String asignatura = campos[2];

		if (!(existeProfesor(profesores, persona))) {
			guardarError("ACOORD", "Profesor inexistente");
			return;
		}
		if (!(profesorTitular(profesores, persona))) {
			guardarError("ACOORD", "Profesor no titular");
			return;
		}
		if (!(existeAsignatura(asignaturas, asignatura))) {
			guardarError("ACOORD", "Asignatura inexistente");
			return;
		}
		if (coordinadorDosMaterias(profesores, persona)) {
			guardarError("ACOORD", "Profesor ya es coordinador de 2 materias");
			return;
		}
		Set<Integer> setAsignaturas = asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		while (it.hasNext()) {
			Integer idAsignatura = it.next();
			if (asignaturas.get(idAsignatura).getSiglas().contentEquals(asignatura)) {
				if (!(asignaturas.get(idAsignatura).getCoordinador() == null))
					asignaturas.get(idAsignatura).getCoordinador().getAsignaturasCoordinadas().remove(idAsignatura);
				asignaturas.get(idAsignatura).setCoordinador(profesores.get(persona));
				profesores.get(persona).getAsignaturasCoordinadas().put(asignaturas.get(idAsignatura).getIdAsignatura(), asignaturas.get(idAsignatura));
			}
		}

	}

	/**
	 * Permite asignar un profesor a un grupo de una asignatura.
	 * @param linea Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            AsignaCargaDocente dniProfesor siglasAsignatura tipoGrupo idGrupo
	 * @param profesores TreeMap de profesores.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void asignarCargaDocente(String linea, TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {

		String[] campos = linea.split(" ");
		String persona = campos[1];
		String asignatura = campos[2];
		String tipoGrupo = campos[3];
		Integer idGrupo = Integer.parseInt(campos[4]);
		Boolean flagError = false;
		Boolean flagErrorAsignatura = false;

		if (!(existeProfesor(profesores, persona))) {
			guardarError("ACDOC", "Profesor inexistente");
			flagError = true;
		}
		if (!(existeAsignatura(asignaturas, asignatura))) {
			guardarError("ACDOC", "Asignatura inexistente");
			flagError = true;
			flagErrorAsignatura = true;
		}
		if (!(tipoGrupo.contentEquals("A") || tipoGrupo.contentEquals("B"))) {
			guardarError("ACDOC", "Tipo de grupo incorrecto");
			flagError = true;
		}
		if (!flagErrorAsignatura) {
			if (existeGrupo(asignaturas, idGrupo, tipoGrupo, asignatura)) {
				if (grupoYaAsignado(persona, asignatura, tipoGrupo, idGrupo, asignaturas, profesores)) {
					guardarError("ACDOC", "Grupo ya asignado");
					flagError = true;
				}
				if (horasAsignablesSuperiorMaximo(persona, asignatura, tipoGrupo, idGrupo, profesores, asignaturas)) {
					guardarError("ACDOC", "Horas asignables superior al maximo");
					flagError = true;
				}
				if (generaSolape(persona, asignatura, tipoGrupo, idGrupo, profesores, asignaturas)) {
					guardarError("ACDOC", "Se genera solape");
					flagError = true;
				}
			} else {
				guardarError("ACDOC", "Grupo inexistente");
				return;
			}
		}
		if (flagError)
			return;

		Integer key = siglasToID(asignaturas, asignatura);
		if (tipoGrupo.contentEquals("A")) {
			Grupo grupo = asignaturas.get(key).getGruposA().get(idGrupo);
			profesores.get(persona).getDocenciaImpartidaA().put(grupo, grupo);
		} else {
			Grupo grupo = asignaturas.get(key).getGruposB().get(idGrupo);
			profesores.get(persona).getDocenciaImpartidaB().put(grupo, grupo);
		}
	}

	/**
	 * Permite enrolar a un alumno en una asignatura
	 * @param linea Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            Matricula dniAlumno siglasAsignatura
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void matricularAlumno(String linea, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		String[] campos = linea.split(" ");
		String alumno = campos[1];
		String asignatura = campos[2];

		if (!(existeAlumno(alumnos, alumno))) {
			guardarError("MAT", "Alumno inexistente");
			return;
		}
		if (!(existeAsignatura(asignaturas, asignatura))) {
			guardarError("MAT", "Asignatura inexistente");
			return;
		}
		if (matriculaExistente(alumnos, asignaturas, alumno, asignatura)) {
			guardarError("MAT", "Ya es alumno de la asignatura indicada");
			return;
		}
		if (!(cumplePrerrequisitos(alumnos, asignaturas, alumno, asignatura))) {
			guardarError("MAT", "No cumple requisitos");
			return;

		}
		Integer key = siglasToID(asignaturas, asignatura);
		alumnos.get(alumno).getAsignaturasMatriculadas().put(key, asignaturas.get(key));
		alumnos.get(alumno).getAsignaturasSinGrupo().put(key, asignaturas.get(key));

	}

	/**
	 * Permite asignar un grupo de una asignatura a un alumno previamente matriculado en ella.
	 * @param linea Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            AsignaGrupo dniAlumno siglasAsignatura tipoGrupo idGrupo
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void asignarGrupo(String linea, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
		// AsignaGrupo alumno asignatura A 2
		String[] campos = linea.split(" ");
		String alumno = campos[1];
		Integer idGrupo = Integer.parseInt(campos[4]);
		if (!existeAlumno(alumnos, alumno)) {
			guardarError("AGRUPO", "Alumno inexistente");
			return;
		}

		String siglas = campos[2];
		if (!existeAsignatura(asignaturas, siglas)) {
			guardarError("AGRUPO", "Asignatura inexistente");
			return;
		}

		if (!matriculaExistente(alumnos, asignaturas, alumno, siglas)) {
			guardarError("AGRUPO", "Alumno no matriculado");
			return;
		}

		if (!(campos[3].equals("A") || campos[3].equals("B"))) {
			guardarError("AGRUPO", "Tipo de grupo incorrecto");
			return;
		}

		if (!existeGrupo(asignaturas, idGrupo, campos[3], campos[2])) {
			guardarError("AGRUPO", "Grupo inexistente");
			return;
		}

		if (generaSolapeAlumnos(alumno, siglas, campos[3], idGrupo, alumnos, asignaturas)) {
			guardarError("AGRUPO", "Se genera solape");
			return;
		}
		// Meter datos en TreeMap
		Integer idAsignatura = siglasToID(asignaturas, siglas);
		if (campos[3].equals("A")) {
			Grupo grupoNuevo = asignaturas.get(idAsignatura).getGruposA().get(idGrupo);
			alumnos.get(alumno).getDocenciaRecibidaA().put(idAsignatura, grupoNuevo);
		} else {
			Grupo grupoNuevo = asignaturas.get(idAsignatura).getGruposB().get(idGrupo);
			alumnos.get(alumno).getDocenciaRecibidaB().put(idAsignatura, grupoNuevo);
		}
		alumnos.get(alumno).getAsignaturasSinGrupo().remove(idAsignatura);
	}

	/**
	 * Permite introducir las notas de una asignatura a través de un fichero de texto con los dni y la nota de la parte A y B de cada alumno
	 * @param lineaComando Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            Evalua siglasAsignatura cursoAcademico fichero
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void evaluarAsignatura(String lineaComando, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		String[] campos = lineaComando.split(" ");
		String asignatura = campos[1];
		String cursoAcademico = campos[2];
		String fichero = campos[3];

		if (!(existeAsignatura(asignaturas, asignatura))) {
			guardarError("EVALUA", "Asignatura inexistente");
			return;
		}
		FileInputStream flujo_entrada = null;
		try {
			flujo_entrada = new FileInputStream(fichero);
		} catch (FileNotFoundException NoExisteFichero) {
			guardarError("EVALUA", "Fichero de notas inexistente");
			return;
		}
		if (asignaturaYaEvaluada(alumnos, asignaturas, asignatura, cursoAcademico)) {
			guardarError("EVALUA", "Asignatura ya evaluada en este curso acadÃ©mico");
			return;
		}
		Scanner entrada = new Scanner(flujo_entrada);
		String linea, lineaSinEspaciosDuplicados;
		Integer numeroLinea = 0;

		while (entrada.hasNext()) {
			linea = entrada.nextLine();
			lineaSinEspaciosDuplicados = linea.replaceAll("\\s+", " "); // Contiene la informacion del fichero sin espacios duplicados
			String[] camposLinea = lineaSinEspaciosDuplicados.split(" ");
			String alumno = camposLinea[0];
			Float notaGrupoA = Float.parseFloat(camposLinea[1]);
			Float notaGrupoB = Float.parseFloat(camposLinea[2]);
			numeroLinea++;
			Boolean error = false;
			if (!(existeAlumno(alumnos, alumno))) {
				guardarErrorFicheroNotas(numeroLinea, "Alumno inexistente: " + alumno);
				error = true;
				continue;
			}
			if (!matriculaExistente(alumnos, asignaturas, alumno, asignatura)) {
				guardarErrorFicheroNotas(numeroLinea, "Alumno no matriculado: " + alumno);
				error = true;
				continue;
			}
			if (notaGrupoA < 0 || notaGrupoA > 5) {
				guardarErrorFicheroNotas(numeroLinea, "Nota grupo A/B incorrecta");
				error = true;
				continue;

			}
			if (notaGrupoB < 0 || notaGrupoB > 5) {
				guardarErrorFicheroNotas(numeroLinea, "Nota grupo A/B incorrecta");
				error = true;
				continue;
			}
			if (!error) {
				Float notaTotal = notaGrupoA + notaGrupoB;
				if (notaTotal >= 5) {
					alumnos.get(alumno).getAsignaturasSuperadas().put(siglasToID(asignaturas, asignatura), new NotaFinal(siglasToID(asignaturas, asignatura), cursoAcademico, notaTotal, asignaturas.get(siglasToID(asignaturas, asignatura))));
				}
				alumnos.get(alumno).getAsignaturasMatriculadas().remove(siglasToID(asignaturas, asignatura));
				TreeMap<Integer, Asignatura> asignaturasSinGrupo = new TreeMap<Integer, Asignatura>();
				asignaturasSinGrupo.putAll(alumnos.get(alumno).getAsignaturasSinGrupo());
				Set<Integer> setAsignaturasSinGrupoIterable = asignaturasSinGrupo.keySet();
				Iterator<Integer> it0 = setAsignaturasSinGrupoIterable.iterator();
				if (!setAsignaturasSinGrupoIterable.isEmpty()) {
					while (it0.hasNext()) {
						if (asignaturas.get(it0.next()).getSiglas().contentEquals(asignatura))
							alumnos.get(alumno).getAsignaturasSinGrupo().remove(siglasToID(asignaturas, asignatura));
					}
				}
				Set<Integer> setGruposA = alumnos.get(alumno).getDocenciaRecibidaA().keySet();
				Iterator<Integer> itA = setGruposA.iterator();
				if (!setGruposA.isEmpty()) {
					Integer idGrupoA;
					while (itA.hasNext()) {
						idGrupoA = itA.next();
						if (alumnos.get(alumno).getDocenciaRecibidaA().get(idGrupoA).getAsignatura().getIdAsignatura().compareTo(siglasToID(asignaturas, asignatura)) == 0)
							alumnos.get(alumno).getDocenciaRecibidaA().remove(idGrupoA);
					}
				}
				Set<Integer> setGruposB = alumnos.get(alumno).getDocenciaRecibidaB().keySet();
				Iterator<Integer> itB = setGruposB.iterator();
				if (!setGruposB.isEmpty()) {
					Integer idGrupoB;
					while (itB.hasNext()) {
						idGrupoB = itB.next();
						if (alumnos.get(alumno).getDocenciaRecibidaB().get(idGrupoB).getAsignatura().getIdAsignatura().compareTo(siglasToID(asignaturas, asignatura)) == 0)
							alumnos.get(alumno).getDocenciaRecibidaB().remove(idGrupoB);
					}
				}
			}
		}
		entrada.close();
	}

	/**
	 * Permite exportar el expediente de un alumno a un fichero ordenado por curso y asignatura.
	 * @param linea Linea de texto que acompana al comando en el fichero "ejecucion.txt" con el formato:
	 *            Expediente dniAlumno salida
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void obtenerExpedienteAlumno(String linea, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		String[] campos = linea.split(" ");
		String alumno = campos[1];
		String salida = correctoTXT(campos[2]);

		if (!existeAlumno(alumnos, alumno)) {
			guardarError("EXP", "Alumno inexistente");
			return;
		}
		if (expedienteVacio(alumnos, alumno)) {
			guardarError("EXP", "Expediente vacio");
			return;
		}

		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(salida);
			pw = new PrintWriter(fichero);

			Float notaMedia = (float) 0;
			TreeMap<String, NotaFinal> treeMapNotas = new TreeMap<String, NotaFinal>();
			Set<Integer> setNotas = alumnos.get(alumno).getAsignaturasSuperadas().keySet();
			Iterator<Integer> it = setNotas.iterator();
			while (it.hasNext()) {
				NotaFinal nota = alumnos.get(alumno).getAsignaturasSuperadas().get(it.next());
				treeMapNotas.put(nota.getAsignatura().getNombre(), nota);
			}
			Iterator<String> it0 = treeMapNotas.keySet().iterator();
			while (it0.hasNext()) {
				String key = it0.next();
				pw.println(treeMapNotas.get(key));
				notaMedia += treeMapNotas.get(key).getNota();
			}
			notaMedia = notaMedia / setNotas.size();
			pw.println("Nota media del expediente: " + notaMedia);
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
	 * Permite exportar el horario semanal de docencia de un profesor.
	 * @param profesor dni del profesor
	 * @param ficheroSalida nombre del fichero donde se va exportar la informacion
	 * @param profesores TreeMap de profesores.
	 * @param asignaturas TreeMap de asignaturas.
	 */

	public void obtenerCalendarioProfesor(String profesor, String ficheroSalida, TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {
		if (!existeProfesor(profesores, profesor)) {
			guardarError("CALENP", "Profesor inexistente");
			return;
		}
		if (profesores.get(profesor).getArrayDocenciaImpartida()[0].compareTo("") == 0) {
			guardarError("CALENP", "Profesor sin asignaciones");
			return;
		}
		// Creamos archivo
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(correctoTXT(ficheroSalida), true);
			pw = new PrintWriter(fichero);
			pw.println("Dia; Hora; Asignatura; Tipo grupo; Id grupo");
			// Todas las horas se aï¿½adirï¿½n a este TreeMap
			TreeMap<Integer, Grupo> ordenar = new TreeMap<Integer, Grupo>();

			for (int i = 0; i < profesores.get(profesor).getArrayDocenciaImpartida().length; i++) {
				String[] grupo = profesores.get(profesor).getArrayDocenciaImpartida()[i].split(" ");
				Integer idAsignatura = Integer.parseInt(grupo[0]);
				Integer idGrupo = Integer.parseInt(grupo[2]);
				Grupo nuevoGrupo = null;
				if (grupo[1].compareTo("A") == 0) {
					nuevoGrupo = asignaturas.get(idAsignatura).getGruposA().get(idGrupo);
				} else {
					nuevoGrupo = asignaturas.get(idAsignatura).getGruposB().get(idGrupo);
				}
				Integer n_orden = nuevoGrupo.getHoraInicio();

				// Sumo 20 unidades por dia para que el TreeMap estï¿½ ordenado
				if (nuevoGrupo.getDia().compareTo("L") == 0) {
					ordenar.put(n_orden, nuevoGrupo);
				}
				if (nuevoGrupo.getDia().compareTo("M") == 0) {
					n_orden += 20;
					ordenar.put(n_orden, nuevoGrupo);
				}
				if (nuevoGrupo.getDia().compareTo("X") == 0) {
					n_orden += 40;
					ordenar.put(n_orden, nuevoGrupo);
				}
				if (nuevoGrupo.getDia().compareTo("J") == 0) {
					n_orden += 60;
					ordenar.put(n_orden, nuevoGrupo);
				}
				if (nuevoGrupo.getDia().compareTo("V") == 0) {
					n_orden += 80;
					ordenar.put(n_orden, nuevoGrupo);
				}
			}
			for (int i = 9; i < 101; i++) {
				if (ordenar.get(i) != null) {
					pw.println(ordenar.get(i).toString2());
				}

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
	 * Permite exportar a fichero todos los alumnos ordenados por la nota del expediente.
	 * @param ficheroSalida nombre del fichero donde se va exportar la informacion
	 * @param alumnos TreeMap de alumnos.
	 */

	public void ordenarAlumnosPorExpediente(String ficheroSalida, TreeMap<String, Alumno> alumnos) {
		TreeMap<String, Alumno> NotasMediasMap = new TreeMap<String, Alumno>(new ComparadorNota());
		Set<String> setAlumnos = alumnos.keySet();
		Iterator<String> it = setAlumnos.iterator();
		while (it.hasNext()) {
			Alumno alumnoAlumno = alumnos.get(it.next());
			String caracterVacio = "";
			String alumno = alumnoAlumno.getDni();
			if (alumnos.get(alumno).getArrayAsignaturasSuperadas()[0].compareTo(caracterVacio) != 0) {
				Float notaMedia = (float) 0;
				TreeMap<String, NotaFinal> treeMapNotas = new TreeMap<String, NotaFinal>();
				Set<Integer> setNotas = alumnos.get(alumno).getAsignaturasSuperadas().keySet();
				Iterator<Integer> it2 = setNotas.iterator();
				while (it2.hasNext()) {
					NotaFinal nota = alumnos.get(alumno).getAsignaturasSuperadas().get(it2.next());
					treeMapNotas.put(nota.getAsignatura().getNombre(), nota);
				}
				Iterator<String> it0 = treeMapNotas.keySet().iterator();
				while (it0.hasNext()) {
					String key = it0.next();
					notaMedia += treeMapNotas.get(key).getNota();
				}
				notaMedia = notaMedia / setNotas.size();
				alumnos.get(alumno).setNotaMedia(notaMedia);
				NotasMediasMap.put(notaMedia + " " + alumnos.get(alumno).getApellidos() + " " + alumnos.get(alumno).getNombre(), alumnoAlumno);
			}
		}
		// Escribir en fichero
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(correctoTXT(ficheroSalida), true);
			pw = new PrintWriter(fichero);
			Set<String> setNotas = NotasMediasMap.keySet();
			Iterator<String> it3 = setNotas.iterator();
			while (it3.hasNext()) {
				String key = it3.next();
				pw.println(NotasMediasMap.get(key).getApellidos() + " " + NotasMediasMap.get(key).getNombre() + " " + NotasMediasMap.get(key).getDni() + " " + NotasMediasMap.get(key).getNotaMedia());
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
	 * Permite ordenar por nota media de mayor a menor y en caso de coincidencia por orden alfabetico del apellido en ordenarAlumnosPorExpediente().
	 */

	class ComparadorNota implements Comparator<String> {
		@Override
		public int compare(String e1, String e2) {
			String[] campos1 = e1.split(" ");
			Float n1 = Float.parseFloat(campos1[0]);
			String[] campos2 = e2.split(" ");
			Float n2 = Float.parseFloat(campos2[0]);
			if (n1 > n2) {
				return -1;
			} else if (n1 < n2) {
				return 1;
			} else {
				return campos1[1].compareTo(campos2[1]);
			}
		}
	}

	/**
	 * Permite crear una asignatura
	 * @param linea
	 *            CrearAsignatura nombre siglas curso prerrequisitos gruposA gruposB
	 * @param asignaturas TreeMap asignaturas.
	 */

	public void crearAsignatura(String linea, TreeMap<Integer, Asignatura> asignaturas) {
		String[] camposEntrecomillados = linea.split("\"");
		String[] campos = camposEntrecomillados[2].split(" ");

		Integer idAsignatura = idLibre(asignaturas);
		String nombre = nombreSinEspacios(camposEntrecomillados[1].split(" "));
		String siglas = campos[1];
		if (existeAsignatura(asignaturas, siglas)) {
			guardarError("CREAASIG", "Siglas ya pertenecientes a asignatura existente");
		}
		Integer curso = Integer.parseInt(campos[2]);
		String[] arrayPrerrequisitos = camposEntrecomillados[3].split(", ");
		TreeMap<Integer, Asignatura> prerrequisitos = new TreeMap<Integer, Asignatura>();

		Asignatura asignatura = new Asignatura(idAsignatura, nombre, siglas, curso, new Profesor(), prerrequisitos, new TreeMap<Integer, Grupo>(), new TreeMap<Integer, Grupo>(), arrayPrerrequisitos);
		asignatura.setCoordinador(null);

		// Grupos A
		TreeMap<Integer, Grupo> gruposA = new TreeMap<Integer, Grupo>();
		String[] arrayGruposA = camposEntrecomillados[5].split("; ");
		int i;
		if (arrayGruposA[0].compareTo("") != 0) {
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

		// Grupos B
		TreeMap<Integer, Grupo> gruposB = new TreeMap<Integer, Grupo>();
		String[] arrayGruposB = camposEntrecomillados[7].split("; ");
		if (arrayGruposB[0].compareTo("") != 0) {
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

		// Prerrequisitos
		if (asignatura.getArrayPrerrequisitos()[0].compareTo("") != 0) {
			TreeMap<Integer, Asignatura> nuevosPrerrequisitos = new TreeMap<Integer, Asignatura>();
			for (i = 0; i < asignatura.getArrayPrerrequisitos().length; i++) {
				nuevosPrerrequisitos.put(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i]), asignaturas.get(Integer.parseInt(asignatura.getArrayPrerrequisitos()[i])));
			}
			asignatura.setPrerrequisitos(nuevosPrerrequisitos);
		}
	}

	/**
	 * Devuelve un identificador de asignatura que no este en uso
	 * @param asignaturas TreeMap de asignaturas
	 * @return identificador de asignatura libre
	 */

	public Integer idLibre(TreeMap<Integer, Asignatura> asignaturas) {
		return asignaturas.lastKey() + 1;
	}

	/**
	 * Imprime a fichero el error producido con el formato pedido en las especificaciones.
	 * @param abreviatura Siglas del comando que ha dado el error
	 * @param texto Descripcion del error
	 */

	public void guardarError(String abreviatura, String texto) { // Metodo que permite la escritura en el fichero "avisos.txt".
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
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

	/**
	 * Imprime a fichero el comando no existente introducido
	 * @param comando Texto introducido que no corresponde a ningun comando del programa.
	 */

	public void comandoIncorrecto(String comando) {
		guardarError("", "Comando incorrecto: " + comando);
	}

	/**
	 * Imprime a fichero el error "Numero de argumentos incorrecto"
	 * @param comando Siglas del comando que ha dado el error.
	 */
	public void argumentosIncorrectos(String comando) {
		guardarError(comando, "Numero de argumentos incorrecto");
	}

	/**
	 * Comprueba si el profesor introducido es titular.
	 * @param profesores TreeMap con todos los profesores
	 * @param dni DNI del profesor deseado
	 * @return true si el profesor es titular
	 */

	public Boolean profesorTitular(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.get(dni).getCategoria().contains("titular"))
			return true;
		else
			return false;
	}

	/**
	 * Comprueba si hay algun alumno en el sistema con ese DNI
	 * @param alumnos TreeMap con todos los alumnos
	 * @param dni DNI del alumno deseado
	 * @return true si el alumno existe
	 */

	public Boolean existeAlumno(TreeMap<String, Alumno> alumnos, String dni) {
		if (alumnos.containsKey(dni))
			return true;
		else
			return false;
	}

	/**
	 * Comprueba si hay algun profesor en el sistema con ese DNI
	 * @param profesores TreeMap con todos los profesores
	 * @param dni DNI del profesor deseado
	 * @return true si el profesor existe
	 */

	public Boolean existeProfesor(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.containsKey(dni))
			return true;
		else
			return false;
	}

	/**
	 * Comprueba si hay alguna asignatura en el sistema con esas siglas
	 * @param asignaturas TreeMap con todas las asignaturas
	 * @param siglas Siglas de la asignatura deseada
	 * @return true si existe la asignatura
	 */

	public Boolean existeAsignatura(TreeMap<Integer, Asignatura> asignaturas, String siglas) {
		Set<Integer> setAsignaturas = asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		Boolean flag = false;
		while (it.hasNext())
			if (asignaturas.get(it.next()).getSiglas().contentEquals(siglas))
				flag = true;
		if (flag)
			return true;
		else
			return false;
	}

	/**
	 * Comprueba si el profesor pedido ya coordina dos asignaturas
	 * @param profesores TreeMap de profesores.
	 * @param dni DNI del profesor pedido
	 * @return true si el profesor coordina 2 asignaturas
	 */

	public Boolean coordinadorDosMaterias(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.get(dni).getAsignaturasCoordinadas().size() == 2)
			return true;
		else
			return false;
	}

	/**
	 * Metodo que convierte un String de una fecha con formato "dd/mm/aaaa" en un objeto GregorianCalendar con dicha fecha
	 * @param fechaEntrada String con la fecha que se desea devolver
	 * @return objeto GregorianCalendar con la fecha pedida
	 */

	public static GregorianCalendar stringToCalendar(String fechaEntrada) {
		String[] aux = fechaEntrada.split("/");
		GregorianCalendar fecha = new GregorianCalendar(Integer.parseInt(aux[2]), Integer.parseInt(aux[1]) - 1, Integer.parseInt(aux[0]));
		return fecha;
	}

	/**
	 * Comprueba si la fecha introducida es correcta y que esté comprendida entre 01/01/1950 y 01/01/2050
	 * @param fecha Fecha que se desea comprobar
	 * @return true si la fecha es valida
	 */

	public static boolean validarFecha(GregorianCalendar fecha) {
		Calendar fechaMinima = new GregorianCalendar(1950, 0, 1);
		Calendar fechaMaxima = new GregorianCalendar(2020, 0, 1);
		/**
		 * Al desactivar el modo lenient, si la fecha no es correcta salta una excepcion
		 */
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

	/**
	 * Comprueba que entre las dos fechas hay más de 15 años y menos de 65
	 * @param fechaNacimiento Fecha desde que se quiere contar
	 * @param fechaInscripcion Fecha hasta la que se quiere contar
	 * @return true si el tiempo entre las dos fehcas esta comprendido entre 15 y 65 anos
	 */

	public static boolean validarEdad(GregorianCalendar fechaNacimiento, GregorianCalendar fechaInscripcion) {
		int anho1 = fechaNacimiento.get(GregorianCalendar.YEAR);
		int anho2 = fechaInscripcion.get(GregorianCalendar.YEAR);
		double n_years = 0.0;
		while (anho1 < anho2) {
			n_years++;
			anho1++;
		}
		int dianho1 = fechaNacimiento.get(Calendar.DAY_OF_YEAR);
		int dianho2 = fechaInscripcion.get(Calendar.DAY_OF_YEAR);
		if (fechaInscripcion.isLeapYear(fechaInscripcion.get(Calendar.YEAR))) {
			if (fechaInscripcion.get(Calendar.DAY_OF_YEAR) > 60) {
				fechaInscripcion.add(Calendar.DAY_OF_YEAR, -1);
				dianho2 = fechaInscripcion.get(Calendar.DAY_OF_YEAR);
			}
		}
		double dif_dias = dianho2 - dianho1;
		if (dif_dias < 0) {
			n_years--;
			dif_dias += 365;
		}
		n_years += dif_dias / 365;
		if (fechaInscripcion.isLeapYear(fechaInscripcion.get(Calendar.YEAR))) {
			if (fechaInscripcion.get(Calendar.DAY_OF_YEAR) > 60) {
				fechaInscripcion.add(Calendar.DAY_OF_YEAR, 1);
				dianho2 = fechaInscripcion.get(Calendar.DAY_OF_YEAR);
			}
		}
		if (n_years < 15 || n_years > 65) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Compruebo si el DNI es válido
	 * @param dni DNI que se quiere comprobar
	 * @return true si el DNI es valido
	 */

	public static boolean validarDNI(String dni) {
		/**
		 * Compruebo el tamano del DNI
		 */
		if (dni.length() != 9)
			return false;
		/**
		 * Compruebo si tiene 8 numeros
		 */
		String numero = dni.substring(0, 8);
		try {
			Integer.parseInt(numero);
		} catch (NumberFormatException e) {
			return false;
		}
		/**
		 * Compruebo que sea una letra mayuscula
		 */
		char letra = dni.charAt(8);
		if (letra < 65 || letra > 90)
			return false;

		return true;
	}

	/**
	 * Compruebo si el alumno introducido esta matriculado de la asignatura introducida
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 * @param alumno DNI del alumno que se pide
	 * @param asignatura siglas de la asignatura pedida
	 * @return true si el alumno introducido esta matriculado de esa asignatura
	 */

	public Boolean matriculaExistente(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String alumno, String asignatura) {
		Set<Integer> setAsignaturasMatriculadas = alumnos.get(alumno).getAsignaturasMatriculadas().keySet();
		Iterator<Integer> it = setAsignaturasMatriculadas.iterator();
		while (it.hasNext()) {
			Asignatura asignaturaMatriculada = asignaturas.get(it.next());
			if (asignaturaMatriculada.getSiglas().contentEquals(asignatura))
				return true;
		}
		return false;
	}

	/**
	 * Se introduce un texto.split(" ") y se devuelve el texto sin espacios al principio ni al final
	 * @param entrada String[] del que se desea eliminar los espacios innecesarios
	 * @return String con el texto introducido sin espacios innecesario.
	 *         Ejemplo: Si se introduce "  hola     mundo  " devuelve "hola mundo"
	 */

	public String nombreSinEspacios(String[] entrada) {
		String nombre = null;
		int i = 0;
		/**
		 * Hace el bucle hasta que encuentra el primer array con por lo menos un caracter, ya que el split(" ") crea arrays vacios si hay dos
		 * o mas espacios seguidos
		 */
		for (; nombre == null; i++) {
			if (entrada[i].length() > 0) {
				nombre = entrada[i];
			}
		}
		/**
		 * Recorre el bucle concatenando al array nombre solo los arrays que tengan por lo menos un caracter
		 */
		for (int j = entrada.length; i != j; i++) {
			if (entrada[i].length() > 0) {
				nombre = nombre + " " + entrada[i];
			}
		}
		return nombre;
	}

	/**
	 * Comprueba si en la asignatura introducida existe un grupo con los parametros introducidos
	 * @param asignaturas TreeMap de asignaturas.
	 * @param grupo identificador unico de grupo dentro de una asignatura
	 * @param tipoGrupo A o B
	 * @param siglas siglas de la asignatura pedida
	 * @return true si existe el grupo pedido en la asignatura pedida
	 */

	public Boolean existeGrupo(TreeMap<Integer, Asignatura> asignaturas, Integer grupo, String tipoGrupo, String siglas) {
		/**
		 * Busco el ID de la asignatura usando las siglas
		 */
		Integer key = siglasToID(asignaturas, siglas); // EN ESTE INTEGER QUEDA EL ID DE LA ASIGNATURA A LA QUE PERTENECEN LAS INICIALES

		/**
		 * Busco si el treemap GruposX contiene a "grupo"
		 */
		if (tipoGrupo.contains("A")) {
			if (!asignaturas.get(key).getGruposA().containsKey(grupo)) {
				return false;
			}
		} else if (tipoGrupo.contains("B")) {
			if (!asignaturas.get(key).getGruposB().containsKey(grupo)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Devuelve el identificador unico de la asignatura que corresponde con las siglas introducidas
	 * @param asignaturas TreeMap de asignaturas.
	 * @param siglas Siglas de la asignatura buscada
	 * @return Integer con el identidicador unico de la asigntura pedida
	 */

	public Integer siglasToID(TreeMap<Integer, Asignatura> asignaturas, String siglas) {
		Set<Integer> setAsignaturas = asignaturas.keySet();
		Iterator<Integer> it = setAsignaturas.iterator();
		Integer key = 0; // EN ESTE INTEGER QUEDA EL ID DE LA ASIGNATURA A LA QUE PERTENECEN LAS INICIALES
		while (it.hasNext()) {
			key = it.next();
			Asignatura asignaturaId = asignaturas.get(key);
			if (asignaturaId.getSiglas().contentEquals(siglas)) {
				key = asignaturaId.getIdAsignatura();
				break;
			}
		}
		return key;
	}

	/**
	 * Comprueba si el alumno introducido cumple los prerrequisitos para matricularse en la asignatura introducida
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 * @param alumno DNI del alumno al que se quiere acceder
	 * @param asignatura siglas de la asignatura en la que se quiere matricular al alumno
	 * @return true si cumple los prerrequisitos
	 */

	public Boolean cumplePrerrequisitos(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String alumno, String asignatura) {
		Integer key = siglasToID(asignaturas, asignatura);
		Set<Integer> setPrerrequisitos = asignaturas.get(key).getPrerrequisitos().keySet();// Asignatura
		Iterator<Integer> it1 = setPrerrequisitos.iterator();
		Asignatura asignaturaPrerrequisito, asignaturaSuperada;
		Boolean flag = false;
		while (it1.hasNext()) {
			asignaturaPrerrequisito = asignaturas.get(it1.next());
			Set<Integer> setAsignaturasSuperadas = alumnos.get(alumno).getAsignaturasSuperadas().keySet();
			Iterator<Integer> it0 = setAsignaturasSuperadas.iterator();
			if (setAsignaturasSuperadas.isEmpty())
				return false;
			while (it0.hasNext()) {
				asignaturaSuperada = asignaturas.get(it0.next());
				if (asignaturaPrerrequisito.getIdAsignatura().compareTo(asignaturaSuperada.getIdAsignatura()) == 0)
					flag = true;
				if (!(it0.hasNext()) && !(flag))
					return false;
			}
		}
		return true;
	}

	/**
	 * Comprueba si el grupo al que se quiere asociar un profesor ya tiene un profesor asignado
	 * @param persona DNI del profesor que se quiere asociar
	 * @param asignatura Siglas de la asignatura deseada
	 * @param tipoGrupo Tipo de grupo deseado
	 * @param idGrupo Identificador del grupo deseado
	 * @param asignaturas TreeMap de asignaturas.
	 * @param profesores TreeMap de profesores.
	 * @return true si ese grupo ya tiene un profesor asignado
	 */

	public Boolean grupoYaAsignado(String persona, String asignatura, String tipoGrupo, Integer idGrupo, TreeMap<Integer, Asignatura> asignaturas, TreeMap<String, Profesor> profesores) {
		Set<String> setProfesores = profesores.keySet();
		Iterator<String> it0 = setProfesores.iterator();
		while (it0.hasNext()) {
			Profesor profesor = profesores.get(it0.next());
			if (tipoGrupo.contentEquals("A")) {
				Set<Grupo> ks = profesor.getDocenciaImpartidaA().keySet();
				for (Grupo keyEmp : ks) {
					Grupo Grupo = profesor.getDocenciaImpartidaA().get(keyEmp);
					String aux = Grupo.toString();
					String[] campos = aux.split(" ");
					Integer idGrupo2 = Integer.parseInt(campos[4]);
					if (campos[2].compareTo(asignatura) == 0 && campos[3].compareTo(tipoGrupo) == 0 && idGrupo2 == idGrupo) {
						return true;
					}

				}
			} else if (tipoGrupo.contentEquals("B")) {
				Set<Grupo> ks = profesor.getDocenciaImpartidaB().keySet();

				for (Grupo keyEmp : ks) {
					Grupo Grupo = profesor.getDocenciaImpartidaB().get(keyEmp);
					String aux = Grupo.toString();
					String[] campos = aux.split(" ");
					Integer idGrupo2 = Integer.parseInt(campos[4]);
					if (campos[2].compareTo(asignatura) == 0 && campos[3].compareTo(tipoGrupo) == 0 && idGrupo2 == idGrupo) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Comprueba si al asignar un grupo a un profesor se sobrepasan las horas maximas de clase que este puede impartir
	 * @param persona DNI del profesor que se quiere asociar
	 * @param asignatura Siglas de la asignatura deseada
	 * @param tipoGrupo Tipo de grupo deseado
	 * @param idGrupo Identificador del grupo deseado
	 * @param profesores TreeMap de profesores.
	 * @param asignaturas TreeMap de asignaturas.
	 * @return true si se sobrepasan el numero maximo de horas que este profesor puede impartir
	 */

	public Boolean horasAsignablesSuperiorMaximo(String persona, String asignatura, String tipoGrupo, Integer idGrupo, TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {
		Integer maxHorasAsignables = profesores.get(persona).getHorasDocenciaAsignables();
		Set<Grupo> setDocenciaA = profesores.get(persona).getDocenciaImpartidaA().keySet();
		Iterator<Grupo> itA = setDocenciaA.iterator();
		Set<Grupo> setDocenciaB = profesores.get(persona).getDocenciaImpartidaB().keySet();
		Iterator<Grupo> itB = setDocenciaB.iterator();
		Integer horasAsignadas = 0;
		if (!setDocenciaA.isEmpty()) {
			while (itA.hasNext()) {
				Grupo grupo = profesores.get(persona).getDocenciaImpartidaA().get(itA.next());
				Integer duracion = grupo.getHoraFin() - grupo.getHoraInicio();
				horasAsignadas += duracion;
			}
		}
		if (!setDocenciaB.isEmpty()) {
			while (itB.hasNext()) {
				Grupo grupo = profesores.get(persona).getDocenciaImpartidaB().get(itB.next());
				Integer duracion = grupo.getHoraFin() - grupo.getHoraInicio();
				horasAsignadas += duracion;
			}
		}
		Integer key = siglasToID(asignaturas, asignatura);
		Integer duracionGrupo = 0;
		if (tipoGrupo.contentEquals("A")) {
			Grupo grupo = asignaturas.get(key).getGruposA().get(idGrupo);
			duracionGrupo = grupo.getHoraFin() - grupo.getHoraInicio();
		} else {
			Grupo grupo = asignaturas.get(key).getGruposB().get(idGrupo);
			duracionGrupo = grupo.getHoraFin() - grupo.getHoraInicio();
		}
		if ((duracionGrupo + horasAsignadas > maxHorasAsignables))
			return true;
		else
			return false;
	}

	/**
	 * Comprueba si el grupo al que se quiere asignar este profesor se solapa con alguna clase que ya este impartiendo
	 * @param persona DNI del profesor que se quiere asociar
	 * @param asignatura Siglas de la asignatura deseada
	 * @param tipoGrupo Tipo de grupo deseado
	 * @param idGrupo Identificador del grupo deseado
	 * @param profesores TreeMap de profesores.
	 * @param asignaturas TreeMap de asignaturas.
	 * @return true si se produce solape
	 */

	public Boolean generaSolape(String persona, String asignatura, String tipoGrupo, Integer idGrupo, TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {
		Integer key = siglasToID(asignaturas, asignatura);
		Integer horaInicio = 0;
		Integer horaFin = 0;
		String dia;
		if (tipoGrupo.contentEquals("A")) {
			Grupo grupo = asignaturas.get(key).getGruposA().get(idGrupo);
			horaInicio = grupo.getHoraInicio();// Hora de inicio y fin del grupo que se quiere asignar
			horaFin = grupo.getHoraFin();
			dia = grupo.getDia();
		} else {
			Grupo grupo = asignaturas.get(key).getGruposB().get(idGrupo);
			horaInicio = grupo.getHoraInicio();
			horaFin = grupo.getHoraFin();
			dia = grupo.getDia();
		}
		Set<Grupo> setGruposA = profesores.get(persona).getDocenciaImpartidaA().keySet(); // Comparar el grupo que se quiere asignar con los existentes
		Iterator<Grupo> itA = setGruposA.iterator();
		if (!setGruposA.isEmpty()) {
			Grupo grupoA;
			while (itA.hasNext()) {
				grupoA = profesores.get(persona).getDocenciaImpartidaA().get(itA.next());
				if (grupoA.getDia().contentEquals(dia)) {
					if (!((horaInicio < grupoA.getHoraInicio() && horaFin <= grupoA.getHoraInicio()) || (horaInicio >= grupoA.getHoraFin() && horaFin > grupoA.getHoraFin())))
						return true;
				}
			}
		}
		Set<Grupo> setGruposB = profesores.get(persona).getDocenciaImpartidaB().keySet();
		Iterator<Grupo> itB = setGruposB.iterator();
		if (!setGruposB.isEmpty()) {
			Grupo grupoB;
			while (itB.hasNext()) {
				grupoB = profesores.get(persona).getDocenciaImpartidaB().get(itB.next());
				if (grupoB.getDia().contentEquals(dia)) {
					if (!((horaInicio < grupoB.getHoraInicio() && horaFin <= grupoB.getHoraInicio()) || (horaInicio >= grupoB.getHoraFin() && horaFin > grupoB.getHoraFin())))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Comprueba si el grupo al que se quiere matricular a este alumno se solapa con alguna clase que ya este recibiendo
	 * @param persona DNI del alumno que se quiere matricular
	 * @param asignatura Siglas de la asignatura deseada
	 * @param tipoGrupo Tipo de grupo deseado
	 * @param idGrupo Identificador del grupo deseado
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 * @return true si se produce solape
	 */
	public Boolean generaSolapeAlumnos(String persona, String asignatura, String tipoGrupo, Integer idGrupo, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
		Integer key = siglasToID(asignaturas, asignatura);
		Integer horaInicio = 0;
		Integer horaFin = 0;
		String dia;
		if (tipoGrupo.contentEquals("A")) {
			Grupo grupo = asignaturas.get(key).getGruposA().get(idGrupo);
			horaInicio = grupo.getHoraInicio();// Hora de inicio y fin del grupo que se quiere asignar
			horaFin = grupo.getHoraFin();
			dia = grupo.getDia();
		} else {
			Grupo grupo = asignaturas.get(key).getGruposB().get(idGrupo);
			horaInicio = grupo.getHoraInicio();
			horaFin = grupo.getHoraFin();
			dia = grupo.getDia();
		}
		Set<Integer> setGruposA = alumnos.get(persona).getDocenciaRecibidaA().keySet(); // Comparar el grupo que se quiere asignar con los existentes
		Iterator<Integer> itA = setGruposA.iterator();
		if (!setGruposA.isEmpty()) {
			Grupo grupoA;
			while (itA.hasNext()) {
				grupoA = alumnos.get(persona).getDocenciaRecibidaA().get(itA.next());
				if (grupoA.getDia().contentEquals(dia)) {
					if (!((horaInicio < grupoA.getHoraInicio() && horaFin <= grupoA.getHoraInicio()) || (horaInicio >= grupoA.getHoraFin() && horaFin > grupoA.getHoraFin())))
						return true;
				}
			}
		}
		Set<Integer> setGruposB = alumnos.get(persona).getDocenciaRecibidaB().keySet();
		Iterator<Integer> itB = setGruposB.iterator();
		if (!setGruposB.isEmpty()) {
			Grupo grupoB;
			while (itB.hasNext()) {
				grupoB = alumnos.get(persona).getDocenciaRecibidaB().get(itB.next());
				if (grupoB.getDia().contentEquals(dia)) {
					if (!((horaInicio < grupoB.getHoraInicio() && horaFin <= grupoB.getHoraInicio()) || (horaInicio >= grupoB.getHoraFin() && horaFin > grupoB.getHoraFin())))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Comprueba si el alumno introducido tiene alguna asignatura evaluada
	 * @param alumnos TreeMap de alumnos.
	 * @param alumno DNI del alumno
	 * @return true si el alumno no tiene ninguna asignatura evaluada
	 */

	public Boolean expedienteVacio(TreeMap<String, Alumno> alumnos, String alumno) {
		if (alumnos.get(alumno).getAsignaturasSuperadas().isEmpty())
			return true;
		else
			return false;
	}

	/**
	 * Comprueba que la asignatura introducida ya haya sido evaluada en el curso academico introducido
	 * @param alumnos TreeMap de alumnos.
	 * @param asignaturas TreeMap de asignaturas.
	 * @param asignatura Siglas de la asignatura pedida
	 * @param cursoAcademico Curso en que se evalua con formato aa/aa. Ejemplo 15/16
	 * @return true si esa asignatura ya ha sido evaluada en ese curso academico
	 */

	public Boolean asignaturaYaEvaluada(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String asignatura, String cursoAcademico) {
		Set<String> setAlumnos = alumnos.keySet();
		Iterator<String> it = setAlumnos.iterator();
		NotaFinal asignaturaSuperada;
		Alumno alumno;
		while (it.hasNext()) {
			alumno = alumnos.get(it.next());
			Set<Integer> setAsignaturasSuperadas = alumno.getAsignaturasSuperadas().keySet();
			Iterator<Integer> it0 = setAsignaturasSuperadas.iterator();
			if (!setAsignaturasSuperadas.isEmpty()) {
				while (it0.hasNext()) {
					asignaturaSuperada = alumno.getAsignaturasSuperadas().get(it0.next());
					if (asignaturaSuperada.getAsignatura().getSiglas().contentEquals(asignatura) && asignaturaSuperada.getCursoAcademico().contentEquals(cursoAcademico))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metodo para escribir en el fichero "avisos.txt" los errores producidos en la funcion evaluarAsignatura();
	 * @param numeroLinea Numero de la linea donde se produjo el error
	 * @param info Descripcion del error producido
	 * @see evaluarAsignatura();
	 */

	public void guardarErrorFicheroNotas(Integer numeroLinea, String info) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("avisos.txt", true);
			pw = new PrintWriter(fichero);
			pw.println("Error en linea " + numeroLinea + ": " + info);
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
	 * Si el String introducido no termina por ".txt" se le concatena
	 * @param linea String que se quiere que termine por .txt
	 * @return linea + ".txt"
	 */

	public String correctoTXT(String linea) {
		if (linea.endsWith(".txt")) {
			return linea;
		} else {
			return linea.concat(".txt");
		}
	}

	/* CONSTRUCTORES */
	public Funcionalidades() {
	}
}