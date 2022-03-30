package ru.lanolin.agent.rsocket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.lanolin.agent.config.AgentParameters;
import ru.lanolin.common.Status;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class FileServiceImpl implements FileService {

	private final AgentParameters agentParameters;
	private final DataBufferFactory dataBufferFactory;

	@Override
	public Flux<Status> uploadFile(Path filename, Flux<DataBuffer> bufferFlux) throws IOException {
		Path opPath = agentParameters.getOwnDir().resolve(filename);
		AsynchronousFileChannel channel =
				AsynchronousFileChannel.open(opPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		return DataBufferUtils.write(bufferFlux, channel)
				.map(b -> Status.CHUNK_COMPLETED);
	}


	@Override
	public Flux<DataBuffer> downloadFile(Path filename) throws IOException {
		Path opPath = agentParameters.getOwnDir().resolve(filename);

		if (!opPath.toFile().exists()) {
			throw new FileNotFoundException("File not found");
		}

		return DataBufferUtils.read(opPath, dataBufferFactory, 4069, StandardOpenOption.READ);
	}

	@Override
	public Flux<String> getFileList() throws IOException {
		Path ownDir = agentParameters.getOwnDir();
		Stream<String> stringStream = Files.list(ownDir)
				.map(Path::getFileName)
				.map(Path::toString);
		return Flux.fromStream(stringStream);
	}

}
