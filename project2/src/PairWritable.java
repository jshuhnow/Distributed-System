package com.jaeseok;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


public class PairWritable<L extends WritableComparable, R extends WritableComparable> implements WritableComparable<PairWritable> {
	private L first;
	private R second;
	public PairWritable() {}	
	public PairWritable(PairWritable<L, R> p){
		this.first = p.getFirst();
		this.second = p.getSecond();
	}
	public L getFirst() {
		return first;
	}
	public R getSecond(){
		return second;
	}
	public void setFirst(L _first) {
		first = _first;
	}
	public void setSecond(R _second) {
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
			PairWritable<L, R> pair = (PairWritable<L, R>)o;
			return first.equals(pair.first) && second.equals(pair.second);
		}
		return false;
	}
	
	@Override
	public String toString(){
		return first.toString() + "," + second.toString();
	}
	
}
