package uniandes.isis2304.alohandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.negocio.Reserva;

public class SQLReservaHabitacion {
	

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
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
	public SQLReservaHabitacion(PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un RESERVAHABITACION a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idHabitacion - El identificador de la habitacion
	 * @param idReserva - El identificador de la reserva
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarReservaHabitacion(PersistenceManager pm, long idHabitacion, long idReserva) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReservaHabitacion() + "(id_Habitacion, id_Reserva) values (?, ?)");
        q.setParameters(idHabitacion, idReserva);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN RESERVAHABITACION de la base de datos de Parranderos, por sus identificador
	 * @param pm - El manejador de persistencia
	 * @param idHabitacion - El identificador del habitacion
	 * @param idReserva - El identificador de la reserva
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarReservaHabitacion(PersistenceManager pm, long idReserva)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReservaHabitacion()+ " WHERE id_Reserva = ?");
        q.setParameters( idReserva);
        return (long) q.executeUnique();
	}

	public void cambiarReservaHabitacion(PersistenceManager pm, long idR, long idP)
	{
		String sql = "UPDATE " + pp.darTablaReservaHabitacion();
		sql += "SET idHabitacion = "+idP;
		sql += "WHERE idReserva= "+ idR;
		Query q = pm.newQuery(SQL, sql);
		
		q.setResultClass(Habitacion.class);
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public long darIdReservaPorIdHabitacion (PersistenceManager pm, long idHabitacion) 
	{
		Query q = pm.newQuery(SQL, "SELECT idReserva FROM " + pp.darTablaReservaHabitacion() + " WHERE idApartamento = ?");
		q.setResultClass(Long.class);
		q.setParameters(idHabitacion);
		return (long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA de la 
	 * base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA que tiene el identificador dado
	 */
	public long darIdHabitacionPorIdReserva (PersistenceManager pm, long idReserva) 
	{
		Query q = pm.newQuery(SQL, "SELECT idApartamento FROM " + pp.darTablaReservaHabitacion() + " WHERE idReserva = ?");
		q.setResultClass(Long.class);
		q.setParameters(idReserva);
		return (long) q.executeUnique();
	}

}
