package nguyenthanhtung.datn.util.S3_Service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsS3Properties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String s3BucketName;
}
