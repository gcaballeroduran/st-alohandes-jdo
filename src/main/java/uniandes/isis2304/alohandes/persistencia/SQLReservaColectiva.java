package uniandes.isis2304.alohandes.persistencia;

import java.sql.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.ReservaColectiva;

public class SQLReservaColectiva {

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
	public SQLReservaColectiva (PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	  * Crea y ejecuta la sentencia SQL para adicionar una RESERVA_COLECTIVA a la base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @param id - id de la reserva.
	 * @param fechaInicio - fecha de inicio de la reserva colectiva.
	 * @param duracion - duracion en dias de la rerserva colectiva.
	 * @param cantidad - numero de propiedades que desean reservar.(Mayor o igual a 1)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarReserva(PersistenceManager pm,long id, Date fechaInicio, int duracion, int cantidad ){
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReservaColectiva() + "(id, fechaInicio, duracion, cantidad)"
				+ " values (?, ?, ?, ?)");
        q.setParameters(id, fechaInicio, duracion, cantidad);
        return (long) q.executeUnique();
		
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar RESERVAS de la base de datos de Alohandes, por su id
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarReservaColectivaPorId (PersistenceManager pm, long idReserva)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReservaColectiva() + " WHERE id = ?");
        q.setParameters(idReserva);
        return (long) q.executeUnique();            
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UN RESERVA COLECTIVA 
	 * de la base de datos de Alohandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idReserva - id de la reserva
	 * @return El objeto RESERVA COLECTIVA que tiene el identificador dado
	 */
	public ReservaColectiva darReservaPorId (PersistenceManager pm, long idReserva) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReservaColectiva() + " WHERE id = ?");
		q.setResultClass(ReservaColectiva.class);
		q.setParameters(idReserva);
		return (ReservaColectiva) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LAS RESERVA de la 
	 * base de datos de Alohandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos RESERVA
	 */
	public List<ReservaColectiva> darReservasColectivas(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaReservaColectiva());
		q.setResultClass(ReservaColectiva.class);
		return (List<ReservaColectiva>) q.executeList();
	}
	
	
}
