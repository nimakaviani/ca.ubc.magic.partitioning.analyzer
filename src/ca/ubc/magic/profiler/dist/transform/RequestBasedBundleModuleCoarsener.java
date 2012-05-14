/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.granularity.CodeEntity;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
import ca.ubc.magic.profiler.dist.transform.model.NodeObj;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nima
 */
public class RequestBasedBundleModuleCoarsener extends ThreadBasedBundleModuleCoarsener {
    
    private Map<NodeObj, String> nodeNameMap = new HashMap<NodeObj, String>();

    public RequestBasedBundleModuleCoarsener(EntityConstraintModel constraintModel){
        super(constraintModel);
    }

    @Override
    protected boolean recursiveWriteNode(NodeObj node, int id) {
        
        if (node == null)
            return true;
        
        Set<NodeObj> childSet = node.getChildSet();
        
        //checks to see if any subnode is remained in the list
        if (childSet == null || childSet.isEmpty()){
           
           // if there is no subnode and this node should be ignored, ignore it
           if (shouldIgnore(node))
               return true;
           
           addNode( node, null, id);
           return false;
        }
        
        // if there are subnodes in the graph assume that they are all going to
        // be ignored unless otherwise said.
        boolean descendentIgnore = true;
        
        // for all descendents check whether or not they should be ignored.
        for (NodeObj childNode : childSet){
           // we && the return value from the descendents with the current
           // decision flag. If it changes to false, the node should not be 
           // ignored.
           boolean descendentIgnoreImmediate = recursiveWriteNode(childNode, id);
           
//           if (descendentIgnoreImmediate){
//               node.add(childNode);
//               node.getChildSet().remove(childNode);
//           }
           
           descendentIgnore =  descendentIgnoreImmediate && descendentIgnore;
           
           // if the node is a replicable node that should be added to the graph, make a 
           // replication of it and add it to the graph.
           if (!(shouldIgnore(node) && descendentIgnore) && isReplicable(node.getName())){
               addNode(node, childNode, id);
           }
        }
          
        // if there is a subnode not ignored, the descendent is not ignored either
        // However it is only added if the node is not marked as replicable
        if (!(shouldIgnore(node) && descendentIgnore) && !isReplicable(node.getName())){
               addNode(node, null, id);
        }
        
        return descendentIgnore;
    }

    protected void addNode(NodeObj node, NodeObj childNode, int id) {
        // otherwise add it to the module map and indicate that it hasn't
        // been ignored by returning false
        String name = getConstrainedNodeName(node, childNode, id);
        
        Module m = mModuleModel.getModuleMap().get(name);
        if (m == null ) {
            m = new Module(name, node.getType());
            m.setExecutionCost(node.getVertexWeight());
            m.setExecutionCount(node.getCount());
            mModuleModel.getModuleMap().put(m.getName(), m);
        }else {
            m.addExecutionCost(node.getVertexWeight());
            m.addExecutionCount(node.getCount());
        }
    }

    @Override
    protected boolean recursiveWriteEdge(NodeObj node, int id){
        
        if (node == null)
            return true;
        
        Set<NodeObj> childSet = node.getChildSet();
        if (childSet == null || childSet.isEmpty()){
            
            if (shouldIgnore(node))
                return true;
            
//            Module m1 = new Module(node.getName()+"_"+node.getId()+"_"+node.getInteractionId());
//            Module m2 = new Module(mEnd.getName());
//            addDataExchange(new ModulePair(m1, m2), 0L , 0L, 0L);	
            return false;
        }
        
        // if there are subnodes in the graph assume that they are all going to
        // be ignored unless otherwise said.
        boolean descendentIgnoreAll = true;
        for (NodeObj childNode : childSet){
            
            boolean descendentIgnore = recursiveWriteEdge(childNode, id);
            
            descendentIgnoreAll = descendentIgnoreAll && descendentIgnore;
            
            if (!descendentIgnore){
                String m1Name = getConstrainedNodeName(node, childNode, id);
                String m2Name = getConstrainedNodeName(childNode, null, id);
                addEdge(m1Name, m2Name, childNode); 
            }
        }
        if (shouldIgnore(node) && descendentIgnoreAll)
            return true;
        
        if (!shouldIgnore(node) && descendentIgnoreAll){
//            Module m1 = new Module(node.getName()+"_"+node.getId()+"_"+node.getInteractionId());
//            Module m2 = new Module(mEnd.getName());
//            addDataExchange(new ModulePair(m1, m2), 0L , 0L, 0L);	
        }
        return false;
    }
    
