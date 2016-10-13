package org.eclipse.cxide.CxEditor;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.cxide.utilities.Editor_Utilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * Esta classe usa a representação DOM do w3c para retirar
 * informações de um xml
 * @author andreramos
 *
 */
public class ExtractBuiltins {

	//Obter a absolute path(desde a raiz) do workspace
	//Estava a ter problemas em carregar ficheiro dando o seu path
	//Com o absolute path os problemas desapareceram
	private static String getChangeDir() {
		String path = System.getProperty("user.dir");
		return path;
	}

	
	
	/**
	 * Este método retorna uma representação DOM
	 * do xml builts.
	 * 
	 * Esta representação é posteriormente utilizada
	 * para obter uma lista de nós que representam todos os
	 * elementos <pred> do xml builts
	 * @return
	 */
	private static Document parseXmlFile() {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			///src/builts.xml
			String contentAssistFile = Editor_Utilities.getContentAssistFile();
			// parse using builder to get DOM representation of the XML file
			Document dom = db.parse(contentAssistFile);
			return dom;

		} catch (ParserConfigurationException pce) {
			System.err.println("Erro no parseXmlFile em ExtractBuiltins");
			pce.printStackTrace();
		} catch (SAXException se) {
			System.err.println("Erro no parseXmlFile em ExtractBuiltins");
			se.printStackTrace();
		} catch (IOException ioe) {
			System.err.println("Erro no parseXmlFile em ExtractBuiltins");
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * O XML com as informações sobre os builts é formado por
	 * elementos <pred> que representam predicados:
	    <pred>
        <id>pwd</id>
		</pred>
		Este método retorna o valor do sub-elemento <id>
	 * @param pred - O elemento xml que representa um predicado
	 * @return o valor do sub-elemento id
	 */
	private static String getPredXML(Element pred) {

		String id = getTextValue(pred, "id");
		return id;

	}

	/**
	 * O XML com as informações sobre os builts é formado por
	 * elementos <pred> que representam predicados:
	 <pred>
        <id>pop</id>
        <desc>pop
            Pops the current context. Therefore restores the
            previous current unit, which will be used in
            the top-level iteration
       </desc>
     </pred>
     
     Este método retorna o valor do sub-elemento desc
	 * @param pred
	 * @return
	 */
	private static String getDescXML(Element pred) {

		String desc = getTextValue(pred, "desc");
		return desc;

	}
	
	/**
	 * Este método permite aceder ao valor de um determinado
	 * sub-elemento do elemento ele
	 * @param ele
	 * @param tagName
	 * @return
	 */

	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	/**
	 * Este método utiliza a representação DOM
	 * para obter todos os elemento pred do xml builts, como uma nodeList
	 * 
	 * A nodelist é processada criando um array de PredInfo
	 * PredInfo é uma representação dos predicados que facilita
	 * a posterior consulta do nome do predicado e da sua descrição 
	 * para a criação do contentAssist
	 * @return
	 */
	public PredInfo[] getBuiltinsXML() {
		System.out.println("GettingBuiltins");
		Document dom = parseXmlFile();
		Element docEle = dom.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("pred");
		PredInfo[] xmlPredsInfo = new PredInfo[nl.getLength()];
		
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				Element el = (Element) nl.item(i);

				xmlPredsInfo[i] = new PredInfo(getPredXML(el), getDescXML(el));

			}
		}
		return xmlPredsInfo;
	}
}
