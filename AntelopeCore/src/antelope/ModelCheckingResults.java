/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antelope;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author pedrogl
 */
public class ModelCheckingResults implements Serializable {
    
    private long modelBDDCreationTime = 0;
    private long formulaVerificationTime = 0;
    private List<byte[]> labelsArray;

    public long getFormulaVerificationTime() {
        return formulaVerificationTime;
    }

    public void setFormulaVerificationTime(long formulaVerificationTime) {
        this.formulaVerificationTime = formulaVerificationTime;
    }

    public List<byte[]> getLabelsArray() {
        return labelsArray;
    }

    public void setLabelsArray(List<byte[]> labelsArray) {
        this.labelsArray = labelsArray;
    }

    public long getModelBDDCreationTime() {
        return modelBDDCreationTime;
    }

    public void setModelBDDCreationTime(long modelBDDCreationTime) {
        this.modelBDDCreationTime = modelBDDCreationTime;
    }
    
}
