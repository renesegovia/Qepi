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
public class RecursoConfiguracion {
    private boolean debug;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public RecursoConfiguracion(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return "RecursoConfiguracion{" + "debug=" + debug + '}';
    }

    
    
    
    
    
    
    
}
