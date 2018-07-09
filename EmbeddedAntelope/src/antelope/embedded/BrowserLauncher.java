/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antelope.embedded;

import java.util.Arrays;

public class BrowserLauncher {

    /**
     * System-independent web browser launcher.
     * Based on:  <a href="http://www.centerkey.com/java/browser/">www.centerkey.com/java/browser</a><br>
     */
    static final String[] browsers = {"gnome-open", "firefox", "google-chrome", "opera",
        "epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla"};
    static final String errMsg = "Error attempting to launch web browser";

    public static void openURL(String url) {
        try {  //attempt to use Desktop library from JDK 1.6+
            Class<?> d = Class.forName("java.awt.Desktop");
            d.getDeclaredMethod("browse", new Class[]{java.net.URI.class}).invoke(
                    d.getDeclaredMethod("getDesktop").invoke(null),
                    new Object[]{java.net.URI.create(url)});
            //above code mimicks:  java.awt.Desktop.getDesktop().browse()
        } catch (Exception ignore) {  //library not available or failed
            try {
                String osName = System.getProperty("os.name");
                if (osName.startsWith("Mac OS")) {
                    Class.forName("com.apple.eio.FileManager").getDeclaredMethod(
                            "openURL", new Class[]{String.class}).invoke(null,
                            new Object[]{url});
                } else if (osName.startsWith("Windows")) {
                    Runtime.getRuntime().exec(
                            "rundll32 url.dll,FileProtocolHandler " + url);
                } else { //assume Unix or Linux
                    String browser = null;
                    for (String b : browsers) {
                        if (browser == null && Runtime.getRuntime().exec(new String[]{"which", b}).getInputStream().read() != -1) {
                            Runtime.getRuntime().exec(new String[]{browser = b, url});
                        }
                    }
                    if (browser == null) {
                        throw new Exception("Attempted: " + Arrays.toString(browsers));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(errMsg, e);
            }
        }
    }
}
