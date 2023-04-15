import co.gromao.cointracker.client.CoinClient
import co.gromao.cointracker.repository.CoinRepository
import co.gromao.cointracker.repository.entity.Coin
import co.gromao.cointracker.scheduler.CoinMarketScheduler
import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.Instant

class CoinMarketSchedulerTest {

    private val coinClient: CoinClient = mock(CoinClient::class.java)
    private val repository: CoinRepository = mock(CoinRepository::class.java)

    private val scheduler: CoinMarketScheduler = CoinMarketScheduler(coinClient, repository)

    @Test
    fun `should update coins data and values`() {
        val dtos = (1..50).map { CoinDto(it.toLong(), "Coin-$it", "\$C-$it", 1) }.toSet()
        val marketDtos = (1..50).map {
            CoinMarketDto(
                30.0, 50.0, 100.0,
                mapOf("\$C-$it" to CoinMarketDto.QuoteValue(20.0, 50.0)),
                it.toLong(), "Coin-$it", "\$C-$it", 1
            )
        }.toSet()
        val coins = (1..50).map {
            Coin(
                it.toLong(), "\$C-$it", "Coin-$it", true,
                20.0, 30.0, 50.0, 100.0,
                Instant.now()
            )
        }

        `when`(coinClient.getCoinsSet(anyInt())).thenReturn(dtos).thenReturn(emptySet())
        `when`(repository.findByOffsetAndLimit(anyLong(), anyInt())).thenReturn(coins).thenReturn(emptyList())
        `when`(coinClient.getCoinsValuesFor(anySet())).thenReturn(marketDtos)

        scheduler.updateCoinData()

        verify(coinClient, atLeast(1)).getCoinsSet(anyInt())
        verify(repository, atLeast(1)).updateBatch(anySet())
        verify(repository, atLeast(1)).findByOffsetAndLimit(anyLong(), anyInt())
        verify(coinClient, atLeast(1)).getCoinsValuesFor(anySet())
    }

}