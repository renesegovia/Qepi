/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mavericksoft.qepi.util;

import com.mavericksoft.qepi.util.Escenario;
import com.mavericksoft.qepi.util.Personaje;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author lsegovia
 */
public class DatosCliente {
    private Personaje personaje;
    private Escenario escenario;
    private DataInputStream in;
    private DataOutputStream out;

    public Personaje getPersonaje() {
        return personaje;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }

    public Escenario getEscenario() {
        return escenario;
    }

    public void setEscenario(Escenario escenario) {
        this.escenario = escenario;
    }

    

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public DatosCliente(Personaje personaje, Escenario escenario, DataInputStream in, DataOutputStream out) {
        this.personaje = personaje;
        this.escenario = escenario;
        this.in = in;
        this.out = out;
    }

   
    
    
    
}
