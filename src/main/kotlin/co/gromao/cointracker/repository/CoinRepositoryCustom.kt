package co.gromao.cointracker.repository

import co.gromao.cointracker.repository.entity.Coin
import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto

interface CoinRepositoryCustom {

    fun updateBatch(dtos: Set<CoinDto>)

    fun updateBatchValues(dtos: Set<CoinMarketDto>)

    fun findByOffsetAndLimit(offset: Long, limit: Int): List<Coin>

}