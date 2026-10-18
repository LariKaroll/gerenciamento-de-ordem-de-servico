package com.projeto.service_user.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private record ErroResposta(
            Instant timestamp,
            int status,
            String erro,
            String mensagem,
            List<CampoErro> errosValidacao) {
        static ErroResposta criar(HttpStatus status, String erro, String mensagem) {
            return new ErroResposta(Instant.now(), status.value(), erro, mensagem, null);
        }

        static ErroResposta criarValidacao(HttpStatus status, String erro, String mensagem, List<CampoErro> detalhes) {
            return new ErroResposta(Instant.now(), status.value(), erro, mensagem, detalhes);
        }
    }

    private record CampoErro(String campo, String mensagem) {
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> handleRegraDeNegocio(RegraDeNegocioException ex) {
        ErroResposta erro = ErroResposta.criar(HttpStatus.BAD_REQUEST, "Requisição Inválida", ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidacaoCampos(MethodArgumentNotValidException ex) {
        List<CampoErro> detalhes = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String campo = (error instanceof FieldError fieldError) ? fieldError.getField() : "objeto";
                    return new CampoErro(campo, error.getDefaultMessage());
                })
                .toList();

        ErroResposta erro = ErroResposta.criarValidacao(
                HttpStatus.BAD_REQUEST,
                "Erro de Validação",
                "Um ou mais campos contêm erros de preenchimento.",
                detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String mensagem = "O corpo da requisição é inválido ou contém valores não reconhecidos pelo sistema.";
        String causa = ex.getMessage() != null ? ex.getMessage() : "";

        if (causa.contains("PerfilUsuario")) {
            mensagem = "Perfil de usuário inexistente ou inválido. Valores aceitos: ADMINISTRADOR, AUDITOR, TECNICO.";
        }

        ErroResposta erro = ErroResposta.criar(HttpStatus.BAD_REQUEST, "Corpo da Requisição Inválido", mensagem);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> handleNoResourceFound(NoResourceFoundException ex) {
        ErroResposta erro = ErroResposta.criar(HttpStatus.NOT_FOUND, "Recurso Não Encontrado",
                "O endpoint solicitado não existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ErroResposta erro = ErroResposta.criar(HttpStatus.METHOD_NOT_ALLOWED, "Método Não Permitido",
                "O método HTTP informado não é suportado para este endpoint.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erro);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResposta> handleConflito(ConflitoException ex) {
        ErroResposta erro = ErroResposta.criar(HttpStatus.CONFLICT, "Conflito de Dados", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResposta> handleDataIntegrity(DataIntegrityViolationException ex) {

        if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
            String sqlState = constraintEx.getSQLState();
            String constraintName = constraintEx.getConstraintName() != null
                    ? constraintEx.getConstraintName().toLowerCase()
                    : "";

            // Código padrão ANSI/PostgreSQL/H2 para violação de unicidade (Unique Key) é
            // 23505
            if ("23505".equals(sqlState) || constraintName.contains("uk_") || constraintName.contains("unique")) {
                String mensagem = "Não foi possível salvar o registro devido a uma duplicidade de dados cadastrais.";

                if (constraintName.contains("cpf")) {
                    mensagem = "O CPF informado já está cadastrado no sistema.";
                } else if (constraintName.contains("email")) {
                    mensagem = "O e-mail informado já está cadastrado no sistema.";
                }

                ErroResposta erro = ErroResposta.criar(HttpStatus.CONFLICT, "Conflito de Integridade", mensagem);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
            }
        }

        logger.error("Violação física de integridade de dados detectada: ", ex);
        ErroResposta erro = ErroResposta.criar(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro de Consistência",
                "Ocorreu uma inconsistência ao processar os dados do registro no banco.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleErrosGlobais(Exception ex) {
        logger.error("Erro crítico inesperado na API: ", ex);

        ErroResposta erro = ErroResposta.criar(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro Interno do Servidor",
                "Ocorreu um erro interno inesperado no servidor. Por favor, tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}