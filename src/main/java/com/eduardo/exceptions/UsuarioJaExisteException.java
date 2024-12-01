package com.eduardo.exceptions;

public class UsuarioJaExisteException extends Exception {
  public UsuarioJaExisteException(String message) {
    super(message);
  }
}