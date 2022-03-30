package ru.lanolin.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.lanolin.common.YamlPropertySourceFactory;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "server")
@PropertySource(value = "classpath:agents.yaml", factory = YamlPropertySourceFactory.class)
public class AgentParameters {
	private List<AgentConnectionParameter> agents;
}
