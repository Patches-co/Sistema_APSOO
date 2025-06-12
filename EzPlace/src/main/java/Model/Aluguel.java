/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author João
 */
public class Aluguel extends Reserva{
    private int valor;

    public Aluguel(int valor, int id_reserva, Morador morador, String data, Time rTime_ini, Time rTime_fim, int id_espaco, boolean disponivel, String data_ini, String data_fim, Time time_ini, Time time_fim, int id_condominio) {
        super(id_reserva, morador, data, rTime_ini, rTime_fim, id_espaco, disponivel, data_ini, data_fim, time_ini, time_fim, id_condominio);
        this.valor = valor;
    }

    public Aluguel(int valor, int id_reserva, Funcionario funcionario, String data, Time rTime_ini, Time rTime_fim, int id_espaco, boolean disponivel, String data_ini, String data_fim, Time time_ini, Time time_fim, int id_condominio) {
        super(id_reserva, funcionario, data, rTime_ini, rTime_fim, id_espaco, disponivel, data_ini, data_fim, time_ini, time_fim, id_condominio);
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

}
