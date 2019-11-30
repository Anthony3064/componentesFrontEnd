/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import edu.uLatina.model.SeleccionMultiple;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Personal
 */

@ManagedBean(name = "controllerFormulario")
@SessionScoped 
public class ControllerFormulario {
    
    private String tipo = "";
    private boolean seleccionMultipleRendered = false;
    private boolean textoRendered = false;
    private SeleccionMultiple seleccionMultiple = new SeleccionMultiple();
    private List<SeleccionMultiple> listaSelecciones = new ArrayList<>();
    private String preguntaGuardar = "";
    private String opcion1 = "";
    private String opcion2 = "";
    private String opcion3 = "";
    private String opcion4 = "";
    private List<Integer> items = new ArrayList<>();
    
    public ControllerFormulario() {
        
        this.esconder();
        this.llenarItems();
        
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isSeleccionMultipleRendered() {
        return seleccionMultipleRendered;
    }

    public void setSeleccionMultipleRendered(boolean seleccionMultipleRendered) {
        this.seleccionMultipleRendered = seleccionMultipleRendered;
    }

    public boolean isTextoRendered() {
        return textoRendered;
    }

    public void setTextoRendered(boolean textoRendered) {
        this.textoRendered = textoRendered;
    }
    
    public void esconder(){
        this.seleccionMultipleRendered = false;
        this.textoRendered = false;
    }

    public SeleccionMultiple getSeleccionMultiple() {
        return seleccionMultiple;
    }

    public void setSeleccionMultiple(SeleccionMultiple seleccionMultiple) {
        this.seleccionMultiple = seleccionMultiple;
    }

    public List<SeleccionMultiple> getListaSelecciones() {
        return listaSelecciones;
    }

    public void setListaSelecciones(List<SeleccionMultiple> listaSelecciones) {
        this.listaSelecciones = listaSelecciones;
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

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public String getPreguntaGuardar() {
        return preguntaGuardar;
    }

    public void setPreguntaGuardar(String preguntaGuardar) {
        this.preguntaGuardar = preguntaGuardar;
    }

    public void cargar(SeleccionMultiple sm){
        
        this.listaSelecciones.add(sm);
        
    }
    
    public void llenarItems(){
        
        for (int i = 0; i < 4; i++) {
            this.items.add(i);
        }
        
    }
    
    public boolean seleccionFormulario(){
        
        if (tipo.equals("Texto")){
            
            this.seleccionMultipleRendered = false;
            textoRendered = true;
            return this.textoRendered;
        }
        else if (tipo.equals("Seleccion Multiple")){
            
            this.textoRendered = false;
            this.seleccionMultipleRendered = true;
            
        }
        return this.seleccionMultipleRendered;
    }
    
    
    public void agregarSeccion(){

        this.prueba(preguntaGuardar, opcion1, opcion2, opcion3, opcion4);
        
        for (SeleccionMultiple m : this.listaSelecciones) {
            
            System.out.println(m.getPregunta());
            
            for (String s:m.getRespuestas()) {
                System.out.println(s);
            }
            
        }
        
        this.limpiar();
        
    }
    
    public void prueba(String pregunta,String op1,String op2,String op3,String op4){
    
        this.seleccionMultiple.setPregunta(pregunta);
        this.seleccionMultiple.getRespuestas().add(op1);
        this.seleccionMultiple.getRespuestas().add(op2);
        this.seleccionMultiple.getRespuestas().add(op3);
        this.seleccionMultiple.getRespuestas().add(op4);
    
        this.listaSelecciones.add(seleccionMultiple);
        
    }

    public List<SeleccionMultiple> getAll(){
    
        return this.listaSelecciones;
        
    }
    
    public void limpiar(){
    
        this.seleccionMultiple.setPregunta("");
        this.seleccionMultiple.getRespuestas().clear();
        
        this.opcion1 = "";
        this.opcion2 = "";
        this.opcion3 = "";
        this.opcion4 = "";
        this.preguntaGuardar = "";
        
    }

    public void redireccionACrearFormulario(){
                    try {
           
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/CrearFormulario.xhtml?faces-redirect=true");  //Aqui deberia ir a la Landing Page pero pues aun no existe
            }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
           
     }
    
}
