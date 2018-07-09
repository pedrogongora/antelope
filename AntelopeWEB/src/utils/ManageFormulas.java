package utils;

import java.io.*;

public class ManageFormulas {

    public static final String BASE_PATH = Starter.REAL_PATH;
    public static final String USER_HOME = System.getProperty("user.home");
    public static final String ANTELOPE_HOME = USER_HOME + "/.antelope";
    public static final String serializatedFile = "/formulas.ser";
    public static final String MODEL_PATH = BASE_PATH + "/modelos/";

    public static void save(FormulaArray formulaArray) {
        createAntelopeHomeDir();
        
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            //fos = new FileOutputStream(BASE_PATH + serializatedFile);
            fos = new FileOutputStream(ANTELOPE_HOME + serializatedFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(formulaArray);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static FormulaArray load() {
        FormulaArray formulaArray = null;
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try {
            //fis = new FileInputStream(BASE_PATH + serializatedFile);
            fis = new FileInputStream(ANTELOPE_HOME + serializatedFile);
            in = new ObjectInputStream(fis);
            formulaArray = (FormulaArray) in.readObject();
            in.close();
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
        return formulaArray;
    }
    
    private static void createAntelopeHomeDir() {
        File antelopeTempDir = new File(ANTELOPE_HOME);
        antelopeTempDir.mkdirs();
    }
}
