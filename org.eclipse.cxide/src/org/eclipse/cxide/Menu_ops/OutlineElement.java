package org.eclipse.cxide.Menu_ops;

import java.util.ArrayList;
import java.util.Iterator;

public class OutlineElement {
	private String functor;
	private int line;
	private int arity;
	private String[] args;
	private int start;
	private int offset;
	
	public OutlineElement(String functor, int arity, String[] args, int start, int offset){
		this.functor=functor;
		this.arity=arity;
		this.args=args;
		this.start=start;
		this.offset=offset;
	}

	public int getStart(){
		return start;
	}
	
	public int getRange(){
		return offset;
	}
	
	public String getFunctor() {
		return functor;
	}
	
	public String[] getArgs(){
		return args;
	}

	public void setFunctor(String functor) {
		this.functor = functor;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getArity() {
		return arity;
	}

	public void setArity(int arity) {
		this.arity = arity;
	}
	
	
	
	
}
