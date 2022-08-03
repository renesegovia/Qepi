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
public class Personaje {
    private String id;
    private String nombre;
    private Universo universo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Universo getUniverso() {
        return universo;
    }

    public void setUniverso(Universo universo) {
        this.universo = universo;
    }

    public Personaje(String id, String nombre, Universo universo) {
        this.id = id;
        this.nombre = nombre;
        this.universo = universo;
    }

    @Override
    public String toString() {
        //return "Personaje{" + "id=" + id + ", nombre=" + nombre + ", universo=" + universo + '}';
        return nombre;
    }

    
    
    
    
}
