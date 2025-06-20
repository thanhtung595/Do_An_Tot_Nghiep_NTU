package nguyenthanhtung.datn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MainApplication {

	private static final Logger log = LogManager.getLogger(MainApplication.class);

	public static void main(String[] args) {
		try {
			loadConfigServer();
			SpringApplication.run(MainApplication.class, args);
			log.info("***Start Application***");

			String javaVer = System.getProperty("java.version");
            log.info("Java version: {}", javaVer);

		} catch (Exception e) {
			log.debug(e.getMessage());
			System.exit(16);
		}
	}

//	private static void loadConfigServer() {
//		log.info("***Start LoadConfigServer***");
//		File file = new File("default_env");
//		if (!file.exists()) {
//			log.error("***Config file 'default_env' not found in project root***");
//			System.exit(16);
//		}
//
//		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//			br.lines()
//					.filter(line -> line.contains("="))
//					.forEach(line -> {
//						String[] parts = line.split("=", 2);
//						if (parts.length == 2) {
//							String key = parts[0].trim();
//							String value = parts[1].trim();
//							// Set biến môi trường trong JVM
//							System.setProperty(key, value);
//						}
//					});
//		} catch (Exception e) {
//			log.debug(e.getMessage());
//			System.exit(16);
//		}
//		log.info("***End LoadConfigServer***");
//	}

	private static void loadConfigServer() {
		log.info("***Start LoadConfigServer***");

		List<Config> configs = List.of(
				new Config("AWS_S3_BUCKET_NAME", "ntt-datn-clinic-management"),
				new Config("AWS_REGION", "ap-southeast-1"),
				new Config("AWS_ACCESS_KEY", "AKIAT36VJWQRO7GF2IB4"),
				new Config("AWS_SECRET_KEY", "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"),

				new Config("SECRET_KEY", "6qs0iilasstr+55s/avzaQ8/D/RBJcQKGxMFuBdfwX+Aw="),
				new Config("VALIDITY_INMILLI_SECONDS_ACCESS", "1"),
				new Config("VALIDITY_INMILLI_SECONDS_REFESH", "7"),

				new Config("URL_AWS_STORAGE", "https://ntt-datn-clinic-management.s3.ap-southeast-1.amazonaws.com/"),
				new Config("PATH_AVATAR_DOCTOR", "avatar/doctor/"),
				new Config("PATH_AVATAR_PATIENTS", "avatar/patients/"),
				new Config("PATH_IMG_SERVICE", "img/service/")
		);

		for (Config config : configs) {
			System.setProperty(config.getKey(), config.getValue());
		}

		log.info("***End LoadConfigServer***");
	}


	private static class Config {
		private final String key;
		private final String value;

		public Config(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}
}
