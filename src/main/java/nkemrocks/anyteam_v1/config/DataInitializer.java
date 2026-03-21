package nkemrocks.anyteam_v1.config;

import nkemrocks.anyteam_v1.dto.sysConfig.request.SysConfig_RequestDTO;
import nkemrocks.anyteam_v1.service.SysConfig_Service;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner initializer(SysConfig_Service sysConfigService) {
        /* approx 100 years ttl */
        return args -> sysConfigService.initConfigSession(
                new SysConfig_RequestDTO(100 * 365 * 24 * 3600L)
        );
    }
}
