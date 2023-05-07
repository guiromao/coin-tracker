package co.gromao.cointracker.service

import co.gromao.cointracker.exception.ResourceNotFoundException
import co.gromao.cointracker.repository.CoinRepository
import co.gromao.cointracker.repository.entity.Coin
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class CoinServiceTest {

    private lateinit var repository: CoinRepository
    private lateinit var service: CoinService

    @BeforeEach
    fun setup() {
        repository = mock()

        service = CoinService(repository)
    }

    @Test
    fun `should throw not found`() {
        `when`(repository.findBySymbol(anyString())).thenReturn(null)

        Assertions.assertThrows(
            ResourceNotFoundException::class.java
        ) { service.getCoin("SYMBOL-X") }
    }

    @Test
    fun `should return coin by its symbol`() {
        val coin = coin()

        `when`(repository.findBySymbol(anyString())).thenReturn(coin())

        val result = service.getCoin("SYMBOL-1")

        Assertions.assertEquals(coin.symbol, result.symbol)
        Assertions.assertEquals(coin.circulatingSupply, result.marketCap)
        Assertions.assertEquals(coin.valueInDollars, result.value)
    }

    @Test
    fun `should return paged coins`() {
        val pageable = PageRequest.of(0, 10, Sort.by(Coin.FIELD_CIRCULATING_SUPPLY).descending())
        val coins = (1..10).map { coin().copy(id = it.toLong(), symbol = "SYMBOL-$it") }

        `when`(repository.findByPageable(any())).thenReturn(coins)

        val results = service.getCoinsList(pageable)

        Assertions.assertEquals(
            coins.map { it.symbol }.toSet(),
            results.map { it.symbol }.toSet()
        )
    }

    private fun coin(): Coin {
        return Coin(1L, "SYMBOL-1", "Coin 1",
            true, 100.0, 10000.0)
    }

}