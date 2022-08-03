/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.client;

import com.mavericksoft.qepi.util.Personaje;
import com.mavericksoft.qepi.util.Protocolo;
import com.mavericksoft.qepi.util.Util;
import com.mavericksoft.qepi.util.DatosCliente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javafx.application.Platform;

/**
 *
 * @author lsegovia
 */
public class HiloProcesaRespuesta extends Thread{
//public class HiloProcesaRespuesta implements Runnable{
    private FXMLDocumentController fXMLDocumentController;
    List<WindowFxmlNodeController> wControllers;
    private DatosCliente datosCliente;
    private boolean leerRespuestaServidor;
    private Util util;

    public HiloProcesaRespuesta(FXMLDocumentController fXMLDocumentController) {
        this.fXMLDocumentController = fXMLDocumentController;
        this.datosCliente = fXMLDocumentController.getDatosClienteOrigen();
        this.leerRespuestaServidor = true;
        this.wControllers = new ArrayList<WindowFxmlNodeController>();
        this.util = new Util();
    }

    public List<WindowFxmlNodeController> getwControllers() {
        return wControllers;
    }

    public void setwControllers(List<WindowFxmlNodeController> wControllers) {
        this.wControllers = wControllers;
    }

    

    @Override
    public void run() {
        while(leerRespuestaServidor){
            try {
                procesarRespuestaServidor();    
            } catch (Exception ex) {
                if(ex instanceof IOException){
                    //leerRespuestaServidor = false;
                    Util.imprimir("No se puede responder al cliente: "+ex.getMessage());
                }else{
                    Util.imprimir("No se puede cifrar/descifrar: "+ex.getMessage());
                }
            }
        }
    }

    private void procesarRespuestaServidor() throws IOException, Exception {
        Protocolo p = Util.recibirMensajeCliente(datosCliente.getIn());
        switch(p.getAccion()){
            case Protocolo.ACTION_CONNECT:
                respuestaAccionConectarPersonaje(p);
                break;
            case Protocolo.ACTION_DISCONNECT:
                respuestaAccionDesconectarPersonaje(p);
                break;
            case Protocolo.ACTION_ADD_ALIAS_CHAT_ROOM:
                respuestaAccionAgregarPersonajeAlEscenarioChat(p);
                break;
            case Protocolo.ACTION_CLOSE_CHAT_ALIAS_ROOM:
                respuestaAccionCerrarEscenarioChat(p);
                break;
            case Protocolo.ACTION_CHAT_ROOM:
                respuestaAccionChatearEnEscenario(p);
                break;
            default:
                //accionNoSoportada(idPersonaje);
                break;
        }
    }

    private void respuestaAccionConectarPersonaje(Protocolo p) {
        if(p.getExito().equalsIgnoreCase(Protocolo.RESPONSE_SUCESSFULL)){
            fXMLDocumentController.setIdThreadServidor(p.getIdThread());
            StringTokenizer st = new StringTokenizer(p.getTrama(), " ");
            String idPersonaje = "";
            String idUniverso = "";
            String idThread = "";
            String claveMapaPersonajesConectados="";
            while (st.hasMoreTokens()) {
                 claveMapaPersonajesConectados = st.nextToken();
                 if(!fXMLDocumentController.getClavesMapaPersonajesConectados().contains(claveMapaPersonajesConectados)){
                     //guardo las claves del mapa de personajes conectados, que incluye el hilo destino
                     fXMLDocumentController.getClavesMapaPersonajesConectados().add(claveMapaPersonajesConectados);
                 }
                 idPersonaje = claveMapaPersonajesConectados.substring(0, claveMapaPersonajesConectados.indexOf("|"));
                 idUniverso = claveMapaPersonajesConectados.substring(claveMapaPersonajesConectados.indexOf("|")+1, claveMapaPersonajesConectados.indexOf("|", claveMapaPersonajesConectados.indexOf("|")+1));
                 idThread = claveMapaPersonajesConectados.substring(claveMapaPersonajesConectados.indexOf("|", claveMapaPersonajesConectados.indexOf("|")+1)+1);
                 final Personaje personaje = fXMLDocumentController.getUtil().getPersonaje(idPersonaje,idUniverso);
                 Platform.runLater(() -> {
                     //Remover personajes conectados, desde el combo de opciones
                     if(fXMLDocumentController.getPersonajesObservable().contains(personaje)){
                         if(personaje.getId().equalsIgnoreCase(datosCliente.getPersonaje().getId()) 
                                 && personaje.getUniverso().getId().equalsIgnoreCase(datosCliente.getPersonaje().getUniverso().getId())){
                             fXMLDocumentController.getAliasComboBox().getSelectionModel().select(personaje);
                             fXMLDocumentController.getAliasComboBox().setDisable(true);
                         }else{
                             fXMLDocumentController.getPersonajesObservable().remove(personaje);
                         }
                     }
                     //Agregar a la lista de personajes conectados.
                     if(!fXMLDocumentController.getPersonajesConectadosObservable().contains(personaje)){
                         fXMLDocumentController.getPersonajesConectadosObservable().add(personaje);
                     }
                 });
            }
            Platform.runLater(() -> {
                fXMLDocumentController.getOffToggleButton().setSelected(false);
                fXMLDocumentController.getOnToggleButton().setSelected(true);
                fXMLDocumentController.getMensajeLb().setText("");
                fXMLDocumentController.getUsuariosListView().setDisable(false);
            });
        }else{ //Protocolo.RESPONSE_ERROR
            final Personaje personaje = fXMLDocumentController.getUtil().getPersonaje(p.getIdPersonajeOrigen());
            Platform.runLater(() -> {
                //Remover personajes conectados, desde el combo de opciones
                if(fXMLDocumentController.getPersonajesObservable().contains(personaje)){
                    fXMLDocumentController.getPersonajesObservable().remove(personaje);
                }
                //Agregar a la lista de personajes conectados.
                if(!fXMLDocumentController.getPersonajesConectadosObservable().contains(personaje)){
                    fXMLDocumentController.getPersonajesConectadosObservable().add(personaje);
                }
                fXMLDocumentController.getOffToggleButton().setSelected(true);
                fXMLDocumentController.getOnToggleButton().setSelected(false);
                fXMLDocumentController.getMensajeLb().setText(p.getTrama());
            });
            /*
            poner estado de boton como no conectado, actualizar como quitar el personaje
            del combo y agregar en la lista al personaje, pero mostrar el mensaje al final
            de la ventana indicando que el Personaje ya fue registrado, intente otro...
            */
            //poner la funcion de desconexion
        }
    }

