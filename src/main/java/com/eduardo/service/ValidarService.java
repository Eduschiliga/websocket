package com.eduardo.service;

import java.util.regex.Pattern;

public class ValidarService {
  static boolean isValidSenha(String senha) {
    return senha != null && senha.length() >= 8 && senha.length() <= 20 && Pattern.matches("^[a-zA-Z]+$", senha);
  }

  static boolean isValidRa(String ra) {
    return ra != null && ra.length() == 7 && Pattern.matches("^[0-9]+$", ra);
  }

  static boolean isValidNome(String nome) {
    return nome != null && nome.length() <= 50 && Pattern.matches("^[A-Z]+$", nome);
  }
}
