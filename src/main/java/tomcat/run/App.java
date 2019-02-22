package tomcat.run;

public interface App {
	
	public void start(Integer port,String hostname,Class<?> c) throws Throwable;
	
}