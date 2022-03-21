package ru.lanolin.server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.*;
import java.util.Properties;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public final class Configuration {
	private static final File CONF_FILE = new File("conf", "server.properties");

	private String serverAddress;
	private String serverPort;
	private String agentPort;
	private String dbURI;
	private String dbUserName;
	private String dbPassword;


	public static Configuration loadProperties() throws IOException {
		if (!CONF_FILE.exists()) {
			throw new FileNotFoundException("File conf/server.properties not found");
		}
		InputStream resource = new FileInputStream(CONF_FILE);
		Properties p = new Properties(defaultValues());
		p.load(resource);
		return new Configuration(
				p.get("serverAddress").toString(),
				p.get("serverPort").toString(),
				p.get("agentPort").toString(),
				p.get("dbURI").toString(),
				p.get("dbUserName").toString(),
				p.get("dbPassword").toString()
		);
	}

	private static Properties defaultValues() {
		Properties defaultProps = new Properties();
		defaultProps.put("serverAddress", "127.0.0.1");
		defaultProps.put("serverPort", "9998");
		defaultProps.put("agentPort", "9999");
		defaultProps.put("dbURI", "");
		defaultProps.put("dbUserName", "");
		defaultProps.put("dbPassword", "");
		return defaultProps;
	}

}
