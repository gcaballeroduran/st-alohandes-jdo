package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Interfaz para los métodos get de operador.
 */
public interface VOOperador {

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/

	/**
	 * 
	 * @return numero de RNT.
	 */
	public int getNumeroRNT();

	/**
	 * 
	 * @return vencimiento del RNT
	 */
	public Date getVencimientoRNT();

	/**
	 * 
	 * @return registro del super turismo del operador.
	 */
	public String getRegistroSuperTurismo();

	/**
	 * 
	 * @return fecha de vencimiento del super tutimo.
	 */
	public Date getVencimientoRegistroSuperTurismo();

	/**
	 * 
	 * @return categoria del operador.
	 */
	public Modalidad getCategoria();

	/**
	 * 
	 * @return direccion del operador.
	 */
	public String getDireccion();

	/**
	 * 
	 * @return hora de apertura.
	 */
	public Date getHoraApertura();

	/**
	 * 
	 * @return hora de cierre.
	 */
	public Date getHoraCierre();

	/**
	 * 
	 * @return tiempo minmo de estadia.
	 */
	public int getTiempoMinimo();

	/**
	 * 
	 * @return ganacias del anio actual.
	 */
	public double getGananciaAnioActual();

	/**
	 * 
	 * @return ganacias anio de corrido.
	 */
	public double getGananciAnioCorrido();

	/**
	 * 
	 * @return habitaciones del operador.
	 */
	public ArrayList getHabitaciones();

	/**
	 * 
	 * @return apartamentos del operador.
	 */
	public ArrayList getApartamentos();
	
	@Override
	/**
	 * @return Una cadena de caracteres con todos los atributos del operador.
	 */
	public String toString();
	
}
