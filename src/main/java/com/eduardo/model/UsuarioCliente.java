package com.eduardo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.Nullable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioCliente {
  private String operacao;

  @Nullable
  private String nome;

  private String senha;
  private String ra;
}
