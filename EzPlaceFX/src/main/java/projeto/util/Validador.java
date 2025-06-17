/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.util;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 *
 * @author Jvvpa
 */
public class Validador {
    private Validador() {
        
    }
    
    public static boolean isEmailValido(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public static boolean isCpfValido(String cpf) {
        cpf = cpf.replaceAll("[^\\d]", "");
        
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i)- '0') * (10 - i);
            }
            int resto = 11 - (soma % 11);
            char digito1 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');
            
            if (digito1 != cpf.charAt(9)) {
                return false;
            }
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            resto = 11 - (soma % 11);
            char digito2 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            return digito2 == cpf.charAt(10);
        } catch (InputMismatchException e) {
            return false;
        }
    }
    
    public static void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
