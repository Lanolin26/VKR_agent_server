package ru.lanolin.common;

import com.codingrodent.jackson.crypto.CryptoModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MimeType;

public class Jackson2JsonEncoderWithEncrypt extends Jackson2JsonEncoder {

	@Autowired
	public Jackson2JsonEncoderWithEncrypt(CryptoModule cryptoModule) {
		this(Jackson2ObjectMapperBuilder.json()
				.modulesToInstall(cryptoModule)
				.build());
	}

	public Jackson2JsonEncoderWithEncrypt(ObjectMapper mapper, MimeType... mimeTypes) {
		super(mapper, mimeTypes);
	}
}
