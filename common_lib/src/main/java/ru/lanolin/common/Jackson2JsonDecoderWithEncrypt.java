package ru.lanolin.common;

import com.codingrodent.jackson.crypto.CryptoModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MimeType;

public class Jackson2JsonDecoderWithEncrypt extends Jackson2JsonDecoder {

	@Autowired
	public Jackson2JsonDecoderWithEncrypt(CryptoModule cryptoModule) {
		this(Jackson2ObjectMapperBuilder.json()
				.modulesToInstall(cryptoModule)
				.build());
	}

	public Jackson2JsonDecoderWithEncrypt(ObjectMapper mapper, MimeType... mimeTypes) {
		super(mapper, mimeTypes);
	}
}
