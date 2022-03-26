package net.wushilin.kafka.jmxtologconverter

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Configuration
@EnableScheduling
class SchedulingConfig : SchedulingConfigurer {
    private var logger = LoggerFactory.getLogger(SchedulingConfig::class.java)
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        logger.info("Registering scheduler executor...")
        taskRegistrar.setScheduler(taskExecutor())
    }

    @Bean(destroyMethod = "shutdown")
    fun taskExecutor(): Executor {
        return Executors.newScheduledThreadPool(10)
    }
}