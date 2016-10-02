package org.eclipse.cxide.views;

public class UserElement extends Node {
	
	public UserElement(String title, String authorGivenName, String authorSirName) {
		super(title, authorGivenName, authorSirName);
	}

	
	
	/*
	 * @see Model#accept(ModelVisitorI, Object)
	 */
	public void accept(IModelVisitor visitor, Object passAlongArgument) {
		visitor.visitBoardgame(this, passAlongArgument);
	}
}