package uniandes.isis2304.alohandes.negocio;


public class ReservaHabitacion implements VOReservaHabitacion {

	private long idReserva;
	
	private long idHabitacion;
	
	/** Constructor por defecto */
	public  ReservaHabitacion() {
		
		idReserva = 0;
		idHabitacion = 0;
	}

	/**
	 * Constructor con valores
	 * @param idReserva identificador de la reserva
	 * @param idHabitacion identificador de la habitacion
	 */
	public ReservaHabitacion(long idReserva, long idHabitacion)
	{
		this.idReserva = idReserva;
		this.idHabitacion = idHabitacion;
	}

	@Override
	public long getIdHabitacion() {
		return idHabitacion;
	}
	@Override
	public long getIdReserva() {
		return idReserva;
	}
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos.
	 */
	public String toString() 
	{
		return "ReservaHabitacion [idHabitacion=" + idHabitacion + ", idReserva=" + idReserva +"]";
	}

}
