package co.gromao.cointracker.client

import co.gromao.cointracker.scheduler.dto.InformationDto
import co.gromao.cointracker.scheduler.dto.ValuesDto
import co.gromao.cointracker.scheduler.dto.aValuesDto
import co.gromao.cointracker.scheduler.dto.anInformationDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class CoinClientTest {

    private lateinit var client: CoinClient
    private lateinit var restTemplate: RestTemplate

    @BeforeEach
    fun setup() {
        restTemplate = mock(RestTemplate::class.java)

        client = CoinClient(restTemplate, baseUrl, authHeader, authKey, listPath, valuesPath)
    }

    @Test
    fun `should get coins set`() {
        val informationDto = anInformationDto()

        `when`(restTemplate.exchange(anyString(), any(), nullable(HttpEntity::class.java),
            eq(InformationDto::class.java)))
            .thenReturn(ResponseEntity(informationDto, HttpStatus.OK))

        val test = client.getCoinsSet(0)

        Assertions.assertTrue(
            test.all {
                informationDto.data.contains(it)
            }
        )
    }

    @Test
    fun `should get values for coins`() {
        val valuesDto = aValuesDto()

        `when`(restTemplate.exchange(anyString(), any(),
            nullable(HttpEntity::class.java), eq(ValuesDto::class.java)))
                .thenReturn(ResponseEntity(valuesDto, HttpStatus.OK))

        val test = client.getCoinsValuesFor(setOf(1L, 2L, 3L))

        Assertions.assertTrue(
            test.all {
                valuesDto.data[it.symbol] == it
            }
        )
    }

    companion object {
        const val baseUrl = "https://baseUrl.com"
        const val authHeader = "header-auth"
        const val authKey = "auth-key"
        const val listPath = "/list-path"
        const val valuesPath = "/values-path"
    }

}