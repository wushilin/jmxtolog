package net.wushilin.kafka.jmxtologconverter

import net.wushilin.kafka.jmxtologconverter.crawlers.Crawler
import net.wushilin.kafka.jmxtologconverter.crawlers.Endpoint
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EndpointConfigs {
    private val logger = LoggerFactory.getLogger(EndpointConfigs::class.java)
    @Autowired
    private lateinit var appContext: ApplicationContext

    fun getWhitelists():List<Regex> {
        var result = mutableListOf<Regex>()
        var index = 0
        while(index++ < 3000){
            var regKey = "whitelist.regex.${index}"
            var regexVal = read(regKey)
            if(regexVal == null || regexVal.isBlank()) {
                continue
            }
            var regexStr = regexVal.trim()
            result.add(Regex(regexStr))
        }
        return result
    }

    @Bean
    fun getConfigs():List<Endpoint> {
        var result = mutableListOf<Endpoint>()
        var index = 0
        while(index++ < 3000){
            var nameKey = "jmx${index}.name"
            var tagKey = "jmx${index}.tag"
            var urlKey = "jmx${index}.url"
            var name = read(nameKey)
            var tag = read(tagKey)
            var url = read(urlKey)
            if(name == null || tag == null || url ==null) {
                continue
            }
            result.add(Endpoint(url=url, tag=tag, name=name))
        }
        return result
    }

    fun read(key:String):String? {
        return appContext.environment.getProperty(key)
    }

    @Bean
    fun getCrawler(@Autowired endpoints: List<Endpoint>,
                   @Value("\${timeout}") timeout:Long,
                   @Value("\${magic}") magic:String, @Value("\${crawler.threads}") threads:Int): Crawler {
        return Crawler(endpoints, magic=magic, timeout=timeout, threads=threads, whitelists = getWhitelists());
    }
}