package com.cnh.rvcalculatorprocessor.conf;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretConfig {

    private static Logger logger = LoggerFactory.getLogger(SecretConfig.class);

    public String getStoredValue(String keyName){
        logger.info(("******************* Intializing Secret Configuration **********************"));
        String keyVaultName = System.getenv("KEY_VAULT_NAME");
        String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
        SecretClient secretClient = new SecretClientBuilder()
                .vaultUrl(keyVaultUri)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
        KeyVaultSecret storedSecret = secretClient.getSecret("keyName");
        return storedSecret.getValue();
    }
}
