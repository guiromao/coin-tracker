package co.gromao.cointracker.mapper

import co.gromao.cointracker.controller.model.CoinDto
import co.gromao.cointracker.repository.entity.Coin

class CoinMapper {

    companion object {

        fun mapToDto(coin: Coin): CoinDto {
            return CoinDto(
                coin.symbol, coin.valueInDollars, coin.circulatingSupply
            )
        }

    }

}