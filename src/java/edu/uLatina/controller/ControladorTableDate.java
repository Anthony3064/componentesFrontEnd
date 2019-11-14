/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import edu.uLatina.model.OpcionTexto;
import edu.uLatina.model.SeleccionMultiple;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Personal
 */
@ManagedBean(name = "dataTableController")
@SessionScoped 
public class ControladorTableDate {
    
    @ManagedProperty("#{ControllerFormulario}")
    private ControllerFormulario lista;
    private String pregunta;
    private String respuesta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private List<OpcionTexto> listaSeccionesTexto = new ArrayList<>(); 
    private List<SeleccionMultiple> listaSecciones = new ArrayList<>();
    
    public ControladorTableDate() {

    }

    public List<SeleccionMultiple> getListaSecciones() {
        return listaSecciones;
    }

    public void setListaSecciones(List<SeleccionMultiple> listaSecciones) {
        this.listaSecciones = listaSecciones;
    }

    public ControllerFormulario getLista() {
        return lista;
    }

    public void setLista(ControllerFormulario lista) {
        this.lista = lista;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }
    
    public void llenarListaSeccion(){
        
        SeleccionMultiple sm = new SeleccionMultiple();
        
        sm.setPregunta(pregunta);
        sm.getRespuestas().add(opcion1);
        sm.getRespuestas().add(opcion2);
        sm.getRespuestas().add(opcion3);
        sm.getRespuestas().add(opcion4);
        
        this.listaSecciones.add(sm);
        this.limpiar();
        
    }
    
    public void llenarListaOpcionTexto(){
    
        OpcionTexto opcionTexto = new OpcionTexto();
        
        opcionTexto.setPregunta(pregunta);
        opcionTexto.setRespuesta(respuesta);
    
        this.listaSeccionesTexto.add(opcionTexto);
        this.limpiar();
        
    }

    public List<OpcionTexto> getListaSeccionesTexto() {
        return listaSeccionesTexto;
    }

    public void setListaSeccionesTexto(List<OpcionTexto> listaSeccionesTexto) {
        this.listaSeccionesTexto = listaSeccionesTexto;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    
    
    public void limpiar(){
    
        this.pregunta = "";
        this.opcion1 = "";
        this.opcion2 = "";
        this.opcion3 = "";
        this.opcion4 = "";
        this.respuesta = "";
    }
    
    public void llenarLista(){
        this.listaSecciones = this.lista.getListaSelecciones();    
    }
    
}
