package launch;

import tomcat.run.impl.V1Start;
import tomcat.run.impl.V2Start;

public class AppMain {

	private final static Integer PORT = 8080;
	private final static String HOSTHOME = "localhost";
	
	public static void main(String[] args) throws Throwable {
		//new V1Start().start(PORT,HOSTHOME,AppMain.class);
		new V2Start().start(PORT,HOSTHOME,AppMain.class);
	}
		
}
