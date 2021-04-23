package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file-location.properties")
public class FileLocationConfig {

	@Value("${file_path}")
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

//	public void setFilePath(String filePath) {
//		this.filePath = filePath;
//	}

}
