package ru.lanolin.server;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.lanolin.common.Status;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@RestController
@RequestMapping("/control")
public class ControlRestController {

	private final AgentParameters agentParameters;
	private final RSocketService rSocketService;

	private RSocketRequester findRequesterByIp(String ip) {
		return agentParameters.getAgents()
				.stream()
				.filter(AgentConnectionParameter::isAvailable)
				.filter(acp -> acp.getIp().equalsIgnoreCase(ip))
				.map(AgentConnectionParameter::getRequester)
				.filter(Objects::nonNull)
				.findFirst()
				.orElseThrow(() -> new ProviderNotFoundException("not found active connection"));
	}

	@GetMapping("/get/{ip}")
	public List<String> getFiles(@PathVariable String ip) {
		RSocketRequester socketRequester = findRequesterByIp(ip);
		List<String> list = rSocketService.getFileList(socketRequester);
		return list;
	}

	@GetMapping("/download/{ip}/{filename}.{ext}")
	public void downloadFileByIp(@PathVariable String filename,
	                             @PathVariable String ext,
	                             @PathVariable String ip,
	                             HttpServletResponse response) throws IOException {
		RSocketRequester socketRequester = findRequesterByIp(ip);
		InputStream inputStream = rSocketService.downloadFile(socketRequester, filename, ext);
		IOUtils.copy(inputStream, response.getOutputStream());
		response.setContentType(MediaType.MULTIPART_MIXED.getType());
		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "." + ext + "\"");
		response.flushBuffer();
	}

	@PutMapping("/upload/{ip}/{filename}.{ext}")
	public ResponseEntity<String> uploadFileByIp(@PathVariable String filename,
	                                             @PathVariable String ext,
	                                             @PathVariable String ip,
	                                             @RequestParam("file") MultipartFile file) throws IOException {
		RSocketRequester socketRequester = findRequesterByIp(ip);
		Status status = rSocketService.uploadFile(socketRequester, filename, ext, file);
		return ResponseEntity.ok(status.name());
	}

}
