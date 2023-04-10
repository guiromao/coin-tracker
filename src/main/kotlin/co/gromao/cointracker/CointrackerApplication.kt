package co.gromao.cointracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class CointrackerApplication

fun main(args: Array<String>) {
	runApplication<CointrackerApplication>(*args)
}
