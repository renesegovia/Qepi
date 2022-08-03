/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.server;

import com.mavericksoft.qepi.util.Protocolo;
import com.mavericksoft.qepi.util.DatosCliente;
import com.mavericksoft.qepi.util.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lsegovia
 */
public class AtencionCliente extends Thread{
    private DataInputStream in;
    private DataOutputStream out;
    private boolean conectado = true;
    private Util util;
    private String idThread;

    public AtencionCliente(Socket clientSocket) {
        util = new Util();
        try {
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(AtencionCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        idThread = ""+Thread.currentThread().getId();
        Protocolo protocoloIn = null;
            try {
                while(conectado){
                    protocoloIn = procesarPeticionCliente();    
                }
            } catch (Exception ex) {
                //conectado = false;
                if(ex instanceof IOException){
                    Util.imprimir("No se puede responder al cliente: "+ex.getMessage());
                }else{
                    Util.imprimir("No se puede cifrar/descifrar: "+ex.getMessage());
                }
            } finally {
                actualizarRecursoDesconectado(protocoloIn);
            }
        Util.imprimir("Conexion "+idThread+" termino su proceso");
    } 

    private void imprimirCantidadEscenariosYPersonajes(){
        Util.imprimir("Escenarios Totales: "+ServidorChat.mapaEscenariosActivos.size());
        for (String claveEscenario : ServidorChat.mapaEscenariosActivos.keySet()) {
            Util.imprimir("Numero de Personajes en escenarios "+claveEscenario+": "+ServidorChat.mapaEscenariosActivos.get(claveEscenario).size());
            for (DatosCliente datosClienteTmp : ServidorChat.mapaEscenariosActivos.get(claveEscenario)) {
                Util.imprimir("Escenario: "+claveEscenario+" Personaje en escenario: "+datosClienteTmp.getPersonaje().getNombre());
            }
        }
        Util.imprimir("Numero Personajes conectados: "+ServidorChat.mapaPersonajesConectados.size());
        for (DatosCliente datosCliente : ServidorChat.mapaPersonajesConectados.values()) {
            Util.imprimir("DatosCliente conectado: "+datosCliente.getPersonaje().getNombre());
        }
    }
    
    private void actualizarRecursoDesconectado(Protocolo protocoloIn){
        conectado = false;
        //removiendo personaje de los mapas de personajes
        if(ServidorChat.mapaPersonajesConectados.containsKey(Util.getClaveIdPersonajeOrigen(protocoloIn))){
            ServidorChat.mapaPersonajesConectados.remove(Util.getClaveIdPersonajeOrigen(protocoloIn));
        }    
        //removiendo personaje de los mapas de los escenarios activos en los que este incluido
        for (String claveEscenario : ServidorChat.mapaEscenariosActivos.keySet()) {
            List<DatosCliente> datosClientesTmp = new ArrayList<DatosCliente>();
            for (DatosCliente datosClienteTmp : ServidorChat.mapaEscenariosActivos.get(claveEscenario)) {
                String idPersonajeOrigen = datosClienteTmp.getPersonaje().getId()+"|"+datosClienteTmp.getPersonaje().getUniverso().getId();
                if(idPersonajeOrigen.equalsIgnoreCase(protocoloIn.getIdPersonajeOrigen())){
                    datosClientesTmp.add(datosClienteTmp);
                }
            }
            ServidorChat.mapaEscenariosActivos.get(claveEscenario).removeAll(datosClientesTmp);
        }
    }
    
    private Protocolo procesarPeticionCliente() throws IOException, Exception {
        /*
        En el lado del cliente, si no existe la necesidad de enviar un campo, Ej para el ACTION_CHAT_ROOM
        no existe la necesidad de enviar el idPersonajeDestino, debido a que se chatea contra el Escenario,
        entonces desde el cliente en esta posición se enviará vacio, respetando la posicion y cantidad de caracteres
        del campo.
        */
        Protocolo protocoloIn = Util.recibirMensajeServidor(in);
        protocoloIn.setIdThread(idThread);
        switch(protocoloIn.getAccion()){
            case Protocolo.ACTION_CONNECT:
                accionConectarPersonaje(protocoloIn);
                break;
            case Protocolo.ACTION_DISCONNECT:
                accionDesconectarPersonaje(protocoloIn);
                break;
            case Protocolo.ACTION_ADD_ALIAS_CHAT_ROOM:
                accionAgregarPersonajeAlEscenarioChat(protocoloIn);
                break;
            case Protocolo.ACTION_CLOSE_CHAT_ALIAS_ROOM:
                accionCerrarEscenarioChat(protocoloIn);
                break;
            case Protocolo.ACTION_CHAT_ROOM:
                accionChatearEnEscenario(protocoloIn);
                break;
            default:
                accionNoSoportada(protocoloIn);
                break;
        }
        return protocoloIn;
    }

    private void accionConectarPersonaje(Protocolo protocoloIn) throws Exception {
        String trama = "";
        Protocolo p = new Protocolo(
                Protocolo.SEND_TO_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO, 
                Protocolo.ACTION_CONNECT, 
                Protocolo.RESPONSE_SUCESSFULL,
                protocoloIn.getIdPersonajeOrigen(), 
                "", 
                "", 
                protocoloIn.getIdThread(), 
                "",
                trama);
        if(!ServidorChat.mapaPersonajesConectados.containsKey(protocoloIn.getIdPersonajeOrigen()+"|"+idThread)){
            DatosCliente datosClienteOrigen = new DatosCliente(util.getPersonaje(protocoloIn.getIdPersonajeOrigen()), null, in, out);
            ServidorChat.mapaPersonajesConectados.put(protocoloIn.getIdPersonajeOrigen()+"|"+idThread, datosClienteOrigen);
            //Notificar personajes conectados
            p.setExito(Protocolo.RESPONSE_SUCESSFULL);
            trama = ServidorChat.mapaPersonajesConectados.keySet().stream()
                    .map((key) -> key+" ").reduce(trama, String::concat);
            p.setTrama(trama);
            for (DatosCliente personajeConectado : ServidorChat.mapaPersonajesConectados.values()) {
                Util.enviarMensajeServidor(personajeConectado.getOut(), p);
                //System.out.println("--->"+trama+"<---");
            }
        }else{
            p.setExito(Protocolo.RESPONSE_ERROR);
            trama = "Personaje ya registrado, intente otro";
            p.setTrama(trama);
            Util.enviarMensajeServidor(out, p);
            conectado = false;
        }
        imprimirCantidadEscenariosYPersonajes();
    }

    private void accionDesconectarPersonaje(Protocolo protocoloIn) throws Exception {
        Protocolo p = new Protocolo(
                Protocolo.SEND_TO_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO, 
                Protocolo.ACTION_DISCONNECT, 
                Protocolo.RESPONSE_SUCESSFULL,
                protocoloIn.getIdPersonajeOrigen(), 
                "", 
                protocoloIn.getIdEscenario(), 
                protocoloIn.getIdThread(),
                "",
                protocoloIn.getTrama());
        //aqui debo enviar un notificacion a todos que sali o cerre session
        //al cliente debe presentarle un mensaje en pantalla, pero tambien debe limpiar su lista de usuarios conectados
        //ojo, esto lo debo hacer solo para el cierre de session o deslogueo
        for (String claveEscenario : ServidorChat.mapaEscenariosActivos.keySet()) {
            for (DatosCliente datosClienteTmp : ServidorChat.mapaEscenariosActivos.get(claveEscenario)) {
                String idPersonajeOrigen = datosClienteTmp.getPersonaje().getId()+"|"+datosClienteTmp.getEscenario().getId();
                if(!idPersonajeOrigen.equalsIgnoreCase(protocoloIn.getIdPersonajeOrigen())){
                    Util.enviarMensajeServidor(datosClienteTmp.getOut(), p);
                }
            }
        }
        //removiendo personaje de los mapas de personajes
        if(ServidorChat.mapaPersonajesConectados.containsKey(Util.getClaveIdPersonajeOrigen(protocoloIn))){
            ServidorChat.mapaPersonajesConectados.remove(Util.getClaveIdPersonajeOrigen(protocoloIn));
        }    
        //removiendo personaje de los mapas de los escenarios activos en los que este incluido
        for (String claveEscenario : ServidorChat.mapaEscenariosActivos.keySet()) {
            for (DatosCliente datosClienteTmp : ServidorChat.mapaEscenariosActivos.get(claveEscenario)) {
                String idPersonajeOrigen = datosClienteTmp.getPersonaje().getId()+"|"+datosClienteTmp.getPersonaje().getUniverso().getId();
                if(idPersonajeOrigen.equalsIgnoreCase(protocoloIn.getIdPersonajeOrigen())){
                    ServidorChat.mapaEscenariosActivos.get(claveEscenario).remove(datosClienteTmp);
                }
            }
        }
        conectado = false;
        imprimirCantidadEscenariosYPersonajes();
    }

    private void accionAgregarPersonajeAlEscenarioChat(Protocolo protocoloIn) throws Exception {
        String claveIdEscenario = protocoloIn.getIdEscenario();
        if(protocoloIn.getIdEscenario().trim().equalsIgnoreCase("")){
            claveIdEscenario = util.getIdEscenarioAleatorio(idThread, ServidorChat.mapaEscenariosActivos.keySet());    
            protocoloIn.setIdEscenario( claveIdEscenario );
        }
        Protocolo p = new Protocolo(
                Protocolo.SEND_TO_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO, 
                Protocolo.ACTION_ADD_ALIAS_CHAT_ROOM, 
                Protocolo.RESPONSE_SUCESSFULL,
                protocoloIn.getIdPersonajeOrigen(),
                protocoloIn.getIdPersonajeDestino(), 
                protocoloIn.getIdEscenario(), 
                protocoloIn.getIdThread(),
                protocoloIn.getIdThreadDestino(), 
                "");
        List<DatosCliente> datosClientes = new ArrayList<>();
        DatosCliente datosClienteOrigen = ServidorChat.mapaPersonajesConectados.get(protocoloIn.getIdPersonajeOrigen()+"|"+protocoloIn.getIdThread());
        datosClienteOrigen.setEscenario(util.getEscenario(claveIdEscenario));
        ServidorChat.mapaPersonajesConectados.put(protocoloIn.getIdPersonajeOrigen()+"|"+protocoloIn.getIdThread(), datosClienteOrigen);
        DatosCliente datosClienteDestino = ServidorChat.mapaPersonajesConectados.get(protocoloIn.getIdPersonajeDestino()+"|"+protocoloIn.getIdThreadDestino());
        datosClienteDestino.setEscenario(util.getEscenario(claveIdEscenario));
        ServidorChat.mapaPersonajesConectados.put(protocoloIn.getIdPersonajeDestino()+"|"+protocoloIn.getIdThreadDestino(), datosClienteDestino);
        if(!ServidorChat.mapaEscenariosActivos.containsKey(protocoloIn.getIdEscenario())){ //ya contiene el idThread del que lo creo.
            datosClientes.add(datosClienteOrigen);
        }else{
            datosClientes = ServidorChat.mapaEscenariosActivos.get(protocoloIn.getIdEscenario());    
        }
        datosClientes.add(datosClienteDestino);
        ServidorChat.mapaEscenariosActivos.put(protocoloIn.getIdEscenario(), datosClientes);
        //no olvidar las respuestas al cliente, para que se hagan las acciones en javafx
        /*
        for (DatosCliente personajeConectado : ServidorChat.mapaPersonajesConectados.values()) {
            Util.enviarMensaje(personajeConectado.getOut(), p);
        }
        */
        for (DatosCliente personajeConectadoEscenario : ServidorChat.mapaEscenariosActivos.get(protocoloIn.getIdEscenario())) {
            Util.enviarMensajeServidor(personajeConectadoEscenario.getOut(), p);
        }
        imprimirCantidadEscenariosYPersonajes();
    }
    

    private void accionCerrarEscenarioChat(Protocolo protocoloIn) throws Exception {
        String idPersonajeOrigen = protocoloIn.getIdPersonajeOrigen().substring(0, protocoloIn.getIdPersonajeOrigen().indexOf("|"));
        String idUniverso = protocoloIn.getIdPersonajeOrigen().substring(protocoloIn.getIdPersonajeOrigen().indexOf("|")+1);
        Protocolo p = new Protocolo(
                Protocolo.SEND_TO_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO, 
                Protocolo.ACTION_CLOSE_CHAT_ALIAS_ROOM, 
                Protocolo.RESPONSE_SUCESSFULL,
                protocoloIn.getIdPersonajeOrigen(),
                "", 
                protocoloIn.getIdEscenario(), 
                protocoloIn.getIdThread(),
                protocoloIn.getIdThreadDestino(), 
                protocoloIn.getTrama());
        if(ServidorChat.mapaEscenariosActivos.containsKey(protocoloIn.getIdEscenario())){
            List<DatosCliente> datosClientes = ServidorChat.mapaEscenariosActivos.get(protocoloIn.getIdEscenario());
            for (DatosCliente datosClienteTmp : datosClientes) {
                if(datosClienteTmp.getPersonaje().getId().equalsIgnoreCase(idPersonajeOrigen) 
                    && datosClienteTmp.getPersonaje().getUniverso().getId().equalsIgnoreCase(idUniverso)){
                    datosClientes.remove(datosClienteTmp);
                    break;
                }
            }
            if(datosClientes.size()>0){
                ServidorChat.mapaEscenariosActivos.put(protocoloIn.getIdEscenario(), datosClientes);
                for (DatosCliente datosClienteTmp : datosClientes) {
                    Util.enviarMensajeServidor(datosClienteTmp.getOut(), p);
                }
            }else{
                ServidorChat.mapaEscenariosActivos.remove(protocoloIn.getIdEscenario());
            }    
        }
        imprimirCantidadEscenariosYPersonajes();
    }

    private void accionChatearEnEscenario(Protocolo protocoloIn) throws Exception {
        Protocolo p = new Protocolo(
                Protocolo.SEND_TO_CLIENT, 
                Protocolo.TIPO_EVENTO_USUARIO, 
                Protocolo.ACTION_CHAT_ROOM, 
                Protocolo.RESPONSE_SUCESSFULL,
                protocoloIn.getIdPersonajeOrigen(), 
                "", 
                protocoloIn.getIdEscenario(), 
                protocoloIn.getIdThread(), 
                protocoloIn.getIdThreadDestino(), 
                protocoloIn.getTrama());
        List<DatosCliente> datosClientes = null;
        if(ServidorChat.mapaEscenariosActivos.containsKey(protocoloIn.getIdEscenario())){
            datosClientes = ServidorChat.mapaEscenariosActivos.get(protocoloIn.getIdEscenario());
            for (DatosCliente datosClienteTmp : datosClientes) {
                String idPersonajeOrigen = protocoloIn.getIdPersonajeOrigen().substring(0, protocoloIn.getIdPersonajeOrigen().indexOf("|"));
                String idUniverso = protocoloIn.getIdPersonajeOrigen().substring(protocoloIn.getIdPersonajeOrigen().indexOf("|")+1);
                if(!datosClienteTmp.getPersonaje().getId().equalsIgnoreCase(idPersonajeOrigen) 
                    || !datosClienteTmp.getPersonaje().getUniverso().getId().equalsIgnoreCase(idUniverso)){
                    Util.enviarMensajeServidor(datosClienteTmp.getOut(), p);
                }else{
                    //Es el mismo usuario que envio el mensaje
                    if(datosClientes.size()==1){
                        p.setTrama("El mensaje no fue entregado al personaje destino");
                        Util.enviarMensajeServidor(datosClienteTmp.getOut(), p);
                    }
                }
            }
        }
    }

    private void accionNoSoportada(Protocolo protocoloIn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
