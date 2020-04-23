package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

public class Operador extends Usuario implements VOOperador {
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	

	/**
	 *  numero de RNT del operador.
	 */
	private int numeroRNT;
	
	/**
	 * fecha de vencimiento del RNT.
	 */
	private Date vencimientoRNT;
	
	/**
	 *  regsitro del super turismo del operador.
	 */
	private String registroSuperTurismo;
	
	/**
	 * fecha devencimiento del regsitro del super turismo del operador..
	 */
	private Date vencimientoRegistroSuperTurismo;
	
	/**
	 *  Categoria a la que pertenece el operador.
	 */
	private Modalidad categoria;
	
	/**
	 * Direccion del operador.
	 */
	private String direccion;
	
	/**
	 *  Hora de apertura.
	 */
	private Date horaApertura;
	
	/**
	 *  Hora de cierre.
	 */
	private Date horaCierre;
	
	/**
	 * Tiempo minimo de estadia.
	 */
	private int tiempoMinimo;
	
	/**
	 *  Ganancias actuales.
	 */
	private double gananciaAnioActual;
	
	/**
	 *  Ganancias del anio corrido.
	 */
	private double gananciAnioCorrido;
	
	/**
	 * Habitaciones del operador.
	 */
	private ArrayList habitaciones;
	
	/**
	 *  Apartamentos del operador.
	 */
	private ArrayList apartamentos;
	
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto.
	 */
	public Operador(){
		
		super("",null,0,null);
		numeroRNT = 0;
		vencimientoRNT = null;
		registroSuperTurismo = "";
		vencimientoRegistroSuperTurismo = null;
		categoria = null;
		direccion = "";
		horaApertura = null;
		horaCierre = null;
		tiempoMinimo = 1;
		gananciaAnioActual = 0;
		gananciAnioCorrido = 0;
		habitaciones = new ArrayList<>();
		apartamentos = new ArrayList<>();
	}
	
	/**
	 * Constructor con valores.
	 * @param id -  id del operador.
	 * @param numeroRNT -  numero de RNT.
	 * @param vencimientoRNT - fecha de vencimiento de RNT.
	 * @param registroSuperTurismo -  registro del super turismo del operador.
	 * @param vencimientoRegistroSuperTurismo fecha de vencimiento del registro de super turismo.
	 * @param categoria - categoria del operador.
	 * @param direccion -  direccion del operador.
	 * @param horaApertura - hora de apertura.
	 * @param horaCierre - hora de cierre.
	 * @param tiempoMinimo -  tiempo minimo (Mayor a 0)
	 * @param gananciaAnioActual - ganacia del anio actual.
	 * @param gananciAnioCorrido - ganacia anio corrido.
	 * @param habitaciones - habitaciones del operador.
	 * @param apartamentos - apartamentos del operador.
	 */
	public Operador(String logIn,TipoIdentificacion tipoId, long numeroId,TipoCliente relacionU, int numeroRNT, Date vencimientoRNT, String registroSuperTurismo,Date vencimientoRegistroSuperTurismo,Modalidad categoria, String direccion, 
			Date horaApertura, Date horaCierre, int tiempoMinimo, double gananciaAnioActual, double gananciAnioCorrido, ArrayList habitaciones, ArrayList apartamentos ){
		
		super(logIn, tipoId, numeroId, relacionU);
		this.numeroRNT = numeroRNT;
		this.vencimientoRNT = vencimientoRNT;
		this.registroSuperTurismo = registroSuperTurismo;
		this.vencimientoRegistroSuperTurismo = vencimientoRegistroSuperTurismo;
		this.categoria = categoria;
		this.direccion = direccion;
		this.horaApertura = horaApertura;
		this.horaCierre = horaCierre;
		this.tiempoMinimo = tiempoMinimo;
		this.gananciaAnioActual = gananciaAnioActual;
		this.gananciAnioCorrido = gananciAnioCorrido;
		this.habitaciones = habitaciones;
		this.apartamentos = apartamentos;
		
	}
	
	
	/**
	 * 
	 * @return numero de RNT.
	 */
	public int getNumeroRNT() {
		return numeroRNT;
	}

	/**
	 * 
	 * @param numeroRNT - nuevo numeroRNT del operador.
	 */
	public void setNumeroRNT(int numeroRNT) {
		this.numeroRNT = numeroRNT;
	}

	/**
	 * 
	 * @return vencimiento del RNT
	 */
	public Date getVencimientoRNT() {
		return vencimientoRNT;
	}

	/**
	 * 
	 * @param vencimientoRNT - nueva fecha de vencimiento del RNT del operador.
	 */
	public void setVencimientoRNT(Date vencimientoRNT) {
		this.vencimientoRNT = vencimientoRNT;
	}

