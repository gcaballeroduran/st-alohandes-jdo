package uniandes.isis2304.alohandes.negocio;

public class Cliente  extends Usuario implements VOCliente {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	
	/**
	 * Medio de pago del cliente.
	 */
	private String medioPago;
	
	/**
	 * Numero de reservas que tiene un cliente.
	 */
	private int reservas;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor por defecto.
	 */
	public Cliente(){
		
		super("",null,0,null);
		this.medioPago = "";
		this.reservas = 0;
	}
	
	/**
	  * Constructor con valores. 
	 * @param logIn - logIn del cliente
	 * @param tipoId - tipo de Id
	 * @param numeroId - numero del Id
	 * @param relacionU - relacion con la universidad
	 * @param id - id del cliente.
	 * @param medioPago - medio de pago del cliente.
	 * @param reservas - reservas del cliente.(Mayor o igual a 0)
	 */
	public Cliente (String logIn,String tipoId, long numeroId,String relacionU, String medioPago, int reservas){
		
		super(logIn, tipoId, numeroId, relacionU);
		this.medioPago = medioPago;
		this.reservas = reservas;
	}
	
	/**
	 * 
	 * @return medio de pago del cliente.
	 */
	public String getMedioPago(){
		return medioPago;
	}
	
	/**
	 * 
	 * @param medioPago - nuevo medio de pago del cliente.
	 */
	public void setMedioPago(String medioPago){
		
		this.medioPago = medioPago;
		
	}
	/**
	 * 
	 * @return numero de reservas del cliente.
	 */
	public int getReservas(){
		return reservas;
	}
	
	/**
	 * 
	 * @param reservas- nuevo numero de reservas del cliente.
	 */
	public void setReservas(int reservas){
		
		this.reservas = reservas;
	}
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del cliente.
	 */
	public String toString() 
	{
		return "Cliente [logIn="+super.getLogIn()+", tipo Id="+super.getTipoId()+", numero de Id="+super.getNumeroId()
		+", relacion U="+super.getRelacioU()+", medio de pago=" + medioPago + ", reservas=" + reservas +  "]";
	}
	
	
	
	
}

