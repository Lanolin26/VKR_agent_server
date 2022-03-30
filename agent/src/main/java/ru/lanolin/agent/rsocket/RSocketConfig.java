package ru.lanolin.agent.rsocket;

import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.MetadataExtractorRegistry;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;
import reactor.util.retry.Retry;
import ru.lanolin.agent.config.AgentParameters;
import ru.lanolin.common.Jackson2JsonDecoderWithEncrypt;
import ru.lanolin.common.Jackson2JsonEncoderWithEncrypt;
import ru.lanolin.common.Metadata;

import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Configuration
public class RSocketConfig {

	private final AgentParameters agentParameters;
	private final Jackson2JsonEncoderWithEncrypt jackson2JsonEncoderWithEncrypt;
	private final Jackson2JsonDecoderWithEncrypt jackson2JsonDecoderWithEncrypt;

	@Bean
	public RSocketStrategies rSocketStrategies() {
		return RSocketStrategies.builder()
				.encoders(this::registerEncoders)
				.decoders(this::registerDecoders)
				.metadataExtractorRegistry(this::registerMetadataExtractor)
				.build();
	}

	private void registerEncoders(List<Encoder<?>> encoders) {
		encoders.add(new Jackson2JsonEncoder());
//		encoders.add(jackson2JsonEncoderWithEncrypt);
	}

	private void registerDecoders(List<Decoder<?>> decoders) {
		decoders.add(new Jackson2JsonDecoder());
//		decoders.add(jackson2JsonDecoderWithEncrypt);
	}

	private void registerMetadataExtractor(MetadataExtractorRegistry metadataExtractorRegistry) {
		metadataExtractorRegistry.metadataToExtract(MimeType.valueOf(Constants.MIME_FILE_EXTENSION), String.class, Constants.FILE_EXTN);
		metadataExtractorRegistry.metadataToExtract(MimeType.valueOf(Constants.MIME_FILE_NAME), String.class, Constants.FILE_NAME);
		metadataExtractorRegistry.metadataToExtract(MimeType.valueOf(Metadata.MIME_TYPE), Metadata.class, Metadata.METADATA_NAME);
	}

	private void rsocketConnector(RSocketConnector rSocketConnector) {
		rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2)));
	}

	@Bean
	public RSocketRequester getRSocketRequester(RSocketRequester.Builder builder) {
		return builder
				.rsocketConnector(this::rsocketConnector)
				.transport(TcpClientTransport.create(agentParameters.getPort()));
	}

	@Bean
	public DataBufferFactory defaultDataBufferFactory() {
		return new DefaultDataBufferFactory();
	}

}
