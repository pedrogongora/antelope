/*  **************************************************************************
 *
 *  Simple Read-Eval-Print Loop UI.
 *
 *  Author: Pedro A. Góngora <pedro.gongora@gmail.com>
 *
 *  Copyright (C) 2010 Pedro A. Góngora
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *  *************************************************************************/

package antelope.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Set;
import java.util.List;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import antelope.*;
import antelope.booleanmodel.*;
import antelope.ctl.*;
import antelope.ctl.parser.*;

public class SimpleREPL implements ActionListener {
    
    private static Logger log = Logger.getLogger(SimpleREPL.class.getName());
    
    private final static int MAIN_FRAME_WIDTH  = 750;
    private final static int MAIN_FRAME_HEIGHT = 400;
    private final static String FRAME_TITLE = "ANTELOPE";
    private JFrame mainFrame;
    private JFrame captureFrame;
    private JMenuItem quitItem;
    private JMenuItem openItem;
    private JMenuItem captureItem;
    private JMenuItem runItem;
    private JMenuItem aboutItem;
    private JMenuItem clearItem;
    private JMenuItem reloadModelItem;
    private JMenuItem modelInfoItem;
    private JMenuItem convertToAsync1Item;
    private JMenuItem convertToAsync2Item;
    private JTextArea output;
    private JTextArea input;
    private JTextArea captureInput;
    private JButton runButton;
    private JButton loadCapturedModelButton;
    private JButton cancelCaptureButton;
    
    //private Model model;
    private File modelFile;
    private String capturedModel;
    private AntelopeAccess antelopeAccess = new AntelopeAccess();
    
