/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antelope.embedded;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.ExpandWar;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author pedrogl
 */
public class WebServerLauncher extends AprLifecycleListener implements Runnable {
    
    // war file must be located in the same directory of these class, the
    // project's custom build.xml copies the file from the other project
    private final static String WAR_FILENAME = "AntelopeWEB.war";
    
    private Tomcat tomcat;
    private boolean running = false;
    private LauncherWindow window;
    
    public static void main(String args[]) throws Exception {
        WebServerLauncher s = new WebServerLauncher();
            s.deleteTempDir(); // clean from previous execution attempts
            s.createTempDir();

            String baseDir = s.getAntelopeHome();
            URL warLocation = s.getWarURL(); // new URL("jar:"+this.getClass().getResource(WAR_FILENAME)+"!/");
            System.out.println(warLocation);
            //String warLocation = "jar:file:/home/pedrogl/progs/java/Antelope/antelope/dist/antelope.war!/";

            Integer port = Integer.valueOf(8888);

            s.tomcat = new Tomcat();
            s.tomcat.setPort(port);
            s.tomcat.setBaseDir(baseDir);
            String appBase = ExpandWar.expand(s.tomcat.getHost(), warLocation, "/AntelopeWEB");
            s.tomcat.getHost().setAppBase(appBase);

            String contextPath = "";

            // Add AprLifecycleListener
            StandardServer server = (StandardServer) s.tomcat.getServer();
            //AprLifecycleListener listener = new AprLifecycleListener();
            //server.addLifecycleListener(this);

            s.tomcat.addWebapp(contextPath, appBase);
            s.tomcat.start();
            //running = true;
            s.tomcat.getServer().await();
    }
    
    public WebServerLauncher() {
        super();
    }

    public void setWindow(LauncherWindow window) {
        this.window = window;
    }

    @Override
    public void run() {
        try {
            deleteTempDir(); // clean from previous execution attempts
            createTempDir();

            String baseDir = getAntelopeHome();
            URL warLocation = getWarURL(); // new URL("jar:"+this.getClass().getResource(WAR_FILENAME)+"!/");
            System.out.println(warLocation);
            //String warLocation = "jar:file:/home/pedrogl/progs/java/Antelope/antelope/dist/antelope.war!/";

            Integer port = Integer.valueOf(8888);

            tomcat = new Tomcat();
            tomcat.setPort(port);
            tomcat.setBaseDir(baseDir);
            String appBase = ExpandWar.expand(tomcat.getHost(), warLocation, "/AntelopeWEB");
            tomcat.getHost().setAppBase(appBase);

            String contextPath = "";

            // Add AprLifecycleListener
            StandardServer server = (StandardServer) tomcat.getServer();
            //AprLifecycleListener listener = new AprLifecycleListener();
            server.addLifecycleListener(this);

            tomcat.addWebapp(contextPath, appBase);
            tomcat.start();
            running = true;
            tomcat.getServer().await();
            //tomcat.stop();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (event.getType().equals("after_start")) {
            running = true;
            window.notifyServerStarted();
        } else if (event.getType().equals("after_stop")) {
            running = false;
            window.notifyServerStopped();
        }
        super.lifecycleEvent(event);
    }
    
    protected void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
            running = false;
        } catch (LifecycleException ex) {
            throw new RuntimeException(ex);
        }
        deleteTempDir();
    }
    
    private URL getWarURL() throws Exception {
        String warFilename = this.getClass().getResource(WAR_FILENAME).toString();
        
        if (warFilename.startsWith("jar:")) {
            // URL doesn't support nested jars, then we must extract the
            // file to a temp dir
            InputStream in = this.getClass().getResourceAsStream(WAR_FILENAME);
            warFilename = getAntelopeHome() + "/" + WAR_FILENAME;
            FileOutputStream out = new FileOutputStream(warFilename);
            int b = in.read();
            while (b != -1) {
                out.write(b);
                b = in.read();
            }
            in.close();
            out.close();
            warFilename = "file:" + warFilename;
        }
        
        return new URL("jar:" + warFilename + "!/");
    }
    
    private String getAntelopeHome() {
        return System.getProperty("user.home") + "/.antelope";
    }
    
    private void createTempDir() {
        String userHome = System.getProperty("user.home");
        File antelopeTempDir = new File(userHome + "/.antelope/webapps");
        antelopeTempDir.mkdirs();
    }
    
    private void deleteTempDir() {
        String userHome = System.getProperty("user.home");
        File antelopeTempDir = new File(userHome + "/.antelope/webapps");
        FileUtils.deleteQuietly(antelopeTempDir);
        antelopeTempDir = new File(userHome + "/.antelope/work");
        FileUtils.deleteQuietly(antelopeTempDir);
        antelopeTempDir = new File(userHome + "/.antelope/AntelopeWEB.war");
        FileUtils.deleteQuietly(antelopeTempDir);
    }
}
