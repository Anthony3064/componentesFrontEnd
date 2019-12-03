/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uLatina.controller;

import com.componentes.dao.FormularioDAO;
import com.componentes.dao.UsuarioDAO;
import com.componentes.entidades.Formulario;
import com.componentes.entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
 * @author Kainthel
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController {

    //Parametros para el usuario
    private String username; //Estos se consiguen directamente del login page ("index.html")
    private String password;
    private Usuario user = null;
    Map<String, String> params = FacesContext.getCurrentInstance().
            getExternalContext().getRequestParameterMap();
    private String formId;
    private List<Formulario> listaFormularios = new ArrayList<>();
    private List<Formulario> listaFormulariosTodos = new ArrayList<>();

    public LoginController() {
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public List<Formulario> getListaFormulariosTodos() {
        return listaFormulariosTodos;
    }

    public void setListaFormulariosTodos(List<Formulario> listaFormulariosTodos) {
        this.listaFormulariosTodos = listaFormulariosTodos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public List<Formulario> getListaFormularios() {
        return listaFormularios;
    }

    public void setListaFormularios(List<Formulario> listaFormularios) {
        this.listaFormularios = listaFormularios;
    }

    public void login() {

        if (this.username.equalsIgnoreCase("") || this.password.equalsIgnoreCase("")) {

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe llenar todos los campos.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else {

            UsuarioDAO uDao = new UsuarioDAO();

            //Busca si el username y password esta en la database
            this.user = (Usuario) uDao.login(this.username, this.password);

            if (user != null) {

                this.redireccionALandingPage(user); //si es valido el user lo manda a meterse al landing page

            } else {
                //en caso de login fallido, usuario invalido, etc...
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrecta.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                this.password = "";
                this.username = "";
                this.user = null;
            }
        }

    }

    public void redireccionALandingPage(Usuario u) {
        try {
            this.listaFormularios.clear();
            this.listaFormulariosTodos.clear();
            this.cargarListaFormulariosTodos();
            this.cargarListaFormularios();
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/LandingPage.xhtml?faces-redirect=true");  //Aqui deberia ir a la Landing Page pero pues aun no existe
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void redireccionARegistro() {
        try {

            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/Registro.xhtml?faces-redirect=true");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cargarListaFormulariosTodos() {

        FormularioDAO fD = new FormularioDAO();

        for (Formulario f : fD.GetList()) {
            this.listaFormulariosTodos.add(f);
        }

    }

    public void cargarListaFormularios() {

        FormularioDAO fD = new FormularioDAO();

        for (Formulario f : fD.buscarFormulariosUsuario(this.getUser())) {
            this.listaFormularios.add(f);
        }

    }

    public void cerrarSesion() {

        try {

            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/Login.xhtml?faces-redirect=true");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void mandarCorreo() {
        Properties p = new Properties();

        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.user", "parapropruebas@gmail.com");
        p.put("mail.smtp.auth", "true");

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
         * FacesContext.getCurrentInstance().addMessage(null, msg);
        }*
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
            message.setFrom(new InternetAddress("DF@diarioFacil.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getCorreo()));
            message.setSubject("Link del formulario");
            message.setText("Hola ,"
                    + "\n\n Este es el link del formulario que usted solicitó: " + fc.getLink());

            Transport.send(message);
            FacesMessage messages = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informe", "El correo se envio exitosamente..");
            FacesContext.getCurrentInstance().addMessage(null, messages);

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }
    }

}
