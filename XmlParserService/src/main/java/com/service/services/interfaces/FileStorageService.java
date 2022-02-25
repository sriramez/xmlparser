package com.service.services.interfaces;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	
	public File storeFileToServer(MultipartFile file, String key);
	
	public String getOutputPath(String key);

}
