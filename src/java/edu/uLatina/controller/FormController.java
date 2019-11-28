/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import com.componentes.dao.FormularioDAO;
import com.componentes.dao.ItemDAO;
import com.componentes.dao.SeccionDAO;
import com.componentes.dao.UsuarioDAO;
import com.componentes.entidades.Formulario;
import com.componentes.entidades.Item;
import com.componentes.entidades.Item_;
import com.componentes.entidades.Seccion;
import com.componentes.entidades.Usuario;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dacor
 */
@ManagedBean (name = "formController")
@SessionScoped
public class FormController {

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }
    
    private int formId;
    private Formulario form;
    private List <Seccion> secciones;
    private List <Item> items;
    private Usuario u;
    

    public FormController() {

    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }
    
    public void validateId(int id) {
        try {

       if(id != 0){
                HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/ResponderFormulario.xhtml?faces-redirect=true"); 
            this.retrieveForm(id);

            
       }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void retrieveForm(int id){
        
        FormularioDAO fd = new FormularioDAO();
        form = fd.Get(id);
        SeccionDAO sd = new SeccionDAO();
        secciones = sd.seccionesEnFormulario(form);
        ItemDAO ids = new ItemDAO();
        for(Seccion ss : secciones){
          ss.SetItem(ids.itemsEnSecciones(ss));
        }
        form.SetSecciones(secciones);
        
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public Formulario getForm() {
        return form;
    }

    public void setForm(Formulario form) {
        this.form = form;
    }

    
}
