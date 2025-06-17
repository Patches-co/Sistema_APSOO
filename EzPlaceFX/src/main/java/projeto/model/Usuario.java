/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.model;

/**
 *
 * @author Jvvpa
 */
public class Usuario {
    private int id;
    private String nomeCompleto;
    private String CPF;
    private String email;
    private String telefone;
    private String senha;
    private String unidade;
    private String tipoUsuario;

    public Usuario(int id, String nomeCompleto, String CPF, String email, String telefone, String senha, String unidade, String tipoUsuario) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.CPF = CPF;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.unidade = unidade;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    @Override
    public String toString() {
        return this.getNomeCompleto();
    }
}
