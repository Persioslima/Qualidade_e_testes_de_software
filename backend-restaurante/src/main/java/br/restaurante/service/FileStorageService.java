package br.restaurante.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Service
public class FileStorageService {

	private static final String UPLOADS_ROOT_DIR = "uploads";

	public String saveFile(MultipartFile file, String tipo) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IOException("Arquivo vazio");
		}

		String safeType = sanitizePathSegment(tipo);
		Path destinationDir = Paths.get(UPLOADS_ROOT_DIR, safeType).toAbsolutePath().normalize();
		Files.createDirectories(destinationDir);

		String originalFilename = file.getOriginalFilename();
		String extension = extractExtension(originalFilename);
		String uniqueName = generateUniqueFileName(extension);

		Path destinationFile = destinationDir.resolve(uniqueName);
		Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

		// Retorna URL relativa servida pelo ResourceHandler
		return "/uploads/" + safeType + "/" + uniqueName;
	}

	private String sanitizePathSegment(String input) {
		if (input == null) {
			return "geral";
		}
		String cleaned = input.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9-_]", "-");
		if (cleaned.isBlank()) {
			return "geral";
		}
		return cleaned;
	}

	private String extractExtension(String filename) {
		if (filename == null) {
			return "";
		}
		int dotIdx = filename.lastIndexOf('.');
		if (dotIdx == -1 || dotIdx == filename.length() - 1) {
			return "";
		}
		return filename.substring(dotIdx);
	}

	private String generateUniqueFileName(String extension) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String random = UUID.randomUUID().toString().replace("-", "");
		return timestamp + "_" + random + (extension != null ? extension : "");
	}
}


