package com.ssotom.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements IFileService{

	@Override
	public Resource get(Path filePath) throws MalformedURLException {
		return new UrlResource(filePath.toUri());
	}

	@Override
	public String upload(MultipartFile file, String filePath, String prefix) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String fileName = prefix + "_" + UUID.randomUUID().toString() 
				+ originalFilename.substring(originalFilename.lastIndexOf("."));
		Path Path = getPath(filePath, fileName);
		Files.copy(file.getInputStream(), Path);
		return fileName;
	}

	@Override
	public void delete(Path filePath) {
		File oldFile = filePath.toFile();
		if(oldFile.exists() && oldFile.canRead()) {
			oldFile.delete();
		}
	}

	@Override
	public Path getPath(String filePath, String fileName) {
		return Paths.get(filePath).resolve(fileName).toAbsolutePath();
	}

}
