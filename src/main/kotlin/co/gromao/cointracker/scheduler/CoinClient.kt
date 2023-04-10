package co.gromao.cointracker.scheduler

import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto
import co.gromao.cointracker.scheduler.dto.InformationDto
import co.gromao.cointracker.scheduler.dto.ValuesDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class CoinClient(
    private val restTemplate: RestTemplate,
    @Value("\${coinclient.baseurl}") private val baseUrl: String,
    @Value("\${cointclient.authHeader}") private val authHeaderField: String,
    @Value("\${cointclient.authKey}") private val authKey: String,
    @Value("\${coinclient.coins-list-path}") private val coinsListPath: String,
    @Value("\${coinclient.coins-values-path}") private val coinsValuesPath: String
) {

    companion object {
        private const val COINS_LIMIT = 500
        private val LOGGER = LoggerFactory.getLogger(CoinClient::class.java)
    }

    fun getCoinsList(): Set<CoinDto> {
        val getCoinsUrl = "$baseUrl$coinsListPath?limit=$COINS_LIMIT"
        val httpEntity = HttpEntity<String>(authHeaders())

        return try {
            restTemplate.exchange(getCoinsUrl, HttpMethod.GET, httpEntity, InformationDto::class.java)
                .body?.data ?: emptySet()
        } catch (e: RestClientException) {
            LOGGER.error("Error getting updated Coins information")
            emptySet()
        }
    }

    fun getCoinsValuesFor(coinsSymbols: Set<String>): Set<CoinMarketDto> {
        val coinsSymbolsStr = coinsSymbols.joinToString(",")
        val coinsValuesUrl = "$baseUrl$coinsValuesPath?symbol=$coinsSymbolsStr"
        val httpEntity = HttpEntity<String>(authHeaders())

        return try {
            restTemplate.exchange(coinsValuesUrl, HttpMethod.GET, httpEntity, ValuesDto::class.java)
                .body?.data?.values?.toSet() ?: emptySet()
        } catch (e: RestClientException) {
            LOGGER.error("Error getting updated Coins values")
            emptySet()
        }
    }

    private fun authHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)
        headers.set(authHeaderField, authKey)

        return headers
    }

}