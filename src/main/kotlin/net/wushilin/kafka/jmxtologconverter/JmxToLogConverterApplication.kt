package net.wushilin.kafka.jmxtologconverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ComponentScan
@EnableAsync
class JmxToLogConverterApplication

fun main(args: Array<String>) {
	runApplication<JmxToLogConverterApplication>(*args)
}
