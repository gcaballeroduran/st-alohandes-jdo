package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;

public class Propiedad implements VOPropiedad
{
	///////////////////////////////////////
	////////////// ATRIBUTOS //////////////
	///////////////////////////////////////

	/** El identificador ÚNICO de la propiedad */
	private long id;

	/** La capacidad en número de personas de la propiedad */
	private int capacidad;

	/** El tamaño en m2 de la propiedad */
	private double tamanio;

	/** El precio de alquiler de la propiedad por el mínimo de tiempo */
	private double precio;

	/** La fecha creación de la propiedad en la aplicación */
	private String fechaCreacion;

	/** Cantidad de Días en los que la propiedad ha estado reservada desde su fecha de creación */
	private int diasReservados;

	/** Piso en el que se encuentra ubicada la propiedad, si es una casa que se renta completa el piso será 0 */
	private int piso;


	/** Indica si la propiedad esta habilitada para ser reservada */
	private boolean habilitada;

	///////////////////////////////////////
	/////////// Constructor ///////////////
	///////////////////////////////////////

	/**
	 * Constructor por defecto
	 */
	public Propiedad()
	{
		id = 0;
		capacidad = 0;
		tamanio = 0;
		precio = 0;
		fechaCreacion = null;
		habilitada = false;

	}

	/**
	 * Constructor con valores
	 * @param pID identificador de la propiedad
	 * @param pCapacidad capacidad en numero de personas de la propiedad
	 * @param pTamanio tamaño de la propiedad en m2
	 * @param pPrecio precio por tiempo minimo de reserva de la propiedad
	 * @param pFecha fecha de registro de la propiedad en la aplicacion
	 * @param pDiasR dias que la propiedad ha estado reservada desde su registro en la aplicacion
	 * @param pPiso piso en el que se encuentra la propiedad
	 * @param pDireccion direccion en la que se encuentra la propiedad
	 */
	public Propiedad(long pID, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso)
	{
		id = pID;
		capacidad = pCapacidad;
		tamanio = pTamanio;
		precio = pPrecio;
		fechaCreacion = pFecha;
		diasReservados = pDiasR;
		piso = pPiso;
		habilitada = true;
	}

	///////////////////////////////////////
	////////////// GET / SET //////////////
	///////////////////////////////////////

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public double getTamanio() {
		return tamanio;
	}

	public void setTamanio(double tamanio) {
		this.tamanio = tamanio;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public int getDiasReservados() {
		return diasReservados;
	}

	public void setDiasReservados(int diasReservados) {
		this.diasReservados = diasReservados;
	}

	public int getPiso() {
		return piso;
	}

	public void setPiso(int piso) {
		this.piso = piso;
	}
	
	public boolean isHabilitada() {
		return habilitada;
	}

	public void setHabilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}
	
	
	///////////////////////////////////////
	//////////// Otros M�todos ////////////
	///////////////////////////////////////

	

	/**
	 * @param prop - La propiedad a alquilar
	 * @return True si tienen el mismo id
	 */
	public boolean equals(Object prop) 
	{
		Propiedad tb = (Propiedad) prop;
		return id == tb.id;
	}



}