    private JMenuItem catalogueMenuItems[] = {
        new JMenuItem("There is a cycle"),
        new JMenuItem("Stable configurations (cycle of size 1)"),
        new JMenuItem("Membership to a cycle of any size"),
        new JMenuItem("Membership to a cycle of size 2"),
        new JMenuItem("Membership to a cycle of size 3"),
        new JMenuItem("Membership to a cycle of size 4"),
        new JMenuItem("Membership to a cycle of size 4 (exactly)"),
        new JMenuItem("More than one successor"),
        new JMenuItem("Exactly one successor"),
        new JMenuItem("!x.x"),
        new JMenuItem("!x.0"),
        new JMenuItem("true"),
        new JMenuItem("false")
    };
    private String catalogue[] = {
        "]s. @s. EX EF s",
        "!s. AX s",
        "!s. EX EF s",
        "!s. EX EX s",
        "!s. EX EX EX s",
        "!s. EX EX EX EX s",
        "!s. EX EX EX EX s & ~!s. EX EX s",
        "!s. EX !t. @s. EX ~t",
        "!s. EX !t. @s. AX t",
        "!x.x",
        "!x.0",
        "true",
        "false"
    };
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}
        SimpleREPL repl = new SimpleREPL();
    }
    
    /** Creates a new instance of MainWindow */
    public SimpleREPL() {
        // barra de menú
        JMenuBar menuBar = new JMenuBar();
        
        // File
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        // File->New
        captureItem = new JMenuItem("New model", KeyEvent.VK_N);
        captureItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        captureItem.addActionListener(this);
        fileMenu.add(captureItem);
        // File->Open
        openItem = new JMenuItem("Open model", KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openItem.addActionListener(this);
        fileMenu.add(openItem);
        // File->Quit
        fileMenu.addSeparator();
        quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        quitItem.addActionListener(this);
        fileMenu.add(quitItem);
        
        // Run
        JMenu runMenu = new JMenu("Run");
        runMenu.setMnemonic(KeyEvent.VK_R);
        // Run->Run
        runItem = new JMenuItem("Run", KeyEvent.VK_R);
        runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        runItem.addActionListener(this);
        runMenu.add(runItem);
        runMenu.addSeparator();
        // Run->Clear output
        clearItem = new JMenuItem("Clear output panel");
        clearItem.setMnemonic(KeyEvent.VK_C);
        clearItem.addActionListener(this);
        runMenu.add(clearItem);
        
        // Model
        JMenu modelMenu = new JMenu("Model");
        modelMenu.setMnemonic(KeyEvent.VK_M);
        reloadModelItem = new JMenuItem("Reload model");
        modelInfoItem = new JMenuItem("Print model info.");
        convertToAsync1Item = new JMenuItem("Make transitions asynchronous");
        convertToAsync2Item = new JMenuItem("Make transitions strictly asynchronous");
        reloadModelItem.addActionListener(this);
        modelInfoItem.addActionListener(this);
        convertToAsync1Item.addActionListener(this);
        convertToAsync2Item.addActionListener(this);
        modelMenu.add(reloadModelItem);
        modelMenu.add(modelInfoItem);
        modelMenu.add(convertToAsync1Item);
        modelMenu.add(convertToAsync2Item);
        
        // Help
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        // Help->About
        aboutItem = new JMenuItem("About", KeyEvent.VK_B);
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        menuBar.add(modelMenu);
        menuBar.add(buildCatalogueMenu());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);
        
        output = new JTextArea("", 10, 80);
        output.setEditable(false);
        JScrollPane sp_output = new JScrollPane();
        sp_output.setViewportView(output);
        
        input = new JTextArea("", 5, 40);
        input.setEditable(true);
        JScrollPane sp_input = new JScrollPane();
        sp_input.setViewportView(input);
        runButton = new JButton("Run");
        runButton.addActionListener(this);
        JPanel inPanel = new JPanel(new BorderLayout());
        inPanel.add(sp_input, BorderLayout.CENTER);
        inPanel.add(runButton, BorderLayout.EAST);
        
        
        captureInput = new JTextArea("", 10, 80);
        captureInput.setEditable(true);
        JScrollPane sp_capture = new JScrollPane();
        sp_capture.setViewportView(captureInput);
        loadCapturedModelButton = new JButton("Ok");
        cancelCaptureButton = new JButton("Cancel");
        JPanel ok_cancel_panel = new JPanel();
        ok_cancel_panel.add(cancelCaptureButton);
        ok_cancel_panel.add(loadCapturedModelButton);
        loadCapturedModelButton.addActionListener(this);
        cancelCaptureButton.addActionListener(this);
        captureFrame = new JFrame("New Model");
        captureFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        captureFrame.setSize(MAIN_FRAME_WIDTH,  MAIN_FRAME_HEIGHT);
        captureFrame.getContentPane().add(sp_capture, BorderLayout.CENTER);
        captureFrame.getContentPane().add(ok_cancel_panel, BorderLayout.SOUTH);
        captureFrame.setVisible(false);
        
                
        mainFrame = new JFrame(FRAME_TITLE);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MAIN_FRAME_WIDTH,  MAIN_FRAME_HEIGHT);
        mainFrame.setJMenuBar(menuBar);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().add(sp_output, BorderLayout.CENTER);
        mainFrame.getContentPane().add(inPanel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
        
        input.requestFocusInWindow();
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource().equals(quitItem)) {
                System.exit(0);
            } else if (e.getSource().equals(captureItem)) {
                captureFrame.setVisible(false);
                captureFrame.setVisible(true);
                captureFrame.toFront();
            } else if (e.getSource().equals(openItem)) {
                loadModel();
            } else if (e.getSource().equals(cancelCaptureButton)) {
                captureFrame.setVisible(false);
            } else if (e.getSource().equals(loadCapturedModelButton)) {
                loadFromString();
            } else if (e.getSource().equals(runItem) || e.getSource().equals(runButton)) {
                run();
            } else if (e.getSource().equals(aboutItem)) {
                showAboutDialog();
            } else if (e.getSource().equals(clearItem)) {
                output.setText("");
                //input.setText("");
            } else if (e.getSource().equals(reloadModelItem)) {
                reloadModel();
            } else if (e.getSource().equals(modelInfoItem)) {
                modelInfo();
            } else if (e.getSource().equals(convertToAsync1Item)) {
                if (modelFile == null && capturedModel == null) {
                    throw new RuntimeException("Please load a model using File->Open model.");
                }
                antelopeAccess.makeModelAsynchronous();
            } else if (e.getSource().equals(convertToAsync2Item)) {
                if (modelFile == null && capturedModel == null) {
                    throw new RuntimeException("Please load a model using File->Open model.");
                }
                antelopeAccess.makeModelStrictlyAsynchronous();
            } else {
                for (int i=0; i<catalogueMenuItems.length; i++) {
                    if (e.getSource().equals(catalogueMenuItems[i])) input.setText(catalogue[i]);
                }
            }
        } catch (Exception exception) {
            Object options[] = {"Ok"};
            JOptionPane.showOptionDialog(mainFrame, 
                    exception.getMessage(),
                    "Error",
                    JOptionPane.OK_OPTION, 
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]);
            //exception.printStackTrace();
            log.log(Level.WARNING, "Error", exception);
        }
    }
    
    private void loadModel() {
        JFileChooser openDialog = new JFileChooser("Open model");
        openDialog.setMultiSelectionEnabled(false);
        openDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openDialog.setCurrentDirectory(new java.io.File(System.getProperty("user.dir")));
        openDialog.showDialog(mainFrame, "Open");
        modelFile = openDialog.getSelectedFile();
        capturedModel = null;
        if (modelFile != null) {
            loadFile();
        }
    }
    
    private void reloadModel() {
        if (modelFile == null && capturedModel == null) {
            throw new RuntimeException("Please load a model using File->Open model.");
        }
        if (modelFile == null) loadFromString();
        else loadFile();
    }
    
    private void loadFile() {
        try {
            //antelopeAccess.loadTableModel(new FileReader(modelFile));
            antelopeAccess.loadEquationsModel(new FileReader(modelFile));
            output.append("\n");
            output.append(new java.util.Date().toString()+":\n");
            output.append("    Loaded file: " + modelFile +"\n");
            output.append("    " + antelopeAccess.getModelVariables().size() + " variables:\n");
            output.append("        " + antelopeAccess.getModelVariables() +"\n");
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    
    private void loadFromString() {
        capturedModel = captureInput.getText();
        antelopeAccess.loadTableModel(new StringReader(capturedModel));
        modelFile = null;
        output.append("\n");
        output.append(new java.util.Date().toString()+":\n");
        output.append("    Loaded model successfully.\n");
        output.append("    " + antelopeAccess.getModelVariables().size() + " variables:\n");
        output.append("        " + antelopeAccess.getModelVariables() +"\n");
        captureFrame.setVisible(false);
    }
    
    private void run() {
        if (modelFile == null && capturedModel == null) {
            throw new RuntimeException("Please load a model using File->Open model.");
        }
        String formula = input.getText();
        if (formula.trim().equals("")) {
            throw new RuntimeException("Please type a formula or choose one from the Catalogue menu.");
        }
        
        long beginTime = System.currentTimeMillis();
        //Set<Set<String>> labels = antelopeAccess.getLabelsSet(formula);
        List<byte[]> labels = antelopeAccess.getLabelsArray(formula);
        long endTime = System.currentTimeMillis();
        
        //double totalTime = ((double)(endTime - beginTime)) / 1000.0;
        long totalMillis = endTime - beginTime;
        
        output.append("\n");
        output.append(new java.util.Date().toString()+":\n");
        output.append("    Sat(" + formula + ") =" + "\n");
        
        SortedSet<String> ss = antelopeAccess.getModelVariables();

        for(String s : ss){
        	output.append(s + " ");
        }
        output.append("\n");

        for (byte[] o : labels) {
        	for(byte b : o){
        	    if (b == -1) {
        	        output.append("* ");
        	    } else {
            		output.append(b+" ");
            	}
        	}
            output.append("\n");
        }
        output.append("  Execution time: " + totalMillis + " ms\n");
        
        //output.append("Using string labels:\n\t"+antelopeAccess.getLabelsSet(formula)+"\n");
        input.requestFocusInWindow();
    }
    
    private void modelInfo() {
        if (modelFile == null && capturedModel == null) {
            throw new RuntimeException("Please load a model using File->Open model.");
        }
        output.append("\n");
        output.append(new java.util.Date().toString()+":\n");
        output.append("    File: " + modelFile +"\n");
        output.append("    " + antelopeAccess.getModelVariables().size() + " variables:\n");
        output.append("        " + antelopeAccess.getModelVariables() +"\n");
    }
    
    private JMenu buildCatalogueMenu() {
        JMenu menuCatalogue = new JMenu("Catalogue");
        menuCatalogue.setMnemonic(KeyEvent.VK_C);
        for (int i=0; i<catalogueMenuItems.length; i++) {
            menuCatalogue.add(catalogueMenuItems[i]);
            catalogueMenuItems[i].addActionListener(this);
        }
        return menuCatalogue;
    }
    
    private void showAboutDialog() {
        Object options[] = {"Ok"};
        JOptionPane.showOptionDialog(mainFrame, 
                "                      ANTELOPE (Java version)\n"+
                "Pedro Arturo Góngora Luna <pedro.gongora@gmail.com>\n"+
                "     Copyright (C) 2010 Pedro Arturo Góngora",
                "About ANTELOPE",
                JOptionPane.OK_OPTION, 
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
    }
}