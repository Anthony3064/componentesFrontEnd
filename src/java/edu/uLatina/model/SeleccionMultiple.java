/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Personal
 */
public class SeleccionMultiple {

private String pregunta;
private List<String> respuestas=new ArrayList<String>();
private String respuestaFinal;

    public SeleccionMultiple() {
        
    }

    public String getPregunta() {
        return pregunta;
    }

    public void llenarDefault(){
        
        for (int i = 0; i < 4; i++) {
            this.respuestas.add("Opcion " + String.valueOf(i+1));
        }
        
    }
    
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<String> respuestas) {
        this.respuestas = respuestas;
    }

    public String getRespuestaFinal() {
        return respuestaFinal;
    }

    public void setRespuestaFinal(String respuestaFinal) {
        this.respuestaFinal = respuestaFinal;
    }
    
    
    
}
