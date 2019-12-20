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
import com.componentes.entidades.EItem;
import com.componentes.entidades.Encuesta;
import com.componentes.entidades.Formulario;
import com.componentes.entidades.Item;
import com.componentes.entidades.Seccion;
import com.componentes.entidades.Usuario;
import edu.uLatina.model.OpcionTexto;
import edu.uLatina.model.SeleccionMultiple;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author dacor
 */
@ManagedBean(name = "formController")
@SessionScoped
public class FormController {

    private List<Seccion> listaSecciones = new ArrayList<>();
    private String link = "";
    private String tempEmail = "";

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }

    private int formId;
    private Formulario form = null;
    private List<Seccion> secciones;
    private List<Item> items;
    private Usuario u;
    private List<SeleccionMultiple> listaSeccionSeleccionMultiple = new ArrayList<>();
    private List<OpcionTexto> listaSeccionOpcionTexto = new ArrayList<>();
    private ArrayList<PieChartModel> models = new ArrayList<>();
    private List<Seccion> seccionMostrar = new ArrayList<>();

    public List<SeleccionMultiple> getListaSeccionSeleccionMultiple() {
        return listaSeccionSeleccionMultiple;
    }

    public void setListaSeccionSeleccionMultiple(List<SeleccionMultiple> listaSeccionSeleccionMultiple) {
        this.listaSeccionSeleccionMultiple = listaSeccionSeleccionMultiple;
    }

    public List<Seccion> getSeccionMostrar() {
        return seccionMostrar;
    }

    public void setSeccionMostrar(List<Seccion> seccionMostrar) {
        this.seccionMostrar = seccionMostrar;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<OpcionTexto> getListaSeccionOpcionTexto() {
        return listaSeccionOpcionTexto;
    }

    public void setListaSeccionOpcionTexto(List<OpcionTexto> listaSeccionOpcionTexto) {
        this.listaSeccionOpcionTexto = listaSeccionOpcionTexto;
    }

    public FormController() {

    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public List<Seccion> getListaSecciones() {
        return listaSecciones;
    }

    public void setListaSecciones(List<Seccion> listaSecciones) {
        this.listaSecciones = listaSecciones;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getTempEmail() {
        return tempEmail;
    }

    public void setTempEmail(String tempEmail) {
        this.tempEmail = tempEmail;
    }

    public void validateId(int id) {
        this.asignarId(id);
        try {

            if (id != 0) {
                this.listaSeccionOpcionTexto.clear();
                this.listaSeccionSeleccionMultiple.clear();

                this.retrieveForm(id);
                HttpServletRequest request = (HttpServletRequest) FacesContext
                        .getCurrentInstance().getExternalContext().getRequest();
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .redirect(
                                request.getContextPath()
                                + "/faces/ResponderFormulario.xhtml?faces-redirect=true");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String linkCompartir(int id) {

        String link = "http://localhost:8080/CreadorDeFormularios" + "/?id=" + String.valueOf(id) + "";

        return link;
    }

    public void retrieveForm(int id) {

        Encuesta encuesta = null;
        EncuestaController eC = new EncuestaController();

        FormularioController fC = new FormularioController();

        Formulario form1 = fC.Get(id);

        encuesta = eC.get(form1);

        if (encuesta != null) {
            this.form = encuesta.getFrmScaffolding();

            SeccionDAO sd = new SeccionDAO();
            secciones = sd.seccionesEnFormulario(form);
            ItemDAO ids = new ItemDAO();
            for (Seccion ss : secciones) {
                ss.SetItem(ids.itemsEnSecciones(ss));
            }
            form.SetSecciones(secciones);

            for (Seccion s : form.GetSecciones()) {
                if (s.getItem().size() == 4) {

                    SeleccionMultiple sM = new SeleccionMultiple();

                    sM.setPregunta(s.getPregunta());
                    sM.getRespuestas().clear();
                    for (Item item : s.getItem()) {
                        sM.getRespuestas().add(item.getDefaultName());
                    }

                    this.listaSeccionSeleccionMultiple.add(sM);
                }

                if (s.getItem().size() == 1) {

                    OpcionTexto oT = new OpcionTexto();
                    oT.setPregunta(s.getPregunta());

                    for (Item item : s.getItem()) {

                        oT.setRespuesta(item.getDefaultName());

                    }

                    this.listaSeccionOpcionTexto.add(oT);
                }
            }

        }

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

    public void mandarCorreo(String link) {

        //Session session = Session.getDefaultInstance(p, null);
        //MimeMessage m = new MimeMessage(session);
        /**
         * try {
         *
         * m.setFrom(new InternetAddress("parapropruebas@gmail.com"));
         *
         * m.addRecipient(Message.RecipientType.TO, new
         * InternetAddress(user.getCorreo())); m.setSubject("Link de
         * formulario.");
         *
         *
         *
         * String mensaje = "El link del formulario que usted pidio es: " +
         * fc.getLink(); m.setText(mensaje);
         *
         * Transport transport = session.getTransport("smtp");
         * transport.connect("parapropruebas@gmail.com", "123456pruebas");
         * transport.sendMessage(m, m.getAllRecipients()); transport.close();
         *
         * } catch (MessagingException me) { me.printStackTrace(); FacesMessage
         * msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informe", "El
         * correo se envio exitosamente..");
         * FacesContext.getCurrentInstance().addMessage(null, msg); }*
         */
        FormController fc = new FormController();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("parapropruebas@gmail.com", "123456pruebas");
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("parapropruebas@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.tempEmail));
            message.setSubject("Link del formulario");
            message.setText("Hola ,"
                    + "\n\n Este es el link del formulario que usted solicitó: " + link);

            Transport.send(message);
            FacesMessage messages = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informe", "El correo se envio exitosamente..");
            FacesContext.getCurrentInstance().addMessage(null, messages);
            this.tempEmail = "";
        } catch (MessagingException e) {
            FacesMessage messages = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El correo no se pudo enviar.");
            FacesContext.getCurrentInstance().addMessage(null, messages);
            throw new RuntimeException(e);

        }
    }

    public void asignarId(int id) {

        this.formId = id;

    }

    public void alimentarFormulario() {

        List<OpcionTexto> tempList = new ArrayList<>();
        EncuestaController eC = new EncuestaController();
        List<Formulario> respuestas = new ArrayList<>();

        for (SeleccionMultiple sM : this.listaSeccionSeleccionMultiple) {

            OpcionTexto oT = new OpcionTexto();

            oT.setPregunta(sM.getPregunta());
            oT.setRespuesta(sM.getRespuestaFinal());

            System.out.println(oT.getPregunta());
            System.out.println(oT.getRespuesta());

            tempList.add(oT);

        }

        for (OpcionTexto oT : this.listaSeccionOpcionTexto) {

            OpcionTexto oT2 = new OpcionTexto();

            oT2.setPregunta(oT.getPregunta());
            oT2.setRespuesta(oT.getRespuesta());

            System.out.println(oT2.getPregunta());
            System.out.println(oT2.getRespuesta());

            tempList.add(oT2);

        }

        Encuesta encuesta = eC.get(this.form);
        Formulario formulario = new Formulario();
        List<Seccion> secciones = new ArrayList<>();
        formulario.setNombre(this.form.getNombre());
        formulario.setFavorito(false);

        for (OpcionTexto oT : tempList) {

            Seccion seccion = new Seccion();
            seccion.setFormularioPadre(formulario);
            seccion.setPregunta(oT.getPregunta());

            List<Item> item = new ArrayList<>();
            Item i = new Item();
            i.setSeccion(seccion);
            i.setTipoDato(EItem.TextBox);
            i.setDefaultName(oT.getRespuesta());
            item.add(i);

            seccion.SetItem(item);

            secciones.add(seccion);

        }

        formulario.SetSecciones(secciones);
        formulario.setEncuesta(encuesta);

        respuestas.add(formulario);

        encuesta.setRespuestas(respuestas);
        eC.Update(encuesta);

        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/FormularioRespondido.xhtml?faces-redirect=true");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mostrarDialogo() {
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('d').show();");
    }

    public void llenarPieChart(int id) {

        PieChartModel model = new PieChartModel();

        FormularioController fC = new FormularioController();

        for (Formulario f : fC.Get(id).getEncuesta().getRespuestas()) {
            if (f.isIsInterface()) {

                for (Seccion scc : f.GetSecciones()) {

                    if (scc.getItem().size() == 4) {

                        String seccionNombre = scc.getPregunta();
                        model = new PieChartModel();
                        model.setTitle(seccionNombre);
                        model.setShowDatatip(true);
                        for (Item i : scc.getItem()) {
                            model.set(i.getDefaultName(), contador(id, seccionNombre, i.getDefaultName()));
                        }
                        //model.set(scc.getItem().get(0).getDefaultName(),contador(id,seccionNombre,i));
                        //model.set(scc.getItem().get(1).getDefaultName(),12);
                        //model.set(scc.getItem().get(2).getDefaultName(),12);
                        //model.set(scc.getItem().get(3).getDefaultName(),12);

                        this.models.add(model);

                    }

                }

            }

        }

    }

    public Integer contador(int id, String pregunta, String item) {
        FormularioController fC = new FormularioController();
        int cont = 0;
        for (Formulario form : fC.Get(id).getEncuesta().getRespuestas()) {

            if (!form.isIsInterface()) {
                for (Seccion seccion : form.GetSecciones()) {
                    if (seccion.getPregunta().equalsIgnoreCase(pregunta)) {
                        for (Item i : seccion.getItem()) {
                            if (i.getDefaultName().equalsIgnoreCase(item)) {
                                cont += 1;
                            }
                        }
                    }
                }
            }

        }

        return cont;
    }

    public void texto(int id) {
        FormularioController fC = new FormularioController();

        for (Formulario form : fC.Get(id).getEncuesta().getRespuestas()) {

            if (form.isIsInterface()) {

                for (Seccion s : form.GetSecciones()) {
                    if (s.getItem().size() == 1) {
                        for (Formulario form2 : fC.Get(id).getEncuesta().getRespuestas()) {

                            if (!form2.isIsInterface()) {

                                for (Seccion s2 : form2.GetSecciones()) {
                                    if (s2.getPregunta().equalsIgnoreCase(s.getPregunta())) {
                                        OpcionTexto oT = new OpcionTexto();
                                        oT.setPregunta(s2.getPregunta());
                                        oT.setRespuesta(s2.getItem().get(0).getDefaultName());
                                        this.listaSeccionOpcionTexto.add(oT);
                                    }

                                }

                            }

                        }
                    }
                }

            }

        }

    }

    public void validateIdRespuestas(int id) {

        this.asignarId(id);
        try {

            if (id != 0) {
                this.listaSeccionOpcionTexto.clear();
                this.models.clear();
                this.texto(id);
                this.llenarPieChart(id);

                HttpServletRequest request = (HttpServletRequest) FacesContext
                        .getCurrentInstance().getExternalContext().getRequest();
                FacesContext
                        .getCurrentInstance()
                        .getExternalContext()
                        .redirect(
                                request.getContextPath()
                                + "/faces/pieTest.xhtml?faces-redirect=true");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<PieChartModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<PieChartModel> models) {
        this.models = models;
    }

}
