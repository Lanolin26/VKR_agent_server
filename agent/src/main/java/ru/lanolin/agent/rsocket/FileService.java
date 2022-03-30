package ru.lanolin.agent.rsocket;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import ru.lanolin.common.Status;

import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
	Flux<Status> uploadFile(Path path, Flux<DataBuffer> bufferFlux) throws IOException;

	Flux<DataBuffer> downloadFile(Path filename) throws IOException;

	Flux<String> getFileList() throws IOException;
}
