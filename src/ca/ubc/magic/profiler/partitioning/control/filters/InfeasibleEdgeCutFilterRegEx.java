/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.filters;

import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import java.util.HashSet;

/**
 *
 * @author nima
 */
public class InfeasibleEdgeCutFilterRegEx extends InfeasibleEdgeCutFilter {

    public InfeasibleEdgeCutFilterRegEx(ModuleModel moduleModel, String name, String[][] mpstr) {
        super(moduleModel, name, mpstr);
    }
    
    @Override
    protected void initFilter(ModuleModel moduleModel, String[][] modulePairNames){
        if (moduleModel == null)
            throw new RuntimeException("Module model is not defined");
        if (modulePairNames == null)
            throw new RuntimeException("Pair names for the filter is not available");
        
        mModulePairSet = new HashSet<ModulePair>();
        
        for (String[] modulePairName :modulePairNames){
            for (ModulePair mp : moduleModel.getModuleExchangeMap().keySet())
                if ((mp.getModules()[0].getName().contains(modulePairName[0]) &&
                        mp.getModules()[1].getName().contains(modulePairName[1])) ||
                    (mp.getModules()[0].getName().contains(modulePairName[1]) &&
                        mp.getModules()[1].getName().contains(modulePairName[0])))
                    mModulePairSet.add(mp);
        }
    }
}
