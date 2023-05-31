package ibf2022.batch2.csf.backend.repositories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import static ibf2022.batch2.csf.backend.util.Util.*;

@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3;

	private static final String bucketName = "csfb2";
	private static final String domainName = "legendory.com";

	// TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public List<String> upload(MultipartFile zip) throws IOException {

		List<String> urls = new LinkedList<>();

		InputStream is = zip.getInputStream();
		ZipInputStream zis = new ZipInputStream(zip.getInputStream());

		ZipEntry entry = zis.getNextEntry();
		while (entry != null) {
			if (!entry.isDirectory()) {
				// files should not be a directory but just in case
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int length;

				while ((length = zis.read(buffer)) > 0) {
					baos.write(buffer, 0, length);
				}

				byte[] fileBytes = baos.toByteArray();
				ByteArrayInputStream fileBais = new ByteArrayInputStream(fileBytes);

				urls.add(uploadSingleFile(entry, fileBais));

				fileBais.close();
				baos.close();
			}

			zis.closeEntry();
			entry = zis.getNextEntry();
		}

		zis.close();
		is.close();

		return urls;
	}

	private String uploadSingleFile(ZipEntry entry, InputStream file) throws IOException {
		// extract filename and content type
		String filename = entry.getName().substring(0, entry.getName().lastIndexOf("."));
		String fileType = entry.getName().substring(entry.getName().lastIndexOf(".") + 1);

		// extract filesize
		Long fileSize = entry.getSize();

		// generate unique key
		String key = generateFullKey(generateUUID());

		// generate metadata
		Map<String, String> ud = new HashMap<>();
		ud.put("filename", filename);
		ud.put("upload-date", LocalDateTime.now().toString());

		ObjectMetadata md = new ObjectMetadata();
		md.setContentType(fileType);
		md.setContentLength(fileSize);
		md.setUserMetadata(ud);

		// upload
		PutObjectRequest putReq = new PutObjectRequest(bucketName, key, file, md);
		putReq.withCannedAcl(CannedAccessControlList.PublicRead);

		try {
			s3.putObject(putReq);
		} catch (AmazonServiceException e) {
			throw new IOException("Error encountered uploading to database.");
		}

		return generateR2Url(key);
	}

	// this method is required because cloudflare R2 does not support public bucket URL retrieval using S3 API.  
	// https://community.cloudflare.com/t/get-public-bucket-url-using-boto3/470008
	private String generateR2Url(String fullKey) {
		return "https://%s.%s/%s".formatted(bucketName, domainName, fullKey);
	}

	private String generateFullKey(String key) {
		return "images/%s".formatted(key);
	}
}
