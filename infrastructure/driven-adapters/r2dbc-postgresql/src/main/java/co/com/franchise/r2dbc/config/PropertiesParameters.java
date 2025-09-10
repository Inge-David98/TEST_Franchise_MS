package co.com.franchise.r2dbc.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class PropertiesParameters {
    @Value("${postgres.database:franchise_db}")
    private String database;
    @Value("${postgres.schema:public}")
    private String schema;
    @Value("${postgres.user:postgres}")
    private String username;
    @Value("${postgres.password:password}")
    private String password;
    @Value("${postgres.host:localhost}")
    private String host;
    @Value("${postgres.port:5432}")
    private Integer port;

}
