package ir.softernet.co2.monitoring.config;

import ir.softernet.co2.monitoring.aspect.InvocationAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration for activating {@link ir.softernet.co2.monitoring.aspect.InvocationAspect} aspect
 *
 * @author saman
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

  @Bean
  public InvocationAspect invocationAspect() {
    return new InvocationAspect();
  }

}
