/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
import ca.ubc.magic.profiler.dist.transform.model.NodeObj;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nima
 */
public class LowLatencyExtendedRequestBasedBundleModuleCoarsener extends LowLatencyCoarseRequestBasedBundleModuleCoarsener {
    
    public LowLatencyExtendedRequestBasedBundleModuleCoarsener(EntityConstraintModel constraintModel){
        super(constraintModel);
    }
    
    @Override
    ExtendedNodeObj mergeNodes(ExtendedNodeObj root){
                       
        if (mIsDataRoot)
            return super.mergeNodes(root);
        else if (root.mNodetype == NodeType.DATA)
            return super.mergeNodes(root);
        else
            return mergeWebNodes(root);
    }
    
    private ExtendedNodeObj mergeWebNodes(ExtendedNodeObj root){
        if (!Constants.WEB_NODES.contains(root.getName()))
            return root;
        Set<NodeObj> tmpSet = new HashSet<NodeObj>(root.getChildSet());
        for (NodeObj childObj : tmpSet)
            if (Constants.WEB_NODES.contains(childObj.getName()))
                root.merge((ExtendedNodeObj) childObj);
        return root;
    }
}