	/**
	 * 
	 * @return registro del super turismo del operador.
	 */
	public String getRegistroSuperTurismo() {
		return registroSuperTurismo;
	}

	/**
	 * 
	 * @param registroSuperTurismo - nuevo registro del super turismo del operador.
	 */
	public void setRegistroSuperTurismo(String registroSuperTurismo) {
		this.registroSuperTurismo = registroSuperTurismo;
	}

	/**
	 * 
	 * @return fecha de vencimiento del super tutimo.
	 */
	public Date getVencimientoRegistroSuperTurismo() {
		return vencimientoRegistroSuperTurismo;
	}

	/**
	 * 
	 * @param vencimientoRegistroSuperTurismo - nueva fecha de vencimiento del super turismo.
	 */
	public void setVencimientoRegistroSuperTurismo(Date vencimientoRegistroSuperTurismo) {
		this.vencimientoRegistroSuperTurismo = vencimientoRegistroSuperTurismo;
	}

	/**
	 * 
	 * @return categoria del operador.
	 */
	public Modalidad getCategoria() {
		return categoria;
	}

	/**
	 * 
	 * @param categoria - nueva categoria del operador.
	 */
	public void setCategoria(Modalidad categoria) {
		this.categoria = categoria;
	}

	/**
	 * 
	 * @return direccion del operador.
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * 
	 * @param direccion - nueva direccion del operador.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * 
	 * @return hora de apertura.
	 */
	public Date getHoraApertura() {
		return horaApertura;
	}

	/**
	 * 
	 * @param horaApertura - nueva hora de apertura.
	 */
	public void setHoraApertura(Date horaApertura) {
		this.horaApertura = horaApertura;
	}

	/**
	 * 
	 * @return hora de cierre.
	 */
	public Date getHoraCierre() {
		return horaCierre;
	}

	/**
	 * 
	 * @param horaCierre - nueva hora de cierre.
	 */
	public void setHoraCierre(Date horaCierre) {
		this.horaCierre = horaCierre;
	}

	/**
	 * 
	 * @return tiempo minmo de estadia.
	 */
	public int getTiempoMinimo() {
		return tiempoMinimo;
	}

	/**
	 * 
	 * @param tiempoMinimo -  nuevo tiempo minimo.
	 */
	public void setTiempoMinimo(int tiempoMinimo) {
		this.tiempoMinimo = tiempoMinimo;
	}

	/**
	 * 
	 * @return ganacias del anio actual.
	 */
	public double getGananciaAnioActual() {
		return gananciaAnioActual;
	}

	/**
	 * 
	 * @param gananciaAnioActual - nuevas ganancias del anio actual.
	 */
	public void setGananciaAnioActual(double gananciaAnioActual) {
		this.gananciaAnioActual = gananciaAnioActual;
	}

	/**
	 * 
	 * @return ganacias anio de corrido.
	 */
	public double getGananciAnioCorrido() {
		return gananciAnioCorrido;
	}

	/**
	 * 
	 * @param gananciAnioCorrido - nuevas ganancias anio de corrido.
	 */
	public void setGananciAnioCorrido(double gananciAnioCorrido) {
		this.gananciAnioCorrido = gananciAnioCorrido;
	}

	/**
	 * 
	 * @return habitaciones del operador.
	 */
	public ArrayList getHabitaciones() {
		return habitaciones;
	}

	/**
	 * 
	 * @param habitaciones - nuevas habitaciones del operador.
	 */
	public void setHabitaciones(ArrayList habitaciones) {
		this.habitaciones = habitaciones;
	}

	/**
	 * 
	 * @return apartamentos del operador.
	 */
	public ArrayList getApartamentos() {
		return apartamentos;
	}

	/**
	 * 
	 * @param apartamentos - nuevos apartamentos del operador.
	 */
	public void setApartamentos(ArrayList apartamentos) {
		this.apartamentos = apartamentos;
	}
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador.
	 */
	public String toString() 
	{
		return "Operador [logIn="+super.getLogIn()+", tipo Id="+super.getTipoId()+", numero de Id="+super.getNumeroId()
		        +", relacion U="+super.getRelacioU()+ ", numero de RNT=" + numeroRNT + ", vencimiento de RNT=" + vencimientoRNT +
				", registro de super turismo="+ registroSuperTurismo+",vencimiento de super turismo="+ vencimientoRegistroSuperTurismo+
				",categoria="+categoria+",direccion= "+direccion+ ", hora de apertura="+ horaApertura+", hora de cierre="+horaCierre+
				",tiempo minimo="+tiempoMinimo+", ganacia del anio actual="+gananciaAnioActual+", ganancia de anio de corrido="+ gananciAnioCorrido+
				", habitaciones="+habitaciones+ ",apatamentos="+ apartamentos+"]";
	}

}
