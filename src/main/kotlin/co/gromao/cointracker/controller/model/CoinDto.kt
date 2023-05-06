package co.gromao.cointracker.controller.model

data class CoinDto(
    val symbol: String,
    val value: Double?,
    val marketCap: Double?
)