package ru.lanolin.agent.rsocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lanolin.common.Metadata;
import ru.lanolin.common.Status;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Controller
public class RSocketFileOperationController {

	private final FileService fileService;

	@MessageMapping("file.list")
	public Flux<String> getAvailableFile() throws IOException {
		return fileService.getFileList();
	}

	@MessageMapping("file.upload")
	public Flux<Status> uploadFile(@Headers Map<String, Object> metadata, @Payload Flux<DataBuffer> content)
			throws IOException {
		Path filename = extractFullFilenameOnMetadata(metadata);
		Flux<Status> uploadFileStatus = fileService.uploadFile(filename, content);
		return Flux.concat(uploadFileStatus, Mono.just(Status.COMPLETED)).onErrorReturn(Status.FAILED);
	}

	@MessageMapping("file.download")
	public Flux<DataBuffer> downloadFile(@Headers Map<String, Object> metadata) throws IOException {
		Path filename = extractFullFilenameOnMetadata(metadata);
		return fileService.downloadFile(filename);
	}

	@MessageExceptionHandler
	public Mono<Void> exceptionHandler(Exception exception) {
		return Mono.error(exception);
	}

	private Path extractFullFilenameOnMetadata(Map<String, Object> metadata) {
		Metadata filenameMetadata = (Metadata) metadata.get("file-info");
		Path path;
		if (Objects.nonNull(filenameMetadata)) {
			path = Paths.get(filenameMetadata.getFullFileName());
		} else {
			String fileName = (String) metadata.get(Constants.FILE_NAME);
			String fileExtn = (String) metadata.get(Constants.FILE_EXTN);
			path = Paths.get(fileName + "." + fileExtn);
		}
		return path;
	}

}
