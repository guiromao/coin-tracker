package co.gromao.cointracker.service

import co.gromao.cointracker.controller.model.CoinDto
import co.gromao.cointracker.exception.ResourceNotFoundException
import co.gromao.cointracker.mapper.CoinMapper
import co.gromao.cointracker.repository.CoinRepository
import org.springframework.stereotype.Service

@Service
class CoinService(private val coinRepository: CoinRepository) {

    fun getCoin(symbol: String): CoinDto {
        val coin = coinRepository.findBySymbol(symbol.uppercase().trim())
            ?: throw ResourceNotFoundException("Coin with symbol $symbol not found")

        return CoinMapper.mapToDto(coin)
    }

}