/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author João
 */
public class Espacos extends Condominio{
    private int id_espaco;
    private boolean disponivel;
    private Date data_ini;
    private Date data_fim;
    private Time time_ini;
    private Time time_fim;

    public Espacos(int id_espaco, boolean disponivel, String data_ini, String data_fim, Time time_ini, Time time_fim, int id_condominio) {
        super(id_condominio);
        this.id_espaco = id_espaco;
        this.disponivel = disponivel;
        try {
            this.data_ini = new SimpleDateFormat("dd/MM/yyyy").parse(data_ini);
        } catch (ParseException ex) {
            Logger.getLogger(Espacos.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.data_fim = new SimpleDateFormat("dd/MM/yyyy").parse(data_fim);
        } catch (ParseException ex) {
            Logger.getLogger(Espacos.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.time_ini = time_ini;
        this.time_fim = time_fim;
    }

    public Espacos(int id_espaco, boolean disponivel, int id_condominio) {
        super(id_condominio);
        this.id_espaco = id_espaco;
        this.disponivel = disponivel;
    }

    public int getId_espaco() {
        return id_espaco;
    }

    public void setId_espaco(int id_espaco) {
        this.id_espaco = id_espaco;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Date getData_ini() {
        return data_ini;
    }

    public void setData_ini(Date data_ini) {
        this.data_ini = data_ini;
    }

    public Date getData_fim() {
        return data_fim;
    }

    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    public Time getTime_ini() {
        return time_ini;
    }

    public void setTime_ini(Time time_ini) {
        this.time_ini = time_ini;
    }

    public Time getTime_fim() {
        return time_fim;
    }

    public void setTime_fim(Time time_fim) {
        this.time_fim = time_fim;
    }
      
}
