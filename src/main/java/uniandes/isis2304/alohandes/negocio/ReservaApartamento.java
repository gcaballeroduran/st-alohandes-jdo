package uniandes.isis2304.alohandes.negocio;

public class ReservaApartamento implements VOReservaApartamento {
	
private long idReserva;
	
	private long idApartamento;
	
	/** Constructor por defecto */
	public  ReservaApartamento() {
		
		idReserva = 0;
		idApartamento = 0;
	}

	/**
	 * Constructor con valores
	 * @param idReserva identificador de la reserva
	 * @param idApartamento identificador del apartamento
	 */
	public ReservaApartamento(long idReserva, long idApartamento)
	{
		this.idReserva = idReserva;
		this.idApartamento = idApartamento;
	}

	@Override
	public long getIdApartamento() {
		return idApartamento;
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
		return "ReservaHabitacion [idApartamento=" + idApartamento + ", idReserva=" + idReserva +"]";
	}


}
