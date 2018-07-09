package utils;

import java.io.Serializable;

public class Formula implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String nombre;
	private String formula;
	private String descripcion;
	
	public Formula() {
		this.id= System.currentTimeMillis();
	}
	
	public Formula(String nombre, String formula, String descripcion) {
		this.id= System.currentTimeMillis();
		this.nombre = nombre;
		this.formula = formula;
		this.descripcion = descripcion;
	}
	public Formula(long id, String nombre, String formula, String descripcion) {
		this.id= id;
		this.nombre = nombre;
		this.formula = formula;
		this.descripcion = descripcion;
	}
	public long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
