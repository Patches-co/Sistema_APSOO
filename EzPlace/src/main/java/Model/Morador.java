/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jvvpa
 */
public class Morador extends Usuario{
    private String telefone;
    private String email;
    private String endereço;
    private Date dataNascimento;
    private boolean morador;

    public Morador(String telefone, String email, String endereço, String dataNascimento, boolean morador, int id, String nome, String senha, String CPF, int id_condominio) {
        super(id, nome, senha, CPF, id_condominio);
        this.telefone = telefone;
        this.email = email;
        this.endereço = endereço;
        try {
            this.dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimento);
        } catch (ParseException ex) {
            Logger.getLogger(Morador.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.morador = morador;
    }
    

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public boolean isMorador() {
        return morador;
    }

    public void setMorador(boolean morador) {
        this.morador = morador;
    }
    
    
}
