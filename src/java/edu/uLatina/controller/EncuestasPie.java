/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import com.componentes.controlador.EncuestaController;
import com.componentes.entidades.Encuesta;
import com.componentes.entidades.Formulario;
import com.componentes.entidades.Item;
import com.componentes.entidades.Seccion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Kainthel
 */
@ManagedBean(name = "encuestasPie")
@SessionScoped
public class EncuestasPie {

    private EncuestaController eCon = new EncuestaController();
    private Encuesta encuesta = new Encuesta();
    private PieChartModel model = new PieChartModel();

    public ArrayList<PieChartModel> conseguirRespuesta() {
        
        List<Formulario> Formularios = encuesta.getRespuestas();
        ArrayList<PieChartModel> Modelos = new ArrayList();
        Formulario sca = encuesta.getFrmScaffolding();
        List<Seccion> secciones = sca.GetSecciones();
       
        for (Seccion s : secciones) {
            String seccionNombre = s.getPregunta();
            model = new PieChartModel();
            model.setTitle(seccionNombre);
            model.setLegendPosition("e");
            model.setShowDatatip(true);
            List<Item> items = s.getItem();
            for (Item i : items) {
                String itemNombre = i.getDefaultName();
                int itemRespuesta = 0;
                //conseguir sección que calze con seccionNombre
                //conseguir el item que calce con itemNombre
                //si calza entonces se le agrega al "itemRespuesta"
                for (Formulario F : Formularios) {
                     for (Seccion sec : F.GetSecciones()) {
                         if (sec.getPregunta().equalsIgnoreCase(seccionNombre)){
                             for (Item item : sec.getItem()){
                                 if (item.getDefaultName().equalsIgnoreCase(itemNombre)){
                                     itemRespuesta++;
                                 }
                             }
                         }
                     }
                }
                model.set(itemNombre, itemRespuesta);
                Modelos.add(model);
            }

        }
        return Modelos;
    }


    public EncuestaController geteCon() {
        return eCon;
    }

    public void seteCon(EncuestaController eCon) {
        this.eCon = eCon;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public PieChartModel getModel() {
        return model;
    }

    public void setModel(PieChartModel model) {
        this.model = model;
    }
    
    
}
