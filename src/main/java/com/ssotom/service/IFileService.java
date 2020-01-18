package com.ssotom.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
	
	public Resource get(Path filePath) throws MalformedURLException;
	
	public String upload(MultipartFile file, String filePath, String prefix) throws IOException;
	
	public void delete(Path filePath);
	
	public Path getPath(String filePath, String fileName);
}
