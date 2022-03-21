package ru.lanolin.agent;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;
import java.util.Properties;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Configuration {
	private static final File CONF_FILE = new File("conf", "agent.properties");


	private String serverAddress;
	private String serverPort;
	private String pathToSave;
	private String identificator;

	public void setIdentificator(String identificator) {
		this.identificator = identificator;
		saveIdentificator(this);
	}

	private static void saveIdentificator(Configuration configuration) {
		Properties properties = new Properties();
		properties.put("serverAddress", configuration.getServerAddress());
		properties.put("serverPort", configuration.getServerPort());
		properties.put("pathToSave", configuration.getPathToSave());
		properties.put("identificator", configuration.getIdentificator());

		try {
			OutputStream os = new FileOutputStream(CONF_FILE);
			properties.store(os, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Configuration loadProperties() throws IOException {
		if (!CONF_FILE.exists()) {
			throw new FileNotFoundException("File conf/agent.properties not found");
		}
		InputStream resource = new FileInputStream(CONF_FILE);
		Properties p = new Properties(defaultValues());
		p.load(resource);
		return new Configuration(
				p.getOrDefault("serverAddress", "").toString(),
				p.getOrDefault("serverPort", "").toString(),
				p.getOrDefault("pathToSave", "").toString(),
				p.getOrDefault("identificator", "").toString()
		);
	}

	private static Properties defaultValues() {
		Properties defaultProps = new Properties();
		defaultProps.put("serverAddress", "127.0.0.1");
		defaultProps.put("serverPort", "9999");
		defaultProps.put("pathToSave", ".");
		defaultProps.put("identificator", "");
		return defaultProps;
	}

}
