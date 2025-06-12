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
public class Reserva extends Espacos{
    private int id_reserva;
    private Morador morador;
    private Funcionario funcionario;
    private Date data;
    private Time rTime_ini;
    private Time rTime_fim;

    public Reserva(int id_reserva, Morador morador, String data, Time rTime_ini, Time rTime_fim, int id_espaco, boolean disponivel, String data_ini, String data_fim, Time time_ini, Time time_fim, int id_condominio) {
        super(id_espaco, disponivel, data_ini, data_fim, time_ini, time_fim, id_condominio);
        this.id_reserva = id_reserva;
        this.morador = morador;
        try {
            this.data = new SimpleDateFormat("dd/MM/yyyy").parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(Reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.rTime_ini = rTime_ini;
        this.rTime_fim = rTime_fim;
    }

    public Reserva(int id_reserva, Funcionario funcionario, String data, Time rTime_ini, Time rTime_fim, int id_espaco, boolean disponivel, String data_ini, String data_fim, Time time_ini, Time time_fim, int id_condominio) {
        super(id_espaco, disponivel, data_ini, data_fim, time_ini, time_fim, id_condominio);
        this.id_reserva = id_reserva;
        this.funcionario = funcionario;
        try {
            this.data = new SimpleDateFormat("dd/MM/yyyy").parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(Reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.rTime_ini = rTime_ini;
        this.rTime_fim = rTime_fim;
    }

    public Reserva(int id_reserva, Morador morador, String data, int id_espaco, boolean disponivel, int id_condominio) {
        super(id_espaco, disponivel, id_condominio);
        this.id_reserva = id_reserva;
        this.morador = morador;
        try {
            this.data = new SimpleDateFormat("dd/MM/yyyy").parse(data);
        } catch (ParseException ex) {
            Logger.getLogger(Reserva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public Morador getMorador() {
        return morador;
    }

    public void setMorador(Morador morador) {
        this.morador = morador;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Time getrTime_ini() {
        return rTime_ini;
    }

    public void setrTime_ini(Time rTime_ini) {
        this.rTime_ini = rTime_ini;
    }

    public Time getrTime_fim() {
        return rTime_fim;
    }

    public void setrTime_fim(Time rTime_fim) {
        this.rTime_fim = rTime_fim;
    }

}
