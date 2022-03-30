package ru.lanolin.common;

import com.codingrodent.jackson.crypto.Encrypt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
	@Encrypt
	private String id;
	@Encrypt
	private Command command;
	@Encrypt
	private String message;
}
