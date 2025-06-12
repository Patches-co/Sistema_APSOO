/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Jvvpa
 */
abstract public class Condominio {
    private int id_condominio;
    private String nomeCondominio;
    private String cep;
    private String tipo;

    public Condominio(int id_condominio, String nome, String cep, String tipo) {
        this.id_condominio = id_condominio;
        this.nomeCondominio = nome;
        this.cep = cep;
        this.tipo = tipo;
    }

    public Condominio(int id_condominio) {
        this.id_condominio = id_condominio;
    }
    

    public int getId_condominio() {
        return id_condominio;
    }

    public void setId_condominio(int id_condominio) {
        this.id_condominio = id_condominio;
    }

    public String getNomeCondominio() {
        return nomeCondominio;
    }

    public void setNomeCondominio(String nomeCondominio) {
        this.nomeCondominio = nomeCondominio;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