    private void respuestaAccionAgregarPersonajeAlEscenarioChat(Protocolo p) {
        Platform.runLater(() -> {
            if(wControllers.size()>0){
                //java.util.ConcurrentModificationException
                //si falla, hay que cambiar el orden de las listas...
                List<WindowFxmlNodeController> wControllersIteracion = new ArrayList<WindowFxmlNodeController>(wControllers);
                for (WindowFxmlNodeController wController : wControllersIteracion) {
                    if(wController.getIdEscenarioActivo().equals(p.getIdEscenario())){
                        continue;
                    }
                    fXMLDocumentController.windowChat(p);
                }
                wControllersIteracion.addAll(wControllers);
            }else{
                fXMLDocumentController.windowChat(p);
            }
        });
    }

    private void respuestaAccionChatearEnEscenario(Protocolo p) {
        Platform.runLater(() -> {
            for (WindowFxmlNodeController wController : wControllers) {
                if(wController.getIdEscenarioActivo().equals(p.getIdEscenario())){
                    /*
                    wController.getTextAreaOut().appendText(Util.getHora()+" "+fXMLDocumentController.getUtil().getPersonaje(p.getIdPersonajeOrigen()).getNombre()+":\n"+p.getTrama()+"\n");
                    */
                    if(p.getIdPersonajeOrigen().equals(datosCliente.getPersonaje().getId()+"|"+datosCliente.getPersonaje().getUniverso().getId())){
                        wController.agregarChat("", p.getTrama(), "CENTRO");
                    }else{
                        wController.agregarChat(fXMLDocumentController.getUtil().getPersonaje(p.getIdPersonajeOrigen()).getNombre(), p.getTrama(), "IZQUIERDA");
                    }
                    
                    fXMLDocumentController.alertaMensaje(wController.getIdEscenarioActivo());
                    break;
                }
            }
        });
        
    }

    private void respuestaAccionCerrarEscenarioChat(Protocolo p) {
        Platform.runLater(() -> {
            if(wControllers != null && wControllers.size() > 0){
                for (WindowFxmlNodeController wController : wControllers) {
                    if(wController.getIdEscenarioActivo().equals(p.getIdEscenario())){
                        wController.agregarChat("", p.getTrama(), "CENTRO");
                        break;
                    }
                }
            }
            
        });
    }

    private void respuestaAccionDesconectarPersonaje(Protocolo p) {
        Platform.runLater(() -> {
            if(wControllers != null && wControllers.size() > 0){
                for (WindowFxmlNodeController wController : wControllers) {
                    if(wController.getIdEscenarioActivo().equals(p.getIdEscenario())){
                        wController.agregarChat("", p.getTrama(), "CENTRO");
                        break;
                    }
                }
            }
            
        });
        ////////
        //Terminado
        //Quiero poner al reves de este codigo, es decir agregar personaje desde el combo
        //de opciones pero eliminar de la lista de personajes conectados
        //debo usar p.getPersonaje() que viene idPersonaje y idUniverso
        Personaje personaje =   util.getPersonaje(p.getIdPersonajeOrigen());
        Platform.runLater(() -> {
            //Agregar personajes libres, desde el combo de opciones
            if (fXMLDocumentController.getPersonajesObservable().contains(personaje)) {
                if (personaje.getId().equalsIgnoreCase(datosCliente.getPersonaje().getId())
                        && personaje.getUniverso().getId().equalsIgnoreCase(datosCliente.getPersonaje().getUniverso().getId())) {
                    fXMLDocumentController.getAliasComboBox().setDisable(false);
                } else {
                    fXMLDocumentController.getPersonajesObservable().add(personaje);
                }
            }
            //Eliminar de la lista de personajes conectados.
            if (fXMLDocumentController.getPersonajesConectadosObservable().contains(personaje)) {
                fXMLDocumentController.getPersonajesConectadosObservable().remove(personaje);
            }
        });
        //Tambien actualizar los mapas locales de los usuarios
        if(fXMLDocumentController.getClavesMapaPersonajesConectados().contains(personaje)){
            fXMLDocumentController.getClavesMapaPersonajesConectados().remove(personaje);
        }
        
        
        ////////
        leerRespuestaServidor = false;
        
    }
    
    
    
}
