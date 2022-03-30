package ru.lanolin.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentConnectionParameter {
	private String ip;
	private int port;
	private boolean available;
	private RSocketRequester requester;
}
