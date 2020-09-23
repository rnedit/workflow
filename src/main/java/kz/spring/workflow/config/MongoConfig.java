package kz.spring.workflow.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories(basePackages = "kz.spring.workflow.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${mongodb.username}")
    private String username;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.db}")
    private String db;

    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(new StringBuilder()
                .append("mongodb://")
                .append(username)
                .append(":")
                .append(password)
                .append("@172.16.5.10:27017/")
                .append(db).toString());
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }


    @Override
    protected String getDatabaseName() {
        return "workflow";
    }

}
