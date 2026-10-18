package com.projeto.service_user.dto;

import org.hibernate.validator.constraints.br.CPF;

import com.projeto.service_user.model.enums.PerfilUsuario;
import com.projeto.service_user.validation.SenhaDiferenteNome;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SenhaDiferenteNome 
public record UsuarioRequestDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 120)
    String nome,

    @NotBlank(message = "O cpf é obrigatório")
    @CPF (message = "Formato de cpf inválido")
    @Pattern (
        regexp = "^\\d{11}$",
        message = "O CPF deve conter exatamente 11 dígitos numéricos"
    )
    @Size(max = 14)
    String cpf,

    @NotBlank(message = "O email é obrigatório")
    @Email (message = "Formato de email inválido")
    @Size (max = 255)
    String email,

    @NotBlank(message = "O número de telefone é obrigatório")
    @Pattern (
        regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$",
        message = "O telefone deve estar no formato (XX) XXXXX-XXXX"
    )
    String telefone,

    @NotNull (message = "O perfil é obrigatório")
    PerfilUsuario perfil,

    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 72, message = "A senha so pode ter no maximo 72 caracteres")
    @Pattern (
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>_\\-+=\\[\\]\\\\/~`]).{8,}$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um caractere especial e no mínimo 8 caracteres"
    )
    String senha

) {
    @Override
    public final String toString() {
        return "Usuario{" +
            "nome='" + nome + '\'' +
            ", cpf='" + cpf + '\'' +
            ", email='"+ email + '\'' +
            ", telefone='" + telefone + '\'' +
            ", perfil='" + perfil + '\'' +
            '}';
    }
}
