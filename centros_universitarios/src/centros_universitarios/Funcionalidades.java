package centros_universitarios;

public class Funcionalidades { // Esta clase contendra las funcionalidades que aparecen explicados en las especiicaciones del proyecto.

	
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
	public String start() { // HERRAMIENTA. Metodo de prueba para comprobar que la composicion de la clase Gestion y Funcionalidades funciona.
		return "---Start---";
	}

	
	/* CONSTRUCTORES */
	public Funcionalidades() {
	};
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