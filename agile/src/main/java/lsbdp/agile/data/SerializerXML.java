package lsbdp.agile.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javafx.util.Pair;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

/**
 * Class which permits to deserialize a XML file (a map or a delivery file) in a JAVA object
 * @author Vincent
 *
 */
public class SerializerXML {

	//Permits to assign an id 
	static ArrayList<Long> idIdentifier;

	/**
	 * Method which permits to serialize Java objects to a XML file in order to make a backup 
	 * @param deliveriesSchedule the schedule of deliveries to serialize
	 * @param file the root for the new serialized file
	 */
	public static void serializeDeliveryXML(DeliverySchedule deliveriesSchedule, File file) {
		SimpleDateFormat formater = new SimpleDateFormat("H:m:s");
		try {
			file.createNewFile();
			FileWriter ffw=new FileWriter(file);
			ffw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
			ffw.write("<demandeDeLivraisons>\n");
			ffw.write("<entrepot adresse=\"" + idIdentifier.get((int)(deliveriesSchedule.get(0).getKey().getStartingPoint().getId())) + "\" heureDepart=\"" + formater.format(deliveriesSchedule.getStartingTime()) + "\"/>\n");
			for(Pair<Route,Delivery> d: deliveriesSchedule) {
				if(d.getValue()!=null) {
					ffw.write("<livraison adresse=\"" + idIdentifier.get((int)d.getValue().getLocation().getId()));
					if(d.getValue().getTimespanStart() != null) {
						ffw.write("\" debutPlage=\"" + formater.format(d.getValue().getTimespanStart()));
					}
					ffw.write("\" duree=\"" + d.getValue().getDuration());
					if(d.getValue().getTimespanStart() != null) {
						ffw.write("\" finPlage=\"" + formater.format(d.getValue().getTimespanEnd()));
					}
					ffw.write("\"/>\n");			
				}
			}
			ffw.write("</demandeDeLivraisons>\n");
			ffw.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method which permits to deserialize a map from a XML file
	 * @param fileXML the XML file which contains the deliveries
	 * @return a streetMap the map loaded from the XML file
	 */
	public static StreetMap deserializeMapXML(File fileXML) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		StreetMap streetMap = new StreetMap();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.parse(new FileInputStream(fileXML));
			Element root = xml.getDocumentElement();
			streetMap = readIntersection(root);
			readTroncon(root, streetMap);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return streetMap;
	}
	/**
	 * Method which permits to read and create all the intersections from a XML file
	 * @param root the XML file which contains the deliveries
	 * @return a streetMap 
	 */
	public static StreetMap readIntersection (Element root) {
		NodeList racineNoeuds = root.getChildNodes();
		int nbRacineNoeuds = racineNoeuds.getLength();
		StreetMap streetMap = new StreetMap();
		idIdentifier = new ArrayList<Long>();
		long mid = 0;
		for (int i = 0; i < nbRacineNoeuds; i++) {
			if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) racineNoeuds.item(i);
				if (element.getNodeName() == "noeud") {
					Long idLong = Long.parseLong(element.getAttribute("id"));
					int id = idIdentifier.size();
					idIdentifier.add(idLong);
					int x = Integer.parseInt(element.getAttribute("x"));
					int y = Integer.parseInt(element.getAttribute("y"));
					mid += y;
					streetMap.put((long) id, new Intersection(id, y, x));
				}
			}
		}
		if(streetMap.size() != 0) {
			mid = mid/streetMap.size();
			Set<Long> keys = streetMap.keySet();
			Iterator<Long> iterator = keys.iterator();
			while(iterator.hasNext()) {
				Long key = (Long) iterator.next();
				long yCoord = streetMap.get(key).getY();
				long dif = yCoord - mid;
				if(dif>0) streetMap.get(key).setY((int) (mid+dif));
				if(dif<0) streetMap.get(key).setY((int) (mid-dif));		
			}
			return streetMap;
		}else {
			return null;
		}
	}

	/**
	 * Method which permits to read and create all the roads from a XML file
	 * @param root the XML file which contains the deliveries
	 * @param streetMap the map of the application
	 */
	public static void readTroncon (Element root, StreetMap streetMap) {
		NodeList racineNoeuds = root.getChildNodes();
		int nbRacineNoeuds = racineNoeuds.getLength();
		for (int i = 0; i < nbRacineNoeuds; i++) {
			if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) racineNoeuds.item(i);
				if (element.getNodeName() == "troncon") {
					long endOriginal = Long.parseLong(element.getAttribute("destination"));
					long originOriginal = Long.parseLong(element.getAttribute("origine"));
					int end = idIdentifier.indexOf(endOriginal);
					int origin = idIdentifier.indexOf(originOriginal);
					float length = Float.parseFloat(element.getAttribute("longueur"));
					String name = element.getAttribute("nomRue");
					Intersection originIntersection = streetMap.get((long)origin);
					Intersection endIntersection = streetMap.get((long)end);
					originIntersection.addStreet(new Street(length, name, endIntersection));						
				}
			}
		}
	}

	/**
	 * Method which permits to read a deliveries file and to create a delivery path from a XML file
	 * @param fileXML the XML file which contains the deliveries
	 * @param streetMap the map of the application
	 * @return a request of deliveries
	 */
	public static DeliveriesRequest deserializeDeliveryXML(File fileXML, StreetMap streetMap) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DeliveriesRequest deliveriesRequest = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.parse(fileXML);
			Element root = xml.getDocumentElement();
			ArrayList<Delivery> deliveryList = readDelivery(root, streetMap);
			Pair<Intersection, Date> warehouseInfo = readWarehouse(root, streetMap);
			deliveriesRequest = new DeliveriesRequest(warehouseInfo.getValue(), warehouseInfo.getKey(), deliveryList);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deliveriesRequest;
	}

	/**
	 * Method which permits to read a warehouse and a time delivery departure from a XML file
	 * @param root the root to the XML file to read
	 * @param streetMap the map of the application
	 * @return a pair composed of an intersection which is a warehouse and a starting time 
	 */
	public static Pair <Intersection, Date> readWarehouse (Element root, StreetMap streetMap) {
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date startingTime = null;
		Intersection warehouse = null;
		NodeList racineNoeuds = root.getChildNodes();
		int nbRacineNoeuds = racineNoeuds.getLength();
		try {
			for (int i = 0; i < nbRacineNoeuds; i++) {
				if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) racineNoeuds.item(i);
					if (element.getNodeName() == "entrepot") {
						startingTime = (Date) sdf.parse((element.getAttribute("heureDepart")));
						long idWarehouse= Long.parseLong(element.getAttribute("adresse"));
						Long id = (long) idIdentifier.indexOf(idWarehouse);
						warehouse = streetMap.get(id);
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Pair <Intersection, Date> (warehouse,startingTime);
	} 

	/**
	 * Method which permits to read and create a place of delivery from a XML file
	 * @param root the XML file which contains the deliveries
	 * @param streetMap the map which contains the intersections and the road
	 * @return a list of deliveries
	 */
	public static ArrayList<Delivery> readDelivery(Element root, StreetMap streetMap){
		ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		NodeList racineNoeuds = root.getChildNodes();
		int nbRacineNoeuds = racineNoeuds.getLength();
		try {
			for (int i = 0; i < nbRacineNoeuds; i++) {
				if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) racineNoeuds.item(i);
					if (element.getNodeName() == "livraison") {
						Date timespanStart = null;
						Date timespanEnd = null;
						if (element.getAttribute("debutPlage").length() != 0)
							timespanStart = (Date) sdf.parse(element.getAttribute("debutPlage"));
						if (element.getAttribute("finPlage").length() != 0)
							timespanEnd = (Date) sdf.parse(element.getAttribute("finPlage"));
						int duration = Integer.parseInt(element.getAttribute("duree"));
						long idIntersection= Long.parseLong(element.getAttribute("adresse"));
						Long id = (long) idIdentifier.indexOf(idIntersection);
						Intersection location = streetMap.get(id);
						deliveryList.add(new Delivery(duration, timespanStart, timespanEnd, location, null));
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return deliveryList;
	}

	/**
	 * Method which permits to generate a roadmap
	 * @param f the file which while contain the roadmap
	 * @param d the delivery schedule which permits to generate the roadmap
	 */
	public static void generateRoadMap(File f, DeliverySchedule d) {
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF8"));
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
			String entete = "-------------------------------------------------\r\n\r\n";
			entete += "| FEUILLE DE ROUTE\r\n";
			entete += "|\r\n";
			entete += "| Date/Heure de génération: " + timeStamp + "\r\n";
			entete += "-------------------------------------------------\r\n\r\n";
			out.append(entete + d.toString());			
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
