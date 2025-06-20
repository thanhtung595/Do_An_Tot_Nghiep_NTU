package nguyenthanhtung.datn.util.S3_Service;

import lombok.RequiredArgsConstructor;
import nguyenthanhtung.datn.util.FileUtils.ImageCompressor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileS3Service {

    private final S3Client s3Client;
    private final AwsS3Properties awsS3Properties;

    public String uploadFile(MultipartFile fileOriginal, String newFileName, String path) throws IOException {

        MultipartFile file = ImageCompressor.compressImage(fileOriginal);

        // Lấy content type gốc từ file
        String contentType = file.getContentType();

        // Nếu contentType không rõ ràng, fallback về application/octet-stream
        if (contentType == null || contentType.isBlank()) {
            contentType = "application/octet-stream";
        }

        // Lấy phần mở rộng file từ tên gốc
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        newFileName = newFileName + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsS3Properties.getS3BucketName())
                .key(path + newFileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return newFileName;
    }
}
