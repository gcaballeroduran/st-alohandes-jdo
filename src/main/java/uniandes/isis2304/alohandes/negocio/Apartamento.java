package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

public class Apartamento extends Propiedad implements VOApartamento 
{

	///////////////////////////////////////
	////////////// ATRIBUTOS //////////////
	///////////////////////////////////////


	/** Si esta amoblado es true, de lo contrario es false */
	private boolean amueblado;

	/** Indica el numero de habitaciones que tiene el apartamento */
	private int habitaciones;

	/** Describe el menaje con el que cuenta el apartamento */
	private String descripcionMenaje;

	/** Fecha de vencimiento del seguro */
	private int vencimientoSeguro;

	/** Describe el seguro con el que cuenta el apartamento */
	private String descripcionSeguro;
	
	/** Servicios que tiene el apartamento */
	private ArrayList<Servicio> servicios;
	
	/** Conjunto de las reservas hechas en la habitación */
	private ArrayList<Reserva> reservas;
	
	/** id del Operador encargado del apartamento */
	private long operador;

	///////////////////////////////////////
	///////////// CONSTRUCTOR /////////////
	///////////////////////////////////////

	/** Constructor por defecto */
	public Apartamento()
	{
		super(-1, -1, -1, -1, null, -1, -1);
		amueblado = false;
		habitaciones = 0;
		descripcionMenaje = "";
		vencimientoSeguro = 0;
		descripcionSeguro = null;
		operador = -1;
		reservas = new ArrayList<Reserva>();
		servicios = new ArrayList<Servicio>();

	}

	/**
	 * 
	 * @param pID
	 * @param pCapacidad
	 * @param pTamanio
	 * @param pPrecio
	 * @param pFecha
	 * @param pDiasR
	 * @param pPiso
	 * @param pDireccion
	 * @param pAmueblado
	 * @param pHabitaciones
	 * @param pDMenaje
	 * @param pVenceSeguro
	 * @param pDSeguro
	 */

	public Apartamento(long pID, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso, boolean pAmueblado, int pHabitaciones, String pDMenaje, int pVenceSeguro, String pDSeguro, long pOperador)

	{
		super(pID, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso);
		amueblado = pAmueblado;
		habitaciones = pHabitaciones;
		descripcionMenaje = pDMenaje;
		vencimientoSeguro = pVenceSeguro;
		descripcionSeguro = pDSeguro;
		operador = pOperador;
		reservas = new ArrayList<Reserva>();
		servicios = new ArrayList<Servicio>();
	}


	///////////////////////////////////////
	////////////// GET / SET //////////////
	///////////////////////////////////////

	public boolean isAmueblado() {
		return amueblado;
	}

	public void setAmueblado(boolean amueblado) {
		this.amueblado = amueblado;
	}

	public int getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(int habitaciones) {
		this.habitaciones = habitaciones;
	}

	public String getDescripcionMenaje() {
		return descripcionMenaje;
	}

	public void setDescripcionMenaje(String descripcionMenaje) {
		this.descripcionMenaje = descripcionMenaje;
	}

	public int getTiene_Seguro() {
		return vencimientoSeguro;
	}

	public void setTiene_Seguro(int vencimientoSeguro) {
		this.vencimientoSeguro = vencimientoSeguro;
	}

	public String getDescripcionSeguro() {
		return descripcionSeguro;
	}

	public void setDescripcionSeguro(String descripcionSeguro) {
		this.descripcionSeguro = descripcionSeguro;
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


	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del Apartamento
	 */
	public String toString() 
	{
		return "Apartamento [id=" + super.getId() + ", capacidad="+ super.getCapacidad() +
				", tamanio="+super.getTamanio() + ", precio="+ super.getPrecio() + ", fechaCreacion=" + super.getFechaCreacion()+
				", diasReservados=" + super.getDiasReservados() + ", piso=" + super.getPiso()  + 
				", habilitada=" + boolToInt(super.isHabilitada()) +", amueblado=" + boolToInt(amueblado) + ", habitaciones="+ habitaciones + ", descripcionMenaje=" + descripcionMenaje +
				" , vencimientoSeguro=" + vencimientoSeguro + ", descripcionSeguro="+ descripcionSeguro + 
				", operador="+ operador + ", reservas=" + reservasToString() + ", servicios=" + serviciosToString() + "]";
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
