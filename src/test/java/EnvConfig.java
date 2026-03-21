import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvConfig.class);
    private static final Properties props = new Properties();
    private static final String CONFIG_FILE = "test-config.properties";

    static {
        // Load the file provisioned by Jenkins into src/test/resources
        try (InputStream is = EnvConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                props.load(is);
                logger.info("Successfully loaded configuration from {}", CONFIG_FILE);
            } else {
                logger.warn("{} not found in resources. Falling back to System properties/defaults.", CONFIG_FILE);
            }
        } catch (IOException e) {
            logger.error("Failed to read {}: {}", CONFIG_FILE, e.getMessage());
        }
    }

    /**
     * Priority:
     * 1. System Property (e.g., -Dbase_url from Jenkins/Gradle)
     * 2. Properties File (from Jenkins Managed Files)
     * 3. Hardcoded Default (Local Dev safety net)
     */
    public static String get(String key, String defaultValue) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isEmpty()) {
            return systemProp;
        }
        return props.getProperty(key, defaultValue);
    }

    // Helper methods for your specific Fintech variables
    public static String getBaseUrl() {
        return get("base_url", "http://localhost:8081");
    }

    public static String getApiKey() {
        // Secrets are usually better as Env Vars for security masking in Jenkins
        String key = System.getenv("API_KEY");
        return (key != null) ? key : get("api_key", "default-key");
    }

    public static String getKYCProviderUrl() {
        // Secrets are usually better as Env Vars for security masking in Jenkins
        String key = System.getenv("kyc_provider_url");
        return (key != null) ? key : get("kyc_provider_url", "http://localhost:8081/v1/kyc");
    }
}
