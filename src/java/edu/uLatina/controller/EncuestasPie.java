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
import com.componentes.entidades.Usuario;
import edu.uLatina.model.OpcionTexto;
import edu.uLatina.model.SeleccionMultiple;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
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
    
    private List<Seccion> listaSecciones = new ArrayList<>();
    private String link = "";
    private String tempEmail = "";

    private int formId;
    private Formulario form = null;
    private List<Seccion> secciones;
    private List<Item> items;
    private Usuario u;
    private List<SeleccionMultiple> listaSeccionSeleccionMultiple = new ArrayList<>();
    private List<OpcionTexto> listaSeccionOpcionTexto = new ArrayList<>();


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
    
    public ArrayList<PieChartModel> conseguirRespuesta(int id) {
        
        encuesta = eCon.Get(id);
        List<Formulario> Formularios = encuesta.getRespuestas();
        ArrayList<PieChartModel> Modelos = new ArrayList();
        Formulario sca = encuesta.getFrmScaffolding();
        secciones = sca.GetSecciones();
       
        for (Seccion s : secciones) {
            String seccionNombre = s.getPregunta();
            model = new PieChartModel();
            model.setTitle(seccionNombre);
            model.setLegendPosition("e");
            model.setShowDatatip(true);
            items = s.getItem();
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
    
    
    public void validateId(int id) {
        this.asignarId(id);
        try {

            if (id != 0) {
                this.listaSeccionOpcionTexto.clear();
                this.listaSeccionSeleccionMultiple.clear();

                HttpServletRequest request = (HttpServletRequest) FacesContext
                        .getCurrentInstance().getExternalContext().getRequest();
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .redirect(
                                request.getContextPath()
                                + "/faces/pieTest.xhtml?faces-redirect=true&id="+id);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void asignarId(int id) {

        this.formId = id;

    }

    public List<Seccion> getListaSecciones() {
        return listaSecciones;
    }

    public void setListaSecciones(List<Seccion> listaSecciones) {
        this.listaSecciones = listaSecciones;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTempEmail() {
        return tempEmail;
    }

    public void setTempEmail(String tempEmail) {
        this.tempEmail = tempEmail;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public Formulario getForm() {
        return form;
    }

    public void setForm(Formulario form) {
        this.form = form;
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }

    public List<SeleccionMultiple> getListaSeccionSeleccionMultiple() {
        return listaSeccionSeleccionMultiple;
    }

    public void setListaSeccionSeleccionMultiple(List<SeleccionMultiple> listaSeccionSeleccionMultiple) {
        this.listaSeccionSeleccionMultiple = listaSeccionSeleccionMultiple;
    }

    public List<OpcionTexto> getListaSeccionOpcionTexto() {
        return listaSeccionOpcionTexto;
    }

    public void setListaSeccionOpcionTexto(List<OpcionTexto> listaSeccionOpcionTexto) {
        this.listaSeccionOpcionTexto = listaSeccionOpcionTexto;
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
