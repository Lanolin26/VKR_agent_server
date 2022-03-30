package ru.lanolin.server;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.multipart.MultipartFile;
import ru.lanolin.common.Status;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface RSocketService {

	Status uploadFile(RSocketRequester socketRequester, String filename, String ext, MultipartFile file)
			throws IOException;

	InputStream downloadFile(RSocketRequester socketRequester, String filename, String ext) throws IOException;

	List<String> getFileList(RSocketRequester socketRequester);
}
