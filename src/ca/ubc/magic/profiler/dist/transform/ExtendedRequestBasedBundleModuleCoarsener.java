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
 *The ExtendedRequestBasedBundleModuleCoarsener class ensures that only data 
 * modules are merged and all the other modules remain intact.
 * 
 * 
 * @author nima
 */
public class ExtendedRequestBasedBundleModuleCoarsener extends CoarseRequestBasedBundleModuleCoarsener {
 
    public ExtendedRequestBasedBundleModuleCoarsener(EntityConstraintModel constraintModel){
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
