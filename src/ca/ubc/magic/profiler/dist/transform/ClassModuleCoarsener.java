/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.granularity.CodeEntity;
import ca.ubc.magic.profiler.parser.JipFrame;
import ca.ubc.magic.profiler.parser.JipRun;
import java.util.Set;

/**
 *
 * @author nima
 */
public class ClassModuleCoarsener implements IModuleCoarsener {

    public void setModuleIgnoreSet(Set<CodeEntity> moduleNameSet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ModuleModel getModuleModelFromParser(JipRun jipRun) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getFrameModuleName(JipFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ModuleModel getModuleModel(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void visitFrameForModuling(JipFrame frame) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
