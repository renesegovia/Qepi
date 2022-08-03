/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.util;

//import com.mavericksoft.qepi.cliente.FXMLDocumentController;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 *
 * @author lsegovia
 */
public class Util {

    //private static final String RECURSOS = "/com/mavericksoft/qepi/util/recursos.xml";
    private static final String RECURSOS = "recursos.xml";
    private static final String CONEXIONES = "config/conexiones.xml";
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MMM HH:mm:ss");
    private List<Personaje> personajes;
    private List<Escenario> escenarios;
    private RecursoConexion recursoConexion;
    private static RecursoConfiguracion recursoConfiguracion;

    public RecursoConfiguracion getRecursoConfiguracion() {
        return Util.recursoConfiguracion;
    }

    public void setRecursoConfiguracion(RecursoConfiguracion recursoConfiguracion) {
        Util.recursoConfiguracion = recursoConfiguracion;
    }

    public RecursoConexion getRecursoConexion() {
        return recursoConexion;
    }

    public void setRecursoConexion(RecursoConexion recursoConexion) {
        this.recursoConexion = recursoConexion;
    }

    public List<Personaje> getPersonajes() {
        return personajes;
    }

    public void setPersonajes(List<Personaje> personajes) {
        this.personajes = personajes;
    }

    public List<Escenario> getEscenarios() {
        return escenarios;
    }

    public void setEscenarios(List<Escenario> escenarios) {
        this.escenarios = escenarios;
    }

    private void processConfigurations() {
        BufferedReader br = null;
        Document doc;
        String linea;
        String contenidoXML = "";
        try {
            //br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(Util.RECURSOS), "UTF-8"));
            //https://mkyong.com/java/java-read-a-file-from-resources-folder/
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(Util.RECURSOS), "UTF-8"));
            //fr = new FileReader(FXMLDocumentController.RECURSOS);
            //br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(/archivo.xml)));
            // From ClassLoader, all paths are "absolute" already - there's no context
            // from which they could be relative. Therefore you don't need a leading slash.
            //InputStream in = this.getClass().getClassLoader().getResourceAsStream("SomeTextFile.txt");
            // From Class, the path is relative to the package of the class unless
            // you include a leading slash, so if you don't want to use the current
            // package, include a slash like this:
            //InputStream in = this.getClass().getResourceAsStream("/SomeTextFile.txt");
            //br = new BufferedReader(fr);
            while ((linea = br.readLine()) != null) {
                contenidoXML += "\n" + linea;
            }
            doc = DocumentHelper.parseText(contenidoXML);
            processConfigurationsConexion();
            List<Node> nodosUniverso = doc.selectNodes("//universo");
            personajes = new ArrayList<>();
            escenarios = new ArrayList<>();
            nodosUniverso.forEach((nodoUniverso) -> {
                List<Node> nodosPersonaje = nodoUniverso.selectNodes("personaje");
                nodosPersonaje.forEach((nodoPersonaje) -> {
                    personajes.add(new Personaje(
                            nodoPersonaje.valueOf("@id"),
                            nodoPersonaje.getText(),
                            new Universo(nodoUniverso.valueOf("@id"), nodoUniverso.valueOf("@nombre"))));
                });
                List<Node> nodosEscenario = nodoUniverso.selectNodes("escenario");
                nodosEscenario.forEach((nodoEscenario) -> {
                    escenarios.add(new Escenario(
                            nodoEscenario.valueOf("@id"),
                            nodoEscenario.getText(),
                            new Universo(nodoUniverso.valueOf("@id"), nodoUniverso.valueOf("@nombre"))));
                });
            });

