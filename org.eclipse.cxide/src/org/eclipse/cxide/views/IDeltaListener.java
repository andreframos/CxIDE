package org.eclipse.cxide.views;

public interface IDeltaListener {
	public void add(DeltaEvent event);	
	public void remove(DeltaEvent event);

}
