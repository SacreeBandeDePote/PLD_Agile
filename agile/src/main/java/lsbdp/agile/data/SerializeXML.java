package lsbdp.agile.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SerializeXML {
		
	   public static void main(String[] args) throws ParseException {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		      try {
		         DocumentBuilder builder = factory.newDocumentBuilder();
		         File fileXML = new File("Data/fichiersXML/DLpetit5.xml");
		         Document xml = builder.parse(fileXML);
		         Element root = xml.getDocumentElement();
		         
		         final NodeList racineNoeuds = root.getChildNodes();
		         String racine = root.getNodeName();
		         final int nbRacineNoeuds = racineNoeuds.getLength();

		         if(racine=="demandeDeLivraisons")
		         {
//		        	 List <Delivery> livraisons = new List<Delivery>;
			         for (int i = 0; i<nbRacineNoeuds; i++) {
			        	    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {			        
			        	        final Element element = (Element) racineNoeuds.item(i);
			        	        if(element.getNodeName() == "entrepot"){
//			        	        	Intersection adresse = new Intersection (Integer.parseInt(element.getAttribute("adresse")));
			        	        	String startingTime = element.getAttribute("heureDepart");
//			        	        	DeliveryRequest demandeDeLivraisons = new DeliveryRequest(startingTime, adresse);			        	    
			        	        }       	 			        	     
			        	        if(element.getNodeName() == "livraison"){
//			        	        	livraisons.add(new Delivery(element.getAttribute("duree")), new Intersection(element.getAttribute("adresse") );	
				        		}				
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

	private static Time Time(long time) {
		// TODO Auto-generated method stub
		return null;
	}
}
