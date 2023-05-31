package ibf2022.batch2.csf.backend.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.models.Archive;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;

@Service
public class ArchiveService {
    
    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private ArchiveRepository archiveRepo;

    public String uploadArchive(Archive a) throws IOException {
        List<String> urls = imageRepo.upload(a.getArchive());
        return archiveRepo.recordBundle(a.getName(), a.getTitle(), a.getComments(), urls);
    }
}
