package co.gromao.cointracker.repository

import co.gromao.cointracker.AbstractIntegrationTest
import co.gromao.cointracker.repository.entity.Coin
import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class CoinRepositoryTest: AbstractIntegrationTest() {

    @BeforeEach
    fun setup() {
        teardown()
        repository.saveAll(coins)
    }

    @AfterEach
    fun teardown() {
        repository.deleteAll()
    }

    @Test
    fun `should update batch of Coin DTOs`() {
        val coinDtos = setOf(
            CoinDto(4, "Solana", "SOL", 1),
            CoinDto(5, "Avalanche", "AVAX", 1),
            CoinDto(1, "BTC", "Bitcoin Man!", 0)
        )

        repository.updateBatch(coinDtos)

        val test = repository.findAllById(coinDtos.map { it.id.toString() })

        Assertions.assertTrue(
            test.all {
                coinDtos.contains(CoinDto(it.id, it.name, it.symbol, activeToInt(it.isActive)))
            }
        )
    }

    @Test
    fun `should update batch of CoinMarketDtos`() {
        val coinMarketDtos = setOf(
            createMarketDto(1, "BTC", 30000.2),
            createMarketDto(2, "ETC", 2200.5)
        )

        repository.updateBatchValues(coinMarketDtos)

        val allCoins = repository.findAll()

        Assertions.assertEquals(
            coinMarketDtos.size,
            allCoins.filter { it.valueInDollars != Coin.DEFAULT_PRICE }.size
        )
    }

    @Test
    fun `should find by offset and limit`() {
        val coins = (1..50).map { Coin(it.toLong(), "NAM-$it", "Name $it", true) }

        repository.saveAll(coins)

        val test = repository.findByOffsetAndLimit(0, 10)

        Assertions.assertEquals(10, test.size)
    }

    private fun createMarketDto(id: Long, symbol: String, dollarValue: Double): CoinMarketDto =
        CoinMarketDto(
            100.0, 80.0, 120.0,
            mapOf("USD" to CoinMarketDto.QuoteValue(dollarValue, 200.0)),
            id, "Cryptocurrency", symbol, 1
        )

    private fun activeToInt(isActive: Boolean): Int =
        if (isActive) {
            1
        } else {
            0
        }

    companion object {
        val coins = setOf(
            Coin(
                1, "BTC", "Bitcoin", true, null,
                null, null, null, Instant.now()
            ),
            Coin(
                2, "ETH", "Ethereum", true, null,
                null, null, null, Instant.now()
            ),
            Coin(
                3, "DOT", "Polkadot", true, null,
                null, null, null, Instant.now()
            )
        )
    }

}