/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.granularity.CodeUnitType;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
import ca.ubc.magic.profiler.dist.transform.model.NodeObj;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nima
 */
public class CoarseRequestBasedBundleModuleCoarsener extends RequestBasedBundleModuleCoarsener {
    
    private static final String ARIES_HEAD_DATA_ROOT = "org.apache.aries.samples.ariestrader.core";
    private static final String RUBIS_HEAD_DATA_ROOT = "com.notehive.osgi.hibernate-samples.hibernate-classes";
    private static final String HEAD_DATA_ROOT = ARIES_HEAD_DATA_ROOT;
    
    private static final Set<String> ARIES_FIXED_DATA_NODE_SET = new HashSet<String>(
            Arrays.asList(new String[]{
                "AccountDataBeanImpl",
                "QuoteDataBeanImpl",
                "AccountProfileDataBeanImpl",
                "OrderDataBeanImpl",
                "HoldingDataBeanImpl"
            }));
    private static final Set<String> RUBIS_FIXED_DATA_NODE_SET = new HashSet<String>(
            Arrays.asList(new String[]{
                "user-session",
                "categories-session",
                "item-session",
                "region-session",
                "comment-session",
                "bid-session",
                "buy-session",
                "hibernate-classes"
            }));
    private static final Set<String> FIXED_DATA_NODE_SET = ARIES_FIXED_DATA_NODE_SET;
    
    private static final Set<String> ARIES_FIXED_LOGIC_NODE_SET = new HashSet<String>(
            Arrays.asList(new String[]{
                "TradeServletAction:doSell",
                "TradeServletAction:doBuy",
                "TradeServletAction:doWelcome",
                "TradeServletAction:doHome",
                "TradeServletAction:doLogin",
                "TradeServletAction:doLogout",
                "TradeServletAction:doQuotes",
                "TradeServletAction:doProfile"
            }));
    private static final Set<String> RUBIS_FIXED_LOGIC_NODE_SET = new HashSet<String>(
            Arrays.asList(new String[]{
                "web-searchbycat",
                "web-sellitemform",
                "web-aboutme",
                "web-searchbyreg",
                "web-viewitem",
                "web-putcomment",
                "web-viewbidhistory",
                "web-viewuserinfo",
                "web-browsereg",
                "web-browsecat"
            }));
    private static final Set<String> FIXED_LOGIC_NODE_SET = ARIES_FIXED_LOGIC_NODE_SET;
    
    protected static boolean mIsDataRoot = Boolean.FALSE;
    
    public CoarseRequestBasedBundleModuleCoarsener(EntityConstraintModel constraintModel){
        super(constraintModel);
    }
   
    protected ExtendedNodeObj recursiveCoarsener(NodeObj node, int id) {
        
        ExtendedNodeObj tmpRoot = null;
        boolean isFirstDataNodeSetHere = Boolean.FALSE;
        
        if (node.getName().contains(HEAD_DATA_ROOT)){
            isFirstDataNodeSetHere = !mIsDataRoot;
            mIsDataRoot = Boolean.TRUE;            
        }
        
        Set<NodeObj> childSet = node.getChildSet();
        
        //checks to see if any subnode is remained in the list
        if (childSet == null || childSet.isEmpty()){
           
           tmpRoot = getExtendedNodeObj(node);
           // if there is no subnode and this node should be ignored, ignore it
           if (!isDataNode(tmpRoot) && shouldIgnore(tmpRoot))
               tmpRoot = null;
               
        }else {
        
            // if there are subnodes in the graph assume that they are all going to
            // be ignored unless otherwise said.
            tmpRoot = getExtendedNodeObj(node);

            // for all descendents check whether or not they should be ignored.
            for (NodeObj childNode : childSet){
               // we && the return value from the descendents with the current
               // decision flag. If it changes to false, the node should not be 
               // ignored.
               ExtendedNodeObj descendent = recursiveCoarsener(childNode, id);

               // if the node is a replicable node that should be added to the graph, make a 
               // replication of it and add it to the graph.           
               tmpRoot.mergeChild(descendent);
            }                
            tmpRoot = mergeNodes(tmpRoot);
        }
        
        if (node.getName().contains(HEAD_DATA_ROOT) && isFirstDataNodeSetHere)
            mIsDataRoot = Boolean.FALSE;
        
        return tmpRoot;
    }
    
    ExtendedNodeObj mergeNodes(ExtendedNodeObj root){
               
        if (root == null || (shouldIgnore(root) && root.getChildSet().isEmpty()))
                return null;
        
        Set<NodeObj> tmpSet = new HashSet<NodeObj>(root.getChildSet());
        if (!mIsDataRoot){
            for(NodeObj child : tmpSet){
                ExtendedNodeObj childObj = (ExtendedNodeObj) child;
                if (root.mNodetype == childObj.mNodetype)
                    root.merge(childObj);
                else
                    root.mergeChild(childObj);
            }
        }else {
            root.merge(root.getChildSet());
        }
        return root;
    }
    
