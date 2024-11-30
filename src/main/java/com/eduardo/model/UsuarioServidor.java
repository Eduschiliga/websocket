package com.eduardo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "usuario")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioServidor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario")
  private Long id;

  @Column(name = "is_admin_usuario")
  private Boolean isAdmin = false;

  @Column(name = "nome_usuario")
  private String nome;

  @Column(name = "senha_usuario")
  private String senha;

  @Column(name = "ra_usuario")
  private String ra;
}
