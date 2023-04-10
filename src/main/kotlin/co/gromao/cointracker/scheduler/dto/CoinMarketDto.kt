package co.gromao.cointracker.scheduler.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class CoinMarketDto(

    @JsonProperty(PROPERTY_TOTAL_SUPPLY)
    val totalSupply: Double,

    @JsonProperty(PROPERTY_CIRCULATING_SUPPLY)
    val circulatingSupply: Double,

    @JsonProperty(PROPERTY_MAX_SUPPLY)
    val maxSupply: Double,

    @JsonProperty(PROPERTY_QUOTE)
    val quote: Map<String, QuoteValue>,

    id: Long,
    name: String,
    symbol: String,
    isActive: Int

): CoinDto(id, name, symbol, isActive) {

    companion object {
        const val PROPERTY_TOTAL_SUPPLY = "total_supply"
        const val PROPERTY_CIRCULATING_SUPPLY = "circulating_supply"
        const val PROPERTY_MAX_SUPPLY = "max_supply"
        const val PROPERTY_QUOTE = "quote"
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class QuoteValue(

        @JsonProperty(PROPERTY_PRICE)
        val price: Double,

        @JsonProperty(PROPERTY_MARKET_CAP)
        val marketCap: Double

    ) {
        companion object {
            const val PROPERTY_PRICE = "price"
            const val PROPERTY_MARKET_CAP = "market_cap"
        }
    }

}