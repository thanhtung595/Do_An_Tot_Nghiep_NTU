package nguyenthanhtung.datn.util.FileUtils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompressor {

    public static MultipartFile compressImage(MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image/")) {
            return file;
        }

        long maxSize = 50 * 1024; // 50KB

        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        double quality = 1.0;
        double scale = 1.0;
        byte[] imageBytes;

        do {
            outputStream.reset(); // xóa dữ liệu cũ

            Thumbnails.of(inputStream)
                    .scale(scale) // giữ nguyên kích thước ảnh
                    .outputQuality(quality) // giảm chất lượng ảnh
                    .toOutputStream(outputStream); // ghi ảnh nén vào outputStream

            imageBytes = outputStream.toByteArray(); // chuyển output thành byte[]

            quality -= 0.1; // giảm 10% chất lượng mỗi vòng
            scale -= 0.1; // giảm kích thước ảnh
            inputStream.close();
            inputStream = file.getInputStream(); // reset lại InputStream vì nó chỉ đọc 1 lần
        } while (imageBytes.length > maxSize && quality > 0.1);

        return new MockMultipartFile(
                file.getName(),                // field name
                file.getOriginalFilename(),    // original file name
                file.getContentType(),         // content type
                imageBytes                     // nội dung
        );
    }
}
