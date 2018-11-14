
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


class PairWritable implements WritableComparable<PairWritable> {
	private DoubleWritable first;
	private IntWritable second;
	public PairWritable() {
		this.first = new DoubleWritable();
		this.second = new IntWritable();
	}
	public PairWritable(DoubleWritable l, IntWritable r){
		this.first = l;
		this.second = r;
	}
	public DoubleWritable getFirst() {
		return first;
	}
	public IntWritable getSecond(){
		return second;
	}
	public void setFirst(DoubleWritable _first) {
		first = _first;
	}
	public void setSecond(IntWritable _second) {
		second = _second;
	}

	public void write(DataOutput out) throws IOException {
		first.write(out);
		second.write(out);
	}

	public void readFields(DataInput in) throws IOException {
		first.readFields(in);
		second.readFields(in);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int compareTo(PairWritable other) {
		int cmp =  ((Comparable) first).compareTo((Comparable) other.first);
		if(cmp != 0){
			return cmp;
		}
		return ((Comparable) second).compareTo((Comparable) other.second);
	}

	@Override
	public int hashCode() {
		return first.hashCode() * 37 + second.hashCode();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean equals(Object o) {
		if (o instanceof PairWritable){
			PairWritable pair = (PairWritable)o;
			return first.equals(pair.first) && second.equals(pair.second);
		}
		return false;
	}

	@Override
	public String toString(){
		return first.toString() + "," + second.toString();
	}

}

