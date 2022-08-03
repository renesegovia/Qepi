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
public class RecursoConexion {
    private String id;
    private String servidor;
    private int puerto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public RecursoConexion(String id, String servidor, int puerto) {
        this.id = id;
        this.servidor = servidor;
        this.puerto = puerto;
    }

    @Override
    public String toString() {
        return "RecursoConexion{" + "id=" + id + ", servidor=" + servidor + ", puerto=" + puerto + '}';
    }
    
    
    
}
