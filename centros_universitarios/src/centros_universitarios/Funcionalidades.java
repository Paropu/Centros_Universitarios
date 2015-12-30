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

public class Funcionalidades { // Esta clase contendra las funcionalidades que aparecen explicados en las especificaciones del proyecto y el control de errores.

	/*
	 * FUNCIONALIDADES INCLUIDAS EN LAS ESPECIFICACIONES DEL PROYECTO:
	
	 * Insertar persona
	 * Asignar coordinador
	 * Asignar carga docente
	 * Matricular alumn
	 * Asignar grupo
	 * Evaluar asignatura
	 * Obtener expediente del alumno
	 * Obtener calendario del profesor
	 */

	/* METODOS */
	// ===== Funcionalidades =====
	public void insertarPersona(String linea, TreeMap<String, Profesor> profesores, TreeMap<String, Alumno> alumnos) { // falta a�adir treemaps
		String[] lineaDesplegadaEspacios = linea.split(" ");
		String[] lineaDesplegadaComillas = linea.split("\"");
		String[] lineaDesplegadaBarras = linea.split("/");

		if ((lineaDesplegadaEspacios[1].compareTo("alumno") == 0 && lineaDesplegadaComillas.length != 5)
				|| (lineaDesplegadaEspacios[1].compareTo("profesor") == 0 && lineaDesplegadaComillas.length != 7)) {
			guardarError("IP", "Numero de argumentos incorrecto");
			return;
		}
		if ((lineaDesplegadaEspacios[1].compareTo("alumno") == 0 && lineaDesplegadaBarras.length != 5)
				|| (lineaDesplegadaEspacios[1].compareTo("profesor") == 0 && lineaDesplegadaBarras.length != 3)) {
			guardarError("IP", "Numero de argumentos incorrecto");
			return;
		}

		// DATOS COMUNES
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

		// DATOS ALUMNO
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

			Alumno alumno = new Alumno(dni, nombre, apellidos, fechaNacimiento, fechaIngreso);
			alumno.setAsignaturasSinGrupo(new TreeMap<Integer, Asignatura>());
			alumnos.put(dni, alumno);
		}

		// DATOS PROFESOR
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

