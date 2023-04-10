package co.gromao.cointracker.scheduler.dto

fun aStatusDto(): StatusDto =
    StatusDto(
        errorCode = 0,
        errorMessage = null
    )

fun aCoinDto(): CoinDto =
    CoinDto(
        id = 1,
        name = "Bitcoin",
        symbol = "BTC",
        isActive = 1
    )

fun aCoinMarketDto(): CoinMarketDto =
    CoinMarketDto(
        totalSupply = 100000.5,
        maxSupply = 200000.0,
        circulatingSupply = 150000.3,
        id = 1L,
        isActive = 1,
        name = "Bitcoin",
        symbol = "BTC",
        quote = mapOf(
            "USD" to CoinMarketDto.QuoteValue(30000.2, 500000000.9)
        )
    )

fun anInformationDto(): InformationDto =
    InformationDto(
        status = aStatusDto(),
        data = setOf(aCoinDto(), CoinDto(2, "Ethereum", "ETC", 1))
    )

fun aValuesDto(): ValuesDto =
    ValuesDto(
        status = aStatusDto(),
        data = mapOf(
            "BTC" to aCoinMarketDto(),
            "ETC" to CoinMarketDto(
                1000000.2, 124000000.4, 400000000.1,
                mapOf(
                    "USD" to CoinMarketDto.QuoteValue(3000.7, 700000000.3)
                ),
                2, "Ethereum", "ETC", 1
            )
        )
    )