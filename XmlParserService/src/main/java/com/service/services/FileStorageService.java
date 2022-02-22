package com.service.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	@Value("${xmlservice.storage.path}")
	private String storagePath;

	public File storeFileToServer(MultipartFile file, String key) {
		File convertFile = new File(storagePath + File.separator + key + File.separator + file.getOriginalFilename());
		try {
			if (!convertFile.getParentFile().exists()) {
				convertFile.getParentFile().mkdirs();
			}
			convertFile.createNewFile();
			FileOutputStream fout = new FileOutputStream(convertFile);
			fout.write(file.getBytes());
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return convertFile;
	}

	public String getOutputPath(String key) {
		return storagePath + File.separator + key + File.separator;
	}

}
