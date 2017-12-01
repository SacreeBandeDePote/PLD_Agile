package lsbdp.agile.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

import javafx.util.Pair;
import lsbdp.agile.model.Delivery;
import lsbdp.agile.model.DeliverySchedule;
import lsbdp.agile.model.DeliveriesRequest;
import lsbdp.agile.model.Intersection;
import lsbdp.agile.model.Route;
import lsbdp.agile.model.Street;
import lsbdp.agile.model.StreetMap;

public class SerializerXML {
	
	static ArrayList<Long> idIdentifier;
	
	public static void main(String[] args) {
        File fileMap = new File("Data/fichiersXML/planLyonMoyen.xml");
        File fileDL = new File("Data/fichiersXML/DLmoyen5.xml");
        deserializeDeliveryXML(fileDL, deserializeMapXML(fileMap));
	}

	public static StreetMap deserializeMapXML(File fileXML) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		StreetMap streetMap = new StreetMap();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.parse(fileXML);
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
	
	public static StreetMap readIntersection (Element root) {
		NodeList racineNoeuds = root.getChildNodes();
		int nbRacineNoeuds = racineNoeuds.getLength();
		StreetMap streetMap = new StreetMap();
		idIdentifier = new ArrayList<Long>();
		for (int i = 0; i < nbRacineNoeuds; i++) {
			if (racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) racineNoeuds.item(i);
				if (element.getNodeName() == "noeud") {
					Long idLong = Long.parseLong(element.getAttribute("id"));
					int id = idIdentifier.size();
					idIdentifier.add(idLong);
					int x = Integer.parseInt(element.getAttribute("x"));
					int y = Integer.parseInt(element.getAttribute("y"));
					streetMap.put((long) id, new Intersection(id, x, y));
				}
			}
		}
		return streetMap;
	}
	
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
						deliveryList.add(new Delivery(duration, timespanStart, timespanEnd, location));
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return deliveryList;
	}
	
	public static void generateRoadMap(File f, DeliverySchedule d) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(f);
		String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String entete = "-------------------------------------------------\r\n\r\n";
		entete += "| FEUILLE DE ROUTE\r\n";
		entete += "|\r\n";
		entete += "| Date/Heure de génération: " + timeStamp + "\r\n";
		entete += "-------------------------------------------------\r\n\r\n";
		out.print(entete + d.toString());
		out.close();
	}
}
