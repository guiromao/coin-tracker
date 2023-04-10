package co.gromao.cointracker.scheduler.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StatusDto(

    @JsonProperty(PROPERTY_ERROR_CODE)
    val errorCode: Int,

    @JsonProperty(PROPERTY_ERROR_MESSAGE)
    val errorMessage: String?

) {
    companion object {
        private const val PROPERTY_ERROR_CODE = "error_code"
        private const val PROPERTY_ERROR_MESSAGE = "error_message"
    }
}