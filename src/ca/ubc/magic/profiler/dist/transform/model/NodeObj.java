package ca.ubc.magic.profiler.dist.transform.model;

import ca.ubc.magic.profiler.dist.control.MathUtil;
import ca.ubc.magic.profiler.dist.model.granularity.CodeUnitType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NodeObj {
	protected String _name;
        protected CodeUnitType _type;
	private Long   _id;
	private Long   _interactionId;
	private List<Double> _edge2ParentWeight;
        private List<Double> _edge4ParentWeight;
        private List<Long> _edge2ParentCount;
        private List<Long> _edge4ParentCount;
	private Double _vertexWeight;
	private Long   _count;
        private Long   _nodeVisit;
	
	private transient Set<NodeObj> _childNodeSet = new HashSet<NodeObj>();
	
	public NodeObj(String name, CodeUnitType type, Long id, Long interactionId){
		_name = name;
                _type = type;
		this._id = id;
		this._interactionId = interactionId;
		this._vertexWeight = 0.0;
                this._edge2ParentWeight = new ArrayList<Double>();
                this._edge4ParentWeight = new ArrayList<Double>();
                this._edge2ParentCount = new ArrayList<Long>();
                this._edge4ParentCount = new ArrayList<Long>();
		this._count = 0L;
                this._nodeVisit = 0L;
	}

        public void setVertex(Double weight, Long count){
            _vertexWeight = weight;
            _count = count;
            _nodeVisit = 1L;
        }
        
        public void addVertex(Double weight, Long count){
            _vertexWeight += weight;
            _count += count;
            _nodeVisit++;
        }
        
        public Double getVertexWeight(){
            return _vertexWeight;
        }
	
//	public void setEdge(Double fpweight, Double tpweight, Long fpcount, Long tpcount){
//            _edge4ParentWeight.add(fpweight);
//            _edge4ParentCount.add(fpcount);
//            _edge2ParentWeight.add(tpweight);
//            _edge2ParentCount.add(tpcount);
//	}
        
       public void removeParentEdge(){
            this._edge2ParentWeight = new ArrayList<Double>();
            this._edge4ParentWeight = new ArrayList<Double>();
            this._edge2ParentCount = new ArrayList<Long>();
            this._edge4ParentCount = new ArrayList<Long>();
       }
        
        public void addEdge(Double fpweight, Double tpweight, Long fpcount, Long tpcount){
            _edge4ParentWeight.add(fpweight);
            _edge4ParentCount.add(fpcount);
            _edge2ParentWeight.add(tpweight);
            _edge2ParentCount.add(tpcount);   
        }
        
	public Long getCount(){
		return _count;
	}
        
        public Long getNodeVisit(){
            return _nodeVisit;
        }
	
	public Double getEdge2ParentWeight(){
		return MathUtil.weightedAvg(_edge2ParentWeight, _edge2ParentCount);
	}
        
        public Long getEdge2ParentCount(){
            return (long) MathUtil.sum(_edge2ParentCount);
        }
        
        public Double getEdge4ParentWeight(){
            return MathUtil.weightedAvg(_edge4ParentWeight, _edge4ParentCount);
	}
        
        public Long getEdge4ParentCount(){
            return (long) MathUtil.sum(_edge4ParentCount);
        }
	
        @Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NodeObj))
			return false;
		if (_name.equals(((NodeObj) obj)._name))// &&
//				this._id == (((NodeObj) obj))._id &&
//				this._interactionId == (((NodeObj)obj))._interactionId)
				return true;
		return false;
	}
	
        @Override
	public int hashCode() {
            int hashCode = 5;
            hashCode += 37 * _name.hashCode();
            return hashCode;
	}
	
        @Override
	public String toString() {
		return _name + "_" + Long.toString(_id) + "_" + Long.toString(_interactionId);
	}
	
	public NodeObj getChild(NodeObj node){
		Iterator<NodeObj> itr = _childNodeSet.iterator();
		while (itr.hasNext()){
			NodeObj next = (NodeObj) itr.next();
			if (next.equals(node))
				return next;
		}
		return null;
	}
	
	public List<NodeObj> getChildSetAsList(){
		return new ArrayList<NodeObj>(_childNodeSet);
	}
        
        public Set<NodeObj> getChildSet(){
            return _childNodeSet;
        }
        
        public String getName(){
            return _name;
        }
        
        public CodeUnitType getType(){
            return _type;
        }
        
        public Long getInteractionId(){
            return _interactionId;
        }
        
        public Long getId(){
            return _id;
        }
        
//        public void add(NodeObj node){
//            this._vertexWeight += node._vertexWeight;
//            this._edge2ParentWeight.addAll(node._edge2ParentWeight);
//            this._edge4ParentWeight.addAll(node._edge4ParentWeight);
//            this._edge2ParentCount.addAll(node._edge2ParentCount);
//            this._edge4ParentCount.addAll(node._edge4ParentCount);
//            this._count += node._count;
//        }        
}
