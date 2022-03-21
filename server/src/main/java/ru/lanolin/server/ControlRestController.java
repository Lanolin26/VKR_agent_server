package ru.lanolin.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control")
public class ControlRestController {

	@GetMapping
	@RequestMapping("/get")
	public String getFiles() {
		return "12345";
	}


	@PutMapping
	@RequestMapping("/upload")
	public String uploadFile() {
		return "";
	}

}
