package tomcat.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public abstract class TomcatUtil {

	public static File createTempDir(String prefix, int port) throws IOException {
		File tempDir = File.createTempFile(prefix + ".", "." + port);
		tempDir.delete();
		tempDir.mkdir();
		tempDir.deleteOnExit();
		return tempDir;
	}
	
	public static File getRootFolder(Class<?> c) {
		try {
			File root;
			String runningJarPath = c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
			int lastIndexOf = runningJarPath.lastIndexOf("/target/");
			if (lastIndexOf < 0) {
				root = new File("");
			} else {
				root = new File(runningJarPath.substring(0, lastIndexOf));
			}
			//System.out.println("application resolved root folder: " + root.getAbsolutePath());
			return root;
		} catch (URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}
}
