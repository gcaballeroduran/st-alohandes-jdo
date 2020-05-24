package uniandes.isis2304.alohandes.negocio;

import java.util.ArrayList;

public class Servicio implements VOServicio
{
	///////////////////////////////////////
	////////////// ATRIBUTOS //////////////
	///////////////////////////////////////

	/** El identificador ÚNICO del servicio */
	private long id;

	/** El tipo de servicio */
	private String tipo;

	/** El precio del servicio */
	private double precio;

	/** El intervalo en el que se paga este servicio, cada cuanto debe pagarse en días */
	private int intervalo_Pago;

	/** Lista de los apartamentos que tienen este servicio */
	private ArrayList<Apartamento> apartamentos;
	
	/** Lista de las habitaciones que tienen este servicio */
	private ArrayList<Habitacion> habitaciones;
	
	///////////////////////////////////////
	/////////// Constructor ///////////////
	///////////////////////////////////////

	/** Constructor por defecto */
	public Servicio()
	{
		id = 0;
		tipo = "";
		precio = 0;
		intervalo_Pago = 0;
		apartamentos = new ArrayList<Apartamento>();
		habitaciones = new ArrayList<Habitacion>();
	}

	/**
	 * Constructor con valores
	 * @param pID identificador del servicio
	 * @param pTipo tipo de servicio
	 * @param pPrecio precio del servicio
	 * @param pIntervalo intervalo de pago del servicio
	 */
	public Servicio(long pID, String pTipo, double pPrecio, int intervalo_Pago)
	{
		id = pID;
		tipo = pTipo;
		precio = pPrecio;
		this.intervalo_Pago = intervalo_Pago;
		apartamentos = new ArrayList<Apartamento>();
		habitaciones = new ArrayList<Habitacion>();
	}


	///////////////////////////////////////
	////////////// GET / SET //////////////
	///////////////////////////////////////


	public long getId() {
		return id;
	}

	public void setId(long pId) {
		this.id = pId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String pTipo) {
		this.tipo = pTipo;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double pPrecio) {
		this.precio = pPrecio;
	}

	public ArrayList<Apartamento> getApartamentos() {
		return apartamentos;
	}

	public void setApartamentos(ArrayList<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}

	public ArrayList<Habitacion> getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(ArrayList<Habitacion> habitaciones) {
		this.habitaciones = habitaciones;
	}

	public int getIntervalo_Pago() {
		return intervalo_Pago;
	}

	public void setIntervalo_Pago(int pIntervaloPago) {
		this.intervalo_Pago = pIntervaloPago;
	}

	///////////////////////////////////////
	//////////// Otros M�todos ////////////
	///////////////////////////////////////

	/**
	 * @param prop - El servicio de la propiedad
	 * @return True si tienen el mismo id
	 */
	public boolean equals(Object serv) 
	{
		Servicio tb = (Servicio) serv;
		return id == tb.id;
	}

	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del servicio
	 */
	public String toString() 
	{
		return "Servicio [id=" + id + ", tipo=" + tipo + ", precio=" + precio + 
				", intervaloPago=" + intervalo_Pago +
				", apartamentos:" +"]";
	}
	
	public String apartamentosToString()
	{
		String s = "";
		for( int i = 0; i<apartamentos.size(); i++)
		{
			s = s + "," + apartamentos.get(i).getId();
		}
		
		
		return s;
	}
	
	public String habitacionesToString()
	{
		String s = "";
		for( int i = 0; i<habitaciones.size(); i++)
		{
			s = s + "," + habitaciones.get(i).getId();
		}
		
		
		return s;
	}

}