			Profesor profesor = new Profesor(dni, nombre, apellidos, fechaNacimiento, categoria, departamento, horasDocenciaAsignables);
			profesores.put(dni, profesor);
		}
	}

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
				profesores.get(persona).getAsignaturasCoordinadas().put(asignaturas.get(idAsignatura).getIdAsignatura(),
						asignaturas.get(idAsignatura));
			}
		}

	}

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
			guardarError("EVALUA", "Asignatura ya evaluada en este curso académico");
			return;
		}
		Scanner entrada = new Scanner(flujo_entrada);
		String linea, lineaSinEspaciosDuplicados;
		Integer numeroLinea = 0;

		while (entrada.hasNext()) {
			linea = entrada.nextLine();
			lineaSinEspaciosDuplicados = linea.replaceAll("\\s+", " "); // Contiene la informaci�n del fichero sin espacios duplicados
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
					alumnos.get(alumno).getAsignaturasSuperadas().put(siglasToID(asignaturas, asignatura),
							new NotaFinal(siglasToID(asignaturas, asignatura), cursoAcademico, notaTotal,
									asignaturas.get(siglasToID(asignaturas, asignatura))));
				}
				alumnos.get(alumno).getAsignaturasMatriculadas().remove(siglasToID(asignaturas, asignatura));
				Set<Integer> setAsignaturasSinGrupo = alumnos.get(alumno).getAsignaturasSinGrupo().keySet();
				Iterator<Integer> it0 = setAsignaturasSinGrupo.iterator();
				if (!setAsignaturasSinGrupo.isEmpty()) {
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
						if (alumnos.get(alumno).getDocenciaRecibidaA().get(idGrupoA).getAsignatura().getIdAsignatura()
								.compareTo(siglasToID(asignaturas, asignatura)) == 0)
							alumnos.get(alumno).getDocenciaRecibidaA().remove(idGrupoA);
					}
				}
				Set<Integer> setGruposB = alumnos.get(alumno).getDocenciaRecibidaB().keySet();
				Iterator<Integer> itB = setGruposB.iterator();
				if (!setGruposB.isEmpty()) {
					Integer idGrupoB;
					while (itB.hasNext()) {
						idGrupoB = itB.next();
						if (alumnos.get(alumno).getDocenciaRecibidaB().get(idGrupoB).getAsignatura().getIdAsignatura()
								.compareTo(siglasToID(asignaturas, asignatura)) == 0)
							alumnos.get(alumno).getDocenciaRecibidaB().remove(idGrupoB);
					}
				}
			}
		}
		entrada.close();
	}

	public void obtenerExpedienteAlumno(String linea, TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {

		String[] campos = linea.split(" ");
		String alumno = campos[1];
		String salida = campos[2];

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

	public void obtenerCalendarioProfesor(String profesor, String ficheroSalida, TreeMap<String, Profesor> profesores,
			TreeMap<Integer, Asignatura> asignaturas) {
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
			fichero = new FileWriter(ficheroSalida, true);
			pw = new PrintWriter(fichero);
			pw.println("Dia;\tHora;\tAsignatura;\tTipo grupo;\tId grupo");
			// Todas las horas se a�adir�n a este TreeMap
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

				// Sumo 20 unidades por dia para que el TreeMap est� ordenado
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
					System.out.println(ordenar.get(i).getAsignatura().getSiglas().length());
					if (ordenar.get(i).getAsignatura().getSiglas().length() > 6) {
						pw.println(ordenar.get(i));
					} else if (ordenar.get(i).getAsignatura().getSiglas().length() > 2) {
						pw.println(ordenar.get(i).toString2());
					} else if (ordenar.get(i).getAsignatura().getSiglas().length() < 3) {
						pw.println(ordenar.get(i).toString3());
					}
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
				NotasMediasMap.put(notaMedia + " " + alumnos.get(alumno).getApellidos() + " " + alumnos.get(alumno).getNombre(),
						alumnoAlumno);
			}
		}
		// Escribir en fichero
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(ficheroSalida, true);
			pw = new PrintWriter(fichero);
			Set<String> setNotas = NotasMediasMap.keySet();
			Iterator<String> it3 = setNotas.iterator();
			while (it3.hasNext()) {
				String key = it3.next();
				pw.println(NotasMediasMap.get(key).getApellidos() + " " + NotasMediasMap.get(key).getNombre() + " "
						+ NotasMediasMap.get(key).getDni() + " " + NotasMediasMap.get(key).getNotaMedia());
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

	// ===== Control de errores =====
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

	public void comandoIncorrecto(String comando) {
		guardarError("", "Comando incorrecto: " + comando);
	}

	public void argumentosIncorrectos(String comando) {
		guardarError(comando, "Numero de argumentos incorrecto");
	}

	public Boolean profesorTitular(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.get(dni).getCategoria().contains("titular"))
			return true;
		else
			return false;
	}

	public Boolean existeAlumno(TreeMap<String, Alumno> alumnos, String dni) {
		if (alumnos.containsKey(dni))
			return true;
		else
			return false;
	}

	public Boolean existeProfesor(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.containsKey(dni))
			return true;
		else
			return false;
	}

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

	public Boolean coordinadorDosMaterias(TreeMap<String, Profesor> profesores, String dni) {
		if (profesores.get(dni).getAsignaturasCoordinadas().size() == 2)
			return true;
		else
			return false;
	}

	public static GregorianCalendar stringToCalendar(String fechaEntrada) {
		String[] aux = fechaEntrada.split("/");
		GregorianCalendar fecha = new GregorianCalendar(Integer.parseInt(aux[2]), Integer.parseInt(aux[1]) - 1, Integer.parseInt(aux[0]));
		return fecha;
	}

	public static boolean validarFecha(GregorianCalendar fecha) {
		Calendar fechaMinima = new GregorianCalendar(1950, 0, 1);
		Calendar fechaMaxima = new GregorianCalendar(2020, 0, 1);
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
		fechaInscripcion.add(Calendar.DAY_OF_YEAR, 1);
		if (n_years < 15 || n_years > 65) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean validarDNI(String dni) {
		// COMPRUEBO SI EL TAMANO ES CORRECTO
		if (dni.length() != 9)
			return false;

		// COMPRUEBO SI TIENE 8 NUMEROS
		String numero = dni.substring(0, 8);
		try {
			Integer.parseInt(numero);
		} catch (NumberFormatException e) {
			return false;
		}

		// COMPRUEBO QUE SEA UNA LETRA MAYUS
		char letra = dni.charAt(8);
		if (letra < 65 || letra > 90)
			return false;

		return true;
	}

	public Boolean matriculaExistente(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String alumno,
			String asignatura) {
		Set<Integer> setAsignaturasMatriculadas = alumnos.get(alumno).getAsignaturasMatriculadas().keySet();
		Iterator<Integer> it = setAsignaturasMatriculadas.iterator();
		while (it.hasNext()) {
			Asignatura asignaturaMatriculada = asignaturas.get(it.next());
			if (asignaturaMatriculada.getSiglas().contentEquals(asignatura))
				return true;
		}
		return false;
	}

	public String nombreSinEspacios(String[] entrada) {
		String nombre = null;
		int i = 0;
		for (; nombre == null; i++) {
			if (entrada[i].length() > 0) {
				nombre = entrada[i];
			}
		}
		for (int j = entrada.length; i != j; i++) {
			if (entrada[i].length() > 0) {
				nombre = nombre + " " + entrada[i];
			}
		}
		return nombre;
	}

	public Boolean existeGrupo(TreeMap<Integer, Asignatura> asignaturas, Integer grupo, String tipoGrupo, String siglas) {
		// Busco el ID de la asignatura usando las siglas
		Integer key = siglasToID(asignaturas, siglas); // EN ESTE INTEGER QUEDA EL ID DE LA ASIGNATURA A LA QUE PERTENECEN LAS INICIALES

		// Busco si el treemap GruposX contiene a "grupo"
		if (tipoGrupo.contains("A")) {
			if (!asignaturas.get(key).getGruposA().containsKey(grupo)) {
				// System.out.println("No existe A");
				return false;
			}
		} else if (tipoGrupo.contains("B")) {
			if (!asignaturas.get(key).getGruposB().containsKey(grupo)) {
				// System.out.println("No existe B");
				return false;
			}
		}
		return true;
	}

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

	public Boolean cumplePrerrequisitos(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String alumno,
			String asignatura) {
		Integer key = siglasToID(asignaturas, asignatura);
		Set<Integer> setPrerrequisitos = asignaturas.get(key).getPrerrequisitos().keySet();// Asignatura
		Iterator<Integer> it1 = setPrerrequisitos.iterator();
		Asignatura asignaturaPrerrequisito, asignaturaSuperada;
		Boolean flag = false;
		while (it1.hasNext()) {
			asignaturaPrerrequisito = asignaturas.get(it1.next());
			Set<Integer> setAsignaturasSuperadas = alumnos.get(alumno).getAsignaturasSuperadas().keySet();
			Iterator<Integer> it0 = setAsignaturasSuperadas.iterator();
			while (it0.hasNext()) {
				asignaturaSuperada = asignaturas.get(it0.next());
				if (asignaturaPrerrequisito.getIdAsignatura() == asignaturaSuperada.getIdAsignatura())
					flag = true;
				if (!(it0.hasNext()) && !(flag))
					return false;
			}
		}
		return true;
	}

	public Boolean grupoYaAsignado(String persona, String asignatura, String tipoGrupo, Integer idGrupo,
			TreeMap<Integer, Asignatura> asignaturas, TreeMap<String, Profesor> profesores) {
		Set<String> setProfesores = profesores.keySet();
		Iterator<String> it0 = setProfesores.iterator();
		while (it0.hasNext()) {
			Profesor profesor = profesores.get(it0.next());
			if (tipoGrupo.contentEquals("A")) {
				if (profesor.getDocenciaImpartidaA().containsKey(idGrupo)
						&& profesor.getDocenciaImpartidaA().get(idGrupo).getAsignatura().getSiglas().contentEquals(asignatura))
					return true;
			} else if (profesor.getDocenciaImpartidaB().containsKey(idGrupo)
					&& profesor.getDocenciaImpartidaB().get(idGrupo).getAsignatura().getSiglas().contentEquals(asignatura))
				return true;
		}
		return false;
	}

	public Boolean horasAsignablesSuperiorMaximo(String persona, String asignatura, String tipoGrupo, Integer idGrupo,
			TreeMap<String, Profesor> profesores, TreeMap<Integer, Asignatura> asignaturas) {
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
		if (tipoGrupo == "A") {
			Grupo grupo = asignaturas.get(key).getGruposA().get(idGrupo);
			duracionGrupo = grupo.getHoraFin() - grupo.getHoraInicio();
		} else {
			Grupo grupo = asignaturas.get(key).getGruposA().get(idGrupo);
			duracionGrupo = grupo.getHoraFin() - grupo.getHoraInicio();
		}
		if ((duracionGrupo + horasAsignadas > maxHorasAsignables))
			return true;
		else
			return false;
	}

	public Boolean generaSolape(String persona, String asignatura, String tipoGrupo, Integer idGrupo, TreeMap<String, Profesor> profesores,
			TreeMap<Integer, Asignatura> asignaturas) {
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
					if (!((horaInicio < grupoA.getHoraInicio() && horaFin <= grupoA.getHoraInicio())
							|| (horaInicio >= grupoA.getHoraFin() && horaFin > grupoA.getHoraFin())))
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
					if (!((horaInicio < grupoB.getHoraInicio() && horaFin <= grupoB.getHoraInicio())
							|| (horaInicio >= grupoB.getHoraFin() && horaFin > grupoB.getHoraFin())))
						return true;
				}
			}
		}
		return false;
	}

	public Boolean generaSolapeAlumnos(String persona, String asignatura, String tipoGrupo, Integer idGrupo,
			TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas) {
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
					if (!((horaInicio < grupoA.getHoraInicio() && horaFin <= grupoA.getHoraInicio())
							|| (horaInicio >= grupoA.getHoraFin() && horaFin > grupoA.getHoraFin())))
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
					if (!((horaInicio < grupoB.getHoraInicio() && horaFin <= grupoB.getHoraInicio())
							|| (horaInicio >= grupoB.getHoraFin() && horaFin > grupoB.getHoraFin())))
						return true;
				}
			}
		}
		return false;
	}

	public Boolean expedienteVacio(TreeMap<String, Alumno> alumnos, String alumno) {
		if (alumnos.get(alumno).getAsignaturasSuperadas().isEmpty())
			return true;
		else
			return false;
	}

	public Boolean asignaturaYaEvaluada(TreeMap<String, Alumno> alumnos, TreeMap<Integer, Asignatura> asignaturas, String asignatura,
			String cursoAcademico) {
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
					if (asignaturaSuperada.getAsignatura().getSiglas().contentEquals(asignatura)
							&& asignaturaSuperada.getCursoAcademico().contentEquals(cursoAcademico))
						return true;
				}
			}
		}
		return false;
	}

	public void guardarErrorFicheroNotas(Integer numeroLinea, String info) { // Metodo que permite la escritura en el fichero "avisos.txt".
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

	/* CONSTRUCTORES */
	public Funcionalidades() {
	}
}