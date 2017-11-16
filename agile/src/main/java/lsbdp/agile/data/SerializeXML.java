package lsbdp.agile.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SerializeXML {
	   public static void main(String[] args) {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		      try {
		         DocumentBuilder builder = factory.newDocumentBuilder();
		         File fileXML = new File("Data/fichiersXML/DLpetit5.xml");
		         Document xml = builder.parse(fileXML);
		         Element root = xml.getDocumentElement();
		         
		         final NodeList racineNoeuds = root.getChildNodes();
		         final int nbRacineNoeuds = racineNoeuds.getLength();
		         for (int i = 0; i<nbRacineNoeuds; i++) {
		        	    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {		        	
		        	        final Element element = (Element) racineNoeuds.item(i);
		        	        if(element.getNodeName() == "entrepot"){
		        	        	System.out.println(element.getAttribute("adresse"));
		        	        	System.out.println(element.getAttribute("heureDepart"));
		        	        }
		        	        if(element.getNodeName() == "livraison"){
				        		System.out.print(element.getNodeName());
				        		System.out.println(element.getAttribute("adresse"));		        	    
				        		System.out.println(element.getAttribute("duree"));		        	    
			        		}				
		        	    }
		        	}
		      } catch (ParserConfigurationException e) {
		    	  e.printStackTrace();
		      } catch (SAXException e) {
		          e.printStackTrace();
		      } catch (IOException e) {
		          e.printStackTrace();
		      }
	   }
}
