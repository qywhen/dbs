import ch.qos.logback.core.util.FileSize

import java.nio.charset.Charset

/**
 * Created with IntelliJ IDEA.
 * @date 2018-12-17
 * @time 18:31
 * @author scarlet
 */
def LOG_HOME = 'log'
def appName = 'dbs'

appender('stdout', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} : %line - %msg%n"
    }
}

appender('appLogAppender', RollingFileAppender) {
    file = "${LOG_HOME}/${appName}.log"
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_HOME}/${appName}-%d{yyyy-MM-dd}-%i.log"
        maxHistory = 365
        maxFileSize = FileSize.valueOf('100mb')
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [ %thread ] - [ %-5level ] [ %logger{50} : %line ] - %msg%n"
    }
}
//appender('mysqlAppender', DBAppender) {
//    connectionSource(DataSourceConnectionSource) {
//        dataSource(ComboPooledDataSource) {
//            driverClass = 'org.h2.Driver'
//            password = '123456'
//            jdbcUrl = 'jdbc:h2:file:./dbsdb'
//            user = 'root'
//        }
//    }
//}

logger('com.wisd', DEBUG)
logger('org.apache', WARN)
logger('com.zaxxer', WARN)
logger('org.springframework', WARN, [], additivity = false)

root(DEBUG, ['stdout', 'appLogAppender'])
