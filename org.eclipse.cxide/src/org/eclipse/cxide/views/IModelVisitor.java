package org.eclipse.cxide.views;

public interface IModelVisitor {
	public void visitMovingBox(PredNode box, Object passAlongArgument);
	public void visitBook(AtomNode book, Object passAlongArgument);
	public void visitBoardgame(UserElement boardgame, Object passAlongArgument);

}
