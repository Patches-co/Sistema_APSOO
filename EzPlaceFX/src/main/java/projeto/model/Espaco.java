/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.model;

/**
 *
 * @author Jvvpa
 */
public class Espaco {
    private int id;
    private String nome;
    private String horariosDisponiveis;
    private String regrasUso;
    private int tempoMaximoReservaMin;
    private int limiteMensalPessoa;

    public Espaco(int id, String nome, String horariosDisponiveis, String regrasUso, int tempoMaximoReservaMin) {
        this.id = id;
        this.nome = nome;
        this.horariosDisponiveis = horariosDisponiveis;
        this.regrasUso = regrasUso;
        this.tempoMaximoReservaMin = tempoMaximoReservaMin;
    }

    public Espaco() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    public void setHorariosDisponiveis(String horariosDisponiveis) {
        this.horariosDisponiveis = horariosDisponiveis;
    }

    public String getRegrasUso() {
        return regrasUso;
    }

    public void setRegrasUso(String regrasUso) {
        this.regrasUso = regrasUso;
    }

    public int getTempoMaximoReservaMin() {
        return tempoMaximoReservaMin;
    }

    public void setTempoMaximoReservaMin(int tempoMaximoReservaMin) {
        this.tempoMaximoReservaMin = tempoMaximoReservaMin;
    }

    public int getLimiteMensalPessoa() {
        return limiteMensalPessoa;
    }

    public void setLimiteMensalPessoa(int limiteMensalPessoa) {
        this.limiteMensalPessoa = limiteMensalPessoa;
    }
    
    @Override
    public String toString() {
        return this.getNome(); 
    }
}
