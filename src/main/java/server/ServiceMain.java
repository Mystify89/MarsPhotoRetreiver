package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Provides executable Tracking Ingest service.
 */
@SpringBootApplication(scanBasePackageClasses = {ServiceMain.class})
public class ServiceMain {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMain.class);

    /**
     * Main executable.
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            LOG.error("Uncaught Throwable in Thread: " + thread.getName(),
                    throwable);
        });
        SpringApplication.run(ServiceMain.class, args);
    }
}

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
    }
}
