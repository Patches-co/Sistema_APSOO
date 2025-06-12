/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.DAO.FuncionarioDAO;
import Model.Funcionario;
import View.Cadastro;
import java.sql.SQLException;

/**
 *
 * @author João
 */
public class CadastroController {
    
    private final Cadastro view;
    
    public CadastroController(Cadastro view) {
        this.view = null;
    }
    
    public void insereTabela(Funcionario funcionario) throws SQLException {
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        funcionarioDAO.inserirFuncionario(funcionario);
    }
}
