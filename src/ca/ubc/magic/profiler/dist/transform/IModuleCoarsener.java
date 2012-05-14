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
public interface IModuleCoarsener {
    
    public void visitFrameForModuling(JipFrame frame);
    
    public String getFrameModuleName(JipFrame frame);        
    
    public ModuleModel getModuleModelFromParser(JipRun jipRun);
    
    public void setModuleIgnoreSet(Set<CodeEntity> moduleNameSet);
}
