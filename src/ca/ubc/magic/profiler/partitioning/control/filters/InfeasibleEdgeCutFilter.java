/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.filters;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.ModulePairHostPair;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;
import ca.ubc.magic.profiler.dist.transform.IInteractionFilter;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 *
 * @author nima
 */
public class InfeasibleEdgeCutFilter implements IInteractionFilter {
   
    public static final String COMMA_CHAR = ",";
    public static final String MODULE_PAIR_BOUNDARY_CHAR = "{";
    public static final String MODULE_PAIR_BOUNDARY_CHAR_2 = "}";
    public static final String MODULE_PAIR_DELIMITER = ":";
    public static final String SPACE_CHAR = " ";
   
    Set<ModulePair> mModulePairSet;
    
    String mName;
    
    public InfeasibleEdgeCutFilter (ModuleModel moduleModel, String name, String[][] mpstr){
        mName = name;
        initFilter(moduleModel, mpstr);
    }
    
    public boolean isFilterable(ModulePairHostPair mph) {
        for (ModulePair mp : mModulePairSet){
            if (mph.getModulePair().equals(mp)){
                return true;
            }
        }
        return false;
    }
    
    public double filter(ModulePairHostPair mph){
        return Constants.INFINITE_WEIGHT;
    }
    
    public Set getFilterSet() {
        return mModulePairSet;
    }
    
    public void setFilterName(String name){
        mName = name;
    }
    
    public String getFilterName(){
        return mName;
    }
    
    public String getFilterAsString() {
        StringBuilder strBldr = new StringBuilder();
        for (ModulePair mp : mModulePairSet){
            String[] strArry = new String[] {mp.getModules()[0].getName(), mp.getModules()[1].getName()};
            strBldr.append(MODULE_PAIR_BOUNDARY_CHAR).append(
                    StringUtils.arrayToDelimitedString(strArry, MODULE_PAIR_DELIMITER)).append(
                    MODULE_PAIR_BOUNDARY_CHAR_2).append(COMMA_CHAR);
        }
        return strBldr.toString().trim().replaceAll(COMMA_CHAR, COMMA_CHAR + SPACE_CHAR);
    }

    public void setStringToFilter(ModuleModel moduleModel, HostModel hostModel, String stringFilter) {
        stringFilter = stringFilter.replaceAll( COMMA_CHAR + SPACE_CHAR, COMMA_CHAR);
        String[] pairStr = StringUtils.commaDelimitedListToStringArray(stringFilter);        
        String[][] modulePairNames = new String[pairStr.length][2];
        int index = 0;
        for (String str : pairStr){
            if (str == null || str.equals(""))
                continue;
            String parsedPairStr = str.substring(1, str.length() - 1);
            modulePairNames[index++] = StringUtils.delimitedListToStringArray(
                    parsedPairStr, MODULE_PAIR_DELIMITER);
        }
        initFilter(moduleModel, modulePairNames);
    }
    
    protected void initFilter(ModuleModel moduleModel, String[][] modulePairNames){
        if (moduleModel == null)
            throw new RuntimeException("Module model is not defined");
        if (modulePairNames == null)
            throw new RuntimeException("Pair names for the filter is not available");
        
        mModulePairSet = new HashSet<ModulePair>();
        
        for (String[] modulePairName :modulePairNames){
            for (ModulePair mp : moduleModel.getModuleExchangeMap().keySet())
                if ((mp.getModules()[0].getName().equals(modulePairName[0]) &&
                        mp.getModules()[1].getName().equals(modulePairName[1])) ||
                    (mp.getModules()[0].getName().equals(modulePairName[1]) &&
                        mp.getModules()[1].getName().equals(modulePairName[0])))
                    mModulePairSet.add(mp);
        }
    }
}
