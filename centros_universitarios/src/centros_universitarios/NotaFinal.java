package centros_universitarios;

public class NotaFinal  implements Comparable<NotaFinal>{

	/* ATRIBUTOS */
	private Integer idAsignatura;
	private String cursoAcademico; 
	private Float nota;
	private Asignatura asignatura;


	/*METODOS */
	@Override
	public int compareTo(NotaFinal notaFinalComparada) {
		if(this.getAsignatura().getCurso().compareTo(notaFinalComparada.getAsignatura().getCurso())==0){
			return this.getAsignatura().getSiglas().compareTo(notaFinalComparada.getAsignatura().getSiglas());
		}
		else return this.getAsignatura().getCurso().compareTo(notaFinalComparada.getAsignatura().getCurso());
	}
@Override
public String toString() {
return this.asignatura.getCurso()+"; "+this.asignatura.getNombre()+"; "+this.getNota()+"; "+this.getCursoAcademico();	
}
	
	/* GETTERS & SETTERS */
	public String getCursoAcademico() {
		return cursoAcademico;
	}
	public void setCursoAcademico(String cursoAcademico) {
		this.cursoAcademico = cursoAcademico;
	}

	public Float getNota() {
		return nota;
	}
	public void setNota(Float nota) {
		this.nota = nota;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}
	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}
	
	
	public Integer getIdAsignatura() {
		return idAsignatura;
	}
	public void setIdAsignatura(Integer idAsignatura) {
		this.idAsignatura = idAsignatura;
	}
	
	
	/* CONSTRUCTORES */
	public NotaFinal(Integer idAsignatura, String cursoAcademico, Float nota, Asignatura asignatura) {
		this.idAsignatura= idAsignatura;
		this.cursoAcademico=cursoAcademico;
		this.nota=nota;
		this.asignatura = asignatura;
	}
	


}
