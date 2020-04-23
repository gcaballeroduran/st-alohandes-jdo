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

package uniandes.isis2304.alohandes.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.alohandes.negocio.Alohandes;
import uniandes.isis2304.alohandes.negocio.Modalidad;
import uniandes.isis2304.alohandes.negocio.TipoCliente;
import uniandes.isis2304.alohandes.negocio.TipoIdentificacion;
import uniandes.isis2304.alohandes.negocio.VOCliente;
import uniandes.isis2304.alohandes.negocio.VOOperador;
import uniandes.isis2304.alohandes.negocio.VOReserva;
import uniandes.isis2304.alohandes.negocio.VOTipoBebida;
import uniandes.isis2304.alohandes.negocio.VOUsuario;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAlohandesApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAlohandesApp.class.getName());
	
	/**o) 
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private Alohandes alohandes;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazAlohandesApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        alohandes = new Alohandes (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de Cliente
	 *****************************************************************/
    /**
     * Adiciona un cliente con la información dada por el usuario
     * Se crea una nueva tupla de cliente en la base de datos, si un cliente con ese id no existía
     */
    public void adicionarCliente( )
    {
    	try 
    	{
    		String logIn = JOptionPane.showInputDialog (this, "logIn?", "logIn", JOptionPane.QUESTION_MESSAGE);
    		TipoIdentificacion tipoId =TipoIdentificacion.valueOf( JOptionPane.showInputDialog (this,"Tipo de identificacion?", " Adicionar :CARNET_U,CEDULA, PASAPORTE",JOptionPane.QUESTION_MESSAGE));
    		TipoCliente relacionU = TipoCliente.valueOf(JOptionPane.showInputDialog (this, "Tipo de cliente?", "Adicionar: ESTUDIANTE,EMPLEADO, PROFESOR,PROFESOR_VISITANTE ", JOptionPane.QUESTION_MESSAGE));
    		String medioPago = JOptionPane.showInputDialog (this, "Medio de pago?", "Adicionar medio de pago", JOptionPane.QUESTION_MESSAGE);
    		int reservas = Integer.parseInt(JOptionPane.showInputDialog (this, "Reservas?", "Adicionar reservas", JOptionPane.QUESTION_MESSAGE));
    		if (logIn != null && tipoId != null && relacionU != null && medioPago != null)
    		{
        		VOCliente c = alohandes.adicionarCliente(logIn, tipoId, relacionU, medioPago, reservas);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un cliente");
        		}
        		String resultado = "En adicionarCliente\n\n";
        		resultado += "Cliente adicionado exitosamente: " + c;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los cliente existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarCliente( )
    {
    	try 
    	{
			List <VOCliente> lista = alohandes.darVOTCliente();

			String resultado = "En listarCliente";
			resultado +=  "\n" + listarCliente(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el cliente con el identificador dado po el usuario
     * Cuando dicho cliente no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarClienteId( )
    {
    	try 
    	{
    		String idCliente = JOptionPane.showInputDialog (this, "Id del cliente?", "Borrar cliente por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idCliente != null)
    		{
    			long id = Long.valueOf (idCliente);
    			long clEliminados = alohandes.eliminarClientePorId(id);

    			String resultado = "En eliminar Clientes\n\n";
    			resultado += clEliminados + " Clientes eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    
	/* ****************************************************************
	 * 			CRUD de Operador
	 *****************************************************************/
    
    /**
     * Adiciona un Operador con la información dada por el usuario
     * Se crea una nueva tupla de Operador en la base de datos, si un Operador con ese id no existía
     */
    public void adicionarOperador( )
    {
    	try 
    	{
    		String logIn = JOptionPane.showInputDialog (this, "LogIn?", "Adicionar LogIn", JOptionPane.QUESTION_MESSAGE);
    		TipoIdentificacion tipoId =TipoIdentificacion.valueOf( JOptionPane.showInputDialog (this,"Tipo de identificacion?", " Adicionar :CARNET_U,CEDULA, PASAPORTE",JOptionPane.QUESTION_MESSAGE));
    		int numeroId = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero id?", "Adicionar numero id", JOptionPane.QUESTION_MESSAGE));
    		TipoCliente relacionU = TipoCliente.valueOf(JOptionPane.showInputDialog (this, "Tipo de cliente?", "Adicionar: ESTUDIANTE,EMPLEADO, PROFESOR,PROFESOR_VISITANTE ", JOptionPane.QUESTION_MESSAGE));
    		int numeroRNT = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero RNT?", "Adicionar numero RNT", JOptionPane.QUESTION_MESSAGE));
    		Date vencimientoRNT = Date.valueOf(JOptionPane.showInputDialog (this, "vencimiento RNT?", "Adicionar vencimiento RNT", JOptionPane.QUESTION_MESSAGE));
    		String registroSuperTurismo = JOptionPane.showInputDialog (this, "registro super turismo?", "Adicionar registro super turismo", JOptionPane.QUESTION_MESSAGE);
    		Date vencimientoRegistroSuperTurismo = Date.valueOf(JOptionPane.showInputDialog (this, "vencimiento super T?", "Adicionar vencimiento super T", JOptionPane.QUESTION_MESSAGE));
    		Modalidad categoria = Modalidad.valueOf(JOptionPane.showInputDialog (this, "categoria?", "Adicionar categoria: HOTEL,HOSTAL,P_NATURAL,APARTAMENTO,VECINOS,VIVIENDA_U", JOptionPane.QUESTION_MESSAGE));
    		String direccion = JOptionPane.showInputDialog (this, "direccion?", "Adicionar direccion", JOptionPane.QUESTION_MESSAGE);;
    		Date horaApertura = Date.valueOf(JOptionPane.showInputDialog (this, "hora de apertura?", "Adicionar hora de apertura", JOptionPane.QUESTION_MESSAGE));
    		Date horaCierre = Date.valueOf(JOptionPane.showInputDialog (this, "hora de cierre?", "Adicionar hora de cierre", JOptionPane.QUESTION_MESSAGE));
    		int tiempoMinimo = Integer.parseInt(JOptionPane.showInputDialog (this, "Tiempo minimo?", "Adicionar tiempo minimo", JOptionPane.QUESTION_MESSAGE));
    		if (logIn != null && tipoId != null && relacionU != null )
    		{
        		VOOperador c = alohandes.adicionarOperador(logIn, tipoId, numeroId, relacionU, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, 0, 0, null, null);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un operador con logIn: ");
        		}
        		String resultado = "En adicionarOperador\n\n";
        		resultado += "Cliente adicionado exitosamente: " + c;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los operadores existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarOperador( )
    {
    	try 
    	{
			List <VOOperador> lista = alohandes.darVOOperadores();

			String resultado = "En listarOperador";
			resultado +=  "\n" + listarOperador(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el cliente con el identificador dado po el usuario
     * Cuando dicho cliente no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarOperadorId( )
    {
    	try 
    	{
    		String idCliente = JOptionPane.showInputDialog (this, "Id del operador?", "Borrar operador por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idCliente != null)
    		{
    			long id = Long.valueOf (idCliente);
    			long clEliminados = alohandes.eliminarOperadorPorId(id);

    			String resultado = "En eliminar Operador\n\n";
    			resultado += clEliminados + " Operador eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
	/* ****************************************************************
	 * 			CRUD de Reserva
	 *****************************************************************/

    /**
     * Adiciona una reserva con la información dada por el usuario
     * Se crea una nueva tupla de reserva en la base de datos, si una reserva con ese id no existía
     */
    public void adicionarReserva( )
    {
    	try 
    	{
    		Date fechaInicio = Date.valueOf(JOptionPane.showInputDialog (this, "logIn?", "logIn", JOptionPane.QUESTION_MESSAGE));
    		Date fechaFin =Date.valueOf( JOptionPane.showInputDialog (this,"Tipo de identificacion?", " Adicionar :CARNET_U,CEDULA, PASAPORTE",JOptionPane.QUESTION_MESSAGE));
    		Date finCancelacionOportuna = Date.valueOf(JOptionPane.showInputDialog (this, "Tipo de cliente?", "Adicionar: ESTUDIANTE,EMPLEADO, PROFESOR,PROFESOR_VISITANTE ", JOptionPane.QUESTION_MESSAGE));
    		int personas = Integer.parseInt(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		double porcentajeAPagar = Double.parseDouble(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		double montoTotal = Double.parseDouble(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		if (fechaInicio != null && fechaFin != null && finCancelacionOportuna != null && personas != 0 )
    		{
        		VOReserva c = alohandes.adicionarReserva(fechaInicio, fechaFin, personas, finCancelacionOportuna, porcentajeAPagar, montoTotal);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un usuario con id: " );
        		}
        		String resultado = "En adicionarReserva\n\n";
        		resultado += "Cliente adicionado exitosamente: " + c;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los reservas existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarReserva( )
    {
    	try 
    	{
			List <VOReserva> lista = alohandes.darVOReservas();

			String resultado = "En listarReserva";
			resultado +=  "\n" + listarReserva(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el cliente con el identificador dado po el usuario
     * Cuando dicho cliente no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarReservaId( )
    {
    	try 
    	{
    		String idRerserva = JOptionPane.showInputDialog (this, "Id de la reserva?", "Borrar reserva por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idRerserva != null)
    		{
    			long id = Long.valueOf (idRerserva);
    			long clEliminados = alohandes.eliminarReservasPorId(id);

    			String resultado = "En eliminar Reservas\n\n";
    			resultado += clEliminados + " Reservas eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
 
    
	/* ****************************************************************
	 * 			CRUD de Usuario
	 *****************************************************************/

    /**
     * Adiciona un usuario con la información dada por el usuario
     * Se crea una nueva tupla de usuario en la base de datos, si un usuario con ese id no existía
     */
    public void adicionarUsuario( )
    {
    	try 
    	{
    		String logIn = JOptionPane.showInputDialog (this, "logIn?", "logIn", JOptionPane.QUESTION_MESSAGE);
    		TipoIdentificacion tipoId =TipoIdentificacion.valueOf( JOptionPane.showInputDialog (this,"Tipo de identificacion?", " Adicionar :CARNET_U,CEDULA, PASAPORTE",JOptionPane.QUESTION_MESSAGE));
    		TipoCliente relacionU = TipoCliente.valueOf(JOptionPane.showInputDialog (this, "Tipo de cliente?", "Adicionar: ESTUDIANTE,EMPLEADO, PROFESOR,PROFESOR_VISITANTE ", JOptionPane.QUESTION_MESSAGE));
    		int numeroId = Integer.parseInt(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		if (logIn != null && tipoId != null && relacionU != null )
    		{
        		VOUsuario c = alohandes.adicionarUsuario(logIn, tipoId, numeroId, relacionU);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un usuario con id: " + numeroId);
        		}
        		String resultado = "En adicionarUsuario\n\n";
        		resultado += "Cliente adicionado exitosamente: " + c;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los usuarios existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarUsuario( )
    {
    	try 
    	{
			List <VOUsuario> lista = alohandes.darVOUsuarios();

			String resultado = "En listarUsuario";
			resultado +=  "\n" + listarUsuario(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el cliente con el identificador dado po el usuario
     * Cuando dicho cliente no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarUsuarioId( )
    {
    	try 
    	{
    		String idUsuario = JOptionPane.showInputDialog (this, "Id del usuario?", "Borrar usuario por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idUsuario != null)
    		{
    			long id = Long.valueOf (idUsuario);
    			long clEliminados = alohandes.eliminarUsuarioPorId(id);

    			String resultado = "En eliminar Usuarios\n\n";
    			resultado += clEliminados + " Usuarios eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
 

	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Alohandes
	 */
	public void mostrarLogAlohandes ()
	{
		mostrarArchivo ("alohandes.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de alohandes
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogAlohandes ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("alohandes.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = alohandes.limpiarAlohandes();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Bebidas eliminadas\n";
			resultado += eliminados [4] + " Tipos de bebida eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germán Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jiménez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los clientes recibidos: una línea por cada cliente.
     * @param lista - La lista con los clientes
     * @return La cadena con una líea para cada cliente recibido
     */
    private String listarCliente(List<VOCliente> lista) 
    {
    	String resp = "Los cliente existentes son:\n";
    	int i = 1;
        for (VOCliente tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los operadores recibidos: una línea por cada operador.
     * @param lista - La lista con los operadores.
     * @return La cadena con una líea para cada operador recibido
     */
    private String listarOperador(List<VOOperador> lista) 
    {
    	String resp = "Los  operadores existentes son:\n";
    	int i = 1;
        for (VOOperador tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los usuarios recibidos: una línea por cada usuario.
     * @param lista - La lista con los Usuarios.
     * @return La cadena con una líea para cada usuario recibido
     */
    private String listarUsuario(List<VOUsuario> lista) 
    {
    	String resp = "Los usuarios existentes son:\n";
    	int i = 1;
        for (VOUsuario tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de las recervas recibidas: una línea por cada reserva.
     * @param lista - La lista con las reservas.
     * @return La cadena con una líea para cada reserva recibido
     */
    private String listarReserva(List<VOReserva> lista) 
    {
    	String resp = "Las reservas existentes son:\n";
    	int i = 1;
        for (VOReserva tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    

    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
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

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazAlohandesApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAlohandesApp interfaz = new InterfazAlohandesApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