            Node nodoConfiguracion = doc.selectSingleNode("//configuracion");
            recursoConfiguracion = new RecursoConfiguracion(
                    Boolean.parseBoolean(nodoConfiguracion.selectSingleNode("debug").getText()));

        } catch (IOException | DocumentException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(Objects.nonNull(br)){
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void processConfigurationsConexion() {
        BufferedReader br = null;
        Document doc = null;
        String linea = "";
        String contenidoXML = "";
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(Util.CONEXIONES)));
            while ((linea = br.readLine()) != null) {
                contenidoXML += "\n" + linea;
            }
            doc = DocumentHelper.parseText(contenidoXML);
            Node nodoConexion = doc.selectSingleNode("//conexion[@default='true']");
            recursoConexion = new RecursoConexion(
                    nodoConexion.valueOf("@id"),
                    nodoConexion.selectSingleNode("servidor").getText(),
                    Integer.parseInt(nodoConexion.selectSingleNode("puerto").getText()));
        } catch (IOException | NumberFormatException | DocumentException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Util() {
        processConfigurations();
    }

    public static String getClaveIdPersonajeOrigen(Protocolo p) {
        return p.getIdPersonajeOrigen() + "|" + p.getIdThread();
    }

    public static String getClaveIdPersonajeDestino(Protocolo p) {
        return p.getIdPersonajeDestino() + "|" + p.getIdThreadDestino();
    }

    public static String getIdPersonaje(String claveIdPersonaje) {
        return claveIdPersonaje.substring(0, claveIdPersonaje.indexOf("|"));
    }

    public static String getIdUniverso(String claveIdPersonaje) {
        return claveIdPersonaje.substring(claveIdPersonaje.indexOf("|") + 1);
    }

    public Personaje getPersonaje(String claveIdPersonaje) {
        String idPersonaje = claveIdPersonaje.substring(0, claveIdPersonaje.indexOf("|"));
        String idUniverso = claveIdPersonaje.substring(claveIdPersonaje.indexOf("|") + 1);
        Personaje personaje = null;
        for (Personaje personajeTmp : personajes) {
            if (personajeTmp.getId().equalsIgnoreCase(idPersonaje)
                    && personajeTmp.getUniverso().getId().equalsIgnoreCase(idUniverso)) {
                personaje = personajeTmp;
                break;
            }
        }
        return personaje;
    }

    public Personaje getPersonaje(String idPersonaje, String idUniverso) {
        Personaje personaje = null;
        for (Personaje personajeTmp : personajes) {
            if (personajeTmp.getId().equalsIgnoreCase(idPersonaje)
                    && personajeTmp.getUniverso().getId().equalsIgnoreCase(idUniverso)) {
                personaje = personajeTmp;
                break;
            }
        }
        return personaje;
    }

    public Escenario getEscenario(String claveIdEscenario) {
        String idEscenario = claveIdEscenario.substring(0, claveIdEscenario.indexOf("|"));
        String idUniverso = claveIdEscenario.substring(claveIdEscenario.indexOf("|") + 1,
                claveIdEscenario.indexOf("|", claveIdEscenario.indexOf("|") + 1));
        Escenario escenario = null;
        for (Escenario escenarioTmp : escenarios) {
            if (escenarioTmp.getId().equalsIgnoreCase(idEscenario)
                    && escenarioTmp.getUniverso().getId().equalsIgnoreCase(idUniverso)) {
                escenario = escenarioTmp;
                break;
            }
        }
        return escenario;
    }

    /**
     * OJO Se invoca desde el servidor porque ahi está el mapaEscenariosActivos
     * con data en esa maquina virtual. Si lo invoco desde el cliente en esa
     * maquina virtual el mapa no va a tener data.
     *
     * @param idThread
     * @param escenariosActivos
     * @return
     */
    public String getIdEscenarioAleatorio(String idThread, Set<String> escenariosActivos) {
        int numeroAletatorio;
        String idEscenarioTmp = "";
        String idEscenarioTmpShort;
        Random aleatorio = new Random(System.currentTimeMillis());
        boolean escenarioOcupado;
        do {
            escenarioOcupado = false;
            // Producir nuevo int aleatorio entre 0 y tamano de escenarios -1
            numeroAletatorio = aleatorio.nextInt(escenarios.size() - 1);
            Escenario escenarioTmp = escenarios.get(numeroAletatorio);
            idEscenarioTmp = escenarioTmp.getId() + "|" + escenarioTmp.getUniverso().getId() + "|" + idThread;
            idEscenarioTmpShort = escenarioTmp.getId() + "|" + escenarioTmp.getUniverso().getId();
            //for (String clave : ServidorChat.mapaEscenariosActivos.keySet()) {
            for (String clave : escenariosActivos) {
                if (clave.equalsIgnoreCase(idEscenarioTmp) || clave.startsWith(idEscenarioTmpShort)) {
                    escenarioOcupado = true;
                    break;
                }
            }
        } while (escenarioOcupado);
        return idEscenarioTmp;
    }

    public static void imprimir(String mensaje) {
        if (Util.recursoConfiguracion.isDebug()) {
            Calendar esteInstante = Calendar.getInstance();
            System.out.println("[" + Util.SDF.format(esteInstante.getTime()) + "] " + mensaje);
        }
    }

    public static void enviarMensajeCliente(DataOutputStream out, Protocolo p) throws Exception {
        enviarMensaje(out, p, false);
    }

    public static Protocolo recibirMensajeCliente(DataInputStream in) throws Exception {
        return recibirMensaje(in, false);
    }

    public static void enviarMensajeServidor(DataOutputStream out, Protocolo p) throws Exception {
        enviarMensaje(out, p, true);
    }

    public static Protocolo recibirMensajeServidor(DataInputStream in) throws Exception {
        return recibirMensaje(in, true);
    }

    private static void enviarMensaje(DataOutputStream out, Protocolo p, boolean servidor) throws Exception {
        String mensaje = entramar(p);
        String mensajeCifrado = TripleDES.encripta(TripleDES.CLAVE, mensaje);
        if (servidor) {
            Util.imprimir("-->" + mensajeCifrado + "<--");
        }
        out.writeUTF(mensajeCifrado);
    }

    private static Protocolo recibirMensaje(DataInputStream in, boolean servidor) throws Exception {
        String mensajeCifrado = in.readUTF();
        String mensaje = TripleDES.desencripta(TripleDES.CLAVE, mensajeCifrado);
        if (servidor) {
            Util.imprimir("-->" + mensajeCifrado + "<--");
        }
        return destramar(mensaje);
    }

    public static String rellenarEspaciosDerecha(String cadena, int longitud) {
        return String.format("%1$-" + longitud + "s", cadena);
    }

    public static String entramar(Protocolo p) {
        return p.getFlujo()
                + p.getTipo()
                + p.getAccion()
                + p.getExito()
                + Util.rellenarEspaciosDerecha(p.getIdPersonajeOrigen(), 10)
                + Util.rellenarEspaciosDerecha(p.getIdPersonajeDestino(), 10)
                + Util.rellenarEspaciosDerecha(p.getIdEscenario(), 10)
                + Util.rellenarEspaciosDerecha(p.getIdThread(), 10)
                + Util.rellenarEspaciosDerecha(p.getIdThreadDestino(), 10)
                + Util.rellenarEspaciosDerecha(p.getTrama(), 1);
    }

    public static Protocolo destramar(String mensaje) {
        return new Protocolo(
                mensaje.substring(0, 1),
                mensaje.substring(1, 2),
                mensaje.substring(2, 5),
                mensaje.substring(5, 8),
                mensaje.substring(8, 18).trim(),
                mensaje.substring(18, 28).trim(),
                mensaje.substring(28, 38).trim(),
                mensaje.substring(38, 48).trim(),
                mensaje.substring(48, 58).trim(),
                mensaje.substring(58));
    }

    public static String getFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE, dd 'de' MMMMM 'del' yyyy", new Locale("es", "ES"));
        String fecha = sdf.format(Calendar.getInstance().getTime());
        return fecha.substring(0, 1).toUpperCase() + fecha.substring(1);
    }

    public static String getHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(Calendar.getInstance().getTime());
    }

}
