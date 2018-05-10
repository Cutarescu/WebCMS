package web.cms.springmvc.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String a[]) {
		String sDir = "C:/Users/gabri/OneDrive/Documents/GitHub/WebCMS1/src/main/webapp/WEB-INF/Default Files";
		/*
		 * File file = new File(sDir); File[] files = file.listFiles(); for(File
		 * f: files){ System.out.println(f.getName()); }
		 */

		List<Path> fileNames = new ArrayList<Path>();
		Path dir = Paths.get(sDir);
		getFileNames(fileNames, dir);

		for (Path path : fileNames) {
			System.out.println(path.getName(path.getNameCount() - 2)
					+ " - " + path.getParent() + " - "
					+ path.getFileName());
		}
	}

	private static List<Path> getFileNames(List<Path> fileNames, Path dir) {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
			for (Path path : stream) {
				if (path.toFile().isDirectory()) {
					getFileNames(fileNames, path);
				} else {
					fileNames.add(path);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileNames;
	}
}
