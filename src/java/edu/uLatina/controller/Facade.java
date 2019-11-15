
import com.componentes.controlador.UsuarioController;
import com.componentes.entidades.Usuario;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DacordMachine
 */
public class Facade {
    
    public void login(String username, String password){
        
        Usuario u = new Usuario();
        u.setNombre(username);
        u.setConstrania(password);
        UsuarioController uc = new UsuarioController();
        
        
    }
    
    public void registrar(String nombre, String correo, String password){
        
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setConstrania(password);
        UsuarioController uc = new UsuarioController();
        uc.Insert(u);
        
    }
    
}