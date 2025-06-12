/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Controller.Helper.LoginHelper;
import Model.Administrador;
import Model.DAO.AdministradorDAO;
import Model.DAO.FuncionarioDAO;
import Model.Funcionario;
import View.Login;
import View.PainelADM;
import View.PainelPrincipal;
import java.sql.SQLException;

/**
 *
 * @author Jvvpa
 */
public class LoginController {

    private final Login view;
    private LoginHelper helper;

    public LoginController(Login view) {
        this.view = view;
        this.helper = new LoginHelper(view);
    }
    
    public void autenticarUsuario() throws SQLException {
        // Primeiro tenta autenticar como administrador
        Administrador administrador = helper.getModeloADM();
        AdministradorDAO admDAO = new AdministradorDAO();
        Administrador admAutenticado = admDAO.selecByLogin(administrador);

        if (admAutenticado != null) {
            PainelADM admMenu = new PainelADM();
            admMenu.setVisible(true);
            this.view.dispose();
            return;
        }

        // Caso não seja administrador, tenta como funcionário
        Funcionario funcionario = helper.getModelo();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        Funcionario funcionarioAutenticado = funcionarioDAO.selecByLogin(funcionario);

        if (funcionarioAutenticado != null) {
            PainelPrincipal menu = new PainelPrincipal();
            menu.setVisible(true);
            this.view.dispose();
            return;
        }

        view.exibeMensagem("Usuário ou senha inválidos");
    } 
   
    /*public void entrarPainel() throws SQLException{
        // pegar um user do funcionario
        Funcionario funcionario = helper.getModelo();
        
        // pesquisa do user no banco
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        Funcionario funcionarioAutenticado = funcionarioDAO.selecByLogin(funcionario);
        // se o user da view tiver o mesmo user e senha do banco:
        if(funcionarioAutenticado != null) {
            PainelPrincipal menu = new PainelPrincipal();
            menu.setVisible(true);
            this.view.dispose();
        } else {
            view.exibeMensagem("Usuário ou senha inválidos");
        }
    }*/

    /*
    public void entrarPainelADM () throws SQLException{
        // pegar um user do adm
        Administrador administrador = helper.getModeloADM();
        
        // pesquisa do user no banco
        AdministradorDAO admDAO = new AdministradorDAO();
        Administrador admAutenticado = admDAO.selecByLogin(administrador);
        // se o user da view tiver o mesmo user e senha do banco:
        if(admAutenticado != null) {
            PainelADM AdmMenu = new PainelADM();
            AdmMenu.setVisible(true);
            this.view.dispose();
        } else {
            view.exibeMensagem("Usuário ou senha inválidos");
        }
        // redirecionar pro PainelADM
            // senão: mostrar msg "user ou senha invalidos"
    } 
    
    public void doWork(){
        System.err.println("Busquei algo do banco de dados");
        
        this.view.exibeMensagem("Executei o doWork");
    }
    */
}
