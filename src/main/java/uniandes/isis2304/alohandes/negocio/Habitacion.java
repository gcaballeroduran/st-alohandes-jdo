package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class Habitacion extends Propiedad implements VOHabitacion
{
	///////////////////////////////////////
	////////////// ATRIBUTOS //////////////
	///////////////////////////////////////

	/** True si la habitación es individual false si la habitación es compartida*/
	private boolean individual;

	/** Ruta de la imagen con el esquema de la habitación */
	private String esquema;

	/** Tipo de habitación definido por las constantes */
	private int tipo;
	
	/** Operador encargado de la habitación */
	private long operador;
	
	/** Servicios que tiene la habitación */
	private ArrayList<Servicio> servicios;
	
	/** Conjunto de las reservas hechas en la habitación */
	private ArrayList<Reserva> reservas;

	///////////////////////////////////////
	////////////// CONSTANTES /////////////
	///////////////////////////////////////

	/** Constantes para definir El tipo de habitación*/
	public static final int ESTANDAR = 0;

	public static final int SEMISUIT = 1;
	
	public static final int SUIT = 2;

	///////////////////////////////////////
	////////////// CONSTRUCTOR ////////////
	///////////////////////////////////////

	/**
	 * 
	 * @param pIndiv
	 * @param pEsquema
	 * @param pTipo
	 */
	public Habitacion(long pId, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso,  boolean pIndiv, String pEsquema, int pTipo, long pOperador)
	{
		super(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso);
		individual = pIndiv;
		esquema = pEsquema;
		tipo = pTipo;
		operador = pOperador;
		reservas = new ArrayList<Reserva>();
		servicios = new ArrayList<Servicio>();
	}

	///////////////////////////////////////
	/////////////// GET/SET ///////////////
	///////////////////////////////////////

	public boolean isIndividual() {
		return individual;
	}



	public void setIndividual(boolean individual) {
		this.individual = individual;
	}



	public String getEsquema() {
		return esquema;
	}



	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}



	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public long getOperador() {
		return operador;
	}

	public void setOperador(long operador) {
		this.operador = operador;
	}
	
	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<Servicio> servicios) {
		this.servicios = servicios;
	}
	
	public ArrayList<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(ArrayList<Reserva> reservas) {
		this.reservas = reservas;
	}

	///////////////////////////////////////
	//////////// Otros Métodos ////////////
	///////////////////////////////////////
	
	
	public void setHabilitada(boolean pHabilitada)
	{
			super.setHabilitada(pHabilitada);	
		
	}


	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos de la Habitación
	 */
	public String toString() 
	{
		return "Habitacion [id=" + super.getId() + ", capacidad="+ super.getCapacidad() +
				", tamanio="+super.getTamanio() + ", precio="+ super.getPrecio() + ", fechaCreacion=" + super.getFechaCreacion()+
				", diasReservados=" + super.getDiasReservados() + ", piso=" + super.getPiso()  + 
				", habilitada= "+ boolToInt(super.isHabilitada())+", individual=" + boolToInt(individual) + ", esquema="+ esquema + ", tipo="+ tipo + ", operador="+ operador + 
				", reservas=" + reservasToString() + ", servicios=" + serviciosToString() +"]";
	}
	
	public int boolToInt(boolean p)
	{
		if(p)
			return 1;
		else 
			return 0;
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

	public String serviciosToString()
	{
		String s = "";
		for( int i = 0; i<servicios.size(); i++)
		{
			s = s + "," + servicios.get(i).getId();
		}
		
		
		return s;
	}
}
