package tomcat.run.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import tomcat.conf.WebXmlMountListener;
import tomcat.run.App;
import tomcat.util.TomcatUtil;

public class V2Start implements App {

	@Override
	public void start(Integer port,String hostname,Class<?> c) throws Throwable {
		File root = TomcatUtil.getRootFolder(c);
		System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
		Tomcat tomcat = new Tomcat();
		Path tempPath = Files.createTempDirectory("tomcat-base-dir");
		tomcat.setBaseDir(tempPath.toString());
		tomcat.setPort(port);
		tomcat.setHostname(hostname);
		
		File webContentFolder = new File(root.getAbsolutePath(), "src/main/resources/META-INF/resources/");
		if (!webContentFolder.exists()) {
			webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
		}

		StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
		// Set execution independent of current thread context classloader (compatibility with exec:java mojo)
		ctx.setParentClassLoader(c.getClassLoader());
		// System.out.println("configuring app with basedir: " + webContentFolder.getAbsolutePath());

		// Declare an alternative location for your "WEB-INF/classes" dir Servlet 3.0 annotation will work
		File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "target/classes");
		WebResourceRoot resources = new StandardRoot(ctx);

		WebResourceSet resourceSet;
		if (additionWebInfClassesFolder.exists()) {
			resourceSet = new DirResourceSet(resources, "/WEB-INF/classes",additionWebInfClassesFolder.getAbsolutePath(), "/");
			// System.out.println("loading WEB-INF resources from as '" + additionWebInfClassesFolder.getAbsolutePath() + "'");
		} else {
			resourceSet = new EmptyResourceSet(resources);
		}
		resources.addPreResources(resourceSet);
		ctx.setResources(resources);

		ctx.addLifecycleListener(new WebXmlMountListener());
		
		tomcat.start();
		tomcat.getServer().await();
	
	}

}
