package co.gromao.cointracker.repository.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(value = Coin.COIN_COLLECTION)
data class Coin(

    @Id
    val id: Long,

    @Field(FIELD_SYMBOL)
    val symbol: String,

    @Field(FIELD_NAME)
    val name: String,

    @Field(FIELD_IS_ACTIVE)
    val isActive: Boolean,

    @Field(FIELD_VALUE_IN_DOLLARS)
    val valueInDollars: Double? = DEFAULT_PRICE,

    @Field(FIELD_CIRCULATING_SUPPLY)
    val circulatingSupply: Double? = DEFAULT_PRICE,

    @Field(FIELD_TOTAL_SUPPLY)
    val totalSupply: Double? = DEFAULT_PRICE,

    @Field(FIELD_MAX_SUPPLY)
    val maxSupply: Double? = DEFAULT_PRICE,

    @Field(FIELD_LAST_UPDATED_AT)
    val lastUpdatedAt: Instant? = null

) {

    companion object {
        const val COIN_COLLECTION = "coin"
        const val FIELD_SYMBOL = "symbol"
        const val FIELD_NAME = "name"
        const val FIELD_IS_ACTIVE = "isActive"
        const val FIELD_VALUE_IN_DOLLARS =  "valueInDollars"
        const val FIELD_CIRCULATING_SUPPLY = "circulatingSupply"
        const val FIELD_TOTAL_SUPPLY = "totalSupply"
        const val FIELD_MAX_SUPPLY = "maxSupply"
        const val FIELD_LAST_UPDATED_AT = "lastUpdatedAt"
        const val DEFAULT_PRICE = 0.0
    }

}