package co.gromao.cointracker.repository

import co.gromao.cointracker.repository.entity.Coin
import org.springframework.data.mongodb.repository.MongoRepository

interface CoinRepository: MongoRepository<Coin, String>, CoinRepositoryCustom