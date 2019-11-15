/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.componentes.controlador.UsuarioController;
import com.componentes.entidades.Usuario;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Kainthel
 */
@ManagedBean (name = "loginController")
@SessionScoped
public class LoginController {
    
    //Parametros para el usuario
    private String username; //Estos se consiguen directamente del login page ("index.html")
    private String password;
    private Usuario user = new Usuario();

    public void login(){
        
        UsuarioController uc = new UsuarioController();
        //Busca si el username y password esta en la database
        for (Object obj : uc.Get()) {
            if (((Usuario)obj).getNombre().equals(username) && ((Usuario)obj).getConstrania().equals(password)) { 
                user = ((Usuario)obj);
                this.redireccionALandingPage(user); //si es valido el user lo manda a meterse al landing page
            }else{ //en caso de login fallido, usuario invalido, etc...
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrecta.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }  
    }
    
     public void redireccionALandingPage(Usuario u){
                    try {
           
            HttpServletRequest request = (HttpServletRequest) FacesContext
                    .getCurrentInstance().getExternalContext().getRequest();
            FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            request.getContextPath()
                            + "/faces/landingPage.xhtml?faces-redirect=true");  //Aqui deberia ir a la Landing Page pero pues aun no existe
            }
                    catch (IOException e) {
                    }
           
     }
    
}