package co.gromao.cointracker.scheduler

import co.gromao.cointracker.client.CoinClient
import co.gromao.cointracker.repository.CoinRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CoinMarketScheduler(
    val client: CoinClient,
    val repository: CoinRepository
) {

    companion object {
        private const val COINS_LIMIT = 500
        private const val DELAY_VALUE = (30 * 60 * 1000).toLong()
        private val LOGGER = LoggerFactory.getLogger(CoinMarketScheduler::class.java)
    }

    @Scheduled(fixedDelay = DELAY_VALUE)
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
            val coins = repository.findByOffsetAndLimit(offset, COINS_LIMIT)

            if (coins.isNullOrEmpty()) {
                isTaskDone = true
            } else {
                val coinIds = coins.map { it.id }.toSet()
                val coinMarketDtos = client.getCoinsValuesFor(coinIds)

                repository.updateBatchValues(coinMarketDtos)
                offset += COINS_LIMIT
            }
        }
    }

    private fun updateCoinInfo() {
        var offsetIndex = 0
        var isTaskDone = false

        while (!isTaskDone) {
            val coins = client.getCoinsSet(offsetIndex)

            if (coins.isNullOrEmpty()) {
                isTaskDone = true
            } else {
                repository.updateBatch(coins)
                offsetIndex++
            }
        }
    }

}