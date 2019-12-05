/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import com.componentes.dao.FormularioDAO;
import com.componentes.dao.ItemDAO;
import com.componentes.dao.SeccionDAO;
import com.componentes.entidades.Formulario;
import com.componentes.entidades.Item;
import com.componentes.entidades.Seccion;
import com.componentes.entidades.Usuario;
import edu.uLatina.model.OpcionTexto;
import edu.uLatina.model.SeleccionMultiple;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

/**
 *
 * @author dacor
 */
@ManagedBean(name = "formController")
@SessionScoped
public class FormController {

    private List<Seccion> listaSecciones = new ArrayList<>();
    private String link = "";

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }

    private int formId;
    private Formulario form;
    private List<Seccion> secciones;
    private List<Item> items;
    private Usuario u;
    private List<SeleccionMultiple> listaSeccionSeleccionMultiple = new ArrayList<>();
    private List<OpcionTexto> listaSeccionOpcionTexto = new ArrayList<>();

    public List<SeleccionMultiple> getListaSeccionSeleccionMultiple() {
        return listaSeccionSeleccionMultiple;
    }

    public void setListaSeccionSeleccionMultiple(List<SeleccionMultiple> listaSeccionSeleccionMultiple) {
        this.listaSeccionSeleccionMultiple = listaSeccionSeleccionMultiple;
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

    public void validateId(int id) {
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

        FormularioDAO fd = new FormularioDAO();
        form = fd.Get(id);
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
                    InternetAddress.parse("parapropruebas@gmail.com"));
            message.setSubject("Link del formulario");
            message.setText("Hola ,"
                    + "\n\n Este es el link del formulario que usted solicitó: " + link);

            Transport.send(message);
            FacesMessage messages = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informe", "El correo se envio exitosamente..");
            FacesContext.getCurrentInstance().addMessage(null, messages); 

        } catch (MessagingException e) {
            FacesMessage messages = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "El correo no se pudo enviar.");
            FacesContext.getCurrentInstance().addMessage(null, messages);
            throw new RuntimeException(e);

        }
    }
    
}
