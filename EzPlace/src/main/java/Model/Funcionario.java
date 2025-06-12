/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author João
 */
public class Funcionario extends Usuario{
    protected int nivelAcesso;
    
    public Funcionario(int id, String nome, String senha, String CPF, int id_condominio) {
        super(id, nome, senha, CPF, id_condominio);
        this.nivelAcesso = 1;
    }

    public Funcionario(int id,  int id_condominio, String nome, String senha) {
        super(id, nome, senha, id_condominio);
        this.nivelAcesso = 1;
    }

    public int getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(int nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
    
}
