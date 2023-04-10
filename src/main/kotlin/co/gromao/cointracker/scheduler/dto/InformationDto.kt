package co.gromao.cointracker.scheduler.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class InformationDto(

    @JsonProperty(PROPERTY_STATUS)
    val status: StatusDto,

    @JsonProperty(PROPERTY_DATA)
    val data: Set<CoinDto>

) {

    companion object {
        private const val PROPERTY_STATUS = "status"
        private const val PROPERTY_DATA = "data"
    }

}