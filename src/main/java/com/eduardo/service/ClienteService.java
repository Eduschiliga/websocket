package com.eduardo.service;

import com.eduardo.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteService {
  public static void main(String[] args) throws IOException {
    String serverHostname = "172.20.10.2";

    if (args.length > 0) {
      serverHostname = args[0];
    }
    System.out.println("Tentando se conectar ao host " +
        serverHostname + " na porta 10008.");

    try (Socket echoSocket = new Socket(serverHostname, 20010);
         PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
         Scanner scanner = new Scanner(System.in)) {

      System.out.println("Digite Mensagem (\"Sair\" para sair)");

      while (true) {
        System.out.println("Menu:");
        System.out.println("1. Login");
        System.out.println("2. Logout");
        System.out.println("3. Cadastrar Usuário");
        System.out.println("4. Sair");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao == 1) {
          System.out.print("Digite o RA do usuário: ");
          String ra = scanner.nextLine();

          System.out.print("Digite a senha do usuário: ");
          String senha = scanner.nextLine();


          Login login = new Login("login", ra, senha);

          try {
            String json = Json.serialize(login);
            out.println(json);


            String resposta = in.readLine();
            System.out.println(resposta);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(resposta);

            if (jsonNode.has("token")) {
              MensagemLoginSucesso mensagemLoginSucesso = objectMapper.treeToValue(jsonNode, MensagemLoginSucesso.class);
              System.out.println("Token: " + mensagemLoginSucesso.getToken());
              System.out.println("Status: " + mensagemLoginSucesso.getStatus());
            } else if (jsonNode.has("mensagem")) {
              Mensagem mensagem = objectMapper.treeToValue(jsonNode, Mensagem.class);
              System.out.println("Mensagem: " + mensagem.getMensagem());
              System.out.println("Status: " + mensagem.getStatus());
            } else {
              System.out.println("Formato de resposta desconhecido.");
            }

          } catch (JsonProcessingException e) {
            System.out.println("Erro ao serializar o usuário: " + e.getMessage());
          }
        }

        if (opcao == 3) {
          System.out.print("Digite o RA do usuário: ");
          String ra = scanner.nextLine();

          System.out.print("Digite o nome do usuário: ");
          String nome = scanner.nextLine();

          System.out.print("Digite a senha do usuário: ");
          String senha = scanner.nextLine();

          UsuarioCliente usuario = new UsuarioCliente("cadastrarUsuario", nome.toUpperCase(), senha, ra);

          try {
            String json = Json.serialize(usuario);
            out.println(json);

            String resposta = in.readLine();

            Mensagem mensagem = Json.deserialize(resposta, Mensagem.class);

            System.out.println("Mensagem: " + mensagem.getMensagem());

          } catch (JsonProcessingException e) {
            System.out.println("Erro ao serializar o usuário: " + e.getMessage());
          }
        }

        if (opcao == 4) {
          System.out.println("Saindo...");
          out.println("Sair");  // Enviar mensagem de saída ao servidor
          break;
        }
      }
    } catch (UnknownHostException e) {
      System.err.println("Não sei sobre o host: " + serverHostname);
    } catch (IOException e) {
      System.err.println("Não foi possível obter I/O para a conexão com: " + serverHostname);
    }
  }
}