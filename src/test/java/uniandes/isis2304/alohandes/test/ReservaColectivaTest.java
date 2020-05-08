package uniandes.isis2304.alohandes.test;

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
import uniandes.isis2304.alohandes.negocio.VOReservaColectiva;

public class ReservaColectivaTest {

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
	 * 			Métodos de prueba para la tabla ReservaColectiva - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba los apartamentos sobre la tabla ReservaColectiva
	 * 1. Adicionar un Reserva
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar una Reserva por su identificador
	 */
    @Test
	public void CRDReservaColectivaTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre ReservaColectiva");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de ReservaColectiva incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de ReservaColectiva incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOReservaColectiva> lista = alohandes.darVOReservaColectiva();
			assertEquals ("No debe haber ReservaColectiva creados!!", 0, lista.size ());

			// Lectura de los clientes con una habitacion adicionado
			int pDuracion = 5;
			int pCantidad = 4;
			int pId = 001;
			Date pInicio = new Date(2025, 04, 12);
			String pTipo ="";
			VOReservaColectiva reserva = alohandes.adicionarReserva(pId, pCantidad, pTipo, pInicio, pDuracion);
			lista = alohandes.darVOReservaColectiva();
			assertEquals ("Debe haber un ReservaColectiva creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", reserva, lista.get (0));

			// Lectura de los clientes con dos clientes adicionados
			int pDuracion2 = 3;
			int pCantidad2= 2;
			int pId2 = 002;
			Date pInicio2 = new Date(2025, 04, 12);
			String pTipo2 ="";
			VOReservaColectiva reserva2 = alohandes.adicionarReserva(pId2, pCantidad2, pTipo2, pInicio2, pDuracion2);
			lista = alohandes.darVOReservaColectiva();
			assertEquals ("Debe haber dos ReservaColectiva creados !!", 2, lista.size ());
			assertTrue ("El primer ReservaColectiva adicionado debe estar en la tabla", reserva.equals (lista.get (0)) || reserva.equals (lista.get (1)));
			assertTrue ("El segundo ReservaColectiva adicionado debe estar en la tabla", reserva2.equals (lista.get (0)) || reserva2.equals (lista.get (1)));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = alohandes.eliminarHabitacionPorId(reserva.getId());
			assertEquals ("Debe haberse eliminado una ReservaColectiva !!", 1, tbEliminados);
			lista = alohandes.darVOReservaColectiva();
			assertEquals ("Debe haber un solo ReservaColectiva !!", 1, lista.size ());
			assertFalse ("El primer ReservaColectiva adicionado NO debe estar en la tabla", reserva.equals (lista.get (0)));
			assertTrue ("El segundo ReservaColectiva adicionado debe estar en la tabla", reserva2.equals (lista.get (0)));
			
			// Prueba de eliminación de un tipo de bebida, dado su identificador
			tbEliminados = alohandes.eliminarClientePorId(reserva2.getId());
			assertEquals ("Debe haberse eliminado un ReservaColectiva!!", 1, tbEliminados);
			lista = alohandes.darVOReservaColectiva();
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

    /**
     * Método de prueba de la restricción de unicidad sobre el nombre de Cliente
     */
	@Test
	public void unicidadReservaColectivaTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del nombre de la ReservaColectiva");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Servicio incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de ReservaColectiva incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los apartamento con la tabla vacía
			List <VOReservaColectiva> lista = alohandes.darVOReservaColectiva();
			assertEquals ("No debe haber ReservasColectivas creados!!", 0, lista.size ());

			// Lectura de los apartamento con un cliente adicionado
			int pDuracion = 5;
			int pCantidad = 4;
			int pId = 001;
			Date pInicio = new Date(2025, 04, 12);
			String pTipo ="";
			VOReservaColectiva reserva = alohandes.adicionarReserva(pId, pCantidad, pTipo, pInicio, pDuracion);
			lista = alohandes.darVOReservaColectiva();
			assertEquals ("Debe haber un Servicio creado !!", 1, lista.size ());

			VOReservaColectiva reserva2 = alohandes.adicionarReserva(pId, pCantidad, pTipo, pInicio, pDuracion);
			assertNull ("No puede adicionar dos ReservaColectiva con el mismo id !!", reserva2);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla ReservaColectiva.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla Servicio");
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
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "ReservaColectivaTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }

	
}
