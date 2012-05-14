package ca.ubc.magic.profiler.partitioning.control.alg.metis;

public class EdgeData {
	
	private Integer start;
	private Integer end;
	private Double weight;
	
	public EdgeData(Integer start, Integer end, Double weight){
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final EdgeData other = (EdgeData) obj;
            if (this.start != other.start && (this.start == null || !this.start.equals(other.start))) {
                return false;
            }
            if (this.end != other.end && (this.end == null || !this.end.equals(other.end))) {
                return false;
            }
            if (this.weight != other.weight && (this.weight == null || !this.weight.equals(other.weight))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 89;
            hash = hash * this.start.hashCode() + 11;
            hash = hash * this.end.hashCode() + 11;
            hash = hash * this.weight.hashCode() + 11;
            return hash;
        }
        
        
}
