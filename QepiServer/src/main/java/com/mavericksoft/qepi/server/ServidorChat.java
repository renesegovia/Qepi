/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.server;

import com.mavericksoft.qepi.util.Util;
import com.mavericksoft.qepi.util.DatosCliente;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author lsegovia
 */
public class ServidorChat {

    /**
     * Contiene un mapa de los personajes que al momento estan conectados al servidor de chat, 
     * la clave corresponde al idPersonaje|idUniverso|idThread y el valor es el objeto DatosCliente informacion como Personaje,
     * Escenario, DataInputStream y DataOutputStream del Personaje origen conectado.
     */
    public static Map<String, DatosCliente> mapaPersonajesConectados = new HashMap<String, DatosCliente>();
    
    /**
     * Contiene un mapa de los escenarios que al momento estan activos o estan siendo usados en el servidorde chat, 
     * la clave es el idEscenario|idUniverso|idThread y el valor es una lista de objetos DatosCliente con informacion de los personajes 
     * conectados.
     */
    public static Map<String, List<DatosCliente>> mapaEscenariosActivos = new HashMap<String, List<DatosCliente>>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Util util = new Util();
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(util.getRecursoConexion().getPuerto());
            serverSocket.setSoTimeout(0);
            Util.imprimir("Servidor "+serverSocket.getInetAddress().getHostAddress()
                    +" levantado sobre el puerto: "+serverSocket.getLocalPort());
            while (true) {                
                clientSocket = serverSocket.accept();
                AtencionCliente atencion = new AtencionCliente(clientSocket);
                atencion.start();
            }
        } catch (IOException ex) {
            Util.imprimir("No se puede levantar el servidor "+ex.getMessage());
        }               
                
    }

}
