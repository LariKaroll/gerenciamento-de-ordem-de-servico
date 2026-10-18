package com.projeto.service_user.validation;

import com.projeto.service_user.dto.UsuarioRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaDiferenteNomeValidator implements ConstraintValidator<SenhaDiferenteNome, UsuarioRequestDTO> {

    @Override
    public boolean isValid(UsuarioRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null || dto.nome() == null || dto.senha() == null) {
            return true;
        }

        String nomeLimpo = dto.nome().trim();

        if (nomeLimpo.length() < 3) {
            return true;
        }

        String nomeMinusculo = nomeLimpo.toLowerCase();
        String senhaMinuscula = dto.senha().toLowerCase();

        boolean senhaContemNome = senhaMinuscula.contains(nomeMinusculo);

        if (senhaContemNome) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("senha")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
