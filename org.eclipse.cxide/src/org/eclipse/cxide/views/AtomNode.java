package org.eclipse.cxide.views;

import java.util.ArrayList;
import java.util.List;

public class AtomNode extends Node {
	protected static List newBooks = buildBookList();
	protected static int cursor = 0;
	
	public AtomNode(String name) {
		super(name);
	}
	
	
	
	
	public static AtomNode newBook() {
		AtomNode newBook = (AtomNode)newBooks.get(cursor);
		cursor = ((cursor + 1) % newBooks.size());
		return newBook;
	}
	
	
	protected static List buildBookList() {
		newBooks = new ArrayList();
		
		return newBooks;
	}
	/*
	 * @see Model#accept(ModelVisitorI, Object)
	 */
	public void accept(IModelVisitor visitor, Object passAlongArgument) {
		visitor.visitBook(this, passAlongArgument);
	}

}