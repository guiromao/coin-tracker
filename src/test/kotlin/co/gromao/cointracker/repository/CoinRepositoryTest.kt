package co.gromao.cointracker.repository

import co.gromao.cointracker.AbstractIntegrationTest
import co.gromao.cointracker.repository.entity.Coin
import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
            createMarketDto(2, "ETH", 2200.5)
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

    @Test
    fun `should find by coin symbol`() {
        val coin1 = Coin(1L, "SYMBOL-1", "Coin 1", true)
        val coin2 = Coin(2L, "SYMBOL-2", "Coin 2", true)
        val coin3 = Coin(3L, "SYMBOL-3", "Coin 3", true)

        repository.saveAll(setOf(coin1, coin2, coin3))

        val result = repository.findBySymbol("SYMBOL-1")

        Assertions.assertNotNull(result)
        Assertions.assertEquals(coin1, result)
    }

    @Test
    fun `should find paged results`() {
        val coins = (1..50).map {
            Coin(it.toLong(), "SYMBOL-$it", "Coin $it", true, circulatingSupply = it.toDouble())
        }
        val pageable = PageRequest.of(0, 10, Sort.by(Coin.FIELD_CIRCULATING_SUPPLY).descending())

        repository.saveAll(coins)

        val result = repository.findByPageable(pageable)

        Assertions.assertEquals(10, result.size)
        Assertions.assertEquals(
            (50 downTo 41).map { it.toDouble() },
            result.map { it.circulatingSupply }
        )
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