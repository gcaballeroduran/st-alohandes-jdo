package uniandes.isis2304.parranderos.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.sql.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.alohandes.negocio.Alohandes;
import uniandes.isis2304.alohandes.negocio.VOReserva;

public class ReservaTest {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(ClienteTest.class.getName());
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
	 */
	private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
	/**
	 * La clase que se quiere probar
	 */
    private Alohandes alohandes;
	
    /* ****************************************************************
	 * 			Métodos de prueba para la tabla Reserva - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba los apartamentos sobre la tabla Reserva
	 * 1. Adicionar un Reserva
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar una Reserva por su identificador
	 */
    @Test
	public void CRDReservaTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre Reserva");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de Reserva incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de Reserva incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOReserva> lista = alohandes.darVOReservas();
			assertEquals ("No debe haber Reservas creados!!", 0, lista.size ());

			// Lectura de los clientes con una habitacion adicionado
			int personas = 5;
			double porcentajeAPagar = 60;
			double montoTotal = 60000;
			Date fechaInicio = new Date(2020, 05, 12);
			Date fechaFin = new Date(2020, 05, 20);
			Date finCancelacionOportuna = new Date(2020, 05, 10);
			long idProp = 1234567890;
			VOReserva reserva = alohandes.adicionarReserva(fechaInicio, fechaFin, personas, finCancelacionOportuna, porcentajeAPagar, montoTotal, idProp);;
			lista = alohandes.darVOReservas();
			assertEquals ("Debe haber un Reserva creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", reserva, lista.get (0));

			// Lectura de los clientes con dos clientes adicionados
			int personas2 = 6;
			double porcentajeAPagar2 = 64;
			double montoTotal2 = 65500;
			Date fechaInicio2 = new Date(2020, 05, 01);
			Date fechaFin2 = new Date(2020, 05, 10);
			Date finCancelacionOportuna2 = new Date(2020, 05, 10);
			long idProp2 = 1234567891;
			VOReserva reserva2 = alohandes.adicionarReserva(fechaInicio2, fechaFin2, personas2, finCancelacionOportuna2, porcentajeAPagar2, montoTotal2, idProp2);;
			lista = alohandes.darVOReservas();
			assertEquals ("Debe haber dos Reservas creados !!", 2, lista.size ());
			assertTrue ("El primer Servicio adicionado debe estar en la tabla", reserva.equals (lista.get (0)) || reserva.equals (lista.get (1)));
			assertTrue ("El segundo Reserva adicionado debe estar en la tabla", reserva2.equals (lista.get (0)) || reserva2.equals (lista.get (1)));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = alohandes.eliminarHabitacionPorId(reserva.getId());
			assertEquals ("Debe haberse eliminado una Servicio !!", 1, tbEliminados);
			lista = alohandes.darVOReservas();
			assertEquals ("Debe haber un solo Reserva !!", 1, lista.size ());
			assertFalse ("El primer Reserva adicionado NO debe estar en la tabla", reserva.equals (lista.get (0)));
			assertTrue ("El segundo Reserva adicionado debe estar en la tabla", reserva2.equals (lista.get (0)));
			
			// Prueba de eliminación de un tipo de bebida, dado su identificador
			tbEliminados = alohandes.eliminarClientePorId(reserva2.getId());
			assertEquals ("Debe haberse eliminado un Reserva!!", 1, tbEliminados);
			lista = alohandes.darVOReservas();
			assertEquals ("La tabla debió quedar vacía !!", 0, lista.size ());
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de Reserva sobre la tabla habitacion.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla Reserva");
		}
		finally
		{
			alohandes.limpiarAlohandes();
    		alohandes.cerrarUnidadPersistencia ();    		
		}
	}


	/* ****************************************************************
	 * 			Métodos de configuración
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración de tablas válido");
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "ReservaTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }

}
