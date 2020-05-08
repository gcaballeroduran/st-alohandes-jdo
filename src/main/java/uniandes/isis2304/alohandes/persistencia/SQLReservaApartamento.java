package uniandes.isis2304.alohandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.negocio.Reserva;

public class SQLReservaApartamento {
	

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
	public SQLReservaApartamento(PersistenciaAlohandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un RESERVAAPARTAMENTO a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idApartamento - El identificador del apartamento
	 * @param idReserva - El identificador de la reserva
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarReservaApartamento(PersistenceManager pm, long idApartamento, long idReserva) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReservaHabitacion() + "(id_Apartamento, id_Reserva) values (?, ?)");
        q.setParameters(idApartamento, idReserva);
        return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UN RESERVAAPARTAMENTO de la base de datos de Parranderos, por sus identificador
	 * @param pm - El manejador de persistencia
	 * @param idApartamento - El identificador del apartamento
	 * @param idReserva - El identificador de la reserva
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarReservaApartamento(PersistenceManager pm, long idApartamento, long idReserva)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaReservaHabitacion()+ " WHERE id_Apartamento = ? AND id_Reserva = ?");
        q.setParameters(idApartamento, idReserva);
        return (long) q.executeUnique();
	}

	
	public void cambiarReservaApartamento(PersistenceManager pm, long idR, long idP)
	{
		
		
		String sql = "UPDATE " + pp.darTablaReservaApartamento();
		sql += "SET idApartamento = "+idP;
		sql += "WHERE idReserva= "+ idR;
		Query q = pm.newQuery(SQL, sql);
		
		
		q.setResultClass(Habitacion.class);
	}

}
