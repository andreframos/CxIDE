package thingsToSEE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;

public class XML_stuff {
	
	private static String role1 = null;
	private static String role2 = null;
	private static String role3 = null;
	private static String role4 = null;
	private static ArrayList<String> rolev;
	
	public static void main(String args[]){
		String test="/home/andreramos/workspace1/org.eclipse.cxide/src/teste.xml";
		String deal="/home/andreramos/workspace1/org.eclipse.cxide/plugin.xml";
		readXML(test);
	}
	
	public static boolean readXML(String xml) {
        rolev = new ArrayList<String>();
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the    
            // XML file
            dom = db.parse(xml);
     
            Element doc = dom.getDocumentElement();
            add();
            getTree(doc);
/*
            role1 = getTextValue(role1, doc, "extension");
            if (role1 != null) {
                if (!role1.isEmpty())
                    rolev.add(role1);
            }*/
            /*role2 = getTextValue(role2, doc, "role2");
            if (role2 != null) {
                if (!role2.isEmpty())
                    rolev.add(role2);
            }
            role3 = getTextValue(role3, doc, "role3");
            if (role3 != null) {
                if (!role3.isEmpty())
                    rolev.add(role3);
            }
            role4 = getTextValue(role4, doc, "role4");
            if ( role4 != null) {
                if (!role4.isEmpty())
                    rolev.add(role4);
            }*/
            System.out.println("oi");
            System.out.println(rolev);
            return true;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return false;
    }
	
	private static String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    System.out.println("GetNodeName: "+nl.item(0).getNodeName());
	    System.out.println("GetAttribute: "+nl.item(0).getAttributes().getNamedItem("point"));
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
	
	private static void getTree(Element doc){
		NodeList nl;
		nl = doc.getElementsByTagName("extension");
		NodeList underExtension;
		
		for(int i=0; i<nl.getLength(); i++){
		
			Node n = nl.item(i);
			System.out.println(n.getNodeName());
			printChilds(n,"  ");
			
		}
	}
	
	private static void printChilds(Node n, String spacing){
		if(n.hasChildNodes()){
			NodeList nl = n.getChildNodes();
			for(int i=0; i<nl.getLength(); i++){
				Node sub =  nl.item(i);
				if(!sub.getNodeName().equals("#text"))
				System.out.println(spacing+sub.getNodeName());
				if(sub.hasChildNodes())
					printChilds(sub,spacing+"  ");
			}
			
		}
			
	}
	
	private static void add(){
	
	try {
	    File inputFile = new File("/home/andreramos/workspace1/org.eclipse.cxide/src/teste.xml");
	    DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    // creating input stream
	    Document doc = builder.parse(inputFile );

	    //Xpath compiler
	    //XPathFactory xpf = XPathFactory.newInstance();
	    // XPath xpath = xpf.newXPath();

	    //XPath Query
	   // XPathExpression expr = xpath.compile("/");
	    //Node attributeElement = (Node) expr.evaluate(doc, XPathConstants.NODE);

	    //New Node          
	    //add command
	    Node childnode=doc.createElement("command"); 
	    Element childnode1 = (Element) childnode;
	    childnode1.setAttribute("categoryId", "org.eclipse.cxide.commands.category");
	    childnode1.setAttribute("id", "org.eclipse.cxide.novoXML");
	    childnode1.setAttribute("name", "novoXML");
	    Node parent=doc.getElementsByTagName("extension").item(0);
	    parent.appendChild(childnode1);
	    childnode.setTextContent("\n");
	    //
	    
	    //add handler
	    childnode=doc.createElement("handler"); 
	    childnode1 = (Element) childnode;
	    childnode1.setAttribute("commandId", "org.eclipse.cxide.novoXML");
	    childnode1.setAttribute("class", "org.eclipse.cxide.handlers.NovoComandoHandler");
	    parent=doc.getElementsByTagName("extension").item(1);
	    parent.appendChild(childnode1);
	    childnode.setTextContent("\n");
	    
	    //add menuContribution
	    childnode=doc.createElement("menuContribution");
	    childnode1 = (Element) childnode;
	    childnode1.setAttribute("locationURI", "toolbar:org.eclipse.ui.main.toolbar?after=additions");
	    parent=doc.getElementsByTagName("extension").item(3);
	    parent.appendChild(childnode1);
	    childnode=doc.createElement("toolbar"); 
	    Element childnode2 = (Element) childnode;
	    childnode2.setAttribute("id", "org.eclipse.cxide.toolbars.sampleToolbar");
	    childnode1.appendChild(childnode2);
	    childnode=doc.createElement("command"); 
	    Element childnode3 = (Element) childnode;
	    childnode3.setAttribute("commandId", "org.eclipse.cxide.novoXML");
	    childnode3.setAttribute("icon", "icons/GreenFolderClose.gif");
	    childnode3.setAttribute("tooltip", "New Command from xml");
	    childnode3.setAttribute("id", "org.eclipse.cxide.toolbars.sampleCommand");
	    childnode2.appendChild(childnode3);
	    // writing xml file
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);
	     File outputFile = new File("/home/andreramos/workspace1/org.eclipse.cxide/plugin.xml");
	    StreamResult result = new StreamResult(outputFile );
	    // creating output stream
	    transformer.transform(source, result);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
