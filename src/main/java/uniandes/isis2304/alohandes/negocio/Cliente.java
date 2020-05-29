package uniandes.isis2304.alohandes.negocio;

public class Cliente  extends Usuario implements VOCliente {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	
	/**
	 * Medio de pago del cliente.
	 */
	private String medio_Pago;
	
	/**
	 * Numero de reservas que tiene un cliente.
	 */
	private int reservas;
	
	private String id ;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor por defecto.
	 */
	public Cliente(){
		
		super(0,null, "", null);
		this.medio_Pago = "";
		this.reservas = 0;
		this.id = Long.toString(super.getNumeroId());
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
	public Cliente (String logIn,String tipoId, String numeroId,String relacionU, String medio_Pago, int reservas){
		
		super(Integer.parseInt(numeroId), tipoId, logIn, relacionU);
		this.medio_Pago = medio_Pago;
		this.reservas = reservas;
		this.id = numeroId;
	}
	
	/**
	 * 
	 * @return medio de pago del cliente.
	 */
	public String getMedio_Pago(){
		return medio_Pago;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @param medioPago - nuevo medio de pago del cliente.
	 */
	public void setMedio_Pago(String medio_Pago){
		
		this.medio_Pago = medio_Pago;
		
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
		return "Cliente [logIn="+super.getLogIn()+", tipo Id="+super.getTipoId()+", numero de Id="+getId()
		+", relacion U="+super.getRelacioU()+", medio de pago=" + medio_Pago + ", reservas=" + reservas +  "]";
	}
	
	
	
	
}

