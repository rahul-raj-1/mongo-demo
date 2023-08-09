package com.mongodb.demo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.SslSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    private String mongoUri;

    // Hardcoded file paths for the key store and trust store
    private static final String KEY_STORE_PATH = "/path/to/client.pem";
    private static final String TRUST_STORE_PATH = "/path/to/ca.crt";
    private static final String KEY_STORE_PASSWORD = "yourKeyStorePassword";
    private static final String TRUST_STORE_PASSWORD = "yourTrustStorePassword";

    @Override
    protected String getDatabaseName() {
        // Return the name of the default database you want to connect to
        return "myDatabase";
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);

        // Create SSL settings with the loaded PEM and CRT content
        SslSettings sslSettings = createSslSettings();

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSslSettings(builder -> builder.enabled(sslSettings.isEnabled())
                        .context(sslSettings.getContext()))
                // Add any additional settings here if needed
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    private SslSettings createSslSettings() {
        try {
            // Load the KeyStore with client certificate
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            Resource keyStoreResource = new ClassPathResource(KEY_STORE_PATH);
            InputStream keyStoreInputStream = keyStoreResource.getInputStream();
            keyStore.load(keyStoreInputStream, KEY_STORE_PASSWORD.toCharArray());
            keyStoreInputStream.close();

            // Create KeyManagerFactory with client certificate
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());

            // Load the TrustStore with CA certificate
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            Resource trustStoreResource = new ClassPathResource(TRUST_STORE_PATH);
            InputStream trustStoreInputStream = trustStoreResource.getInputStream();
            trustStore.load(trustStoreInputStream, TRUST_STORE_PASSWORD.toCharArray());
            trustStoreInputStream.close();

            // Create TrustManagerFactory with CA certificate
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            // Create SSL context and set KeyManager and TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            // Create SslSettings with the SSL context
            return SslSettings.builder()
                    .enabled(true)
                    .context(sslContext)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating SSL context.", e);
        }
    }
}
