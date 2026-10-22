package com.projeto.service_user.exception;

public class ConflitoException extends RuntimeException{
    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
