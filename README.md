# Crawl JMX metrics and print to stdout

Use case: Kubernetes based JMX monitoring, you will only need this in very rare situations

# Building
```
$ gradle build
```
You should see `jmxtolog-boot-0.0.1-SNAPSHOT.zip` in build/distributions folder

# Creating config file
You can create JMX config file as an application.properties

```
# Schedule in SECOND MINUTE HOUR DAY MONTH DAY_OF_WEEK in cron syntax
schedule=*/30 * * * * *
# Each crawl will timeout after this many seconds
timeout=50
# How many threads to run the crawler
crawler.threads=15
# What regular expression to whitelist the JMX attribute names
# You can add as many as 3000 of them
whitelist.regex.1=.*

# Special keyword that appears in stdout as magic=xxxx
magic=4l5q819oP

# JMX URL1
# You can add as many as 3000 of them
jmx1.url=http://172.31.38.224:7778/metrics
jmx1.name=Test1
jmx1.tag=Test1Tag
...
jmx2.url=xxx
jmx2.name=xxx
jmx2.tag=xxx

# The ultimage output looks like:
magic=4l5q819oP/time=1648260840698/endpoint=http://172.31.38.224:7778/metrics/name=Test1/tag=Test1Tag jmx_scrape_error 0.0
# The above line repeats for all JMX attributes exported in http://172.31.38.224:7778/metrics
```
# Running
Unzip it to your preferred folder, run with `bin/jmxtolog --spring.config.location=/application.properties`

Note that you need to customize your location above.





