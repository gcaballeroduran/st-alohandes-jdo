package uniandes.isis2304.alohandes.persistencia;

import java.sql.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.negocio.Reserva;

public class SQLReserva {
	

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	
	private final static String SQL = PersistenciaAlohandes.SQL;
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAlohandes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLReserva (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	  * Crea y ejecuta la sentencia SQL para adicionar una RESERVA a la base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @param id - id de la reserva.
	 * @param fechaInicio - fecha de inicio de la reserva.
	 * @param fechaFin - fecha final de la reserva.
	 * @param personas - numero de personas que reservaron.(Mayoe o igual a 1)
	 * @param finCancelacionOportuna - fecha final para cancelar la reserva.
	 * @param porcentajeAPagar - porcentaje a pagar de la reserva.
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarReserva(PersistenceManager pm,long id, String fechaInicio, String fechaFin, int personas, String finCancelacionOportuna,
			double porcentajeAPagar, double montoTotal){
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReserva() + "(id, fecha_Inicio,fecha_Fin,personas,fin_Cancelacion_Oportuna,porcentaje_A_Pagar,monto_Total)"
				+ " values (?, TO_DATE(?), TO_DATE(?), ?,?,?,?)");
        q.setParameters(id, fechaInicio,fechaFin,personas,finCancelacionOportuna,porcentajeAPagar,montoTotal);
        return (long) q.executeUnique();
		
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar RESERVAS de la base de datos de Alohandes, por su id
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarReservaPorId (PersistenceManager pm, long idReserva)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "DELETE FROM " + pp.darTablaReserva() + " WHERE id = ?";
		sql += "COMMIT TRAN";
        Query q = pm.newQuery(SQL, sql);
        q.setParameters(idReserva);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public Reserva darReservaPorId (PersistenceManager pm, long idReserva) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pp.darTablaReserva() + " WHERE id = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		q.setParameters(idReserva);
		return (Reserva) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public List<Reserva> darReservaPorIdColectiva (PersistenceManager pm, long idReservaColectiva) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pp.darTablaReserva() + " WHERE Colectiva = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		q.setParameters(idReservaColectiva);
		return (List<Reserva>) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LAS RESERVA de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos RESERVA
	 */
	public List<Reserva> darReservas(PersistenceManager pm)
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pp.darTablaReserva();
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		return (List<Reserva>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public List<Reserva> darReservasApartamento(PersistenceManager pm, long apt) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pp.darTablaReserva() + " WHERE propiedad = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		q.setParameters(apt);
		return (List<Reserva>) q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public List<Reserva> darReservasActivasApartamento(PersistenceManager pm, long apt) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM "+ pp.darTablaReserva() + "r";
		sql += "INNER JOIN "+ pp.darTablaReservaApartamento() + "ap ON (idapartamento = " + apt +")";
		sql += "WHERE fechaInicio>=getDate() OR fechaFin<getDate()";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		q.setParameters(apt);
		return (List<Reserva>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public List<Reserva> darReservasHabitacion(PersistenceManager pm, long hab) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM " + pp.darTablaReserva() + " WHERE propiedad = ?";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		q.setParameters(hab);
		return null;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public List<Reserva> darReservasActivasHabitacion(PersistenceManager pm, long apt) 
	{
		String sql = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE"; 
		sql += "BEGIN TRAN";
		sql += "SELECT * FROM "+ pp.darTablaReserva() + "r";
		sql += "INNER JOIN "+ pp.darTablaReservaHabitacion() + "ap ON (idhabitacion = " + apt +")";
		sql += "WHERE fechaInicio>=getDate() OR fechaFin<getDate()";
		sql += "COMMIT TRAN";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Reserva.class);
		q.setParameters(apt);
		return (List<Reserva>) q.executeList();
	}

	
	

}
