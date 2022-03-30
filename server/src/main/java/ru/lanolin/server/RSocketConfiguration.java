package ru.lanolin.server;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.MetadataExtractorRegistry;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeType;
import reactor.core.publisher.Hooks;
import ru.lanolin.common.Jackson2JsonDecoderWithEncrypt;
import ru.lanolin.common.Jackson2JsonEncoderWithEncrypt;
import ru.lanolin.common.Metadata;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Configuration
public class RSocketConfiguration {

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
		//		decoders.add(jackson2JsonDecoderWithEncrypt);
		decoders.add(new Jackson2JsonDecoder());
	}

	private void registerMetadataExtractor(MetadataExtractorRegistry metadataExtractorRegistry) {
		metadataExtractorRegistry.metadataToExtract(MimeType.valueOf(Metadata.MIME_TYPE), Metadata.class, Metadata.METADATA_NAME);
	}

	@PostConstruct
	public void post() {
		Hooks.onErrorDropped((error) -> {
		});
	}

}
