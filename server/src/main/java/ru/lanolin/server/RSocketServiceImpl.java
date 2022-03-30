package ru.lanolin.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.lanolin.common.Metadata;
import ru.lanolin.common.Status;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.List;

@Service
@Slf4j
public class RSocketServiceImpl implements RSocketService {

	@Override
	public Status uploadFile(RSocketRequester socketRequester,
	                         String filename,
	                         String ext,
	                         MultipartFile file) {

		Flux<DataBuffer> fileBuffer =
				DataBufferUtils.readInputStream(file::getInputStream, new DefaultDataBufferFactory(), 4096);

		Status finallstatus = socketRequester.route("file.upload")
				.metadata(new Metadata(filename, ext), MimeTypeUtils.APPLICATION_JSON)
				.data(fileBuffer)
				.retrieveFlux(Status.class)
				.doOnNext(status -> log.info("Upload status: {}", status))
				.blockLast();

		return finallstatus;
	}

	@Override
	public InputStream downloadFile(RSocketRequester socketRequester, String filename, String ext) throws IOException {
		Mono<InputStream> reduce = socketRequester.route("file.download")
				.metadata(new Metadata(filename, ext), MimeTypeUtils.APPLICATION_JSON)
				.retrieveFlux(DataBuffer.class)
				.doOnError(error -> {
				})
				.doFinally(signalType -> {
				})
				.map(body -> body.asInputStream(true))
				.reduce(SequenceInputStream::new);

		return reduce.block();
	}

	@Override
	public List<String> getFileList(RSocketRequester socketRequester) {
		List<String> block = socketRequester.route("file.list")
				.retrieveFlux(String.class)
				.collectList()
				.block();
		return block;
	}
}
