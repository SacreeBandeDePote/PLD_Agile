package lsbdp.agile.algorithm;

import java.util.ArrayList;

import javafx.util.Pair;

public class Heuristique {
	int crtNode;
	ArrayList<Integer> nonView;
	float[][] timeCost;
	int nbNodesUnknown;
	
	public Heuristique(int crtNode, ArrayList<Integer> nonView, float[][] timeCost){
		this.crtNode=crtNode;
		this.nonView=nonView;
		this.timeCost=timeCost;
		this.nbNodesUnknown = nonView.size();
	}
	
	public float Estimation(){
		int nextIndex=0;
		int index = crtNode;
		float sum=0.F;
		for(int i = 0; i < nbNodesUnknown-1 ; i++){
			nextIndex = getShortestRoute(timeCost[index], nonView);
			sum += timeCost[index][nextIndex];
			index=nextIndex;
		}
		//ajout du retour à la warehouse
		sum += timeCost [index][timeCost.length-1];
		return sum;
	}
	
	public int getShortestRoute(float[] timeCostCrtNode, ArrayList<Integer> nonView){
		float min = timeCostCrtNode[nonView.get(0)];
		int index=nonView.get(0);
		int i = -1;
		int indexNonView =0;
		for(Integer s : nonView){
			i++;
			if(timeCostCrtNode[s]< min){

				min = timeCostCrtNode[s];
				index = s;
				indexNonView = i;
			}
		}
		nonView.remove(indexNonView);
		return index;
	}
	
	
	public boolean checkTimePossible(int crtNode,ArrayList<Integer> nonView, Pair<Float, Float>[] timeWindows){
		float startTime = timeWindows[crtNode].getKey();

		for(Integer s : nonView){

			if(timeWindows[s].getValue() < startTime){
				return false;
			}
		}
		return true;
	}
}
