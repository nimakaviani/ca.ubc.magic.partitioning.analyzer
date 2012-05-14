/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.granularity.CodeEntity;
import ca.ubc.magic.profiler.dist.model.granularity.CodeUnitType;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
import ca.ubc.magic.profiler.dist.transform.model.NodeObj;
import ca.ubc.magic.profiler.parser.JipFrame;
import ca.ubc.magic.profiler.parser.JipRun;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nima
 */
public class ThreadBasedBundleModuleCoarsener extends BundleModuleCoarsener implements IModuleCoarsener {
    
    protected Long mInteractionId = 0L;
    protected Long mThreadId = 0L;
    
    NodeObj mStart = new NodeObj("start", CodeUnitType.DEFAULT, 0L, 0L);
    NodeObj mEnd = new NodeObj("end", CodeUnitType.DEFAULT, 0L, 0L);
    
    NodeObj mParentNode;
    
    public ThreadBasedBundleModuleCoarsener(EntityConstraintModel constraintModel){
        super(constraintModel);
    }
    
    @Override
    public ModuleModel getModuleModelFromParser(JipRun jipRun) {
        
        mIgnoreSet = mConstraintModel.getIgnoreSet();
        
        mModuleModel.setName("Profile " + jipRun.getDate());
        for(Long threadId: jipRun.threads()) {
            for (JipFrame f: jipRun.interactions(threadId)) {
                try{
                    // setting up threadId and interactionId to be able to separate
                    // threads and interaction from one another in the dependency graph
                    ++mInteractionId;
                    mThreadId = threadId;
                    
                    // creating the stringbuffer to record the communication between modules
                    // in the application
                    mParentNode = new NodeObj(getFrameModuleName(f), 
                            getFrameModuleType(f), mThreadId, mInteractionId);
                    if (!mStart.getChildSet().contains(mParentNode)) {
                            mStart.getChildSet().add(mParentNode);
                    }else {
                            mParentNode = mStart.getChild(mParentNode);
                    }
                    visitFrameForModuling(f);
                }catch(Exception e){
                        System.out.println(e.getMessage());
                }
            }
        }
      
        //recursively initializing modules and interactions in the graph model
        initializeModels();
        
        return mModuleModel;
    }        
    
    @Override
    public void visitFrameForModuling(JipFrame frame){
        StringBuffer b = new StringBuffer(getFrameModuleName(frame)+"\n");
        mParentNode.addVertex(Double.valueOf(frame.getNetTime()), frame.getCount());
        
        populateFrame(frame, mParentNode, mThreadId, mInteractionId, 0, b);
    }
    
    protected String populateFrame(JipFrame f, NodeObj rootNode, Long threadId, Long interactionId, int level, StringBuffer b) {
		
        Iterator<JipFrame> itr = f.getChildren().iterator();

        while (itr.hasNext()){

            NodeObj nextNode  = rootNode;
            int newLevel = level;

            JipFrame childFrame = itr.next();
            NodeObj childNode = new NodeObj(getFrameModuleName(childFrame), 
                    getFrameModuleType(childFrame), threadId, interactionId);
            if (!childNode.equals(rootNode)){

                // going one level deeper in the tree.
                newLevel += 1;

                // proceeding to the next level in the call tree
                nextNode = childNode;

                // collecting size of communicated data and the number of times data is communicated
                double toParentWeight = childFrame.getDataToParent();
                double fromParentWeight = childFrame.getDataFromParent();
                long toParentCount  = childFrame.getCountToParent();
                long fromParentCount = childFrame.getCountFromParent();

                if (rootNode.getChildSet().contains(childNode)){
                    NodeObj nd = rootNode.getChild(childNode);
                    nd.addEdge(fromParentWeight, toParentWeight, fromParentCount, toParentCount);
                    nd.addVertex(Double.valueOf(childFrame.getNetTime()),
                            childFrame.getCount());
                    nextNode = nd;
                } else {
                    // registering the child node in the set of nodes visited
                    childNode.addEdge(fromParentWeight, toParentWeight, fromParentCount, toParentCount);
                    childNode.addVertex(Double.valueOf(childFrame.getNetTime()),
                            childFrame.getCount());
                    rootNode.getChildSet().add(childNode);

                    // The following adds to the depth of the graph
                    getSubTree(rootNode, nextNode, level, b);
                }
            }else {
                rootNode.addVertex(Double.valueOf(childFrame.getNetTime()), childFrame.getCount());
            }
            populateFrame(childFrame, nextNode, threadId, interactionId, newLevel, b);
        }
        return b.toString();
    }
    
