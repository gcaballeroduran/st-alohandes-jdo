package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

public class ReservaColectiva implements VOReservaColectiva{

	///////////////////////////////////////
	////////////// ATRIBUTOS //////////////
	///////////////////////////////////////


	/** Identificador de la reserva colectiva */
	private long id;

	/** Cantidad de propiedades que se quieren reservar colectivamente */
	private int cantidad;

	/** Tipo de alojamiento que se quiere reservar */
	private String tipoAlojamiento;

	/** Fecha de inicio de la reserva */
	private String fechaInicio;

	/** Días que dura la reserva  */
	private int duracion;

	/** Lista de reservas asociada de la esta reserva colectiva */
	private ArrayList<Reserva> reservas;



	///////////////////////////////////////
	///////////// CONSTRUCTOR /////////////
	///////////////////////////////////////

	/** Constructor por defecto */
	public ReservaColectiva()
	{
		id = -1;
		cantidad = -1;
		tipoAlojamiento = null;
		fechaInicio = null;
		duracion = -1;
		reservas = null;

	}

	/** Constructor con informacion por parametro*/
	public ReservaColectiva(long pId, int pCantidad, String pTipo, String pInicio, int pDuracion)
	{
		id = pId;
		cantidad = pCantidad;
		tipoAlojamiento = pTipo;
		fechaInicio = pInicio;
		duracion = pDuracion;
		reservas = null;
	}

	///////////////////////////////////////
	////////////// SET / GET //////////////
	///////////////////////////////////////

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getTipoAlojamiento() {
		return tipoAlojamiento;
	}

	public void setTipoAlojamiento(String tipoAlojamiento) {
		this.tipoAlojamiento = tipoAlojamiento;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	///////////////////////////////////////
	/////////// Otros Métodos /////////////
	///////////////////////////////////////
	
	public ArrayList<Reserva> agendarReservas()
	{
		return null;
		
	}
	
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del Apartamento
	 */
	public String toString() 
	{
		return "ReservaColectiva [id=" + this.getId() + ", cantidad="+ this.getCantidad() +
				", tipoAlojamiento="+ this.getTipoAlojamiento() + ", fechaInicio="+ 
				this.getFechaInicio() + ", duracion=" + this.getDuracion() +
				", reservas=" + reservasToString() + "]";
	}
	
	public String reservasToString()
	{
		String s = "";
		for( int i = 0; i<reservas.size(); i++)
		{
			s = s + "," + reservas.get(i).getId();
		}
		
		
		return s;
	}
	

}
