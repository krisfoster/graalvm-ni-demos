package oracle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.io.IOException;

/**
 * Represents the configuration of our humble little app
 */
public class Configuration {

    public final boolean humanReadable;

    public Configuration(@JsonProperty("humanReadable") final boolean humanReadable) throws Throwable {
        this.humanReadable = humanReadable;
    }

    public boolean isHumanReadable() {
        return this.humanReadable;
    }

    /**
     * Loads our config from a file on the classpath
     * @return
     * @throws IOException
     */
    public static Configuration loadFromFile() throws IOException {
        URL resourceURL = Configuration.class.getResource("configuration.json");
        System.out.println("URL :: " + resourceURL);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resourceURL, Configuration.class);
    }
}