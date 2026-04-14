package com.wagnerquadros.simulapronaf.autenticacao.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioGoogleDto(

        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email informado é inválido.")
        String email,

        @NotBlank(message = "O googleSubject é obrigatório.")
        String googleSubject
) {
}