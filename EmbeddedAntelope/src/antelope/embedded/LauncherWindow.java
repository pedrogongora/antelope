/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antelope.embedded;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author pedrogl
 */
public class LauncherWindow implements ActionListener {
    
    private final static int MAIN_FRAME_WIDTH  = 285;
    private final static int MAIN_FRAME_HEIGHT = 330;
    private final static String FRAME_TITLE = "ANTELOPE";
    private final static String ANTELOPE_URL = "http://localhost:8888/";
    private final static String START_TOMCAT = "Start local web server";
    private final static String STARTING_TOMCAT = "Starting local web server ...";
    private final static String STOP_TOMCAT = "Stop local web server";
    private final static String STOPPING_TOMCAT = "Stopping local web server ...";
    private JFrame mainFrame;
    JButton launchButton;
    
    WebServerLauncher server ;
    private boolean serverRunning = false;

    public LauncherWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignore) {}
        
        
        mainFrame = new JFrame(FRAME_TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MAIN_FRAME_WIDTH,  MAIN_FRAME_HEIGHT);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Welcome to Antelope", SwingConstants.CENTER);
        title.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.BOLD, 18));
        mainFrame.add(title, BorderLayout.NORTH);
        
        JLabel logo = new JLabel(new ImageIcon(this.getClass().getResource("antelope.jpg")));
        mainFrame.add(logo, BorderLayout.CENTER);
        
        launchButton = new JButton(START_TOMCAT);
        launchButton.addActionListener(this);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(launchButton, BorderLayout.CENTER);
        mainFrame.add(panel1, BorderLayout.SOUTH);
        
        mainFrame.setVisible(true);
    }
    
    protected void notifyServerStarted() {
        launchButton.setText(STOP_TOMCAT);
        launchButton.setEnabled(true);
        BrowserLauncher.openURL(ANTELOPE_URL);
    }
    
    protected void notifyServerStopped() {
        launchButton.setText(START_TOMCAT);
        launchButton.setEnabled(true);
        serverRunning = false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(launchButton)) {
            if (!serverRunning) {
                launchButton.setText(STARTING_TOMCAT);
                launchButton.setEnabled(false);
                serverRunning = true;
                server = new WebServerLauncher();
                server.setWindow(this);
                new Thread(server).start();
            } else {
                launchButton.setText(STOPPING_TOMCAT);
                launchButton.setEnabled(false);
                server.stop();
            }
        }
    }
    
}
