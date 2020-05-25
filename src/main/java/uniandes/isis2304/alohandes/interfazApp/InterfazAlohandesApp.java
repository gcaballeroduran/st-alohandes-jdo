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
import java.util.ArrayList;
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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import sun.rmi.server.InactiveGroupException;
import uniandes.isis2304.alohandes.negocio.Alohandes;
import uniandes.isis2304.alohandes.negocio.Apartamento;
import uniandes.isis2304.alohandes.negocio.Cliente;
import uniandes.isis2304.alohandes.negocio.Habitacion;
import uniandes.isis2304.alohandes.negocio.VOApartamento;
import uniandes.isis2304.alohandes.negocio.VOCliente;
import uniandes.isis2304.alohandes.negocio.VOHabitacion;
import uniandes.isis2304.alohandes.negocio.VOOperador;
import uniandes.isis2304.alohandes.negocio.VOPropiedad;
import uniandes.isis2304.alohandes.negocio.VOReserva;
import uniandes.isis2304.alohandes.negocio.VOReservaColectiva;
import uniandes.isis2304.alohandes.negocio.VOServicio;
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
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Alohandes App", JOptionPane.ERROR_MESSAGE);
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
			titulo = "Alohandes APP Default";
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
    		String tipoId = JOptionPane.showInputDialog (this, "Tipo de identificación?", "tipoId", JOptionPane.QUESTION_MESSAGE);
    		String relacionU = JOptionPane.showInputDialog (this, "Relación con la Universidad?", "relacionU", JOptionPane.QUESTION_MESSAGE);
    		String medioPago = JOptionPane.showInputDialog (this, "Medio de pago?", "Adicionar medio de pago", JOptionPane.QUESTION_MESSAGE);
    		int reservas = 0;
    		long numeroId = Integer.parseInt(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		if (logIn != null && tipoId != null && relacionU != null && medioPago != null)
    		{
        		VOCliente c = alohandes.adicionarCliente(numeroId,logIn, tipoId, relacionU, medioPago, reservas);
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
    
    /**
     * Busca en la base de datos el cliente con el identificador dado po el usuario
     */
    public void buscarClienteId( )
    {
    	try 
    	{
    		String idCliente = JOptionPane.showInputDialog (this, "Id del cliente?", "Buscar cliente por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idCliente != null)
    		{
    			long id = Long.valueOf (idCliente);
    			VOCliente cliente = alohandes.darClientePorId(id);

    			String resultado = "En Buscar Cliente\n\n";
    			resultado += cliente;
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
    		String tipoId = JOptionPane.showInputDialog (this, "Tipo de identificación?", "adicionar tipoId", JOptionPane.QUESTION_MESSAGE);
    		long numeroId = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero id?", "Adicionar numero id", JOptionPane.QUESTION_MESSAGE));
    		String relacionU = JOptionPane.showInputDialog (this, "Relación con la Universidad?", "adicionar relacionU", JOptionPane.QUESTION_MESSAGE);
    		int numeroRNT = Integer.parseInt(JOptionPane.showInputDialog (this, "Numero RNT?", "Adicionar numero RNT", JOptionPane.QUESTION_MESSAGE));
    		String vencimientoRNT = JOptionPane.showInputDialog (this, "vencimiento RNT?", "Adicionar vencimiento RNT", JOptionPane.QUESTION_MESSAGE);
    		String registroSuperTurismo = JOptionPane.showInputDialog (this, "registro super turismo?", "Adicionar registro super turismo", JOptionPane.QUESTION_MESSAGE);
    		String vencimientoRegistroSuperTurismo = JOptionPane.showInputDialog (this, "vencimiento super T?", "Adicionar vencimiento super T", JOptionPane.QUESTION_MESSAGE);
    		String categoria = JOptionPane.showInputDialog (this, "Categoria?", "adicionar categoria", JOptionPane.QUESTION_MESSAGE);
    		String direccion = JOptionPane.showInputDialog (this, "direccion?", "Adicionar direccion", JOptionPane.QUESTION_MESSAGE);;
    		String horaApertura = JOptionPane.showInputDialog (this, "hora de apertura?", "Adicionar hora de apertura", JOptionPane.QUESTION_MESSAGE);
    		String horaCierre = JOptionPane.showInputDialog (this, "hora de cierre?", "Adicionar hora de cierre", JOptionPane.QUESTION_MESSAGE);
    		int tiempoMinimo = Integer.parseInt(JOptionPane.showInputDialog (this, "Tiempo minimo?", "Adicionar tiempo minimo", JOptionPane.QUESTION_MESSAGE));
    		ArrayList<Habitacion> habitaciones = new ArrayList<>();
    		ArrayList<Apartamento> apartamentos = new ArrayList<>();
    		if (logIn != null && tipoId != null && relacionU != null )
    		{
        		VOOperador c = alohandes.adicionarOperador(logIn, tipoId, numeroId, relacionU, numeroRNT, vencimientoRNT, registroSuperTurismo, vencimientoRegistroSuperTurismo, categoria, direccion, horaApertura, horaCierre, tiempoMinimo, 0, 0, habitaciones, apartamentos);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un operador ");
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
    			resultado += clEliminados + " Operadores eliminados\n";
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
    		String fechaInicio = JOptionPane.showInputDialog (this, "Fecha de inicio?", "Fecha de inicio", JOptionPane.QUESTION_MESSAGE);
    		String fechaFin = JOptionPane.showInputDialog (this,"Fecha de fin?", "Fecha de fin",JOptionPane.QUESTION_MESSAGE);
    		String finCancelacionOportuna = JOptionPane.showInputDialog (this, "Fecha cancelación oportuna?", "Adicionar fin cancelación oportuna", JOptionPane.QUESTION_MESSAGE);
    		int personas = Integer.parseInt(JOptionPane.showInputDialog (this, "numero de personas?", "Adicionar numero de personas", JOptionPane.QUESTION_MESSAGE));
    		//double porcentajeAPagar = Double.parseDouble(JOptionPane.showInputDialog (this, "Porcentaje a pagar?", "Adicionar porcenaje a pagar", JOptionPane.QUESTION_MESSAGE));
    		//double montoTotal = Double.parseDouble(JOptionPane.showInputDialog (this, "monto total?", "Adicionar monto total a pagar ", JOptionPane.QUESTION_MESSAGE));
    		long idPropiedad = Long.parseLong(JOptionPane.showInputDialog (this, "ID propiedad?", "Adicionar id de la propiedad asociada a la reserva ", JOptionPane.QUESTION_MESSAGE));
    		if (fechaInicio != null && fechaFin != null && personas != 0 )
    		{
    			VOReserva c = alohandes.adicionarReserva(fechaInicio, fechaFin,finCancelacionOportuna, personas, 1, idPropiedad);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear una reserva " ); 
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
    			resultado += clEliminados + " Reservas eliminadas\n";
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
	 * 			CRUD de Apartamento
	 *****************************************************************/

    /**
     * Adiciona un apartamento con la información dada por el usuario
     * Se crea una nueva tupla de reserva en la base de datos, si un apartamento con ese id no existía
     */
    public void adicionarApartamento( )
    {
    	try 
    	{
    		long pId = Long.parseLong(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		int pCapacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar capacidad", JOptionPane.QUESTION_MESSAGE));
    		double pTamanio = Integer.parseInt(JOptionPane.showInputDialog (this, "Tamaño?", "Adicionar tamaño", JOptionPane.QUESTION_MESSAGE));
    		double pPrecio = Integer.parseInt(JOptionPane.showInputDialog (this, "Precio?", "Adicionar precio", JOptionPane.QUESTION_MESSAGE));
    		String pFecha = JOptionPane.showInputDialog (this, "Fecha?", "adicionar fecha", JOptionPane.QUESTION_MESSAGE);
    		int pDiasR = Integer.parseInt(JOptionPane.showInputDialog (this, "Dias reservados?", "Adicionar dias reservados", JOptionPane.QUESTION_MESSAGE));
    		int pPiso = Integer.parseInt(JOptionPane.showInputDialog (this, "Piso?", "Adicionar piso", JOptionPane.QUESTION_MESSAGE));
    		String pDireccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar direccion", JOptionPane.QUESTION_MESSAGE);
    		boolean pAmueblado = JOptionPane.showInputDialog(this,"Amuablado?","Adicionar amuablado", JOptionPane.YES_NO_OPTION) != null;
    		int pHabitaciones = Integer.parseInt(JOptionPane.showInputDialog (this, "Habitaciones?", "Adicionar habitaciones", JOptionPane.QUESTION_MESSAGE));
    		String pDMenaje = JOptionPane.showInputDialog (this, "Mensaje?", "Adicionar mensaje", JOptionPane.QUESTION_MESSAGE);
    		int pVenceSeguro = Integer.parseInt(JOptionPane.showInputDialog (this,"Tiene seguro?", " Adicionar vencimiento seguro",JOptionPane.QUESTION_MESSAGE));
    		String pDSeguro = JOptionPane.showInputDialog (this, "Descripción seguro?", "Adicionar descripcion seguro", JOptionPane.QUESTION_MESSAGE);
    		long pOperador = Long.parseLong(JOptionPane.showInputDialog (this, "Id Operador?", "Adicionar id operador", JOptionPane.QUESTION_MESSAGE));
    		if ( pCapacidad > 0 && pFecha != null && pPiso > 0 && pDireccion != null && pHabitaciones > 0 && pDMenaje != null && pDSeguro != null)
    		{
        		VOPropiedad c =  alohandes.adicionarApartamento(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pAmueblado, pHabitaciones, pDMenaje, pVenceSeguro, pDSeguro, pOperador);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un apartamento" );
        		}
        		String resultado = "En adicionarApartamento\n\n";
        		resultado += "Apartamento adicionado exitosamente: " + c;
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
     * Consulta en la base de datos los apartamentos existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarApartamento( )
    {
    	try 
    	{
			List <VOApartamento> lista = alohandes.darVOApartamento();

			String resultado = "En listarApartamento";
			resultado +=  "\n" + listarApartamento(lista);
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
     * Borra de la base de datos el apartamento con el identificador dado po el usuario
     * Cuando dicho apartamento no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarApartamentoId( )
    {
    	try 
    	{
    		String idApartamento = JOptionPane.showInputDialog (this, "Id del apartamento?", "Borrar apartamento por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idApartamento != null)
    		{
    			long id = Long.valueOf (idApartamento);
    			long clEliminados = alohandes.eliminarApartamentoPorId(id);

    			String resultado = "En eliminar Apartamento\n\n";
    			resultado += clEliminados + " Aartamentos eliminados\n";
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

    
    public void habilitarApartamento(){
    	try{
    		String idApartamento = JOptionPane.showInputDialog (this, "Id del apartamento?", "Borrar apartamento por Id", JOptionPane.QUESTION_MESSAGE);
    		long id = Long.valueOf (idApartamento);
    		alohandes.hablitarApartamento(id);
    		String resultado = "En habilitar Apartamento\n\n";
			resultado +=  " Aartamento habilitado\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void deshabilitarApartamento(){
    	try{
    		String idApartamento = JOptionPane.showInputDialog (this, "Id del apartamento?", "Borrar apartamento por Id", JOptionPane.QUESTION_MESSAGE);
    		long id = Long.valueOf (idApartamento);
    		alohandes.deshablitarApartamento(id);
    		String resultado = "En deshabilitar Apartamento\n\n";
			resultado +=  " Aartamento deshabilitado\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void apartamentosDisponibles(){
    	try{
    		
    		List<Apartamento> c = alohandes.darApartamentosDisponibles();
    		String resultado = "En Apartamentos disponibles\n\n";
    		for (int i = 0 ; i< c.size(); i++)
    		{
    		  resultado += c.get(i).toString();	
    		}
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    /* ****************************************************************
	 * 			CRUD de Propiedad
	 *****************************************************************/


    /**
     * Consulta en la base de datos las propiedades existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarPropiedad( )
    {
    	try 
    	{
			List <VOPropiedad> lista = alohandes.darVOPropiedad();

			String resultado = "En listarPropiedad";
			resultado +=  "\n" + listarPropiedad(lista);
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

    
    /* ****************************************************************
	 * 			CRUD de Habitacion
	 *****************************************************************/

    /**
     * Adiciona una habitacion con la información dada por el usuario
     * Se crea una nueva tupla de habitacion en la base de datos, si una habitacion con ese id no existía
     */
    public void adicionarHabitacion( )
    {
    	try 
    	{
    		long pId = Long.parseLong(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		int pCapacidad = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad?", "Adicionar capacidad", JOptionPane.QUESTION_MESSAGE));
    		double pTamanio = Integer.parseInt(JOptionPane.showInputDialog (this, "Tamaño?", "Adicionar tamaño", JOptionPane.QUESTION_MESSAGE));
    		double pPrecio = Integer.parseInt(JOptionPane.showInputDialog (this, "Precio?", "Adicionar precio", JOptionPane.QUESTION_MESSAGE));
    		String pFecha = JOptionPane.showInputDialog (this, "Fecha?", "adicionar fecha", JOptionPane.QUESTION_MESSAGE);
    		int pDiasR = Integer.parseInt(JOptionPane.showInputDialog (this, "Dias reservados?", "Adicionar dias reservados", JOptionPane.QUESTION_MESSAGE));
    		int pPiso = Integer.parseInt(JOptionPane.showInputDialog (this, "Piso?", "Adicionar piso", JOptionPane.QUESTION_MESSAGE));
    		String pDireccion = JOptionPane.showInputDialog (this, "Direccion?", "Adicionar direccion", JOptionPane.QUESTION_MESSAGE);
    		boolean pIndiv = JOptionPane.showInputDialog(this,"Individual?","Adicionar individual", JOptionPane.YES_NO_OPTION) != null;
    		String pEsquema = JOptionPane.showInputDialog (this, "Esquema?", "Adicionar esquema", JOptionPane.QUESTION_MESSAGE);
    		int pTipo = Integer.parseInt(JOptionPane.showInputDialog (this, "Tipo de habitación?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE));
    		long pOperador = Long.parseLong(JOptionPane.showInputDialog (this, "Id Operador?", "Adicionar id operador", JOptionPane.QUESTION_MESSAGE));
    		if ( pCapacidad > 0 && pFecha != null && pPiso > 0 && pDireccion != null)
    		{
        		VOHabitacion c = alohandes.adicionarHabitacion(pId, pCapacidad, pTamanio, pPrecio, pFecha, pDiasR, pPiso, pIndiv, pEsquema, pTipo, pOperador);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear una habitación" );
        		}
        		String resultado = "En adicionarHabitacion\n\n";
        		resultado += "Habitacion adicionado exitosamente: " + c;
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
     * Consulta en la base de datos las habitaciones existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarHabitacion( )
    {
    	try 
    	{
			List <VOHabitacion> lista = alohandes.darVOHabitacion();

			String resultado = "En listarHabitacion";
			resultado +=  "\n" + listarHabitacion(lista);
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
     * Borra de la base de datos la habitacion con el identificador dado po el usuario
     * Cuando dicha habitacion no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarHabitacionId( )
    {
    	try 
    	{
    		String idHabitacion = JOptionPane.showInputDialog (this, "Id de la habitacion?", "Borrar habitacion por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idHabitacion != null)
    		{
    			long id = Long.valueOf (idHabitacion);
    			long clEliminados = alohandes.eliminarHabitacionPorId(id);

    			String resultado = "En eliminar Habitacion\n\n";
    			resultado += clEliminados + " Habitaciones eliminadas\n";
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
    
    public void habilitarHabitacion(){
    	try{
    		String idApartamento = JOptionPane.showInputDialog (this, "Id del apartamento?", "Borrar apartamento por Id", JOptionPane.QUESTION_MESSAGE);
    		long id = Long.valueOf (idApartamento);
    		alohandes.habilitarHabitacion(id);;
    		String resultado = "En habilitar Habitacion\n\n";
			resultado +=  " Aartamento habilitado\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void deshabilitarHabitacion(){
    	try{
    		String idApartamento = JOptionPane.showInputDialog (this, "Id del apartamento?", "Borrar apartamento por Id", JOptionPane.QUESTION_MESSAGE);
    		long id = Long.valueOf (idApartamento);
    		alohandes.deshabilitarHabitacion(id);;
    		String resultado = "En deshabilitar Habitacion\n\n";
			resultado +=  " habitacion deshabilitado\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
    public void habitacionesDisponibles(){
    	try{
    		
    		List<Habitacion> c = alohandes.darHabitacionesDisponibles();
    		String resultado = "En Habitaciones disponibles\n\n";
    		for (int i = 0 ; i< c.size(); i++)
    		{
    		  resultado += c.get(i).toString();	
    		}
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
    		
    	}
    	
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }

    
    /* ****************************************************************
	 * 			CRUD de ReservaColectiva
	 *****************************************************************/

    /**
     * Adiciona una reserva colectiva con la información dada por el usuario
     * Se crea una nueva tupla de reserva colectiva en la base de datos, si una reserva colectiva con ese id no existía
     */
    public void adicionarReservaColectiva()
    {
    	try 
    	{
    		int pId = Integer.parseInt(JOptionPane.showInputDialog (this, "numero Id?", "Adicionar numero Id", JOptionPane.QUESTION_MESSAGE));
    		int pCantidad = Integer.parseInt(JOptionPane.showInputDialog (this, "cantidad?", "Adicionar cantidad", JOptionPane.QUESTION_MESSAGE));
    		String pTipo = JOptionPane.showInputDialog (this, "Tipo?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    		String pInicio = JOptionPane.showInputDialog (this, "Fecha?", "adicionar fecha", JOptionPane.QUESTION_MESSAGE);
    		int pDuracion = Integer.parseInt(JOptionPane.showInputDialog (this, "Duracion?", "Adicionar duracion", JOptionPane.QUESTION_MESSAGE));
    		if ( pDuracion > 0 )
    		{
        		VOReservaColectiva c=alohandes.adicionarReservaColectiva(pId, pCantidad, pTipo, pInicio, pDuracion);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear una reservaColectiva" );
        		}
        		String resultado = "En adicionarReservaColectiva\n\n";
        		resultado += "ReservaColectiva adicionada exitosamente: " + c;
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
     * Consulta en la base de datos las reservas colectivas existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarRerservaColectiva( )
    {
    	try 
    	{
			List <VOReservaColectiva> lista = alohandes.darVOReservaColectiva();

			String resultado = "En listarRerservaColectiva";
			resultado +=  "\n" + listarReservaColectiva(lista);
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
     * Borra de la base de datos la habitacion con el identificador dado po el usuario
     * Cuando dicha reserva colectiva no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarRerservaColectivaId( )
    {
    	try 
    	{
    		String idReservaColectiva = JOptionPane.showInputDialog (this, "Id de la reserva colectiva?", "Borrar reserva colectiva por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idReservaColectiva != null)
    		{
    			long id = Long.valueOf (idReservaColectiva);
    			long clEliminados = alohandes.eliminarReservaColectivaPorId(id);

    			String resultado = "En eliminar ReservaColectiva\n\n";
    			resultado += clEliminados + " ReservaColectiva eliminadas\n";
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
	 * 			CRUD de Servicio
	 *****************************************************************/

    /**
     * Adiciona un servicio con la información dada por el usuario
     * Se crea una nueva tupla de servicio en la base de datos, si un servicio con ese id no existía
     */
    public void adicionarServicio( )
    {
    	try 
    	{
    		long pId = Integer.parseInt(JOptionPane.showInputDialog (this, "Id?", "Adicionar Id", JOptionPane.QUESTION_MESSAGE));
    		String pTipo = JOptionPane.showInputDialog (this, "Tipo?", "Adicionar tipo", JOptionPane.QUESTION_MESSAGE);
    		double pPrecio = Double.parseDouble(JOptionPane.showInputDialog (this, "Precio?", "adicionar precio", JOptionPane.QUESTION_MESSAGE));
    		int pIntervalo = Integer.parseInt(JOptionPane.showInputDialog (this, "Intervalo de pago?", "adicionar intervalo", JOptionPane.QUESTION_MESSAGE));
    		
    		if (pTipo != null )
    		{
        		VOServicio c = alohandes.adicionarServicio(pId, pTipo, pPrecio, pIntervalo);
        		if (c == null)
        		{
        			throw new Exception ("No se pudo crear un servicio con id: " + pId);
        		}
        		String resultado = "En adicionarServicio\n\n";
        		resultado += "servicio adicionado exitosamente: " + c;
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
     * Consulta en la base de datos los servicios existentes y los muestra en el panel de datos de la aplicación
     */
    public void listarServicio( )
    {
    	try 
    	{
			List <VOServicio> lista = alohandes.darVOServicio();

			String resultado = "En listarServicio";
			resultado +=  "\n" + listarServicio(lista);
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
     * Borra de la base de datos el servicio con el identificador dado po el usuario
     * Cuando dicho servicio no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarServicioId( )
    {
    	try 
    	{
    		String idServicio = JOptionPane.showInputDialog (this, "Id del servicio?", "Borrar servicio por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idServicio != null)
    		{
    			long id = Long.valueOf (idServicio);
    			long clEliminados = alohandes.eliminarServicioPorId(id);

    			String resultado = "En eliminar Servicio\n\n";
    			resultado += clEliminados + " Servicios eliminados\n";
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
	 * 			Métodos Requerimientos
	 *****************************************************************/
    public void darDineroAnioActual(){
    	
    	try 
    	{
    	 
    	List<Object> c =alohandes.darDineroAnioActual();
   		String resultado = "En RFC1\n\n";
   		for (int i = 0; i< c.size(); i++){
   			
   			resultado += c.get(i).toString();
   		}
		resultado += "\n Operación terminada";
		panelDatos.actualizarInterfaz(resultado);
   
    	
    	}
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    	
    }
    
public void darDineroAnioCorrido(){
    	
    	try 
    	{
    	 
    	List<Object> c =alohandes.darDineroAnioCorrido();
   		String resultado = "En RFC1\n\n";
   		for (int i = 0; i< c.size(); i++){
   			
   			resultado += c.get(i).toString();
   		}
		resultado += "\n Operación terminada";
		panelDatos.actualizarInterfaz(resultado);
   
    	
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
			resultado += eliminados [0] + " cliente eliminados\n";
			resultado += eliminados [1] + " operador eliminados\n";
			resultado += eliminados [2] + " reserva eliminados\n";
			resultado += eliminados [3] + " usuario eliminadas\n";
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
     * Genera una cadena de caracteres con la lista de los apartamentos recibidos: una línea por cada apartamento.
     * @param lista - La lista con los apartamentos.
     * @return La cadena con una líea para cada apartamento recibido
     */
    private String listarApartamento(List<VOApartamento> lista) 
    {
    	String resp = "Los apartamentos existentes son:\n";
    	int i = 1;
        for (VOApartamento tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    /**
     * Genera una cadena de caracteres con la lista de las habitaciones recibidos: una línea por cada habitacion.
     * @param lista - La lista con las habitaciones.
     * @return La cadena con una líea para cada habitacion recibido
     */
    private String listarHabitacion(List<VOHabitacion> lista) 
    {
    	String resp = "Las habitaciones existentes son:\n";
    	int i = 1;
        for (VOHabitacion tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    /**
     * Genera una cadena de caracteres con la lista de las propiedades recibidos: una línea por cada propiedad.
     * @param lista - La lista con las propiedades.
     * @return La cadena con una líea para cada propiedad recibido
     */
    private String listarPropiedad(List<VOPropiedad> lista) 
    {
    	String resp = "Las propiedades existentes son:\n";
    	int i = 1;
        for (VOPropiedad tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de las reservas colectivas recibidos: una línea por cada reserva colectivas.
     * @param lista - La lista con las reservas colectivas.
     * @return La cadena con una líea para cada reserva colectivas recibido
     */
    private String listarReservaColectiva(List<VOReservaColectiva> lista) 
    {
    	String resp = "Las reservas colectivas existentes son:\n";
    	int i = 1;
        for (VOReservaColectiva tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    /**
     * Genera una cadena de caracteres con la lista de los servicios recibidos: una línea por cada servicio.
     * @param lista - La lista con los servicios.
     * @return La cadena con una líea para cada servicio recibido
     */
    private String listarServicio(List<VOServicio> lista) 
    {
    	String resp = "Los servicios existentes son:\n";
    	int i = 1;
        for (VOServicio tb : lista)
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
        	BasicConfigurator.configure();
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
