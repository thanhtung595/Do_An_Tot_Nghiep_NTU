package nguyenthanhtung.datn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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