   protected void getSubTree(NodeObj parentNode, NodeObj childNode, int level, StringBuffer b){
        for (int i=0; i<level; i++) 
                b.append("| ");
        b.append("+--"); 			
        b.append(childNode.toString()).append( " :: t=").append(childNode.getId()).append(" :: i=").append(
                childNode.getInteractionId()).append(" --> w:").append(childNode.getVertexWeight()).append( 
                ", count:").append(childNode.getCount()).append( 
                ", dfp:").append(childNode.getEdge4ParentWeight()).append( 
                ", dtp:").append(childNode.getEdge2ParentWeight()).append(
                ", cfp:").append(childNode.getEdge4ParentCount()).append( 
                ", ctp:").append(childNode.getEdge2ParentCount()).append(
                ", visit:").append(childNode.getNodeVisit()).append(" \n");
    }
   
   protected boolean recursiveWriteNode(NodeObj node, int id) {
        Set<NodeObj> childSet = node.getChildSet();
        
        //checks to see if any subnode is remained in the list
        if (childSet == null || childSet.isEmpty()){
           
           // if there is no subnode and this node should be ignored, ignore it
           if (shouldIgnore(node))
               return true;
            
           // otherwise add it to the module map and indicate that it hasn't
           // been ignored by returning false
           Module m = mModuleModel.getModuleMap().get(node.getName()+"_"+id);
           if (m == null){
               m = new Module(node.getName()+"_"+id, node.getType());
               m.setExecutionCost(node.getVertexWeight());
               m.setExecutionCount(node.getCount());
               mModuleModel.getModuleMap().put(m.getName(), m);
           }else {
               m.addExecutionCost(node.getVertexWeight());
               m.addExecutionCount(node.getCount());
           }
                
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
        }
        descendentIgnore = shouldIgnore(node) && descendentIgnore;
          
        // if there is a subnode not ignored, the descendent is not ignored either
        if (!descendentIgnore){
               Module m = mModuleModel.getModuleMap().get(node.getName()+"_"+id);
               if (m == null){
                   m = new Module(node.getName()+"_"+id, node.getType());
                   m.setExecutionCost(node.getVertexWeight());
                   m.setExecutionCount(node.getCount());
                   mModuleModel.getModuleMap().put(m.getName(), m);
               }else {
                   m.addExecutionCost(node.getVertexWeight());
                   m.addExecutionCount(node.getCount());
               }
        }
        
        return descendentIgnore;
    }

    protected boolean recursiveWriteEdge(NodeObj node, int id){
        
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
                Module m1 = mModuleModel.getModuleMap().get(node.getName()+"_"+id);
                Module m2 = mModuleModel.getModuleMap().get(childNode.getName()+"_"+id);
                addDataExchange(new ModulePair(m1, m2), childNode.getEdge4ParentWeight().longValue(), 
                    childNode.getEdge2ParentWeight().longValue(), 
                    childNode.getEdge4ParentCount().longValue(),
                    childNode.getEdge2ParentCount().longValue()); 
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
    
    protected void printTree(NodeObj parentNode, int level, StringBuffer b){
        
        Set<NodeObj> childSet = parentNode.getChildSet();
        if (childSet == null || childSet.isEmpty())
                return;
        for (NodeObj childNode : childSet) {
        
            getSubTree(parentNode, childNode, level, b);
            printTree(childNode, level+1, b);
        }
    }
    
    protected boolean shouldIgnore(NodeObj node){
         if (mIgnoreSet != null)
                for(CodeEntity entity : mIgnoreSet)
                    if (entity.getEntityPattern().matches(node.getName(), null, null))
                        return true;
         return false;
    }
    
    protected void initializeModels(){
        NodeObj rootNode = mStart;
        
        if (mConstraintModel.getRootEntityList() != null)
            rootNode = extractChildNode(rootNode, mConstraintModel.getRootEntityList(), 0);
        applyRecursion(rootNode);
    }

    protected void applyRecursion(NodeObj rootNode) {
        final int i = 0;
       
        recursiveWriteNode(rootNode, i);
        recursiveWriteEdge(rootNode, i);
    }
    
    private NodeObj extractChildNode(NodeObj rtNode, List<CodeEntity> rootEntityList, int depth){
        NodeObj rootNode = null;
        if (depth > rootEntityList.size() - 1)
            return rtNode;
        
        for (NodeObj node : rtNode.getChildSet())
            if (rootEntityList.get(depth).getEntityPattern().matches(node.getName(), null, null)){
                rootNode = extractChildNode(node, rootEntityList, depth + 1);
                break;
            }
        return rootNode;
    }
}
