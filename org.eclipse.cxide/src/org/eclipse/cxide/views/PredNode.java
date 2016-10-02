package org.eclipse.cxide.views;

import java.util.ArrayList;
import java.util.List;

public class PredNode extends Node {
	protected List boxes;
	protected List games;
	protected List books; 
	
	private int start;
	private int offset;
	
	
	private static IModelVisitor adder = new Adder();
	private static IModelVisitor remover = new Remover();
	
	public PredNode(){
		boxes = new ArrayList();
		games = new ArrayList();
		books = new ArrayList();
	}

	
	private static class Adder implements IModelVisitor{
		
		public void visitBoardgame(UserElement boardgame, Object argument){
			((PredNode) argument).addBoardGame(boardgame);
		}
		
		
		public void visitBook(AtomNode book, Object argument){
			((PredNode) argument).addBook(book);
		}
		
		public void visitMovingBox(PredNode box, Object argument){
			((PredNode) argument).addBox(box);
		}
		
	}
	
	private static class Remover implements IModelVisitor{
		public void visitBoardgame(UserElement boardGame, Object argument){
			((PredNode) argument).removeBoardGame(boardGame);
		}
	
		public void visitBook(AtomNode book, Object argument){
			((PredNode) argument).removeBook(book);
		}
	
		public void visitMovingBox(PredNode box, Object argument){
			((PredNode) argument).removeBox(box);
			box.addListener(NullDeltaListener.getSoleInstance());
		}
	}
	
	public PredNode(String name,int start, int offset){
		this();
		this.name = name;
		this.start=start;
		this.offset=offset;
	}
	
	public int getStart(){
		return start;
	}
	
	public int getRange(){
		return offset;
	}
	
	public List getBoxes(){
		return boxes;
	}
	
	protected void addBox(PredNode box){
		boxes.add(box);
		box.parent = this;
		fireAdd(box);
	}
	
	protected void addBook(AtomNode book){
		books.add(book);
		book.parent = this;
		fireAdd(book);
	}
	
	
	protected void addBoardGame(UserElement game){
		games.add(game);
		game.parent = this;
		fireAdd(game);
	}
	
	public List getBooks(){
		return books;
	}
	
	public void remove(Node toRemove){
		toRemove.accept(remover, this);
	}
	
	protected void removeBoardGame(UserElement boardGame){
		games.remove(boardGame);
		boardGame.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(boardGame);
	}
	
	protected void removeBook(AtomNode book){
		books.remove(book);
		book.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(book);
	}
	
	
	protected void removeBox(PredNode box){
		boxes.remove(box);
		box.addListener(NullDeltaListener.getSoleInstance());
		fireRemove(box);
	}
	
	public void add(Node toAdd){
		toAdd.accept(adder, this);
	}
	
	public List getGames(){
		return games;
	}
	
	public int size(){
		return getBooks().size() + getBoxes().size() + getGames().size();
	}
	
	public void accept(IModelVisitor visitor, Object passAlongArgument){
		visitor.visitMovingBox(this, passAlongArgument);
	}
	
}
