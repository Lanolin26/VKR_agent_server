package ru.lanolin.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayCrLfSerializer;
import org.springframework.messaging.MessageChannel;

@Slf4j
@Configuration
@EnableIntegration
@IntegrationComponentScan
public class TcpServerSocketConfiguration {

	@Value("${agent.port}")
	private int agentPort;

	@Bean
	public AbstractServerConnectionFactory tcpServer() {
		log.info("Starting TCP server with port: {}", agentPort);
		TcpNetServerConnectionFactory serverCf = new TcpNetServerConnectionFactory(agentPort);
		serverCf.setSerializer(new ByteArrayCrLfSerializer());
		serverCf.setDeserializer(new ByteArrayCrLfSerializer());
		serverCf.setSoTcpNoDelay(true);
		serverCf.setSoKeepAlive(true);
		return serverCf;
	}

	@Bean
	public TCPService tcpService() {
		return new TCPService();
	}

	@Bean
	public MessageChannel fromTcp() {
		return new DirectChannel();
	}

	@Bean
	public MessageChannel toTcp() {
		return new DirectChannel();
	}

	@Bean
	public TcpInboundGateway tcpInGate() {
		TcpInboundGateway inGate = new TcpInboundGateway();
		inGate.setConnectionFactory(tcpServer());
		inGate.setRequestChannel(fromTcp());
		inGate.setReplyChannel(toTcp());
		return inGate;
	}

}