    protected void addEdge(String m1Name, String m2Name, NodeObj childNode) {
        Module m1 = mModuleModel.getModuleMap().get(m1Name);
        Module m2 = mModuleModel.getModuleMap().get(m2Name);
        addDataExchange(new ModulePair(m1, m2), childNode.getEdge4ParentWeight().longValue(), 
            childNode.getEdge2ParentWeight().longValue(), 
            childNode.getEdge4ParentCount().longValue(),
            childNode.getEdge2ParentCount().longValue());
    }
    
    @Override
    protected void printTree(NodeObj parentNode, int level, StringBuffer b){
        
        Set<NodeObj> childSet = parentNode.getChildSet();
        if (childSet == null || childSet.isEmpty())
                return;
        for (NodeObj childNode : childSet) {
        
            if (childNode == null)
                continue;
            
            getSubTree(parentNode, childNode, level, b);
            Module m = new Module(childNode.getName()+"_"+childNode.getId()+"_"+childNode.getInteractionId(),
                    childNode.getType());
            m.setExecutionCost(childNode.getVertexWeight());
            m.setExecutionCount(childNode.getCount());
            mModuleModel.getModuleMap().put(m.getName(), m);
            
            printTree(childNode, level+1, b);
        }
    }
    
    @Override
    protected void applyRecursion(NodeObj rootNode) {

        int i = 0;
        for (NodeObj child : rootNode.getChildSet()){
            
//            StringBuffer b = new StringBuffer(i + " :: " + child.getName()+ "visit: " + child.getNodeVisit() + "\n");
//            printTree(child, 0, b);
//            System.out.println(b.toString()+"\n\n\n");

            recursiveWriteNode(child, i);
            recursiveWriteEdge(child, i);

//          The following few lines are just debugging code for printing the results of
//          a request-based dependency graph design.
            
//            StringBuffer b = new StringBuffer(i + " :: " + child.getName()+"\n");
//            printTree(child, 0, b);
//            System.out.println(b.toString()+"\n\n\n");

            i++;
        }
    }
    
    protected boolean isNonReplicable(String nodeName){
        return isNodeNameMatchedInEntitySet(mConstraintModel.getNonReplicableSet(), nodeName);
    }
    
    protected boolean isReplicable(String nodeName){
        boolean isReplicable = isNodeNameMatchedInEntitySet(mConstraintModel.getReplicableSet(), nodeName);
        if (isNonReplicable(nodeName) && isReplicable)
            throw new RuntimeException("Node cannot be both replicable and non-replicable: " + nodeName);
        return isReplicable;
    }
    
    protected boolean isNodeNameMatchedInEntitySet(Set<CodeEntity> entitySet, String nodeName){
        for (CodeEntity entity : entitySet)
            if (entity.getEntityPattern().matches(nodeName, null, null))
                return true;
        return false;
    }
    
    protected String getConstrainedNodeName(NodeObj node, NodeObj childNode, int id){
        if (isNonReplicable(node.getName()))
            return node.getName();
        
        if (isReplicable(node.getName())){
            String name;
            if (childNode != null) {
                name = node.getName()+"_"+node.getId()+"_"+node.getInteractionId()+"_"+id+"_"+childNode.hashCode();
                nodeNameMap.put(node, name);
                return name;
            }else {
                name = nodeNameMap.get(node);
                if (name != null)
                    return name;
                return node.getName();
            }
        }
        
        return node.getName()+"_"+node.getId()+"_"+node.getInteractionId()+"_"+id;
    }
}
