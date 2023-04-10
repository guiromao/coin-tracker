package co.gromao.cointracker.scheduler

import co.gromao.cointracker.repository.CoinRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.util.CollectionUtils

@Component
class CoinMarketScheduler(
    val client: CoinClient,
    val repository: CoinRepository
) {

    companion object {
        private const val COINS_LIMIT = 200
        private val LOGGER = LoggerFactory.getLogger(CoinMarketScheduler::class.java)
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    fun updateCoinData() {
        LOGGER.info("Starting task to update coins information and values...")

        val baseTime = System.currentTimeMillis()
        updateCoinInfo()
        updateCoinsValues()

        LOGGER.info("Task to update coins information and values is done in {} seconds.",
            (System.currentTimeMillis() - baseTime).toDouble() / 1000)
    }

    private fun updateCoinsValues() {
        var isTaskDone = false
        var offset = 0L

        while (!isTaskDone) {
            val coins = repository.fetchByOffsetAndLimit(offset, COINS_LIMIT)

            if (CollectionUtils.isEmpty(coins)) {
                isTaskDone = true
            } else {
                val symbols = coins.map { it.id }.toSet()
                val coinMarketDtos = client.getCoinsValuesFor(symbols)

                repository.updateBatchValues(coinMarketDtos)
                offset += COINS_LIMIT
            }
        }
    }

    private fun updateCoinInfo() {
        val coins = client.getCoinsList()

        if (!CollectionUtils.isEmpty(coins)) {
            repository.updateBatch(coins)
        }
    }

}