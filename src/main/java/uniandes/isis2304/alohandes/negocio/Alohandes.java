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
import uniandes.isis2304.parranderos.persistencia.PersistenciaAlohandes;

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
	public Cliente adicionarCliente (String logIn,TipoIdentificacion tipoId, TipoCliente relacionU, String medioPago, int reservas)
	{
        log.info ("Adicionando Cliente: " + logIn);
        Cliente cliente = pp.adicionarCliente(logIn, tipoId,  relacionU, medioPago, reservas);		
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
	public Operador adicionarOperador(String logIn,TipoIdentificacion tipoId, int numeroId,TipoCliente relacionU,int numeroRNT, Date vencimientoRNT, String registroSuperTurismo,Date vencimientoRegistroSuperTurismo,Modalidad categoria, String direccion, 
			Date horaApertura, Date horaCierre, int tiempoMinimo, double gananciaAnioActual, double gananciAnioCorrido, ArrayList habitaciones, ArrayList apartamentos)
	{
		log.info ("Adicionando operador " + logIn);
		Operador operador = pp.adicionarOperador(logIn, tipoId, relacionU, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, gananciaAnioActual, gananciAnioCorrido, habitaciones, apartamentos);
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
	public Usuario adicionarUsuario (String logIn,TipoIdentificacion tipoId, int numeroId, TipoCliente relacionU)
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
	public Usuario darUsuarioPorLogin (long login)
	{
        log.info ("Dar información de un usuario por logIn: " + login);
        Usuario usuario = pp.darUsuarioPorId(login);
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
	 * 			Métodos para manejar las RESERVAS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una reserva. 
	 * Adiciona entradas al log de la aplicación
	 * @param fechaInicio - fecha de inicio de la reserva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayoe o igual a 1)
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
