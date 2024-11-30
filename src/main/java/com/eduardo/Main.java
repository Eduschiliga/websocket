package com.eduardo;

import com.eduardo.service.ServidorService;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    ServidorService servidorService = new ServidorService();

    servidorService.executarServidor();
  }
}