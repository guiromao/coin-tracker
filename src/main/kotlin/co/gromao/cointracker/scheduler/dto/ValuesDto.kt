package co.gromao.cointracker.scheduler.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ValuesDto(

    @JsonProperty(PROPERTY_STATUS)
    val status: StatusDto,

    @JsonProperty(PROPERTY_DATA)
    val data: Map<String, CoinMarketDto>

) {

    companion object {
        private const val PROPERTY_STATUS = "status"
        private const val PROPERTY_DATA = "data"
    }

}