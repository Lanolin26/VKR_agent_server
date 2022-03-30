package ru.lanolin.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.lanolin.common.YamlPropertySourceFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

@Data
@Component
@ConfigurationProperties(prefix = "agent")
@PropertySource(value = "classpath:agent.yaml", factory = YamlPropertySourceFactory.class)
public class AgentParameters {
	private String ip;
	private int port;
	private Path ownDir;
	private String name;

	@PostConstruct
	private void postConstruct() throws UnknownHostException {
		initHostName();
		cleanWorkDir();
	}

	private void initHostName() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		this.name = localHost.getCanonicalHostName();
	}

	private void cleanWorkDir() {
		File file = this.getOwnDir().toFile();
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files != null) {
				for (File listFile : files) {
					listFile.delete();
				}
			}
		} else {
			file.mkdir();
		}
	}

}

