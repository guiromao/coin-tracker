package co.gromao.cointracker.controller

import co.gromao.cointracker.controller.model.CoinDto
import co.gromao.cointracker.service.CoinService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/coins")
class CoinsController(
    private val coinService: CoinService
) {

    @GetMapping("/{symbol}")
    fun getCoin(@PathVariable("symbol") symbol: String): CoinDto {
        return coinService.getCoin(symbol)
    }

    @GetMapping
    fun getCoins(
        @PageableDefault(size = 10, page = 0, sort = ["circulatingSupply", "DESC"]) pageable: Pageable
    ): List<CoinDto> {
        return coinService.getCoinsList(pageable)
    }

}