package ru.lanolin.server;

import com.codingrodent.jackson.crypto.BaseCryptoContext;
import com.codingrodent.jackson.crypto.CryptoModule;
import com.codingrodent.jackson.crypto.EncryptionService;
import com.codingrodent.jackson.crypto.PasswordCryptoContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lanolin.common.Jackson2JsonDecoderWithEncrypt;
import ru.lanolin.common.Jackson2JsonEncoderWithEncrypt;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public Jackson2JsonDecoderWithEncrypt jackson2JsonDecoderWithEncrypt(CryptoModule cryptoModule) {
		return new Jackson2JsonDecoderWithEncrypt(cryptoModule);
	}

	@Bean
	public Jackson2JsonEncoderWithEncrypt jackson2JsonEncoderWithEncrypt(CryptoModule cryptoModule) {
		return new Jackson2JsonEncoderWithEncrypt(cryptoModule);
	}

	@Bean
	public CryptoModule generateCryptoModule() {
		ObjectMapper objectMapper = new JsonMapper();
		BaseCryptoContext cryptoContext = new PasswordCryptoContext("Password");
		// The encryption service holds functionality to map clear to / from encrypted JSON
		EncryptionService encryptionService = new EncryptionService(objectMapper, cryptoContext);
		// Create a Jackson module and tell it about the encryption service
		return new CryptoModule().addEncryptionService(encryptionService);
	}

}
