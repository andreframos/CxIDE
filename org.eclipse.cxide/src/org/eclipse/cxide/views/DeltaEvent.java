package org.eclipse.cxide.views;

public class DeltaEvent {
	protected Object actedUpon;
	
	public DeltaEvent(Object receiver){
		actedUpon = receiver;
	}
	
	public Object receiver(){
		return actedUpon;
	}

}
