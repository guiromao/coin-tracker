package co.gromao.cointracker.repository

import co.gromao.cointracker.repository.entity.Coin
import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.util.Pair
import java.time.Instant

class CoinRepositoryImpl(
    private val template: MongoTemplate
): CoinRepositoryCustom {

    companion object {
        private const val ACTIVE_COIN_VALUE = 1
        private const val USD_STR_VALUE = "USD"
    }

    override fun updateBatch(dtos: Set<CoinDto>) {
        saveBatchUpdates(dtos)
    }

    override fun updateBatchValues(dtos: Set<CoinMarketDto>) {
        saveBatchUpdates(dtos)
    }

    override fun fetchByOffsetAndLimit(offset: Long, limit: Int): List<Coin> {
        val query = Query().skip(offset).limit(limit)

        return template.find(query, Coin::class.java)
    }

    private fun saveBatchUpdates(dtos: Set<CoinDto>) {
        val updates: List<Pair<Query, Update>> = dtos.map {
            updatePairOf(it)
        }

        template.bulkOps(BulkOperations.BulkMode.UNORDERED, Coin::class.java)
            .upsert(updates)
            .execute()
    }

    private fun updatePairOf(dto: CoinDto): Pair<Query, Update> {
        val update = if (dto is CoinMarketDto) {
            updateFromMarketDto(dto)
        } else {
            updateFromDto(dto)
        }
        val criteria = Criteria.`where`("_id").`is`(dto.symbol)
        val query = Query(criteria)

        return Pair.of(query, update)
    }

    private fun updateFromMarketDto(dto: CoinMarketDto): Update {
        val update = Update()
        val valueInDollars = dto.quote[USD_STR_VALUE]?.price

        update.set(Coin.FIELD_VALUE_IN_DOLLARS, valueInDollars)
        update.set(Coin.FIELD_CIRCULATING_SUPPLY, dto.circulatingSupply)
        update.set(Coin.FIELD_MAX_SUPPLY, dto.maxSupply)
        update.set(Coin.FIELD_TOTAL_SUPPLY, dto.totalSupply)
        update.set(Coin.FIELD_LAST_UPDATED_AT, Instant.now())

        return update
    }

    private fun updateFromDto(dto: CoinDto): Update {
        val update = Update()
        update.set(Coin.FIELD_NAME, dto.name)
        update.set(Coin.FIELD_IS_ACTIVE, dto.isActive == ACTIVE_COIN_VALUE)

        return update
    }

}