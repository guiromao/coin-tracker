package co.gromao.cointracker.scheduler.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
open class CoinDto(

    @JsonProperty(PROPERTY_ID)
    val id: Long,

    @JsonProperty(PROPERTY_NAME)
    val name: String,

    @JsonProperty(PROPERTY_SYMBOL)
    val symbol: String,

    @JsonProperty(PROPERTY_IS_ACTIVE)
    val isActive: Int

) {

    companion object {
        const val PROPERTY_ID = "id"
        const val PROPERTY_NAME = "name"
        const val PROPERTY_SYMBOL = "symbol"
        const val PROPERTY_IS_ACTIVE = "is_active"
    }

}