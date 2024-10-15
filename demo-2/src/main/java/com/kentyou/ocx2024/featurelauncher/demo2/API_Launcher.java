package com.kentyou.ocx2024.featurelauncher.demo2;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.ServiceLoader;

import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.service.featurelauncher.FeatureLauncher;
import org.osgi.service.featurelauncher.repository.ArtifactRepository;

public class API_Launcher {

	public static void main(String[] args) throws Exception {
		
		ServiceLoader<FeatureLauncher> loader = ServiceLoader.load(FeatureLauncher.class);
		FeatureLauncher launcher = loader.findFirst()
			.orElseThrow(() -> new ClassNotFoundException("No Feature Launcher implementation found"));

		Path targetDir = Paths.get("target");
		
		ArtifactRepository repository = launcher.createRepository(targetDir.resolve("repo"));
		
		BufferedReader feature = Files.newBufferedReader(targetDir.resolve("features/gogo.json"));
		Framework launchFramework = launcher.launch(feature)
			.withRepository(repository)
			.withFrameworkProperties(Map.of(Constants.FRAMEWORK_STORAGE_CLEAN,
					Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT))
			.launchFramework();
		
		launchFramework.waitForStop(0);
	}
}
