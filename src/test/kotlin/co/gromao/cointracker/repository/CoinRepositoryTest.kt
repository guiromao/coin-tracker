package co.gromao.cointracker.repository

import co.gromao.cointracker.AbstractIntegrationTest
import co.gromao.cointracker.repository.entity.Coin
import co.gromao.cointracker.scheduler.dto.CoinDto
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

    private fun activeToInt(isActive: Boolean): Int =
        if (isActive) {
            1
        } else {
            0
        }

    companion object {
        val coins = setOf(
            Coin(
                1, "BTC", "Bitcoin", true, 30000.5,
                null, null, null, Instant.now()
            ),
            Coin(
                2, "ETH", "Ethereum", true, 2500.2,
                null, null, null, Instant.now()
            ),
            Coin(
                3, "DOT", "Polkadot", true, 10.5,
                null, null, null, Instant.now()
            )
        )
    }

}