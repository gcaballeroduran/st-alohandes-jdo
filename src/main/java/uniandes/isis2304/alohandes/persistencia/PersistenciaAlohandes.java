/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: ALOHANDES
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.alohandes.persistencia;


import java.math.BigDecimal;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
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

import uniandes.isis2304.alohandes.negocio.Apartamento;
import uniandes.isis2304.alohandes.negocio.Cliente;
import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.negocio.Operador;
import uniandes.isis2304.alohandes.negocio.Propiedad;
import uniandes.isis2304.alohandes.negocio.Reserva;
import uniandes.isis2304.alohandes.negocio.ReservaColectiva;
import uniandes.isis2304.alohandes.negocio.Servicio;
import uniandes.isis2304.alohandes.negocio.Usuario;

/**
 * Clase para el manejador de persistencia del proyecto Alohandes
 * Traduce la información entre objetos Java y tuplas de la base de datos, en ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para comunicarse de manera correcta
 * con la base de datos
 * Se apoya en las clases SQLUsuario, SQLCliente, SQLOperador, SQLReserva, SQLApartamento, SQLHabitacion, SQLPropiedad y SQLServicio, que son 
 * las que realizan el acceso a la base de datos
 * 
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
	 * Secuenciador, Usuario, Operador, Cliente, Propiedad, Habitacion, Apartamento
	 * Servicio, ReservaColectiva, Reserva, ReservaApartamento, ReservaHabitacion
	 */
	private List <String> tablas;

	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaAlohandes
	 */
	private SQLUtil sqlUtil;

	/**
	 * Atributo para el acceso a la tabla USUARIO de la base de datos
	 */
	private SQLUsuario sqlUsuario;

	/**
	 * Atributo para el acceso a la tabla OPERADOR de la base de datos
	 */
	private SQLOperador sqlOperador;

	/**
	 * Atributo para el acceso a la tabla CLIENTE de la base de datos
	 */
	private SQLCliente sqlCliente;

	/**
	 * Atributo para el acceso a la tabla PROPIEDAD de la base de datos
	 */
	private SQLPropiedad sqlPropiedad;

	/**
	 * Atributo para el acceso a la tabla HABITACION de la base de datos
	 */
	private SQLHabitacion sqlHabitacion;

	/**
	 * Atributo para el acceso a la tabla APARTAMENTO de la base de datos
	 */
	private SQLApartamento sqlApartamento;

	/**
	 * Atributo para el acceso a la tabla SERVICIO de la base de datos
	 */
	private SQLServicio sqlServicio;

	/**
	 * Atributo para el acceso a la tabla RESERVA de la base de datos
	 */
	private SQLReserva sqlReserva;

	/**
	 * Atributo para el acceso a la tabla RESERVA_COLECTIVA de la base de datos
	 */
	private SQLReservaColectiva sqlReservaColectiva;

	/**
	 * Atributo para el acceso a la tabla RESERVA_APARTAMENTO de la base de datos
	 */
	private SQLReservaApartamento sqlReservaApartamento;

	/**
	 * Atributo para el acceso a la tabla RESERVA_HABITACION de la base de datos
	 */
	private SQLReservaHabitacion sqlReservaHabitacion;



	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaAlohandes ()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("Alohandes");		
		crearClasesSQL ();

		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("Alohandes_sequence");
		tablas.add ("USUARIO");
		tablas.add ("OPERADOR");
		tablas.add ("CLIENTE");
		tablas.add ("PROPIEDAD");
		tablas.add ("HABITACION");
		tablas.add ("APARTAMENTO");
		tablas.add ("SERVICIO");
		tablas.add ("RESERVA_COLECTIVA");
		tablas.add ("RESERVA");
		tablas.add ("RESERVA_APARTAMENTO");
		tablas.add ("RESERVA_HABITACION");
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
	 * @return Retorna el único objeto PersistenciaAlohandes existente - Patrón SINGLETON
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
	 * @return Retorna el único objeto PersistenciaAlohandes existente - Patrón SINGLETON
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
		sqlUsuario = new SQLUsuario(this);
		sqlOperador = new SQLOperador(this);
		sqlCliente = new SQLCliente(this);
		sqlPropiedad = new SQLPropiedad(this);
		sqlHabitacion = new SQLHabitacion(this);
		sqlApartamento = new SQLApartamento(this);
		sqlServicio = new SQLServicio(this);
		sqlReservaColectiva = new SQLReservaColectiva(this);
		sqlReserva = new SQLReserva(this);
		sqlReservaApartamento = new SQLReservaApartamento(this);
		sqlReservaHabitacion = new SQLReservaHabitacion(this);

		sqlUtil = new SQLUtil(this);

	}

	//////////////////////////////////////////////////////////////
	/////////////////////   DAR TABLAS  //////////////////////////
	//////////////////////////////////////////////////////////////

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de alohandes
	 */
	public String darSeqAlohandes()
	{
		return tablas.get (0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Usuario de Alohandes
	 */
	public String darTablaUsuario(){

		return tablas.get(1);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Operador de Alohandes
	 */
	public String darTablaOperador(){

		return tablas.get(2);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cliente de Alohandes
	 */
	public String darTablaCliente(){

		return tablas.get(3);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Propiedad de Alohandes
	 */
	public String darTablaPropiedad(){

		return tablas.get(4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Habitacion de Alohandes
	 */
	public String darTablaHabitacion(){

		return tablas.get(5);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Apartamento de Alohandes
	 */
	public String darTablaApartamento(){

		return tablas.get(6);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Servicio de Alohandes
	 */
	public String darTablaServicio(){

		return tablas.get(7);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ReservaColectiva de Alohandes
	 */
	public String darTablaReservaColectiva(){

		return tablas.get(8);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Reserva de Alohandes
	 */
	public String darTablaReserva(){

		return tablas.get(9);
	}


	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ReservaApartamento de Alohandes
	 */
	public String darTablaReservaApartamento(){

		return tablas.get(10);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ReservaHabitacion de Alohandes
	 */
	public String darTablaReservaHabitacion(){

		return tablas.get(11);
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
	public Usuario adicionarUsuario(String logIn,String tipoId, long numeroId, String relacionU) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlUsuario.adicionarUsuario(pm, logIn, tipoId, numeroId, relacionU);
			tx.commit();

			log.trace ("Inserción de usuario: " + logIn + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Usuario(numeroId, tipoId, logIn, relacionU);
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
	public Operador adicionarOperador(long numeroId,String logIn,String tipoId,String relacionU,int numeroRNT, Date vencimientoRNT, String registroSuperTurismo,Date vencimientoRegistroSuperTurismo,String categoria, String direccion, 
			Date horaApertura, Date horaCierre, int tiempoMinimo, double gananciaAnioActual, double gananciAnioCorrido, ArrayList habitaciones, ArrayList apartamentos) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();            
			adicionarUsuario(logIn, tipoId, numeroId, relacionU);
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
	public Cliente adicionarCliente(long numeroId,String logIn,String tipoId,String relacionU, String medioPago, int reservas)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			adicionarUsuario(logIn, tipoId, numeroId, relacionU);
			long tuplasInsertadas = sqlCliente.adicionarCliente(pm, numeroId, medioPago, reservas);
			tx.commit();

			log.trace ("Inserción de cliente: " + numeroId + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Cliente(logIn, tipoId, numeroId, relacionU, medioPago, reservas);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            System.out.print("Exception : " + e.getMessage());
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
	 * 			Métodos para manejar los PROPIEDAD
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla PROPIEDAD
	 * Adiciona entradas al log de la aplicación
	 * @param pm - El manejador de persistencia
	 * @param idHab - El identificador del Habitacion
	 * @param tipo - Tipo de Habitacion
	 * @param individual - Booleano si la habitacion es individual (t) o compartida (f)
	 * @param esquema - Ruta del esquema de la Habitacion
	 */
	public Propiedad adicionarPropiedad(long idProp, int capacidad, double precio, double tam, int diasR, int piso, String fechaCrea) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlPropiedad.adicionarPropiedad(pm, idProp, capacidad, precio, tam, diasR, fechaCrea , piso);
			tx.commit();

			log.trace ("Inserción de propiedad: " + idProp + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Propiedad(idProp, capacidad, tam, precio, fechaCrea, diasR, piso);
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
	 * Método que elimina, de manera transaccional, una tupla en la tabla PROPIEDAD, dado el id de la propiedad
	 * Adiciona entradas al log de la aplicación
	 * @param id - El id de la propiedad
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarPropiedadPorId(long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlPropiedad.eliminarPropiedadPorId(pm, id);
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
	 * Método que consulta todas las tuplas en la tabla PROPIEDAD 
	 * @return La lista de objetos PROPIEDAD
	 */
	public List<Propiedad> darPropiedades() 
	{
		return sqlPropiedad.darPropiedades(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla PROPIEDAD que tienen el id dado
	 * @param id - El id de la pripiedad
	 * @return El objeto PROPIEDAD
	 */
	public Propiedad darPropiedadesPorId (long id) 
	{
		return (Propiedad) sqlPropiedad.darPropiedadPorId(pmf.getPersistenceManager(), id);
	}


	/* ****************************************************************
	 * 			Métodos para manejar los APARTAMENTO
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla APARTMENTO
	 * Adiciona entradas al log de la aplicación
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

	public Apartamento adicionarApartamento(long pID, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso,  boolean pAmueblado, int pHabitaciones, String pDMenaje, Date pVenceSeguro, String pDSeguro, long pOperador) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			adicionarPropiedad(pID, pCapacidad, pPrecio, pTamanio, pDiasR, pPiso, pFecha);
			tx.begin();
			long tuplasInsertadas = sqlApartamento.adicionarApartamento(pm, pID, pAmueblado, pDMenaje, pDSeguro, pVenceSeguro, pOperador);
			tx.commit();

			log.trace ("Inserción de apartamento: " + pID + ": " + tuplasInsertadas + " tuplas insertadas");



			return new Apartamento(pID, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, pOperador);
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
	 * Método que elimina, de manera transaccional, una tupla en la tabla APARTAMENTO, dado el id de la propiedad
	 * Adiciona entradas al log de la aplicación
	 * @param id - El id de la apartamento
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarApartamentoPorId(long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlApartamento.eliminarApartamentoPorId(pm, id);
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
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO 
	 * @return La lista de objetos APARTAMENTO
	 */
	public List<Apartamento> darApartamentos() 
	{
		return sqlApartamento.darApartamentos(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO que tienen el id dado
	 * @param id - El id de la pripiedad
	 * @return El objeto APARTAMENTO
	 */
	public Apartamento darApartamentosPorId (long id) 
	{
		return (Apartamento) sqlApartamento.darApartamentoPorId(pmf.getPersistenceManager(), id);
	}


	/**
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO  que estan disponibles
	 * @return La lista de objetos APARTAMENTO
	 */
	public List<Apartamento> darApartamentosDisponibles() 
	{
		return sqlApartamento.darApartamentosDisponibles(pmf.getPersistenceManager());
	}

	/**
	 * Habilita una habitacion
	 * @param id identificador de la habitacion que se quiere habilitar
	 */
	public void habilitarApartamento(long id)
	{
		sqlApartamento.habilitarApartamento(pmf.getPersistenceManager(), id);
	}

	/**
	 * Deshabilita un apartamento
	 * @param id identificador de la habitacion que se quiere habilitar
	 */
	public void deshabilitarApartamento(long id)
	{
		List<Reserva> activas = darRerservasActivasApartamento(id);
		// Si la propiedad no tiene reservas activas simplemente la desactiva
		if(activas.isEmpty())
			sqlApartamento.deshabilitarApartamento(pmf.getPersistenceManager(), id);
		//Si hay reservas activas hay que re agendarlas
		else
		{
			List<Apartamento> aptD = darApartamentosDisponibles();
			int limite = 0;
			if(aptD.size()>=activas.size())
			{
				limite = activas.size();
			}
			else
			{
				limite = aptD.size();
				for(int i=aptD.size()-1; i<=activas.size();i++)
				{
					
					sqlReservaApartamento.eliminarReservaApartamento(pmf.getPersistenceManager(), activas.get(i).getId());
				}
			}
			for(int i=0; i<=limite; i++)
			{
				long idApt = aptD.get(i).getId();
				sqlReservaApartamento.cambiarReservaApartamento(pmf.getPersistenceManager(),
						activas.get(i).getId(), idApt);
			}


			sqlApartamento.habilitarApartamento(pmf.getPersistenceManager(), id);
		}
	}

	/* ****************************************************************
	 * 			Métodos para manejar las HABITACION
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla HABITACION
	 * Adiciona entradas al log de la aplicación
	 * @param pm - El manejador de persistencia
	 * @param idHab - El identificador del Habitacion
	 * @param tipo - Tipo de Habitacion
	 * @param individual - Booleano si la habitacion es individual (t) o compartida (f)
	 * @param esquema - Ruta del esquema de la Habitacion
	 */
	public Habitacion adicionarHabitacion(long pId, int pCapacidad, double pTamanio, double pPrecio, String pFecha, int pDiasR, int pPiso, boolean pIndiv, String pEsquema, int pTipo, long pOperador) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			adicionarPropiedad(pId, pCapacidad, pPrecio, pTamanio, pDiasR, pPiso, pFecha);
			tx.begin();
			long tuplasInsertadas = sqlHabitacion.adicionarHabitacion(pm, pId, pTipo, pIndiv, pEsquema, pOperador);
			tx.commit();

			log.trace ("Inserción de habitacion: " + pId + ": " + tuplasInsertadas + " tuplas insertadas");



			return new Habitacion(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pIndiv, pEsquema, pTipo, pOperador);
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
	 * Método que elimina, de manera transaccional, una tupla en la tabla HABITACION, dado el id de la propiedad
	 * Adiciona entradas al log de la aplicación
	 * @param id - El id de la apartamento
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarHabitacionPorId(long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlHabitacion.eliminarHabitacionPorId(pm, id);
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
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO 
	 * @return La lista de objetos APARTAMENTO
	 */
	public List<Habitacion> darHabitaciones() 
	{
		return sqlHabitacion.darHabitaciones(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO que tienen el id dado
	 * @param id - El id de la pripiedad
	 * @return El objeto APARTAMENTO
	 */
	public Habitacion darHabitacionesPorId (long id) 
	{
		return (Habitacion) sqlHabitacion.darHabitacionPorId(pmf.getPersistenceManager(), id);
	}

	/**
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO 
	 * @return La lista de objetos APARTAMENTO
	 */
	public List<Habitacion> darHabitacionesDisponibles() 
	{
		return sqlHabitacion.darHabitacionesDisponibles(pmf.getPersistenceManager());
	}

	/**
	 * Habilita una habitacion
	 * @param id identificador de la habitacion que se quiere habilitar
	 */
	public void habilitarHabitacion(long id)
	{
		sqlHabitacion.habilitarHabitacion(pmf.getPersistenceManager(), id);
	}
	
	/**
	 * Deshabilita una Habitacion
	 * @param id identificador de la habitacion que se quiere habilitar
	 */
	public void deshabilitarHabitacion(long id)
	{
		List<Reserva> activas = darReservasActivasHabitacion(id);
		// Si la propiedad no tiene reservas activas simplemente la desactiva
		if(activas.isEmpty())
			sqlHabitacion.deshabilitarHabitacion(pmf.getPersistenceManager(), id);
		//Si hay reservas activas hay que re agendarlas
		else
		{
			List<Habitacion> aptD = darHabitacionesDisponibles();
			int limite = 0;
			if(aptD.size()>=activas.size())
			{
				limite = activas.size();
			}
			else
			{
				limite = aptD.size();
				for(int i=aptD.size()-1; i<=activas.size();i++)
				{
					
					sqlReservaHabitacion.eliminarReservaHabitacion(pmf.getPersistenceManager(), activas.get(i).getId());
				}
			}
			for(int i=0; i<=limite; i++)
			{
				long idApt = aptD.get(i).getId();
				sqlReservaApartamento.cambiarReservaApartamento(pmf.getPersistenceManager(),
						activas.get(i).getId(), idApt);
			}


			sqlApartamento.habilitarApartamento(pmf.getPersistenceManager(), id);
		}
	}

	/* ****************************************************************
	 * 			Métodos para manejar los SERVICIO	
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla APARTMENTO
	 * Adiciona entradas al log de la aplicación
	 * @param pID identificador del servicio
	 * @param pTipo tipo de servicio
	 * @param pPrecio precio del servicio
	 * @param pIntervalo intervalo de pago del servicio
	 */
	public Servicio adicionarServicio(long idServ, String pTipo, double pPrecio, int pIntervalo) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlServicio.adicionarServicio(pm, idServ, pTipo, pPrecio, pIntervalo);
			tx.commit();

			log.trace ("Inserción de servicio: " + idServ + ": " + tuplasInsertadas + " tuplas insertadas");



			return new Servicio(idServ, pTipo, pPrecio, pIntervalo);
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
	 * Método que elimina, de manera transaccional, una tupla en la tabla SERVICIO, dado el id de la propiedad
	 * Adiciona entradas al log de la aplicación
	 * @param id - El id de la apartamento
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarServicioPorId(long id) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlServicio.eliminarServicioPorId(pm, id);
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
	 * Método que consulta todas las tuplas en la tabla SREVICIO 
	 * @return La lista de objetos SERVICIO
	 */
	public List<Servicio> darServicio() 
	{
		return sqlServicio.darServicios(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla APARTAMENTO que tienen el id dado
	 * @param id - El id de la pripiedad
	 * @return El objeto APARTAMENTO
	 */
	public Servicio darServicioPorId (long id) 
	{
		return (Servicio) sqlServicio.darServicioPorId(pmf.getPersistenceManager(), id);
	}

	/* ****************************************************************
	 * 			Métodos para manejar los RESERVA COLECTIVA
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla RESERVA COLECTIVA
	 * Adiciona entradas al log de la aplicación
	 * @param fechaInicio - fecha de inicio de la reserva colectiva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayoe o igual a 1)
	 * @param finCancelacionOportuna - fecha final para cancelar la reserva.
	 * @param porcentajeAPagar - porcentaje a pagar de la reserva.
	 * @param montoTotal - monto total de la reserva.
	 * @return El objeto Bar adicionado. null si ocurre alguna Excepción
	 */
	public ReservaColectiva adicionarReservaColectiva(int pCantidad, String pTipo, String pInicio, int pDuracion) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();

		try
		{
			tx.begin();
			long id = nextval ();

			// Primero se verifica si se cuenta con la capacidad y se crean las reservas individuales
			List<Reserva> r = new ArrayList<Reserva>();
			if(pTipo.equalsIgnoreCase("apartamento"))
			{
				r = adicionarReservaColectivaApartamento(id, pCantidad, pInicio, pDuracion);
			}
			else
			{
				r = adicionarReservaColectivaApartamento(id, pCantidad, pInicio, pDuracion);
			}

			if(r.size()==0)
			{
				// No se cuenta con la capacidad para hacer la reserva colectiva
				return null;
			}

			long tuplasInsertadas = sqlReservaColectiva.adicionarReservaColectiva(pm, id, pInicio, pDuracion, pCantidad, pTipo);
			tx.commit();

			log.trace ("Inserción de Reserva: " + id + ": " + tuplasInsertadas + " tuplas insertadas");

			return new ReservaColectiva(id, pCantidad, pTipo, pInicio, pDuracion);
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
	 * Retorna reserva colectiva de apartamentos
	 * @param pId
	 * @param pCantidad
	 * @param pInicio
	 * @param pDuracion
	 * @return
	 */
	public List<Reserva> adicionarReservaColectivaApartamento( long idColectiva, int pCantidad, String pInicio, int pDuracion) 
	{

		// Primero se verifica que se tiene la capacidad suficiente
		List<Apartamento> apartamentosD = darApartamentosDisponibles();
		if(apartamentosD.size() < pCantidad)
			return null;
		else 
		{
			List<Reserva> reservas = new ArrayList<Reserva>();
			for(int i=1; i<=pCantidad; i++)
			{
				Apartamento a = apartamentosD.get(i);
				double montoTotal = a.getPrecio()*pDuracion;
				
				
				Date fechaInicio = Date.valueOf(pInicio);		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
				
				// Calcular la fecha final
				LocalDate tf = fechaInicio.toLocalDate().plusDays(pDuracion);
				Date dtf = (Date) Date.from(tf.atStartOfDay().toInstant(null));
				String fechaFin = dateFormat.format(dtf);
				
				// Calcular la fecha de cancelacion oportuna: 5 días antes de la fecha de inicio
				LocalDate t = fechaInicio.toLocalDate().minusDays(5);
				Date fco = (Date) Date.from(t.atStartOfDay().toInstant(null));
				String finCancelacionOportuna = dateFormat.format(fco);
				
				
				Reserva r = adicionarReserva(pInicio, fechaFin, 1, finCancelacionOportuna, 1, montoTotal, a.getId());
				r.setIdRColectiva(idColectiva);
				adicionarReservaApartamento(a.getId(), r.getId());
				reservas.add(r);
			}
			return reservas;
		}
	}

	public List<Reserva> adicionarReservaColectivaHabitacion( long idColectiva, int pCantidad, String pInicio, int pDuracion) 
	{

		// Primero se verifica que se tiene la capacidad suficiente
		List<Habitacion> habitacionesD = darHabitacionesDisponibles();
		if(habitacionesD.size() < pCantidad)
			return null;
		else 
		{
			List<Reserva> reservas = new ArrayList<Reserva>();
			for(int i=1; i<=pCantidad; i++)
			{
				Habitacion h = habitacionesD.get(i);
				double montoTotal = h.getPrecio()*pDuracion;
				
				Date fechaInicio = Date.valueOf(pInicio);		
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
				
				// Calcular la fecha final
				LocalDate tf = fechaInicio.toLocalDate().plusDays(pDuracion);
				Date dtf = (Date) Date.from(tf.atStartOfDay().toInstant(null));
				String fechaFin = dateFormat.format(dtf);
				
				// Calcular la fecha de cancelacion oportuna: 5 días antes de la fecha de inicio
				LocalDate t = fechaInicio.toLocalDate().minusDays(5);
				Date fco = (Date) Date.from(t.atStartOfDay().toInstant(null));
				String finCancelacionOportuna = dateFormat.format(fco);
				
				Reserva r = adicionarReserva(pInicio, fechaFin, 1, finCancelacionOportuna, 1, montoTotal, h.getId());
				adicionarReservaHabitacion(h.getId(), r.getId());
				r.setIdRColectiva(idColectiva);
				reservas.add(r);
			}
			return reservas;
		}
	}


	/**
	 * Método que elimina, de manera transaccional, una tupla en la tabla RESERVA, dado el identificador del bar
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - El identificador de la reserva
	 * @return El número de tuplas eliminadas. -1 si ocurre alguna Excepción
	 */
	public long eliminarReservaColectivaPorId (long idReserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			String tipo = darTipoReserva(idReserva);
			tx.begin();
			if(tipo.equals("apartamento"))
				eliminarReservaColectivaApartamento(idReserva);
			else
				eliminarReservaColectivaHabitacion(idReserva);

			long resp = sqlReservaColectiva.eliminarReservaColectivaPorId(pm, idReserva);
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
	 * Retorna reserva colectiva de apartamentos
	 * @param pId
	 * @param pCantidad
	 * @param pInicio
	 * @param pDuracion
	 * @return
	 */
	public List<Long> eliminarReservaColectivaApartamento( long idColectiva) 
	{

		// Se tienen que eliminar las reservas individuales 
		List<Reserva> reservasC = (List<Reserva>) darReservaPorIdColectiva(idColectiva);
		List<Long> tuplasEliminadas = new ArrayList<Long>(); 
		if(reservasC.size() <= 0)
			return null;
		else 
		{
			for(int i=1; i<=reservasC.size(); i++)
			{
				// Eliminar la reserva de reserva y de reserva apartamento
				long r = reservasC.get(i).getId();
				eliminarReservaPorId(r);
				eliminarReservaApartamentoPorId(r);
				tuplasEliminadas.add(r);
			}
			return tuplasEliminadas;
		}
	}

	/**
	 * Retorna reserva colectiva de apartamentos
	 * @param pId
	 * @param pCantidad
	 * @param pInicio
	 * @param pDuracion
	 * @return
	 */
	public List<Long> eliminarReservaColectivaHabitacion( long idColectiva) 
	{

		// Se tienen que eliminar las reservas individuales 
		List<Reserva> reservasC = (List<Reserva>) darReservaPorIdColectiva(idColectiva);
		List<Long> tuplasEliminadas = new ArrayList<Long>(); 
		if(reservasC.size() <= 0)
			return null;
		else 
		{
			for(int i=1; i<=reservasC.size(); i++)
			{
				// Eliminar la reserva de reserva y de reserva apartamento
				long r = reservasC.get(i).getId();
				eliminarReservaPorId(r);
				eliminarReservaHabitacionPorId(r);
				tuplasEliminadas.add(r);
			}
			return tuplasEliminadas;
		}
	}



	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA
	 * @return La lista de objetos RESERVA, construidos con base en las tuplas de la tabla RESERVA
	 */
	public List<ReservaColectiva> darRerservasColectivas ()
	{
		return sqlReservaColectiva.darReservasColectivas(pmf.getPersistenceManager());
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public ReservaColectiva darReservaColectivaPorId (long idReserva)
	{
		return sqlReservaColectiva.darReservaColectivaPorId(pmf.getPersistenceManager(), idReserva);
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
	public Reserva adicionarReserva( String fechaInicio, String fechaFin, int personas, String finCancelacionOportuna,
			double porcentajeAPagar, double montoTotal, long idProp) 
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
	public long eliminarReservaPorId (long idReserva) 
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
	public List<Reserva> darReservas ()
	{
		return sqlReserva.darReservas(pmf.getPersistenceManager());
	}

	public String darTipoReserva(long idReserva)
	{
		String tipo ="";
		long apt = darIdApartamentoPorIdReserva(idReserva);
		long hab = darIdHabitacionPorIdReserva(idReserva);
		if(apt !=0)
			tipo = "apartamento";
		else if(hab != 0)
			tipo = "habitacion";

		return tipo;
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA cuya fecha de inicio es después de la fecha acutal
	 * @return La lista de objetos RESERVA, construidos con base en las tuplas de la tabla RESERVA
	 */
	public List<Reserva> darRerservasActivasApartamento (long apt)
	{
		return sqlReserva.darReservasActivasApartamento(pmf.getPersistenceManager(), apt);
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA cuya fecha de inicio es después de la fecha acutal
	 * @return La lista de objetos RESERVA, construidos con base en las tuplas de la tabla RESERVA
	 */
	public List<Reserva> darReservasActivasHabitacion (long hab)
	{
		return sqlReserva.darReservasActivasHabitacion(pmf.getPersistenceManager(), hab);
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
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public List<Reserva> darReservaPorIdColectiva (long idReservaColectiva)
	{
		return sqlReserva.darReservaPorIdColectiva(pmf.getPersistenceManager(), idReservaColectiva);
	}


	/* ****************************************************************
	 * 			Métodos para manejar los RESERVA HABITACION
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
	public void adicionarReservaHabitacion( long idHabitacion, long idReserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long id = nextval ();
			long tuplasInsertadas = sqlReservaHabitacion.adicionarReservaHabitacion(pm, idHabitacion, idReserva);
			tx.commit();

			log.trace ("Inserción de ReservaHabitacion: " + id + ": " + tuplasInsertadas + " tuplas insertadas");

		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
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
	public long eliminarReservaHabitacionPorId (long idReserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlReservaHabitacion.eliminarReservaHabitacion(pm, idReserva);
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
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public long darIdHabitacionPorIdReserva (long idReserva)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlReservaHabitacion.darIdHabitacionPorIdReserva(pm, idReserva);
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public long darIdReservaPorIdHabitacion (long idHabitacion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlReservaHabitacion.darIdReservaPorIdHabitacion(pm, idHabitacion);
	}

	/* ****************************************************************
	 * 			Métodos para manejar los RESERVA APARTAMENTO
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
	public void adicionarReservaApartamento( long idApartamento, long idReserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long id = nextval ();
			long tuplasInsertadas = sqlReservaApartamento.adicionarReservaApartamento(pm, idApartamento, idReserva);
			tx.commit();

			log.trace ("Inserción de ReservaApartamento: " + id + ": " + tuplasInsertadas + " tuplas insertadas");

		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
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
	public long eliminarReservaApartamentoPorId (long idReserva) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlReservaApartamento.eliminarReservaApartamento(pm, idReserva);
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
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public long darIdApartamentoPorIdReserva (long idReserva)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlReservaApartamento.darIdApartamentoPorIdReserva(pm, idReserva);
	}

	/**
	 * Método que consulta todas las tuplas en la tabla RESERVA que tienen el identificador dado
	 * @param idReserva - El identificador del bar
	 * @return El objeto RESERVA, construido con base en la tuplas de la tabla RESERVA, que tiene el identificador dado
	 */
	public long darIdReservaPorIdApartamento (long idApartamento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		return sqlReservaApartamento.darIdReservaPorIdApartamento(pm, idApartamento);
	}


	////////////////////////////////////////////////



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
			long [] resp = sqlUtil.limpiarAlohandes(pm);
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
