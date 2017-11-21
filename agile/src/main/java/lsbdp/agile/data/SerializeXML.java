package lsbdp.agile.data;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
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

import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Intersection;

public class SerializeXML {
	
	   public static void main(String[] args) throws ParseException {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		      try {
		         DocumentBuilder builder = factory.newDocumentBuilder();
		         File fileXML = new File("Data/fichiersXML/DLmoyen5TW1.xml");
		         Document xml = builder.parse(fileXML);
		         Element root = xml.getDocumentElement();
		         
		         final NodeList racineNoeuds = root.getChildNodes();
		         String racine = root.getNodeName();
		         final int nbRacineNoeuds = racineNoeuds.getLength();
		         DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		         if(racine=="demandeDeLivraisons")
		         {
		        	 Date startingTime = null;
		        	 Intersection warehouse = null;
		        	 ArrayList <Delivery> deliveryList = new ArrayList<Delivery>();
			         for (int i = 0; i<nbRacineNoeuds; i++) {
		        	    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE){			        
		        	        final Element element = (Element) racineNoeuds.item(i);
		        	        if(element.getNodeName() == "entrepot")
		        	        {			        	      
		        	        	startingTime = (Date) sdf.parse((element.getAttribute("heureDepart")));		        	        	
		        	        	warehouse = new Intersection (Integer.parseInt(element.getAttribute("adresse")),0,0);			        	    
		        	        }     	      	        
		        	        if(element.getNodeName() == "livraison")
		        	        {
		        	        	Date timespanStart = new Date(0);
		        	        	Date timespanEnd = new Date(0);
		        	        	if(element.getAttribute("debutPlage").length()!=0) timespanStart = (Date) sdf.parse(element.getAttribute("debutPlage"));		        	        	
		        	        	if(element.getAttribute("finPlage").length()!=0) timespanEnd = (Date) sdf.parse(element.getAttribute("finPlage"));		        	        	
		        	        	int duration = Integer.parseInt(element.getAttribute("duree"));
		        	        	Intersection location = new Intersection(Integer.parseInt(element.getAttribute("adresse")),0,0);
		        	        	deliveryList.add(new Delivery(duration,timespanStart,timespanEnd, location));
			        		}				
		        	    }
		        	}
			         DeliveriesRequest demandeDeLivraisons = new DeliveriesRequest(startingTime, warehouse, deliveryList);	
		         }     
		         if(racine=="reseau")
		         {
		        	 ArrayList <Intersection> intersections = new ArrayList <Intersection>();
			         for (int i = 0; i<nbRacineNoeuds; i++) {
		        	    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE){			        
		        	        final Element element = (Element) racineNoeuds.item(i);
		        	        if(element.getNodeName() == "noeud")
		        	        {			        	      
		        	        	int id = Integer.parseInt(element.getAttribute("id"));
		        	        	int x = Integer.parseInt(element.getAttribute("x"));
		        	        	int y = Integer.parseInt(element.getAttribute("y"));
		        	        	intersections.add(new Intersection(id,x,y));
		        	        }     	      	        
		        	        if(element.getNodeName() == "troncon")
		        	        {
		        	        	int destination = Integer.parseInt(element.getAttribute("origine"));
//		        	        	Intersection end = getIntersection
		        	        	float length = Float.parseFloat(element.getAttribute("longueur"));
		        	        	String name = element.getAttribute("nomRue");
//		        	        	new Street(length,name)
//		        	        	float length, String name, Intersection end
		        	        	// Ajouter destination à neighbour de origin
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
}
