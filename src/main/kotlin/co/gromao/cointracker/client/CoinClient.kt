package co.gromao.cointracker.client

import co.gromao.cointracker.scheduler.dto.CoinDto
import co.gromao.cointracker.scheduler.dto.CoinMarketDto
import co.gromao.cointracker.scheduler.dto.InformationDto
import co.gromao.cointracker.scheduler.dto.ValuesDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Component
@PropertySource("classpath:client.properties")
class CoinClient(
    private val restTemplate: RestTemplate,
    @Value("\${baseurl}") private val baseUrl: String,
    @Value("\${authHeader}") private val authHeaderField: String,
    @Value("\${authKey}") private val authKey: String,
    @Value("\${coins-list-path}") private val coinsListPath: String,
    @Value("\${coins-values-path}") private val coinsValuesPath: String
) {

    companion object {
        private const val COINS_LIMIT = 1000
        private val LOGGER = LoggerFactory.getLogger(CoinClient::class.java)
    }

    fun getCoinsList(offsetIndex: Int): Set<CoinDto> {
        val start = offsetIndex * COINS_LIMIT + 1
        val getCoinsUrl = "$baseUrl$coinsListPath?start=${start}&limit=$COINS_LIMIT"
        val httpEntity = HttpEntity<String>(authHeaders())

        return try {
            restTemplate.exchange(getCoinsUrl, HttpMethod.GET, httpEntity, InformationDto::class.java)
                .body?.data ?: emptySet()
        } catch (e: RestClientException) {
            LOGGER.error("Error getting updated Coins information with error: {}", e)
            emptySet()
        }
    }

    fun getCoinsValuesFor(coinIds: Set<Long>): Set<CoinMarketDto> {
        val coinIdsStr = coinIds.joinToString(",")
        val coinsValuesUrl = "$baseUrl$coinsValuesPath?id=$coinIdsStr"
        val httpEntity = HttpEntity<String>(authHeaders())

        return try {
            val data = restTemplate.exchange(coinsValuesUrl, HttpMethod.GET, httpEntity, ValuesDto::class.java)
                .body?.data

            return if (!data.isNullOrEmpty()) {
                data.values.toSet()
            } else {
                emptySet()
            }
        } catch (e: RestClientException) {
            LOGGER.error("Error getting updated Coins values with message: {}", e)
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