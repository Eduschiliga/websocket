package com.eduardo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
  @Column(name = "ra_usuario", unique = true)
  @Pattern(regexp = "^[0-9]{7}$", message = "RA deve conter 7 dígitos numéricos")
  private String ra;

  @Column(name = "is_admin_usuario")
  private Boolean isAdmin = false;

  @Column(name = "nome_usuario")
  @NotNull(message = "O nome é obrigatório")
  @Max(value = 50, message = "O nome deve ter no máximo 50 letras sem acentos ou caracteres especiais")
  @Pattern(regexp = "^[A-Z ]*$", message = "O nome deve ter no máximo 50 letras sem acentos ou caracteres especiais")
  private String nome;

  @Column(name = "senha_usuario")
  @NotNull(message = "A senha é obrigatória")
  @Min(value = 8, message = "A senha deve ter de 8 a 20 letras sem acentos ou caracteres especiais")
  @Max(value = 20, message = "A senha deve ter de 8 a 20 letras sem acentos ou caracteres especiais")
  @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "A senha deve ter de 8 a 20 letras sem acentos ou caracteres especiais")
  private String senha;
}
