package ibf2022.batch2.csf.backend.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.models.Archive;
import ibf2022.batch2.csf.backend.models.Bundle;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.services.ArchiveService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@RestController
public class UploadController {

	@Autowired
	private ArchiveService svc;

	@Autowired
	private ArchiveRepository archiveRepo;

	// TODO: Task 2, Task 3, Task 4
	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> uploadArchive(@RequestPart String name, @RequestPart String title,
			@RequestPart(required = false, value = "") String comments, @RequestPart MultipartFile archive) {

		Archive a = new Archive(name, title, comments, archive);
		try {
			String bundleId = svc.uploadArchive(a);

			return ResponseEntity.status(HttpStatus.CREATED)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Json.createObjectBuilder().add("bundleId", bundleId).build().toString());

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(MediaType.APPLICATION_JSON)
					.body(
							Json.createObjectBuilder()
									.add("error", e.getMessage())
									.build().toString());
		}

	}

	// TODO: Task 5
	@GetMapping(path = "/bundle/{bundleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBundleByBundleId(@PathVariable String bundleId) {
		Optional<Bundle> optB = archiveRepo.getBundleByBundleId(bundleId);

		if (optB.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body(
							Json.createObjectBuilder()
									.add("error", "No bundle found with bundle id " + bundleId)
									.build().toString());
		}

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(optB.get().toJson().toString());
	}

	// TODO: Task 6
	@GetMapping(path = "/bundles", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getBundles() {
		Optional<List<Bundle>> optBs = archiveRepo.getBundles();
		if (optBs.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body("[]");
		}

		JsonArrayBuilder ab = Json.createArrayBuilder();
		optBs.get().forEach(b -> ab.add(b.toJson()));

		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(ab.build().toString());
	}
}
