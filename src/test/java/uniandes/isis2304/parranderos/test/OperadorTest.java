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
import uniandes.isis2304.alohandes.negocio.VOOperador;

public class OperadorTest {

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
	 * 			Métodos de prueba para la tabla Operador - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla cliente
	 * 1. Adicionar un operador
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3. Borrar un operador por su identificador
	 */
    @Test
	public void CRDOperadorTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre Operador");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de Operador incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de Operador incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOOperador> lista = alohandes.darVOOperadores();
			assertEquals ("No debe haber Operadores creados!!", 0, lista.size ());

			// Lectura de los clientes con un cliente adicionado
			String logIn = "Vino tinto";
			String tipoId = "Cedula";
			String relacionU = "Estudiante";
			String medioPago = "Tarjeta de credito";
			int numeroId = 001;
			int numeroRNT = 002;
			Date vencimientoRNT = new Date(2025, 04, 12);
			String registroSuperTurismo = "";
			Date vencimientoRegistroSuperTurismo = new Date(2025, 04, 12);
			String categoria = "";
			String direccion = "";
			Date horaApertura = new Date(2020, 12, 01);
			Date horaCierre = new Date(2021, 12, 01); 
			int reservas = 0;
			int tiempoMinimo = 3;
			VOOperador operador = alohandes.adicionarOperador(logIn, tipoId, numeroId, relacionU, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, 0, 0, null, null);
			lista = alohandes.darVOOperadores();
			assertEquals ("Debe haber un operador creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", operador, lista.get (0));

			// Lectura de los clientes con dos clientes adicionados
			String logIn2 = "Vino tinto";
			String tipoId2 = "Cedula";
			String relacionU2 = "Estudiante";
			String medioPago2 = "Tarjeta de credito";
			int numeroId2 = 002;
			int numeroRNT2 = 002;
			Date vencimientoRNT2 = new Date(2025, 04, 12);
			String registroSuperTurismo2 = "";
			Date vencimientoRegistroSuperTurismo2 = new Date(2025, 04, 12);
			String categoria2 = "";
			String direccion2 = "";
			Date horaApertura2 = new Date(2020, 12, 01);
			Date horaCierre2 = new Date(2021, 12, 01); 
			int reservas2 = 0;
			int tiempoMinimo2 = 3;
			VOOperador operador2 = alohandes.adicionarOperador(logIn2, tipoId2, numeroId2, relacionU2, numeroRNT2, vencimientoRNT2, registroSuperTurismo2, vencimientoRegistroSuperTurismo2, categoria2, direccion2, horaApertura2, horaCierre2, tiempoMinimo2, 0, 0, null, null);
			lista = alohandes.darVOOperadores();
			assertEquals ("Debe haber dos operadores creados !!", 2, lista.size ());
			assertTrue ("El primer operador adicionado debe estar en la tabla", operador.equals (lista.get (0)) || operador.equals (lista.get (1)));
			assertTrue ("El segundo operador adicionado debe estar en la tabla", operador2.equals (lista.get (0)) || operador2.equals (lista.get (1)));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = alohandes.eliminarClientePorId(numeroId);
			assertEquals ("Debe haberse eliminado un operador !!", 1, tbEliminados);
			lista = alohandes.darVOOperadores();
			assertEquals ("Debe haber un solo cliente !!", 1, lista.size ());
			assertFalse ("El primer operador adicionado NO debe estar en la tabla", operador.equals (lista.get (0)));
			assertTrue ("El segundo operador adicionado debe estar en la tabla", operador2.equals (lista.get (0)));
			
			// Prueba de eliminación de un tipo de bebida, dado su identificador
			tbEliminados = alohandes.eliminarClientePorId(numeroId2);
			assertEquals ("Debe haberse eliminado un operador!!", 1, tbEliminados);
			lista = alohandes.darVOOperadores();
			assertEquals ("La tabla debió quedar vacía !!", 0, lista.size ());
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla Cliente.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla Cliente");
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
	public void unicidadOperadorTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del nombre del operador");
			alohandes = new Alohandes (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de operador incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de operador incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los clienets con la tabla vacía
			List <VOOperador> lista = alohandes.darVOOperadores();
			assertEquals ("No debe haber operadores creados!!", 0, lista.size ());

			// Lectura de los clientes con un cliente adicionado
			String logIn2 = "Vino tinto";
			String tipoId2 = "Cedula";
			String relacionU2 = "Estudiante";
			String medioPago2 = "Tarjeta de credito";
			int numeroId2 = 001;
			int numeroRNT2 = 002;
			Date vencimientoRNT2 = new Date(2025, 04, 12);
			String registroSuperTurismo2 = "";
			Date vencimientoRegistroSuperTurismo2 = new Date(2025, 04, 12);
			String categoria2 = "";
			String direccion2 = "";
			Date horaApertura2 = new Date(2020, 12, 01);
			Date horaCierre2 = new Date(2021, 12, 01); 
			int reservas2 = 0;
			int tiempoMinimo2 = 3;
			VOOperador operador2 = alohandes.adicionarOperador(logIn2, tipoId2, numeroId2, relacionU2, numeroRNT2, vencimientoRNT2, registroSuperTurismo2, vencimientoRegistroSuperTurismo2, categoria2, direccion2, horaApertura2, horaCierre2, tiempoMinimo2, 0, 0, null, null);
			lista = alohandes.darVOOperadores();
			assertEquals ("Debe haber un cliente creado !!", 1, lista.size ());

			VOOperador operador = alohandes.adicionarOperador(logIn2, tipoId2, numeroId2, relacionU2, numeroRNT2, vencimientoRNT2, registroSuperTurismo2, vencimientoRegistroSuperTurismo2, categoria2, direccion2, horaApertura2, horaCierre2, tiempoMinimo2, reservas2, tiempoMinimo2, null, null);
			assertNull ("No puede adicionar dos operadores con el mismo id !!", operador);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla Operador.\n";
			msg += "Revise el log de alohandes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla Cliente");
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
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "OperadorTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
	
}
