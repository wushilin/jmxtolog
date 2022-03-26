package net.wushilin.kafka.jmxtologconverter.crawlers

import org.slf4j.LoggerFactory
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Crawler(val configs:List<Endpoint>, val magic:String, val threads:Int = 30, val timeout:Long=30, val whitelists:List<Regex>){
    private val logger = LoggerFactory.getLogger(Crawler::class.java)
    init {
        logger.info("Initializing crawler with ${configs.size} endpoints, ${threads} threads, ${whitelists.size} whitelists, timeout $timeout seconds...")
    }
    fun run() {
        logger.debug("Crawler start...")
        var executor = Executors.newFixedThreadPool(threads)
        configs.forEach {
            next ->
            executor.submit {
                newTaskFor(next)
            }
        }
        executor.shutdown()
        while(!executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {

        }
        logger.debug("Crawler end")
    }

    fun newTaskFor(endpoint:Endpoint) {
        logger.debug("Running for ${endpoint}")
        try {
            val request = HttpRequest.newBuilder(URI(endpoint.url)).GET()
                .timeout(Duration.ofSeconds(timeout))
                .build()
            val response: HttpResponse<String> = HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, BodyHandlers.ofString())
            val responseBody = response.body()
            responseBody.lines().filter {
                !it.startsWith("#")
            }.filter{
                it->
                var matched = false
                for(nextWL in whitelists) {
                    if(it.matches(nextWL)) {
                        matched = true
                        break
                    }
                }
                matched
            }.forEach {
                println("magic=$magic/time=${System.currentTimeMillis()}/endpoint=${endpoint.url}/name=${endpoint.name}/tag=${endpoint.tag} $it")
            }
        } catch(t:Throwable) {
            logger.error("Error processing ${endpoint}: $t(${t.message})")
        } finally {
            logger.debug("Ended for ${endpoint}")
        }
    }
}