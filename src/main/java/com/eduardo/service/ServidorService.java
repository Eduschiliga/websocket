package com.eduardo.service;

import com.eduardo.dao.UsuarioDAO;
import com.eduardo.model.*;
import com.eduardo.utils.JPAUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@NoArgsConstructor
public class ServidorService extends Thread {
  protected Socket clientSocket;

  public static void main(String[] args) {
    try {
      new ServidorService().executarServidor();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void executarServidor() throws IOException {
    ServerSocket serverSocket = null;

    try {
      serverSocket = new ServerSocket(20000);
      System.out.println("Conexão de Socket criada");
      try {
        while (true) {
          System.out.println("Aguardando conexão");
          new ServidorService(serverSocket.accept());
        }
      } catch (IOException e) {
        System.err.println("Falha ao aceitar conexão. Erro: " + e.getMessage());
      }
    } catch (IOException e) {
      System.err.println("Não foi possível ouvir a porta: 10008. Erro: " + e.getMessage());
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {
        System.err.println("Não foi possível fechar a porta: 10008. Erro: " + e.getMessage());
      }
    }
  }

  private ServidorService(Socket clientSoc) {
    this.clientSocket = clientSoc;
    start();
  }

  public void run() {
    System.out.println("Novo Cliente Conectado");

    String mensagemJson = "";
    String operacao = "";
    boolean autenticado = false;
    MensagemLoginSucesso mensagemLoginSucesso = new MensagemLoginSucesso();

    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

      String inputLine;

      while ((inputLine = in.readLine()) != null) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(inputLine);
        operacao = jsonNode.get("operacao").asText();


        Mensagem mensagem = new Mensagem();
        try {
          EntityManager entityManager = JPAUtil.getEntityManagerFactory();
          UsuarioDAO usuarioDAO = new UsuarioDAO(entityManager);


          if (operacao.equals("cadastrarUsuario")) {
            try {
              UsuarioCliente usuarioCliente = Json.deserialize(inputLine, UsuarioCliente.class);

              entityManager.getTransaction().begin();

              UsuarioServidor usuarioServidor = new UsuarioServidor();
              usuarioServidor.setNome(usuarioCliente.getNome());
              usuarioServidor.setSenha(usuarioCliente.getSenha());
              usuarioServidor.setRa(usuarioCliente.getRa());

              usuarioDAO.cadastrar(usuarioServidor);
              entityManager.getTransaction().commit();

              mensagem.setMensagem("Usuário cadastrado com sucesso!");
              mensagem.setStatus(201);
            } catch (Exception e) {
              if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
              }

              mensagem.setMensagem("Erro ao cadastrar usuário");
              mensagem.setStatus(401);
            }
          }

          if (operacao.equals("login")) {
            try {
              Login login = objectMapper.treeToValue(jsonNode, Login.class);

              UsuarioServidor usuarioServidor = usuarioDAO.buscarPorRaESenha(login.getRa(), login.getSenha());

              if(usuarioServidor != null) {
                autenticado = true;
                mensagemLoginSucesso.setStatus(200);
                mensagemLoginSucesso.setToken(usuarioServidor.getRa());
              }
            } catch (NoResultException e) {
              autenticado = false;
              mensagem.setMensagem("Usuário ou senha inválidos");
              mensagem.setStatus(401);
            }

          }

        } catch (IOException e) {
          mensagem.setMensagem("Não foi possível ler o json recebido");
          mensagem.setStatus(401);
        } finally {
          if ("cadastrarUsuario".equals(operacao)) {
            mensagemJson = objectMapper.writeValueAsString(mensagem);
          }

          if ("login".equals(operacao) && autenticado) {
            mensagemJson = objectMapper.writeValueAsString(mensagemLoginSucesso);
          }

          if ("login".equals(operacao) && !autenticado) {
            mensagemJson = objectMapper.writeValueAsString(mensagem);
          }
          out.println(mensagemJson);
        }

        if (inputLine.equals("Sair")) {
          break;
        }
      }
    } catch (IOException e) {
      System.err.println("Problema com o servidor de comunicação. Erro: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.err.println("Erro ao fechar socket do cliente. Erro: " + e.getMessage());
      }
    }
  }
}