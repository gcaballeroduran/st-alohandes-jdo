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

package uniandes.isis2304.alohandes.negocio;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;

import uniandes.isis2304.alohandes.persistencia.PersistenciaAlohandes;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 */
public class Alohandes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(Alohandes.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaAlohandes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Alohandes ()
	{
		pp = PersistenciaAlohandes.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Alohandes (JsonObject tableConfig)
	{
		pp = PersistenciaAlohandes.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los USUARIOS
	 *****************************************************************/

	/**
	 * Adiciona de manera persistente un usuario 
	 * Adiciona entradas al log de la aplicación
	 * @param logIn - log in del ususario.
	 * @param tipoId - tipo de identificacion del ususario.
	 * @param numeroId - numero de id del ususario.
	 * @param relacionU -  relacion de usuario con la universidad.
	 * @return El objeto USUARIO adicionado. null si ocurre alguna Excepción
	 */
	public Usuario adicionarUsuario (String logIn,String tipoId, long numeroId, String relacionU)
	{
        log.info ("Adicionando usuario: " + logIn);
        Usuario usuario = pp.adicionarUsuario(logIn, tipoId, numeroId, relacionU);
        log.info ("Adicionando usuario: " + usuario);
        return usuario;
	}

	/**
	 * Elimina un usuario por su id
	 * Adiciona entradas al log de la aplicación
	 * @param id - El id del usuario a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarUsuarioPorId(long id)
	{
        log.info ("Eliminando usuario por logIn: " + id);
        long resp = pp.eliminarUsuarioPorId(id);
        log.info ("Eliminando usuario por logIn: " + resp + " tuplas eliminadas");
        return resp;
	}

	/**
	 * Encuentra un usuario y su información básica, según su identificador
	 * @param id - El id del usuario buscado
	 * @return Un objeto Usuario que corresponde con el id buscado y lleno con su información básica
	 * 			null, si un usuario con dicho id no existe.
	 */
	public Usuario darUsuarioPorId (long id)
	{
        log.info ("Dar información de un usuario por id: " + id);
        Usuario usuario = pp.darUsuarioPorId(id);
        log.info ("Buscando usuario por logIn: " + usuario != null ? usuario : "NO EXISTE");
        return usuario;
	}
	
	/**
	 * Encuentra todos los Usuarios en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Usuario con todos los usuarios que conoce la aplicación, llenos con su información básica
	 */
	public List<Usuario> darUsuarios ()
	{
        log.info ("Consultando Usuarios");
        List<Usuario> usuario = pp.darUsuarios();	
        log.info ("Consultando Usuarios: " + usuario.size() + " Usuarios existentes");
        return usuario;
	}

	/**
	 * Encuentra la información básica de los usuarios y los devuelve como VO.
	 * @return Una lista de Usuarios con su información básica.
	 * 	La lista vacía indica que no existen usuarios.
	 */
	public List<VOUsuario> darVOUsuarios()
	{
		log.info ("Generando los VO de los usuarios");       
        List<VOUsuario> voUsuario = new LinkedList<VOUsuario> ();
        for (Usuario beb : pp.darUsuarios())
        {
        	voUsuario.add (beb);
        }
        log.info ("Generando los VO de los usuarios: " + voUsuario.size() + " existentes");
        return voUsuario;
 	}

	
	/* ****************************************************************
	 * 			Métodos para manejar los CLIENTES
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un cliente
	 * Adiciona entradas al log de la aplicación
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
	public Cliente adicionarCliente (long numeroId,String logIn,String tipoId, String relacionU, String medioPago, int reservas)
	{
        log.info ("Adicionando Cliente: " + logIn);
        Cliente cliente = pp.adicionarCliente(numeroId,logIn, tipoId,  relacionU, medioPago, reservas);		
        log.info ("Adicionando cliente: " + cliente);
        return cliente;
	}
	
	/**
	 * Elimina un cliente por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idCliente - El id del cliente a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarClientePorId (long idCliente)
	{
		log.info ("Eliminando Tipo de bebida por id: " + idCliente);
        long resp = pp.eliminarClientePorId(idCliente);		
        log.info ("Eliminando Tipo de bebida por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	/**
	 * Encuentra un cliente y su información básica, según su identificador
	 * @param id - El id del cliente buscado
	 * @return Un objeto cliente que corresponde con el id buscado y lleno con su información básica
	 * 			null, si un cliente con dicho id no existe.
	 */
	public Cliente darClientePorId (long id)
	{
        log.info ("Dar información de un cliente por id: " + id);
        Cliente cliente = pp.darClientePorId(id);
        log.info ("Buscando cliente por id: " + cliente != null ? cliente : "NO EXISTE");
        return cliente;
	}
	
	/**
	 * Encuentra todos los clientes en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Cliente con todos los clientes que conoce la aplicación, llenos con su información básica
	 */
	public List<Cliente> darCliente ()
	{
		log.info ("Consultando Clientes");
        List<Cliente> clientes = pp.darCliente();	
        log.info ("Consultando Clientes: " + clientes.size() + " existentes");
        return clientes;
	}

	/**
	 * Encuentra todos los clientes en Alohandes y los devuelve como una lista de VOCliente
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOCliente con todos clientes que conoce la aplicación, llenos con su información básica
	 */
	public List<VOCliente> darVOTCliente ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOCliente> voCliente = new LinkedList<VOCliente> ();
        for (Cliente tb : pp.darCliente())
        {
        	voCliente.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voCliente.size() + " existentes");
        return voCliente;
	}


	/* ****************************************************************
	 * 			Métodos para manejar los OPERADORES
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un operador 
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
	public Operador adicionarOperador(String logIn,String tipoId, long numeroId,String relacionU,int numeroRNT, Date vencimientoRNT, String registroSuperTurismo,Date vencimientoRegistroSuperTurismo,String categoria, String direccion, 
			Date horaApertura, Date horaCierre, int tiempoMinimo, double gananciaAnioActual, double gananciAnioCorrido, ArrayList habitaciones, ArrayList apartamentos)
	{
		log.info ("Adicionando operador " + logIn);
		Operador operador = pp.adicionarOperador(numeroId,logIn, tipoId, relacionU, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, gananciaAnioActual, gananciAnioCorrido, habitaciones, apartamentos);
        log.info ("Adicionando operador: " + operador);
        return operador;
	}
	
	/**
	 * Elimina un operador por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idOperador - El identificador del operador a eliminar
	 * @return El número de tuplas eliminadas (1 o 0)
	 */
	public long eliminarOperadorPorId (long idOperador)
	{
        log.info ("Eliminando operador por id: " + idOperador);
        long resp = pp.eliminarOperadorPorId(idOperador);
        log.info ("Eliminando operador por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	/**
	 * Encuentra un operador y su información básica, según su identificador
	 * @param id - El id del operador buscado
	 * @return Un objeto operador que corresponde con el id buscado y lleno con su información básica
	 * 			null, si un operador con dicho id no existe.
	 */
	public Operador darOperadorPorId (long id)
	{
        log.info ("Dar información de un operador por id: " + id);
        Operador operador = pp.darOperadorPorId(id);
        log.info ("Buscando operador por id: " + operador != null ? operador : "NO EXISTE");
        return operador;
	}
	
	/**
	 * Encuentra todos los operadores en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Operador con todos los operadores que conoce la aplicación, llenos con su información básica
	 */
	public List<Operador> darOperadores ()
	{
        log.info ("Consultando Operadores");
        List<Operador> operador = pp.darOperadores();	
        log.info ("Consultando Operadores: " + operador.size() + " Operadores existentes");
        return operador;
	}

	/**
	 * Encuentra todos los operadores en Alohandes y los devuelve como una lista de VOOperador.
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOOperador con todos los operadores que conoce la aplicación, llenos con su información básica
	 */
	public List<VOOperador> darVOOperadores ()
	{
		log.info ("Generando los VO de los operadores");       
        List<VOOperador> voOperador = new LinkedList<VOOperador> ();
        for (Operador beb : pp.darOperadores())
        {
        	voOperador.add (beb);
        }
        log.info ("Generando los VO de loss operadores: " + voOperador.size() + " existentes");
        return voOperador;
	}

	
	/* ****************************************************************
	 * 			Métodos para manejar las PROPIEDADES
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una propiedad
	 * Adiciona entradas al log de la aplicación
	 * Adiciona entradas al log de la aplicación
	 * @param pID identificador de la propiedad
	 * @param pCapacidad capacidad en numero de personas de la propiedad
	 * @param pTamanio tamaño de la propiedad en m2
	 * @param pPrecio precio por tiempo minimo de reserva de la propiedad
	 * @param pFecha fecha de registro de la propiedad en la aplicacion
	 * @param pDiasR dias que la propiedad ha estado reservada desde su registro en la aplicacion
	 * @param pPiso piso en el que se encuentra la propiedad
	 * @param pDireccion direccion en la que se encuentra la propiedad
	 */
	public Propiedad adicionarPropiedad(long pID, int pCapacidad, double pTamanio, double pPrecio, Date pFecha, int pDiasR, int pPiso, String pDireccion)
	{
        log.info ("Adicionando Propiedad: " + pID);
        Propiedad propiedad = pp.adicionarPropiedad(pID, pCapacidad, pPrecio, pTamanio, pDiasR, pPiso, pFecha, pDireccion);	
        log.info ("Adicionando propiedad: " + propiedad);
        return propiedad;
	}
	
	/**
	 * Elimina una propiedad por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idPropiedad - El id de la propiedad a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarPropiedadPorId (long idPropiedad)
	{
		log.info ("Eliminando Propiedad por id: " + idPropiedad);
        long resp = pp.eliminarPropiedadPorId(idPropiedad);		
        log.info ("Eliminando Propiedad por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos las propiedades en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Propiedad con todos las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<Propiedad> darPropiedad()
	{
		log.info ("Consultando Propiedades");
        List<Propiedad> propiedades = pp.darPropiedades();	
        log.info ("Consultando Propiedades: " + propiedades.size() + " existentes");
        return propiedades;
	}
	/**
	 * Encuentra una propiedad y su información básica, según su identificador
	 * @param id - El id de la propiedad buscada
	 * @return Un objeto cliente que corresponde con el id buscado y lleno con su información básica
	 * 			null, si una propiedad con dicho id no existe.
	 */
	public Propiedad darPropiedadPorId (long id)
	{
        log.info ("Dar información de una propiedad por id: " + id);
        Propiedad propiedad = pp.darPropiedadesPorId(id);
        log.info ("Buscando propiedad por id: " + propiedad != null ? propiedad : "NO EXISTE");
        return propiedad;
	}

	/**
	 * Encuentra todos las propiedades en Alohandes y los devuelve como una lista de VOPropiedad
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOPropiedad con todas las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<VOPropiedad> darVOPropiedad()
	{
		log.info ("Generando los VO de Propiedad");        
        List<VOPropiedad> voPropiedad = new LinkedList<VOPropiedad> ();
        for (Propiedad tb : pp.darPropiedades())
        {
        	voPropiedad.add (tb);
        }
        log.info ("Generando los VO de la Propiedad: " + voPropiedad.size() + " existentes");
        return voPropiedad;
	}
	
		
	/* ****************************************************************
	 * 			Métodos para manejar las APARTAMENTO
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un apartamento
	 * Adiciona entradas al log de la aplicación
	 * Adiciona entradas al log de la aplicacióN
	 * Al adicionar apartamento se adiciona como propiedad
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
	public Propiedad adicionarApartamento(int pId, int pCapacidad, double pTamanio, 
			double pPrecio, Date pFecha, int pDiasR, int pPiso, String pDireccion, 
			boolean pAmueblado, int pHabitaciones, String pDMenaje, Date pVenceSeguro, 
			String pDSeguro, long pOperador)
	{
		//Se adiciona la propiedad porque el apartamento es un tipo de propiedad
		
		adicionarPropiedad( pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion);
		
        log.info ("Adicionando Apartamento: " + pId);
        Propiedad apartamento = pp.adicionarApartamento( pId, pCapacidad, pTamanio, pPrecio,
        		pFecha, pDiasR, pPiso, pDireccion, pAmueblado, pHabitaciones, pDMenaje,
        		pVenceSeguro, pDSeguro, pOperador);		
        log.info ("Adicionando apartamento: " + apartamento);
        return apartamento;
	}
	
	/**
	 * Elimina un apartamento por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idApartamento - El id del Apartamento a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarApartamentoPorId (long idApartamento)
	{
		log.info ("Eliminando Apartamento por id: " + idApartamento);
        long resp = pp.eliminarApartamentoPorId(idApartamento);		
        log.info ("Eliminando Apartamento por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los Apartamento en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Apartamento con todos las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<Apartamento> darApartamento()
	{
		log.info ("Consultando Apartamento");
        List<Apartamento> apartamento = pp.darApartamentos();	
        log.info ("Consultando Apartamento: " + apartamento.size() + " existentes");
        return apartamento;
	}

	/**
	 * Encuentra un apartamento y su información básica, según su identificador
	 * @param id - El id del apartamento buscado
	 * @return Un objeto apartamento que corresponde con el id buscado y lleno con su información básica
	 * 			null, si un apartamento con dicho id no existe.
	 */
	public Apartamento darApartamentoPorId (long id)
	{
        log.info ("Dar información de un apartamento por id: " + id);
        Apartamento apartamento = pp.darApartamentosPorId(id);
        log.info ("Buscando apartamento por id: " + apartamento != null ? apartamento : "NO EXISTE");
        return apartamento;
	}
	/**
	 * Encuentra todos las propiedades en Alohandes y los devuelve como una lista de VOApartamento
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOPropiedad con todas las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<VOApartamento> darVOApartamento()
	{
		log.info ("Generando los VO de Apartamento");        
        List<VOApartamento> voApartamento = new LinkedList<VOApartamento> ();
        for (Apartamento tb : pp.darApartamentos())
        {
        	voApartamento.add (tb);
        }
        log.info ("Generando los VO de Apartamento: " + voApartamento.size() + " existentes");
        return voApartamento;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar la HABITACION
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una habitación
	 * Adiciona entradas al log de la aplicación
	 * Adiciona entradas al log de la aplicacióN
	 * Al adicionar habitación se adiciona como propiedad
	 *
	 * @param pIndiv
	 * @param pEsquema
	 * @param pTipo
	 */
	public Habitacion adicionarHabitacion(int pID, int pCapacidad, double pTamanio, double pPrecio, Date pFecha, int pDiasR, int pPiso, String pDireccion, boolean pIndiv, String pEsquema, int pTipo, long pOperador)
	{
		//Se adiciona la propiedad porque el apartamento es un tipo de propiedad
		
		adicionarPropiedad( pID, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion);
		
        log.info ("Adicionando Habitacion: " + pID);
        Habitacion habitacion = pp.adicionarHabitacion(pID, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pDireccion, pIndiv, pEsquema, pTipo, pOperador);
        log.info ("Adicionando Habitacion: " + habitacion);
        return habitacion;
	}
	
	/**
	 * Elimina una habitacion por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idHabitacion - El id del Habitacion a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarHabitacionPorId (long idHabitacion)
	{
		log.info ("Eliminando Habitacion por id: " + idHabitacion);
        long resp = pp.eliminarHabitacionPorId(idHabitacion);		
        log.info ("Eliminando Habitacion por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	/**
	 * Encuentra una habitacion y su información básica, según su identificador
	 * @param id - El id de la habitacion buscado
	 * @return Un objeto habitacion que corresponde con el id buscado y lleno con su información básica
	 * 			null, si una habitacion con dicho id no existe.
	 */
	public Habitacion darHabitacionPorId (long id)
	{
        log.info ("Dar información de un habitacion por id: " + id);
        Habitacion habitacion = pp.darHabitacionesPorId(id);
        log.info ("Buscando habitacion por id: " + habitacion != null ? habitacion : "NO EXISTE");
        return habitacion;
	}
	
	/**
	 * Encuentra todas las Habitaciones en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Apartamento con todos las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<Habitacion> darHabitacion()
	{
		log.info ("Consultando Habitacion");
        List<Habitacion> habitacion = pp.darHabitaciones();	
        log.info ("Consultando Habitacion: " + habitacion.size() + " existentes");
        return habitacion;
	}

	/**
	 * Encuentra todos las propiedades en Alohandes y los devuelve como una lista de VOApartamento
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOPropiedad con todas las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<VOHabitacion> darVOHabitacion()
	{
		log.info ("Generando los VO de Habitacion");        
        List<VOHabitacion> voHabitacion = new LinkedList<VOHabitacion> ();
        for (Habitacion tb : pp.darHabitaciones())
        {
        	voHabitacion.add(tb);
        }
        log.info ("Generando los VO de Habitacion: " + voHabitacion.size() + " existentes");
        return voHabitacion;
	}

	
	/* ****************************************************************
	 * 			Métodos para manejar las RESERVAS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una reserva. 
	 * Adiciona entradas al log de la aplicación
	 * @param fechaInicio - fecha de inicio de la reserva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayor o igual a 1)
	 * @param finCancelacionOportuna - fecha final para cancelar la reserva.
	 * @param porcentajeAPagar - porcentaje a pagar de la reserva.
	 * @param montoTotal - monto total de la reserva.
	 * @return El objeto Reserva adicionado. null si ocurre alguna Excepción
	 */
	public Reserva adicionarReserva (Date fechaInicio, Date fechaFin, int personas, Date finCancelacionOportuna,
			double porcentajeAPagar, double montoTotal)
	{
        log.info ("Adicionando reserva: " );
        Reserva reserva = pp.adicionarReserva(fechaInicio, fechaFin, personas, finCancelacionOportuna, porcentajeAPagar, montoTotal);
        log.info ("Adicionando reserva: " + reserva);
        return reserva;
	}
	
	/**
	 * Elimina una reserva por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - El identificador de la reserva a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarReservasPorId (long idReserva)
	{
        log.info ("Eliminando reserva por id: " + idReserva);
        long resp = pp.eliminarRservaPorId(idReserva);
        log.info ("Eliminando reserva: " + resp);
        return resp;
	}
	
	/**
	 * Encuentra todas las reservas en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Reserva con todas las reservas que conoce la aplicación, llenos con su información básica
	 */
	public List<Reserva> darReservas ()
	{
        log.info ("Listando Reservas");
        List<Reserva> reservas = pp.darRerservas();	
        log.info ("Listando Reservas: " + reservas.size() + " Reservas existentes");
        return reservas;
	}
<<<<<<< HEAD
	
	public List<Reserva> darReservasActivasApartamento(long apartamento)
	{
		log.info ("Listando Reservas Activas de apartamento: "+ apartamento);
        List<Reserva> reservas = pp.darRerservasActivasApartamento(apartamento);	
        log.info ("Listando Reservas Activas de apartamento: " + apartamento
        		+" Hay "+ reservas.size() + " Reservas existentes");
        return reservas;
		
=======
	/**
	 * Encuentra una reserva y su información básica, según su identificador
	 * @param id - El id de la reserva buscado
	 * @return Un objeto reserva que corresponde con el id buscado y lleno con su información básica
	 * 			null, si una reserva con dicho id no existe.
	 */
	public Reserva darReservasPorId (long id)
	{
        log.info ("Dar información de un reserva por id: " + id);
        Reserva reserva = pp.darReservaPorId(id);
        log.info ("Buscando reserva por id: " + reserva != null ? reserva : "NO EXISTE");
        return reserva;
>>>>>>> 2793bde761c924575aeb6c4b1381bf0264bbedb0
	}

	/**
	 * Encuentra todas las reservas en Alohandes y los devuelve como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Reserva con todos las reservas que conoce la aplicación, llenos con su información básica
	 */
	public List<VOReserva> darVOReservas ()
	{
		log.info ("Generando los VO de Reservas");
		List<VOReserva> voReservas = new LinkedList<VOReserva> ();
		for (Reserva bar: pp.darRerservas())
		{
			voReservas.add (bar);
		}
		log.info ("Generando los VO de Reservas: " + voReservas.size () + " Reservas existentes");
		return voReservas;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las RESERVAS COLECTIVAS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una reserva colectiva. 
	 * Adiciona entradas al log de la aplicación
	 * @param fechaInicio - fecha de inicio de la reserva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayoe o igual a 1)
	 * @param finCancelacionOportuna - fecha final para cancelar la reserva.
	 * @param porcentajeAPagar - porcentaje a pagar de la reserva.
	 * @param montoTotal - monto total de la reserva.
	 * @return El objeto Reserva adicionado. null si ocurre alguna Excepción
	 */
	public Reserva adicionarReserva (long pId, int pCantidad, String pTipo, Date pInicio, int pDuracion)
	{
        log.info ("Adicionando reserva colectiva: " );
        Reserva reserva = null; //pp.adicionarReserva(pId, pCantidad, pTipo, pInicio, pDuracion);
        log.info ("Adicionando reserva colectiva: " + reserva);
        return reserva;
	}
	
	/**
	 * Elimina una reserva colectiva por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idReserva - El identificador de la reserva colectiva a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarReservaColectivaPorId (long idReserva)
	{
        log.info ("Eliminando reserva colectiva por id: " + idReserva);
        long resp = pp.eliminarRservaColectivaPorId(idReserva);
        log.info ("Eliminando reserva colectiva: " + resp);
        return resp;
	}
	
	/**
	 * Encuentra todas las reservas colectivas en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Reserva con todas las reservas colectivas que conoce la aplicación, llenos con su información básica
	 */
	public List<ReservaColectiva> darReservasColectivas()
	{
        log.info ("Listando Reservas Colectivas");
        List<ReservaColectiva> reservas = pp.darRerservasColectivas();	
        log.info ("Listando Reservas Colectivas: " + reservas.size() + " Reservas colectivas existentes");
        return reservas;
	}

	/**
	 * Encuentra todas las reservas en Alohandes y los devuelve como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Reserva con todos las reservas que conoce la aplicación, llenos con su información básica
	 */
	public List<VOReservaColectiva> darVOReservaColectiva ()
	{
		log.info ("Generando los VO de Reservas");
		List<VOReservaColectiva> voReservas = new LinkedList<VOReservaColectiva> ();
		for (ReservaColectiva bar: pp.darRerservasColectivas())
		{
			voReservas.add (bar);
		}
		log.info ("Generando los VO de Reservas Colectivas: " + voReservas.size () + " Reservas colectivas existentes");
		return voReservas;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los SERVICIOS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una propiedad
	 * Adiciona entradas al log de la aplicación
	 * Adiciona entradas al log de la aplicación
	 * @param pID identificador del servicio
	 * @param pTipo tipo de servicio
	 * @param pPrecio precio del servicio
	 * @param pIntervalo intervalo de pago del servicio
	 */
	public Servicio adicionarServicio(long pID, String pTipo, double pPrecio, int pIntervalo)
	{
        log.info ("Adicionando Servicio: " + pID);
        Servicio servicio = pp.adicionarServicio(pID, pTipo, pPrecio, pIntervalo);		
        log.info ("Adicionando servicio: " + servicio);
        return servicio;
	}
	
	/**
	 * Elimina una Servicio por su identificador
	 * Adiciona entradas al log de la aplicación
	 * @param idServicio - El id de la Servicio a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarServicioPorId (long idServicio)
	{
		log.info ("Eliminando Servicio por id: " + idServicio);
        long resp = pp.eliminarServicioPorId(idServicio);		
        log.info ("Eliminando Servicio por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los servicios en Alohandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos servicios con todos las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<Servicio> darServicio()
	{
		log.info ("Consultando Servicio");
        List<Servicio> servicio = pp.darServicio();	
        log.info ("Consultando servicios: " + servicio.size() + " existentes");
        return servicio;
	}
	/**
	 * Encuentra un servicio y su información básica, según su identificador
	 * @param id - El id del servicio buscado
	 * @return Un objeto servicio que corresponde con el id buscado y lleno con su información básica
	 * 			null, si un servicio con dicho id no existe.
	 */
	public Servicio darServicioPorId (long id)
	{
        log.info ("Dar información de un servicio por id: " + id);
        Servicio servicio = pp.darServicioPorId(id);
        log.info ("Buscando servicio por id: " + servicio != null ? servicio : "NO EXISTE");
        return servicio;
	}

	/**
	 * Encuentra todos las servicios en Alohandes y los devuelve como una lista de VOServicio
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOServicio con todas las propiedades que conoce la aplicación, llenos con su información básica
	 */
	public List<VOServicio> darVOServicio()
	{
		log.info ("Generando los VO de Servicio");        
        List<VOServicio> voServicio = new LinkedList<VOServicio> ();
        for (Servicio tb : pp.darServicio())
        {
        	voServicio.add (tb);
        }
        log.info ("Generando los VO de la Servicio: " + voServicio.size() + " existentes");
        return voServicio;
	}
	

	
	
	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Alohandes
	 * @return Un arreglo con 8 números que indican el número de tuplas borradas en las tablas APARTAMENTO, CLIENTE, HABITACION, OPERADOR,
	 * PROPIEDAD, RESERVA, SERVICIO y USUARIO, respectivamente
	 */
	public long [] limpiarAlohandes ()
	{
        log.info ("Limpiando la BD de Alohandes");
        long [] borrrados = pp.limpiarAlohandes();	
        log.info ("Limpiando la BD de Alohandes: Listo!");
        return borrrados;
	}
}
