package ru.lanolin.agent.rsocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import ru.lanolin.agent.config.AgentParameters;
import ru.lanolin.agent.config.CommandNotFoundException;
import ru.lanolin.common.Command;
import ru.lanolin.common.Message;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Controller
public class RSocketInformationController {

	private final AgentParameters agentParameters;

	@MessageMapping("info.ping-pong")
	public Mono<Message> pingPong(@Payload(required = false) Mono<Message> input) {
		return input
				.filter(message -> message.getCommand() == Command.Ping)
				.switchIfEmpty(Mono.error(() -> new CommandNotFoundException("Ping command not found")))
				.thenReturn(Message.builder().id(agentParameters.getName()).command(Command.Pong).build());
	}

	@MessageMapping("info.params")
	public Mono<Message> getParameters(@Payload(required = false) Mono<Message> message) {
		return Mono.just(Message.builder()
				.id(agentParameters.getName())
				.command(Command.Info)
				.build());
	}

}
