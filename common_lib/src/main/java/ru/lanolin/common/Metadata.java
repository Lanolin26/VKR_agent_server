package ru.lanolin.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
	public static final String MIME_TYPE = "application/json";
	public static final String METADATA_NAME = "file-info";

	private String filename;
	private String extension;

	public String getFullFileName() {
		return this.filename + "." + this.extension;
	}

}
