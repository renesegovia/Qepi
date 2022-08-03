/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.util;

/**
 *
 * @author lsegovia
 */
public class Protocolo {
    public static final String RECIBE_FROM_CLIENT = "R";
    public static final String SEND_TO_CLIENT = "E";
    public static final String ACTION_CONNECT = "000";
    public static final String ACTION_DISCONNECT = "001";
    public static final String ACTION_ADD_ALIAS_CHAT_ROOM = "002";
    public static final String ACTION_CLOSE_CHAT_ALIAS_ROOM = "003";
    public static final String ACTION_CHAT_ROOM = "004";
    public static final String RESPONSE_SUCESSFULL = "000";
    public static final String RESPONSE_ERROR = "999";
    public static final String TIPO_CONTROL = "0";
    public static final String TIPO_EVENTO_USUARIO = "1";
    
    /**
     * Indica el flujo del mensaje, R si la trama llega al servidor, E si sale del servidor
     */
    private String flujo;
    
    /**
     * Indica el tipo de mensaje, 0 si es de control, 1 si es un mensaje disparado por un evento del usuario
     */
    private String tipo;
    
    /**
     * Indica posibles acciones o eventos ejecutados desde el cliente o respuestas desde el servidor, al momento
     * las siguientes:
     * ACTION_CONNECT = "000";
     * ACTION_DISCONNECT = "001";
     * ACTION_ADD_ALIAS_CHAT_ROOM = "002";
     * ACTION_CLOSE_CHAT_ALIAS_ROOM = "003";
     * ACTION_CHAT_ROOM = "004";
     */
    private String accion;
    
    /**
     * Indica si la respuesta del servidor es exitosa 000, o trae algun codigo de error 999
     */
    private String exito;
    
    /**
     * Codigo del personaje origen que envia el mensaje en el chat
     */
    private String idPersonajeOrigen;
    
    /**
     * Codigo del personaje destino que recibira el mensaje en el chat
     */
    private String idPersonajeDestino;
    
    /**
     * Codigo del escenario o sala donde se producira una conversacion o chat
     */
    private String idEscenario;
    
    /**
     * Codigo del Thread del proceso de atencion al cliente generado en el servidor
     */
    private String idThread;
    
    /**
     * Codigo del Thread Destino del proceso de atencion al cliente generado en el servidor
     */
    private String idThreadDestino;
    
    /**
     * Trama o cuerpo del mesaje, puede contener informacion de control o simplemente
     * mensaje del chat o conversacion propiamente dicha, dependiendo de la necesidad
     * en cada metodo esta trama sera utilizada directamente o parseada
     */
    private String trama;
    
    

    public String getFlujo() {
        return flujo;
    }

    public void setFlujo(String flujo) {
        this.flujo = flujo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getExito() {
        return exito;
    }

    public void setExito(String exito) {
        this.exito = exito;
    }

    public String getIdPersonajeOrigen() {
        return idPersonajeOrigen;
    }

    public void setIdPersonajeOrigen(String idPersonajeOrigen) {
        this.idPersonajeOrigen = idPersonajeOrigen;
    }

    public String getIdPersonajeDestino() {
        return idPersonajeDestino;
    }

    public void setIdPersonajeDestino(String idPersonajeDestino) {
        this.idPersonajeDestino = idPersonajeDestino;
    }

    public String getIdEscenario() {
        return idEscenario;
    }

    public void setIdEscenario(String idEscenario) {
        this.idEscenario = idEscenario;
    }

    public String getIdThread() {
        return idThread;
    }

    public void setIdThread(String idThread) {
        this.idThread = idThread;
    }

    public String getIdThreadDestino() {
        return idThreadDestino;
    }

    public void setIdThreadDestino(String idThreadDestino) {
        this.idThreadDestino = idThreadDestino;
    }
    
    public String getTrama() {
        return trama;
    }

    public void setTrama(String trama) {
        this.trama = trama;
    }
    
    public Protocolo(String flujo, String tipo, String accion, String exito, String idPersonajeOrigen, String idPersonajeDestino, String idEscenario, String idThread, String idThreadDestino, String trama) {
        this.flujo = flujo;
        this.tipo = tipo;
        this.accion = accion;
        this.exito = exito;
        this.idPersonajeOrigen = idPersonajeOrigen;
        this.idPersonajeDestino = idPersonajeDestino;
        this.idEscenario = idEscenario;
        this.idThread = idThread;
        this.idThreadDestino = idThreadDestino;
        this.trama = trama;
    }

    @Override
    public String toString() {
        return "Protocolo{" + "flujo=" + flujo + ", tipo=" + tipo + ", accion=" + accion + ", exito=" + exito + ", idPersonajeOrigen=" + idPersonajeOrigen + ", idPersonajeDestino=" + idPersonajeDestino + ", idEscenario=" + idEscenario + ", idThread=" + idThread + ", idThreadDestino=" + idThreadDestino + ", trama=" + trama + '}';
    }
    
    

    
}
