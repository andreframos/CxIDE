package org.eclipse.cxide.utilities;

public class RulesInfo {
	
	String startSeq;
	String endSeq;
	char escChar;
	String fieldName;
	
	String word;
	
	int index;
	
	int r;
	int g; 
	int b;
	
	String type;
	
	//Single Line Rule
	public RulesInfo(String startSeq, String endSeq, char escChar, int index){
		this.startSeq=startSeq;
		this.endSeq=endSeq;
		this.escChar=escChar;
		this.index=index;
		word="";
		type="SLR";
	}
	
	//End Line Rule
	public RulesInfo(String startSeq, int index){
		this.startSeq=startSeq;
		this.endSeq="";
		this.escChar=' ';
		this.index=index;
		word="";
		type="EOR";
	}
	
	//Word Rule
	public RulesInfo(String fieldName, String word, int index, int r, int g, int b){
		this.fieldName=fieldName;
		this.word=word;
		this.index=index;
		this.r=r;
		this.g=g;
		this.b=b;
		type="WR";
	}
	
	public RulesInfo(int index, String type){
		this.index=index;
		this.type=type;
	}

	public String getStartSeq() {
		return startSeq;
	}

	public String getEndSeq() {
		return endSeq;
	}

	public char getEscChar() {
		return escChar;
	}

	public String getWord() {
		return word;
	}

	public int getIndex() {
		return index;
	}

	public String getType() {
		return type;
	}

}
