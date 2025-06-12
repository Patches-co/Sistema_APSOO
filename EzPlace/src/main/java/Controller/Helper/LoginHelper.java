/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Helper;

import Model.Administrador;
import Model.Funcionario;
import View.Login;

/**
 *
 * @author Jvvpa
 */
public class LoginHelper {
    
    private final Login view;

    public LoginHelper(Login view) {
        this.view = view;
    }
    
   public Funcionario getModelo(){
       String cpf = view.getLogin().getText();
       String senha = new String(view.getSenha().getPassword());
        
       Funcionario modelo = new Funcionario(0, null, senha, cpf, 0);
       return modelo;
   }
   
   public Administrador getModeloADM() {
       String cpf = view.getLogin().getText();
       String senha = new String(view.getSenha().getPassword());
       
       Administrador modelo = new Administrador(0, null, senha, cpf, 0);
       return modelo;
   }
   
   public void setModelo(Funcionario modelo){
       String cpf = modelo.getCPF();
       String senha = modelo.getSenha();
       
       view.getLogin().setText(cpf);
       view.getSenha().setText(senha);
   }
   
   public void limparTela(){
       view.getLogin().setText("");
       view.getSenha().setText("");
   }
}
