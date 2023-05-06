package co.gromao.cointracker.controller

import co.gromao.cointracker.exception.ErrorResponse
import co.gromao.cointracker.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

@ControllerAdvice
class CoinsControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun notFoundExceptionHandler(exception: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, exception)

        return ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND)
    }

}