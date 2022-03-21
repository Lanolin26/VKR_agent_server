package ru.lanolin.server;

import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import ru.lanolin.common.Command;

import java.nio.charset.StandardCharsets;

@Component
@MessageEndpoint
public class TCPService {

	@ServiceActivator(inputChannel = "fromTcp")
	public byte[] handleMessage(byte[] input) {
		return "".getBytes(StandardCharsets.UTF_8);
	}

	@InboundChannelAdapter(channel = "toTcp", poller = @Poller(fixedDelay = "25000"))
	public byte[] pingPong() {
		return Command.Ping.toString().getBytes(StandardCharsets.UTF_8);
	}

}
