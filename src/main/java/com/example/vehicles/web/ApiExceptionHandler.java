package com.example.vehicles.web;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.vehicles.vehicle.DuplicatePlateException;
import com.example.vehicles.vehicle.VehicleNotFoundException;

/** Converte exceções em respostas de erro com um formato único (Problem Details, RFC 9457). */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ProblemDetail notFound(VehicleNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Veículo não encontrado");
        return problem;
    }

    @ExceptionHandler(DuplicatePlateException.class)
    public ProblemDetail duplicate(DuplicatePlateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Matrícula duplicada");
        return problem;
    }

    // Rede de segurança: se dois pedidos simultâneos passarem o existsByPlate(), a BD recusa pela constraint unique
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail integrity(DataIntegrityViolationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "Os dados violam uma restrição da base de dados");
        problem.setTitle("Conflito");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Objects.requireNonNullElse(error.getDefaultMessage(), "inválido"),
                        (first, second) -> first));
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos");
        problem.setTitle("Erro de validação");
        problem.setProperty("errors", errors);
        return problem;
    }
}