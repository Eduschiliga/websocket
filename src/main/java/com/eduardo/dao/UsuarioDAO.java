package com.eduardo.dao;

import com.eduardo.exceptions.UsuarioJaExisteException;
import com.eduardo.model.UsuarioServidor;
import lombok.*;

import javax.persistence.EntityManager;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioDAO {
  private EntityManager entityManager;

  public void cadastrar(UsuarioServidor usuarioServidor) {
    try {
      System.out.println("Usuario recebido para cadastrar: " + usuarioServidor);

      entityManager.merge(usuarioServidor);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public UsuarioServidor buscarPorRa(String ra) {
    return entityManager.createQuery("SELECT a FROM UsuarioServidor a WHERE a.ra = :ra", UsuarioServidor.class)
        .setParameter("ra", ra)
        .getSingleResult();
  }

  public UsuarioServidor buscarPorRaESenha(String ra, String senha) {
    return entityManager.createQuery("SELECT a FROM UsuarioServidor a WHERE a.ra = :ra AND a.senha = :senha", UsuarioServidor.class)
        .setParameter("ra", ra)
        .setParameter("senha", senha)
        .getSingleResult();
  }
}
