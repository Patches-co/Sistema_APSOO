/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.Cadastro;
import View.PainelPrincipal;
/**
 *
 * @author Jvvpa
 */
public class PainelPrincipalController {
    
    private final PainelPrincipal view;

    public PainelPrincipalController(View.PainelPrincipal view) {
        this.view = view;
    }
    
    public void openCadastro() {
        Cadastro cadastro =  new Cadastro();
        cadastro.setVisible(true);
    }
}
