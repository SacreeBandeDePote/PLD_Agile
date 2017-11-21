package lsbdp.agile.data;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
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
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

public class SerializeXML {
	
	   public static void main(String[] args) throws ParseException {
		   SerializeMapXML(new File("Data/fichiersXML/planLyonPetit.xml"));
	   }
	   public static StreetMap SerializeMapXML(File fileXML) throws ParseException {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		      StreetMap streetMap = new StreetMap();
		      try {
		         DocumentBuilder builder = factory.newDocumentBuilder();
		         Document xml = builder.parse(fileXML);
		         Element root = xml.getDocumentElement();		       
		         final NodeList racineNoeuds = root.getChildNodes();
		         final int nbRacineNoeuds = racineNoeuds.getLength();

	        	 
		         for (int i = 0; i<nbRacineNoeuds; i++) {
	        	    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE){			        
	        	        final Element element = (Element) racineNoeuds.item(i);
	        	        if(element.getNodeName() == "noeud")
	        	        {			        	      
	        	        	Float id = Float.parseFloat(element.getAttribute("id"));
	        	        	int x = Integer.parseInt(element.getAttribute("x"));
	        	        	int y = Integer.parseInt(element.getAttribute("y"));
	        	        	streetMap.addIntersection(new Intersection(id,x,y));
	        	        }     	      	        
	        	        if(element.getNodeName() == "troncon")
	        	        {
	        	        	Float end = Float.parseFloat(element.getAttribute("destination"));
	        	        	Float origin = Float.parseFloat(element.getAttribute("origine"));
	        	        	float length = Float.parseFloat(element.getAttribute("longueur"));
	        	        	String name = element.getAttribute("nomRue");
	        	        	Intersection originIntersection = streetMap.getIntersection(origin);
	        	        	Intersection endIntersection = streetMap.getIntersection(end);	        	      
	        	        	originIntersection.addStreet(new Street (length, name, endIntersection));
	        	        	
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
			return streetMap;
	   }
	   
	   
	   public static DeliveriesRequest SerializeDeliveryXML(File fileXML) throws ParseException {
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 DeliveriesRequest demandeDeLivraisons = null;
		 try {
			 DocumentBuilder builder = factory.newDocumentBuilder();
	         Document xml = builder.parse(fileXML);
	         Element root = xml.getDocumentElement();
	         
	         final NodeList racineNoeuds = root.getChildNodes();
	         final int nbRacineNoeuds = racineNoeuds.getLength();
	         DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        	 Date startingTime = null;
        	 Intersection warehouse = null;
        	 ArrayList <Delivery> deliveryList = new ArrayList<Delivery>();
	         for (int i = 0; i<nbRacineNoeuds; i++) {
        	    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE){			        
        	        final Element element = (Element) racineNoeuds.item(i);
        	        if(element.getNodeName() == "entrepot")
        	        {			        	      
        	        	startingTime = (Date) sdf.parse((element.getAttribute("heureDepart")));		        	        	
        	        	warehouse = new Intersection (Float.parseFloat(element.getAttribute("adresse")),0,0);			        	    
        	        }     	      	        
        	        if(element.getNodeName() == "livraison")
        	        {
        	        	Date timespanStart = new Date(0);
        	        	Date timespanEnd = new Date(0);
        	        	if(element.getAttribute("debutPlage").length()!=0) timespanStart = (Date) sdf.parse(element.getAttribute("debutPlage"));		        	        	
        	        	if(element.getAttribute("finPlage").length()!=0) timespanEnd = (Date) sdf.parse(element.getAttribute("finPlage"));		        	        	
        	        	int duration = Integer.parseInt(element.getAttribute("duree"));
        	        	Intersection location = new Intersection(Float.parseFloat(element.getAttribute("adresse")),0,0);
        	        	deliveryList.add(new Delivery(duration,timespanStart,timespanEnd, location));
	        		}				
        	    }
        	}
	         demandeDeLivraisons = new DeliveriesRequest(startingTime, warehouse, deliveryList);	
	         
		 } catch (ParserConfigurationException e) {
	    	  e.printStackTrace();
	     } catch (SAXException e) {
	          e.printStackTrace();
	     } catch (IOException e) {
	          e.printStackTrace();
	     }
		return demandeDeLivraisons;     
	   }
	   
}
