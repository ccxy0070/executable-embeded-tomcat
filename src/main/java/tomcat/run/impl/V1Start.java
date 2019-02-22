package tomcat.run.impl;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.Tomcat;

import tomcat.conf.EmbededContextConfig;
import tomcat.conf.EmbededStandardJarScanner;
import tomcat.conf.WebXmlMountListener;
import tomcat.run.App;
import tomcat.util.TomcatUtil;

public class V1Start implements App {

	@Override
	public void start(Integer port,String hostname,Class<?> c) throws Throwable {
		String contextPath = "";

		String tomcatBaseDir = TomcatUtil.createTempDir("tomcat", port).getAbsolutePath();
		String contextDocBase = TomcatUtil.createTempDir("tomcat-docBase", port).getAbsolutePath();

		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(tomcatBaseDir);

		tomcat.setPort(port);
		tomcat.setHostname(hostname);

		Host host = tomcat.getHost();
		LifecycleListener conf = new EmbededContextConfig();
		// EmbededContextConfig conf = new EmbededContextConfig();
		
		Context context = tomcat.addWebapp(host, contextPath, contextDocBase, conf);

		context.setJarScanner(new EmbededStandardJarScanner());
		// context.setJarScanner(new StandardJarScanner());

		ClassLoader classLoader = c.getClassLoader();
		context.setParentClassLoader(classLoader);

		// context load WEB-INF/web.xml from classpath
		context.addLifecycleListener(new WebXmlMountListener());
		// 加载context.xml文件，里面可以定义一些资源，包括数据库等
		// context.setConfigFile(new File(baseDir + File.separator + "META-INF\\context.xml").toURI().toURL());

		// 收到请求时的解析编码
		tomcat.getConnector().setURIEncoding("utf-8");
		// 启动tomcat
		tomcat.start();
		// 挂起主线程，tomcat就会一直在后台运行，直到收到shutdown信号
		tomcat.getServer().await();
	}

}