    @Override
    protected void printTree(NodeObj parentNode, int level, StringBuffer b){
        if(parentNode instanceof ExtendedNodeObj)
            System.out.println(((ExtendedNodeObj)parentNode).getCoarsenedNodeNames());
        super.printTree(parentNode, level, b);
    }

    @Override
    protected void applyRecursion(NodeObj rootNode) {

        int i = 0;
        try {
            if (rootNode == null || rootNode.getChildSet() == null)
                return;
            
            for (NodeObj child : rootNode.getChildSet()){

    //          The following few lines are just debugging code for printing the results of
    //          a request-based dependency graph design.
//                System.out.println("\n\n\n"+"--------------");
//                StringBuffer b = new StringBuffer(i + " :: " + child.getName() + ", visit:" + child.getNodeVisit()+"\n");
//                printTree(child, 0, b);
//                System.out.println(b.toString()+"\n");
                
                preprocessing(child);

                // The following line does the coarsening for the modules
                ExtendedNodeObj returnedNode = recursiveCoarsener(child, i);

                if (returnedNode != null){
//                    System.out.println(returnedNode.getName());
//                    System.out.println(returnedNode.getCoarsenedNodeNames());
//                    printTree(returnedNode, 0, b);
//                    System.out.println("--------------"+"\n\n\n");
                    
                    
                    // if we are constrained to add a synthetic node, we add the node here
                    returnedNode = checkForAddingSyntheticNode(i, returnedNode);
                    
                    // The following two lines rely on the info from the superclass
                    // in order to printout information on the nodes.
                    recursiveWriteNode(returnedNode, i);
                    recursiveWriteEdge(returnedNode, i);
                    
                    i++;
                }
                
                postprocessing();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    protected void preprocessing(Object... objs){
       for (Object obj : objs){
           if (obj instanceof NodeObj)
               ((NodeObj)obj).removeParentEdge();
       }
    }
    
    protected void postprocessing(Object... objs){
    }
    
    protected ExtendedNodeObj checkForAddingSyntheticNode(int i, ExtendedNodeObj returnedNode) {
        if (mConstraintModel.getConstraintSwitches().isSyntheticNodeActivated()){
            ExtendedNodeObj syntheticNodeObj = new ExtendedNodeObj(
                    new NodeObj(Constants.SYNTHETIC_NODE + "_" + i, 
                    CodeUnitType.DEFAULT, 0L, 0L));
            syntheticNodeObj.getChildSet().add(returnedNode);
            returnedNode.addEdge(0.0, 0.0, 1L, 1L);
            returnedNode = syntheticNodeObj;
        }
        return returnedNode;
    }
    
    private ExtendedNodeObj getExtendedNodeObj(NodeObj node){
        ExtendedNodeObj nodeObj = new ExtendedNodeObj(node);
        if (isDataNode(nodeObj))
            nodeObj.mNodetype = NodeType.DATA;
        else 
            nodeObj.mNodetype = NodeType.LOGIC;
        return nodeObj;
    }
    
    private boolean isDataNode(ExtendedNodeObj nodeObj){
        for (String s : FIXED_DATA_NODE_SET)
            if (nodeObj.getName().contains(s))
                return true;
        return false;
    }
    
    class ExtendedNodeObj extends NodeObj {
        
        protected NodeType  mNodetype  = NodeType.NULL;
        protected NodeState mNodeState = NodeState.LOOSE;
        
        protected Set<? extends NodeObj> mChildSet;
        protected Set<String> mNameSet;
        
        ExtendedNodeObj(NodeObj node){
            this(node.getName(), 
                    node.getType(), 0L, 0L);            
            this.addEdge(node.getEdge4ParentWeight(), node.getEdge2ParentWeight(),
                    node.getEdge4ParentCount(), node.getEdge2ParentCount());
            this.setVertex(node.getVertexWeight(), node.getCount());
            
            mChildSet  = new HashSet<ExtendedNodeObj>();
            mNameSet   = new HashSet<String>();
            
            mNameSet.add(node.getName());
            mNodeState = updateState(node.getName());
            if (mNodeState == NodeState.FIXED)
                _name = node.getName();
        }
        
        private NodeState updateState(String name){
            for (String s : FIXED_DATA_NODE_SET)
                if (name.contains(s))
                    return NodeState.FIXED;
            for (String s : FIXED_LOGIC_NODE_SET)
                if (name.contains(s))
                    return NodeState.FIXED;
            return NodeState.LOOSE;
        }
        
        private NodeState updateState(ExtendedNodeObj p, ExtendedNodeObj c){
            if (p.mNodeState == NodeState.FIXED || c.mNodeState == NodeState.FIXED)
                return NodeState.FIXED;
            return NodeState.LOOSE;
        }
        
        private ExtendedNodeObj(String name, CodeUnitType type, Long id, Long interactionId){
		super(name, type, id, interactionId);
	}
        
        @Override
        public Set<NodeObj> getChildSet(){
            return (Set<NodeObj>) mChildSet;
        }
        
        public void merge(ExtendedNodeObj node){
            this.addVertex(node.getVertexWeight(), node.getCount());
            this.mNameSet.addAll(node.mNameSet);
            // removing the node from the list of child nodes of a method
            this.mChildSet.remove(node);                
            // adding all the childs of the child as the childs of the parent
            // thus merging the child node into the original node.
            mergeChilds(node.getChildSet());
            
            if (isDataNode(node) && !isDataNode(this) && mIsDataRoot){
                this.mNodeState = this.updateState(this, node);
                this.mNodetype  = node.mNodetype;
                this._name = node.getName();
            }
            if (this.mNodetype == NodeType.LOGIC)
                this._type = CodeUnitType.METHOD;
            else if (this.mNodetype == NodeType.DATA)
                this._type = CodeUnitType.CLASS;
            else
                this._type = CodeUnitType.COMPONENT;
        }
        
        private void merge(Set<NodeObj> childNodes){
            Set<ExtendedNodeObj> tmpChildDataSet = new HashSet<ExtendedNodeObj>();
            Set<ExtendedNodeObj> tmpChildNonDataSet = new HashSet<ExtendedNodeObj>();
            for (NodeObj child : childNodes){
                ExtendedNodeObj childObj = (ExtendedNodeObj) child;
                if (childObj.mNodetype == NodeType.DATA)
                    tmpChildDataSet.add(childObj);
                else
                    tmpChildNonDataSet.add(childObj);
            }
            for (ExtendedNodeObj tmpObj : tmpChildNonDataSet)
                this.merge(tmpObj);
            
            if (tmpChildDataSet.size() > 0 && tmpChildDataSet.size() < 2)
                for (ExtendedNodeObj childObj : tmpChildDataSet){
                    
//                    Printouts for the purpose of testing node merges
//                    System.out.println(childObj.getName() + " -----> " + this.getName() + 
//                          " dfp: " + childObj.getEdge4ParentWeight() +
//                          " dtp: " + childObj.getEdge2ParentWeight());
                    
                    this.merge(childObj);
                    this.addEdge(childObj.getEdge4ParentWeight(), childObj.getEdge2ParentWeight(),
                            childObj.getEdge4ParentCount(), childObj.getEdge2ParentCount());
                }
        }
        
        private void mergeChilds(Set<NodeObj> otherChildSet){
            for (NodeObj otherChild : otherChildSet){
                
                if (otherChild == null)
                    continue;
                
                boolean found = Boolean.FALSE;
                for (NodeObj child : getChildSet()){
                    
                    if (child == otherChild)
                        continue;
                    
                    if (child.getName().equals(otherChild.getName())){
                        ExtendedNodeObj childObj = (ExtendedNodeObj) child;
                        
//                        Printouts for the purpose of testing node merges                        
//                        System.out.println(otherChild.getName() + " -----> " + childObj.getName() + 
//                            " dfp: " + otherChild.getEdge4ParentWeight() +
//                            " dtp: " + otherChild.getEdge2ParentWeight());
                        
                        childObj.merge((ExtendedNodeObj) otherChild);
                        childObj.addEdge(otherChild.getEdge4ParentWeight(), otherChild.getEdge2ParentWeight(),
                            otherChild.getEdge4ParentCount(), otherChild.getEdge2ParentCount());
                        
                        found = Boolean.TRUE;
                        break;
                    }
                }
                if (!found)
                    getChildSet().add(otherChild);
            }
        }
        
        public void mergeChild(ExtendedNodeObj nodeObj){   
            if (nodeObj != null){
                Set tmpSet = new HashSet();
                tmpSet.add(nodeObj);
                mergeChilds(tmpSet);
            }
        }
        
        public String getCoarsenedNodeNames(){
            StringBuilder builder = new StringBuilder();
            for (String s : mNameSet)
                builder.append("\t").append(s).append("<->");
            return builder.toString();
        }              
    }
    
    protected enum NodeType {
        NULL,
        DATA,
        LOGIC
    }
    
    protected enum NodeState {
        LOOSE,
        FIXED
    }
}
