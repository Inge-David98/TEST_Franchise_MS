package co.com.franchise.r2dbc.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class PropertiesParameters {
    @Value("${postgres.database}")
    private String database;
    @Value("${postgres.schema}")
    private String schema;
    @Value("${postgres.user}")
    private String username;
    @Value("${postgres.password}")
    private String password;
    @Value("${postgres.host}")
    private String host;
    @Value("${postgres.port}")
    private Integer port;

}
