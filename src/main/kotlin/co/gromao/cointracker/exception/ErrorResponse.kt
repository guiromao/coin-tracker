package co.gromao.cointracker.exception

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val code: Int,
    val message: String
) {

    companion object {

        fun of(status: HttpStatus, exception: Exception): ErrorResponse {
            val errorMessage = exception.message ?: "Unknown error"
            return ErrorResponse(status.value(), errorMessage)
        }

    }

}