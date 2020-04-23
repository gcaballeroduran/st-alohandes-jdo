/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.persistencia;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.alohandes.negocio.Cliente;
import uniandes.isis2304.alohandes.negocio.Modalidad;
import uniandes.isis2304.alohandes.negocio.Operador;
import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.TipoCliente;
import uniandes.isis2304.alohandes.negocio.TipoIdentificacion;
import uniandes.isis2304.alohandes.negocio.Usuario;

/**
 * Clase para el manejador de persistencia del proyecto Parranderos
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * Se apoya en las clases SQLUsuario, SQLCliente, SQLOperador, SQLReserva, SQLApartamento, SQLHabitacion, SQLPropiedad y SQLServicio, que son 
 * las que realizan el acceso a la base de datos
 * 
 * @author Germán Bravo
 */
public class PersistenciaAlohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaAlohandes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaAlohandes instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoBebida, bebida, bar, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	
	/**
	 * Atributo para el acceso a la tabla CLIENTE de la base de datos
	 */
	private SQLCliente sqlCliente;
	
	/**
	 * Atributo para el acceso a la tabla OPERADOR de la base de datos
	 */
	private SQLOperador sqlOperador;
	
	/**
	 * Atributo para el acceso a la tabla RESERVA de la base de datos
	 */
	private SQLReserva sqlReserva;
	
	/**
	 * Atributo para el acceso a la tabla USUARIO de la base de datos
	 */
	private SQLUsuario sqlUsuario;


	
	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAlohandes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Parranderos");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("Parranderos_sequence");
		tablas.add ("TIPOBEBIDA");
		tablas.add ("BEBIDA");
		tablas.add ("BAR");
		tablas.add ("BEBEDOR");
		tablas.add ("GUSTAN");
		tablas.add ("SIRVEN");
		tablas.add ("VISITAN");
}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaAlohandes (JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas (tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes ();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaAlohandes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaAlohandes (tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		 
		return resp;
	}
	
	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlCliente = new SQLCliente(this);
		sqlOperador = new SQLOperador(this);
		sqlReserva = new SQLReserva(this);
		sqlUsuario = new SQLUsuario(this);	
		sqlUtil = new SQLUtil(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de parranderos
	 */
	public String darSeqParranderos ()
	{
		return tablas.get (0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cliente de Alohandes
	 */
	public String darTablaCliente(){
		
		return tablas.get(4);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Reserva de Alohandes
	 */
	public String darTablaReserva(){
		
		return tablas.get(7);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Operador de Alohandes
	 */
	public String darTablaOperador(){
		
		return tablas.get(14);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Usuario de Alohandes
	 */
	public String darTablaUsuario(){
		return tablas.get(4);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ReservaHabitacion de Alohandes
	 */
	public String darTablaReservaHabitacion(){
		
		return tablas.get(2);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ReservaApartamento de Alohandes
	 */
	public String darTablaReservaApartamento(){
		
		return tablas.get(2);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de habitacion de Alohandes
	 */
	public String darTablaHabitacion(){
		
		return tablas.get(4);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de apartamento de Alohandes
	 */
	public String darTablaApartamento(){
		
		return tablas.get(5);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de propiedad de Alohandes
	 */
	public String darTablaPropiedad(){
		
		return tablas.get(8);
	}
	
	/**
	 * Transacción para el generador de secuencia de Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de Parranderos
	 */
	private long nextval ()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }
	
	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/* ****************************************************************
	 * 			Métodos para manejar los CLIENTES
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla Cliente
	 * Adiciona entradas al log de la aplicación
	 * @param logIn - logIn del cliente
	 * @param tipoId - tipo de Id
	 * @param numeroId - numero del Id
	 * @param relacionU - relacion con la universidad
	 * @param id - id del cliente.
	 * @param medioPago - medio de pago del cliente.
	 * @param reservas - reservas del cliente.
	 * @return El objeto Cliente adicionado. null si ocurre alguna Excepción
	 */
	public Cliente adicionarCliente(String logIn,TipoIdentificacion tipoId,TipoCliente relacionU, String medioPago, int reservas)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long numeroId = nextval ();
            long tuplasInsertadas = sqlCliente.adicionarCliente(pm, numeroId, medioPago, reservas);
            tx.commit();
            
            log.trace ("Inserción de cliente: " + numeroId + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Cliente(logIn, tipoId, numeroId, relacionU, medioPago, reservas);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}


	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla Cliente, dado el identificador del tipo de bebida
	 * Adiciona entradas al log de la aplicación
	 * @param idCliente - El identificador del tipo de bebida
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarClientePorId (long idCliente) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCliente.eliminarClientePorId(pm, idCliente);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla Cliente
	 * @return La lista de objetos Cliente, construidos con base en las tuplas de la tabla CLIENTE
	 */
	public List<Cliente> darCliente ()
	{
		return sqlCliente.darClientes(pmf.getPersistenceManager()); 
	}
 
 
	/**
	 * Método que consulta todas las tuplas en la tabla Cliente con un identificador dado
	 * @param idCliente - El identificador del cliente
	 * @return El objeto Cliente, construido con base en las tuplas de la tabla Cliente con el identificador dado
	 */
	public Cliente darClientePorId (long idCliente)
	{
		return sqlCliente.darClientePorId(pmf.getPersistenceManager(), idCliente);
	}
 
	/* ****************************************************************
	 * 			Métodos para manejar las OPERADOR
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla Bebida
	 * Adiciona entradas al log de la aplicación
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
	 * @return El objeto Operador adicionado. null si ocurre alguna Excepción
	 */
	public Operador adicionarOperador(String logIn,TipoIdentificacion tipoId,TipoCliente relacionU,int numeroRNT, Date vencimientoRNT, String registroSuperTurismo,Date vencimientoRegistroSuperTurismo,Modalidad categoria, String direccion, 
			Date horaApertura, Date horaCierre, int tiempoMinimo, double gananciaAnioActual, double gananciAnioCorrido, ArrayList habitaciones, ArrayList apartamentos) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();            
            long numeroId = nextval ();
            long tuplasInsertadas = sqlOperador.adicionarOperador(pm, numeroId, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, gananciaAnioActual, gananciAnioCorrido, habitaciones, apartamentos);
            tx.commit();
            
            log.trace ("Inserción operador: " + numeroId + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Operador(logIn, tipoId, numeroId, relacionU, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, gananciaAnioActual, gananciAnioCorrido, habitaciones, apartamentos);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	
	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla Operador, dado el identificador de la bebida
	 * Adiciona entradas al log de la aplicación
	 * @param idOperador - El identificador del operador
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarOperadorPorId (long idOperador) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlOperador.eliminarOperadorPorId(pm, idOperador);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla Operador.
	 * @return La lista de objetos operador, construidos con base en las tuplas de la tabla OPERADOR
	 */
	public List<Operador> darOperadores () 
	{
		return sqlOperador.darOperadores(pmf.getPersistenceManager());
	}
 
	/**
	 * Método que consulta todas las tuplas en la tabla Operador con un identificador dado
	 * @param idOpeador - El identificador del operador
	 * @return El objeto Operador, construido con base en las tuplas de la tabla Operador con el identificador dado
	 */
	public Operador darOperadorPorId (long idOpeador)
	{
		return sqlOperador.darOperadorPorId(pmf.getPersistenceManager(), idOpeador);
	}
	
 

	/* ****************************************************************
	 * 			Métodos para manejar los USUARIOS
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla USUARIOS
	 * Adiciona entradas al log de la aplicación
	 * @param logIn - log in del ususario.
	 * @param tipoId - tipo de identificacion del ususario.
	 * @param numeroId - numero de id del ususario.
	 * @param relacionU -  relacion de usuario con la universidad.
	 * @return El objeto USUARIOS adicionado. null si ocurre alguna Excepción
	 */
	public Usuario adicionarUsuario(String logIn,TipoIdentificacion tipoId, int numeroId, TipoCliente relacionU) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlUsuario.adicionarUsuario(pm, logIn, tipoId, numeroId, relacionU);
            tx.commit();

            log.trace ("Inserción de usuario: " + logIn + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Usuario(logIn, tipoId, numeroId, relacionU);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla USUARIO, dado el logIn del usuario
	 * Adiciona entradas al log de la aplicación
	 * @param id - El id del usuario
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarUsuarioPorId(long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlUsuario.eliminarUsuarioPorId(pm, id);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla BEBEDOR que tienen el nombre dado
	 * @param nombreBebedor - El nombre del bebedor
	 * @return La lista de objetos BEBEDOR, construidos con base en las tuplas de la tabla BEBEDOR
	 */
	public List<Usuario> darUsuarios() 
	{
		return sqlUsuario.darUsuarios(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla USUARIO que tienen el logIn dado
	 * @param id - El id del usuario
	 * @return El objeto BEBEDOR, construido con base en la tuplas de la tabla USUARIO, que tiene el logIn dado
	 */
	public Usuario darUsuarioPorId (long id) 
	{
		return (Usuario) sqlUsuario.darUsuarioPorId(pmf.getPersistenceManager(), id);
	}



	
	/* ****************************************************************
	 * 			Métodos para manejar los RESERVA
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla RESERVA
	 * Adiciona entradas al log de la aplicación
	 * @param fechaInicio - fecha de inicio de la reserva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayoe o igual a 1)
	 * @param finCancelacionOportuna - fecha final para cancelar la reserva.
	 * @param porcentajeAPagar - porcentaje a pagar de la reserva.
	 * @param montoTotal - monto total de la reserva.
	 * @return El objeto Bar adicionado. null si ocurre alguna Excepción
	 */
	public Reserva adicionarReserva( Date fechaInicio, Date fechaFin, int personas, Date finCancelacionOportuna,
			double porcentajeAPagar, double montoTotal) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long id = nextval ();
            long tuplasInsertadas = sqlReserva.adicionarReserva(pm, id, fechaInicio, fechaFin, personas, finCancelacionOportuna, porcentajeAPagar, montoTotal);
            tx.commit();

            log.trace ("Inserción de Reserva: " + id + ": " + tuplasInsertadas + " tuplas insertadas");

            return new Reserva(id, fechaInicio, fechaFin, personas, finCancelacionOportuna, porcentajeAPagar, montoTotal);
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla RESERVA, dado el identificador del bar
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - El identificador de la reserva
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarRservaPorId (long idReserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlReserva.eliminarReservaPorId(pm, idReserva);
            tx.commit();

            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA
	 * @return La lista de objetos RESERVA, construidos con base en las tuplas de la tabla RESERVA
	 */
	public List<Reserva> darRerservas ()
	{
		return sqlReserva.darReservas(pmf.getPersistenceManager());
	}
 
	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public Reserva darReservaPorId (long idReserva)
	{
		return sqlReserva.darReservaPorId(pmf.getPersistenceManager(), idReserva);
	}
 
	

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Alohandes
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 8 números que indican el número de tuplas borradas en las tablas APARTAMENTO, CLIENTE, HABITACION, OPERADOR,
	 * PROPIEDAD, RESERVA, SERVICIO y USUARIO, respectivamente
	 */
	public long [] limpiarAlohandes ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long [] resp = sqlUtil.limpiarParranderos (pm);
            tx.commit ();
            log.info ("Borrada la base de datos");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new long[] {-1, -1, -1, -1, -1, -1, -1};
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}
	

 }
