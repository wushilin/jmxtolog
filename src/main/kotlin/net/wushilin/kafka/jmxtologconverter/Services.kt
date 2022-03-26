package net.wushilin.kafka.jmxtologconverter

import net.wushilin.kafka.jmxtologconverter.crawlers.Crawler
import net.wushilin.kafka.jmxtologconverter.crawlers.Endpoint
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

@Service
class Services {
    private var logger = LoggerFactory.getLogger(Services::class.java)

    @Value("\${schedule}")
    lateinit var schedule: String

    @Autowired
    lateinit var crawler: Crawler

    @Scheduled(cron = "\${schedule}")
    fun runScheduled() {
        var run = AtomicBoolean(true)
        var monitorThread = thread {
            var start = System.currentTimeMillis()
            while (run.get()) {
                var now = System.currentTimeMillis()
                var elapsed = now - start
                if (elapsed > 30000) {
                    logger.warn("Thread has run for more then $elapsed ms")
                }
                Thread.sleep(100)
            }
        }
        try {
            crawler.run()
        } finally {
            run.set(false)
            monitorThread.join()
        }
    }


}