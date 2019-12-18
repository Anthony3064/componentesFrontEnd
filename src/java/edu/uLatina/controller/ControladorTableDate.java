package edu.uLatina.controller;

import com.componentes.controlador.UsuarioController;
import com.componentes.entidades.EItem;
import com.componentes.entidades.Encuesta;
import com.componentes.entidades.Formulario;
import com.componentes.entidades.Item;
import com.componentes.entidades.Seccion;
import edu.uLatina.model.OpcionTexto;
import edu.uLatina.model.SeleccionMultiple;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Personal
 */
@ManagedBean(name = "dataTableController")
@SessionScoped
public class ControladorTableDate {

    @ManagedProperty("#{loginController}")
    private LoginController usuario;

    @ManagedProperty("#{ControllerFormulario}")
    private String nombreFormulario;
    private String pregunta;
    private String respuesta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;
    private List<OpcionTexto> listaSeccionesTexto = new ArrayList<>();
    private List<SeleccionMultiple> listaSecciones = new ArrayList<>();
    private Formulario frm = null;

    private String tipo = "";
    private boolean seleccionMultipleRendered = false;
    private boolean textoRendered = false;
    
    public ControladorTableDate() {

    }

    public List<SeleccionMultiple> getListaSecciones() {
        return listaSecciones;
    }

    public void setListaSecciones(List<SeleccionMultiple> listaSecciones) {
        this.listaSecciones = listaSecciones;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Formulario getFrm() {
        return frm;
    }

    public void setFrm(Formulario frm) {
        this.frm = frm;
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

    public String getNombreFormulario() {
        return nombreFormulario;
    }

    public void setNombreFormulario(String nombreFormulario) {
        this.nombreFormulario = nombreFormulario;
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
    
    public void seleccionFormulario(){
        
        if (tipo.equals("Texto")){
            
            this.seleccionMultipleRendered = false;
            textoRendered = true;
            
        }else if (tipo.equals("Seleccion Multiple")){
            
            this.textoRendered = false;
            this.seleccionMultipleRendered = true;
            
        }
        
    }
    
    
    public void llenarListaSeccion() {

        SeleccionMultiple sm = new SeleccionMultiple();

        sm.setPregunta(pregunta);
        sm.getRespuestas().add(opcion1);
        sm.getRespuestas().add(opcion2);
        sm.getRespuestas().add(opcion3);
        sm.getRespuestas().add(opcion4);

        this.listaSecciones.add(sm);
        this.limpiar();

    }

    public void llenarListaOpcionTexto() {

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

    public LoginController getUsuario() {
        return usuario;
    }

    public void setUsuario(LoginController usuario) {
        this.usuario = usuario;
    }

    public void limpiar() {

        this.pregunta = "";
        this.opcion1 = "";
        this.opcion2 = "";
        this.opcion3 = "";
        this.opcion4 = "";
        this.respuesta = "";

    }

    public void redireccionACrearFormulario() {
        try {

            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/CrearFormulario.xhtml?faces-redirect=true");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void guardarFormulario() {
        
        UsuarioController uC = new UsuarioController();
        
        Encuesta encuesta = new Encuesta();
        encuesta.setUsuarioPadre(this.usuario.getUser());
        
        List<Encuesta> encuestas = new ArrayList<>();
        List<Seccion> secciones = new ArrayList<>();

        frm = new Formulario();
        frm.setNombre(this.nombreFormulario);
        frm.setFavorito(false);

        if (!this.getListaSecciones().isEmpty()) {

            for (SeleccionMultiple sM : this.getListaSecciones()) {

                List<Item> items = new ArrayList<>();
                Seccion sccn = new Seccion();
                sccn.setFormularioPadre(frm);
                sccn.setPregunta(sM.getPregunta());

                for (String str : sM.getRespuestas()) {
                    Item item = new Item();
                    item.setSeccion(sccn);
                    item.setDefaultName(str);
                    item.setTipoDato(EItem.RadioButton);
                    items.add(item);
                }
                sccn.SetItem(items);
                secciones.add(sccn);
            }

        }

        if (!this.getListaSeccionesTexto().isEmpty()) {

            for (OpcionTexto oT : this.getListaSeccionesTexto()) {

                List<Item> items = new ArrayList<>();
                Seccion sccn = new Seccion();
                sccn.setFormularioPadre(frm);
                sccn.setPregunta(oT.getPregunta());

                Item item = new Item();
                item.setSeccion(sccn);
                item.setDefaultName(oT.getRespuesta());
                item.setTipoDato(EItem.TextBox);
                items.add(item);

                sccn.SetItem(items);
                secciones.add(sccn);
            }

        }
        frm.SetSecciones(secciones);
        frm.setEncuesta(encuesta);
        frm.setIsInterface(true);
        encuesta.setFrmScaffolding(frm);
        
        if (!frm.GetSecciones().isEmpty() && !frm.getNombre().equalsIgnoreCase("")) {
            encuestas.add(encuesta);
            
            this.usuario.getUser().setEncuestas(encuestas);
            
            uC.Update(this.usuario.getUser());
            
            this.nombreFormulario = "";

            this.listaSecciones.clear();
            this.listaSeccionesTexto.clear();

            this.limpiar();
            this.esconder();
            
            this.usuario.redireccionALandingPage(usuario.getUser());
            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se guardó con éxito.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Por favor llene la pregunta o las secciones.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

}
