/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import com.componentes.controlador.EncuestaController;
import com.componentes.controlador.FormularioController;
import com.componentes.dao.ItemDAO;
import com.componentes.dao.SeccionDAO;
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

    private FormularioController fC = new FormularioController();
    private EncuestaController eCon = new EncuestaController();
    private Encuesta encuesta = new Encuesta();
    private PieChartModel model = new PieChartModel();
    private List<PieChartModel> models = new ArrayList<>();

    private List<Seccion> listaSecciones = new ArrayList<>();
    private String link = "";
    private String tempEmail = "";
    private SeccionDAO sD = new SeccionDAO();
    private ItemDAO iD = new ItemDAO();

    private int formId;
    private Formulario form = null;
    private List<Seccion> secciones;
    private List<Item> items;

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
                        if (sec.getPregunta().equalsIgnoreCase(seccionNombre)) {
                            for (Item item : sec.getItem()) {
                                if (item.getDefaultName().equalsIgnoreCase(itemNombre)) {
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

    public void llenarPieChart(int id) {

        for (Formulario f : this.fC.Get(id).getEncuesta().getRespuestas()) {
            if (f.isIsInterface()) {

                for (Seccion scc : f.GetSecciones()) {
                    String seccionNombre = scc.getPregunta();
                    model = new PieChartModel();
                    model.setTitle(seccionNombre);
                    model.setLegendPosition("e");
                    model.setShowDatatip(true);
                    for (Item i : scc.getItem()) {
                        model.set(i.getDefaultName(), 12);
                    }
                    this.models.add(model);
                }

            }

        }

    }

    public List<PieChartModel> devolverRespuestas(int id) {

        EncuestaController ec = new EncuestaController();
        Encuesta encuesta = fC.Get(id).getEncuesta();
        List<PieChartModel> models = new ArrayList();
        Formulario scaf = encuesta.getFrmScaffolding();

        List<Seccion> secciones = scaf.GetSecciones();

        for (Formulario form : fC.Get(id).getEncuesta().getRespuestas()) {

            if (form.isIsInterface()) {
                for (Seccion sec : secciones) {
                    PieChartModel model = new PieChartModel();
                    model.setTitle(sec.getPregunta());
                    List<Item> items = sec.getItem();
                    for (Item i : items) {

                        for (Formulario form2 : fC.Get(id).getEncuesta().getRespuestas()) {

                            if (!form2.isIsInterface()) {
                                int respuesta = 0;
                                List<Formulario> formularios = encuesta.getRespuestas();
                                for (Formulario f : formularios) {
                                    List<Seccion> secciones2 = f.GetSecciones();
                                    for (Seccion sec2 : secciones2) {
                                        List<Item> items2 = sec2.getItem();
                                        for (Item i2 : items2) {
                                            if (i2 == i) {
                                                respuesta++;
                                            }
                                        }
                                    }
                                }
                                model.set(i.getDefaultName(), respuesta);
                            }
                            
                            models.add(model);
                        }
                    }
                }
            } else {

            }

        }

        return models;
    }

    public void conseguirRespuesta(int id) {

        encuesta = fC.Get(id).getEncuesta();
        List<Formulario> Formularios = encuesta.getRespuestas();
        Formulario sca = encuesta.getFrmScaffolding();

        for (Seccion s : secciones) {
            String seccionNombre = s.getPregunta();
            model = new PieChartModel();
            model.setTitle(seccionNombre);
            model.setLegendPosition("e");
            model.setShowDatatip(true);
            items = iD.itemsEnSecciones(s);
            for (Item i : items) {
                String itemNombre = i.getDefaultName();
                int itemRespuesta = 0;
                //conseguir sección que calze con seccionNombre
                //conseguir el item que calce con itemNombre
                //si calza entonces se le agrega al "itemRespuesta"
                for (Formulario F : eCon.Get(id).getRespuestas()) {
                    for (Seccion sec : sD.seccionesEnFormulario(F)) {
                        if (sec.getPregunta().equalsIgnoreCase(seccionNombre)) {
                            for (Item item : iD.itemsEnSecciones(sec)) {
                                if (item.getDefaultName().equalsIgnoreCase(itemNombre)) {
                                    itemRespuesta++;
                                }
                            }
                        }
                    }
                }
                model.set(itemNombre, itemRespuesta);
                this.models.add(model);
            }

        }

    }

    public void validateId(int id) {
        this.asignarId(id);
        try {

            if (id != 0) {
                this.devolverRespuestas(id);
                HttpServletRequest request = (HttpServletRequest) FacesContext
                        .getCurrentInstance().getExternalContext().getRequest();
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .redirect(
                                request.getContextPath()
                                + "/faces/pieTest.xhtml?faces-redirect=true&ids=" + id);

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

    public FormularioController getfC() {
        return fC;
    }

    public void setfC(FormularioController fC) {
        this.fC = fC;
    }

    public SeccionDAO getsD() {
        return sD;
    }

    public void setsD(SeccionDAO sD) {
        this.sD = sD;
    }

    public List<PieChartModel> getModels() {
        return models;
    }

    public void setModels(List<PieChartModel> models) {
        this.models = models;
    }

    public ItemDAO getiD() {
        return iD;
    }

    public void setiD(ItemDAO iD) {
        this.iD = iD;
    }

}
