package AODV;

import java.util.LinkedList;

import MatlabChart.MatlabChart;
import SimulationNetwork.NetworkGraph;
import SimulationNetwork.NetworkNode;
import SimulationNetwork.PayloadMessage;
import SimulationNetwork.Simulator;

public class AodvSimulator extends Simulator{
	
	
	public void simulation(){
		
		//Speed analysis 
		
		//(Energy) cost analysis
		
		//Lifetime analysis
		//lifetimeAnalysis(networkWidth, sendProbability);
		
		//Partitioning analysis
		
		
		
		
		/*AODV_Graph graph = new AODV_Graph(4);

		graph.nachrichtSenden(0, 14);
		
		//this.geschwindigkeitsanalyse(3, startknoten, zielknoten)
		
		//Geschwindigkeitsanalyse
		LinkedList<Integer> geschwindigkeitsAnalyse = new LinkedList<Integer>();
		double anzahlKnoten[] = new double[32];
		/*
		double uebertragungszeit_max[] = new double[32];
		double uebertragungszeit_min[] = new double[32];
		double uebertragungszeit_med[] = new double[32];
		for(int i=2; i<33; i++){
			anzahlKnoten[i-1] = Math.pow(i, 2);
			uebertragungszeit_max[i-1] = this.geschwindigkeitsanalyse(i, 0, (int)Math.pow(i, 2)-1);
			uebertragungszeit_min[i-1] = this.geschwindigkeitsanalyse(i, 0, 1);
			uebertragungszeit_med[i-1] = this.geschwindigkeitsanalyse(i, 0, (i/2)*i+i/2);
		}

		MatlabChart fig = new MatlabChart();
		
		fig.plot(anzahlKnoten, uebertragungszeit_max, "-r", (float) 2.0, "maximale Distanz");
		fig.plot(anzahlKnoten, uebertragungszeit_min, "-b", (float) 2.0, "minimale Distanz");
		fig.plot(anzahlKnoten, uebertragungszeit_med, "-g", (float) 2.0, "mittlere Distanz");
		
		fig.RenderPlot();
		
		fig.xlabel("Anzahl Knoten");
		fig.ylabel("Übertragungszeit in µ Sekunden");
		fig.grid("on", "on");
		fig.legend("northeast");
		fig.saveas("Output/AODV/AODV_Uebertragungszeit_max_Entfernung.jpeg",640,480);
		
	
		
		//Kostenanalyse
		anzahlKnoten = new double[32];
		double kosten_max[] = new double[32];
		double kosten_min[] = new double[32];
		double kosten_med[] = new double[32];
		for(int i=2; i<33; i++){
			anzahlKnoten[i-1] = Math.pow(i, 2);
			kosten_max[i-1] = this.kostenanalyse(i, 0, (int)Math.pow(i, 2)-1);
			kosten_min[i-1] = this.kostenanalyse(i, 0, 1);
			kosten_med[i-1] = this.kostenanalyse(i, 0, (i/2)*i+i/2);
		}

		MatlabChart fig = new MatlabChart();
		
		fig.plot(anzahlKnoten, kosten_max, "-r", (float) 2.0, "maximale Distanz");
		fig.plot(anzahlKnoten, kosten_min, "-b", (float) 2.0, "minimale Distanz");
		fig.plot(anzahlKnoten, kosten_med, "-g", (float) 2.0, "mittlere Distanz");
		
		fig.RenderPlot();
		
		fig.xlabel("Anzahl Knoten");
		fig.ylabel("Energiekosten [As]");
		fig.grid("on", "on");
		fig.legend("northeast");
		fig.saveas("Output/AODV/AODV_Uebertragungskosten.jpeg",640,480);
		
		//Konstante Energiekosten
		anzahlKnoten = new double[32];
		double konstanteKostenRREQ[] = new double[32];
		double nachrichtenEmpfangen[] = new double[32];
		double gesamtKosten[] = new double[32];
		for(int i=2; i<33; i++){
			anzahlKnoten[i-2] = Math.pow(i, 2);
			
			konstanteKostenRREQ[i-2] = kostenanalyseHelloNachrichten(i);
			
			nachrichtenEmpfangen[i-2] = Math.pow(i, 2)*0.0054*(AODV_Knoten.HELLO_INTERVAL-Math.pow(RREQ.RREQ_UEBERTRAGUNGSZEIT,-6));
		
			gesamtKosten[i-2] = konstanteKostenRREQ[i-2] + nachrichtenEmpfangen[i-2];
		}
		fig = new MatlabChart();
		
		fig.plot(anzahlKnoten, konstanteKostenRREQ, "-r", (float) 2.0, "Energiekosten HELLO-Nachricht");
		fig.plot(anzahlKnoten, nachrichtenEmpfangen, "-g", (float) 2.0, "Energiekosten Empfangsmodus");
		fig.plot(anzahlKnoten, gesamtKosten, "-b", (float) 2.0, "Gesamtkosten");
		
		fig.RenderPlot();
		
		fig.xlabel("Anzahl Knoten");
		fig.ylabel("Energiekosten [As]");
		fig.grid("on", "on");
		fig.legend("northeast");
		fig.saveas("Output/AODV/AODV_KonstanteUebertragungskosten.jpeg",640,480);
		
		*/
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param netzwerkBreite	Definiert Anzahl an Knoten im Netzwerk
	 * @param startknoten		Knoten von dem aus Nachricht gesendet werden soll
	 * @param zielknoten		Knoten zu dem Nachricht gesndet werden soll
	 * @return					Anzahl an Übertragungen bis Paket bei Ziel angekommen ist
	 */
	/*
	public int geschwindigkeitsanalyse(int netzwerkBreite, int startknoten, int zielknoten){
		
		if(netzwerkBreite < 2){
			//Netzwerk besteht nur aus einem oder weniger Knoten, eine Analyse ist in diesem Fall nicht möglich
			return -1;
		}
		
		if(startknoten > netzwerkBreite*netzwerkBreite-1 || zielknoten > netzwerkBreite*netzwerkBreite-1){
			//Start- oder Zielknoten liegen ausserhalb des Netzwerks
			return -1;
		}
		
		AODV_Graph graph = new AODV_Graph(netzwerkBreite);
		graph.nachrichtSenden(startknoten, zielknoten);
		
		//int anzahlUebertragungen = ((AODV_Knoten)graph.getKnoten(startknoten)).getRoutingTableEntry(zielknoten).getNumberOfHops();
		return ((AODV_Knoten)graph.getKnoten(zielknoten)).getLetzteNachricht().getUebertragungszeit();
		
		
	}
	
	public double kostenanalyse(int netzwerkBreite, int startknoten, int zielknoten){
		if(netzwerkBreite < 2){
			//Netzwerk besteht nur aus einem oder weniger Knoten, eine Analyse ist in diesem Fall nicht möglich
			return -1;
		}
		
		if(startknoten > netzwerkBreite*netzwerkBreite-1 || zielknoten > netzwerkBreite*netzwerkBreite-1){
			//Start- oder Zielknoten liegen ausserhalb des Netzwerks
			return -1;
		}
		
		AODV_Graph graph = new AODV_Graph(netzwerkBreite);
		graph.nachrichtSenden(startknoten, zielknoten);
		
		//int anzahlUebertragungen = ((AODV_Knoten)graph.getKnoten(startknoten)).getRoutingTableEntry(zielknoten).getNumberOfHops();
		return graph.getNetzwerkEnergiekosten();
	}
	
	public double kostenanalyseHelloNachrichten(int netzwerkBreite){
		if(netzwerkBreite < 2){
			//Netzwerk besteht nur aus einem oder weniger Knoten, eine Analyse ist in diesem Fall nicht möglich
			return -1;
		}
		
		AODV_Graph graph = new AODV_Graph(netzwerkBreite);
		graph.nachrichtSenden(0, netzwerkBreite*netzwerkBreite -1);
		
		//int anzahlUebertragungen = ((AODV_Knoten)graph.getKnoten(startknoten)).getRoutingTableEntry(zielknoten).getNumberOfHops();
		return graph.getEnergiekostenRREQs();
	}
	
	*/
	
	public long lifetimeAnalysis(int networkWidth, double sendProbability){
		networkLifetime = 0;
		int simulatedHours = 0;
		int simulatedDays = 0;

		NetworkGraph graph = new AodvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for(int id=0; id<networkNodes.length; id++){
			networkNodes[id].setSimulator(this);
		}
		
		
		char dataToSend[] = {'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D'}; 
		
		
		PayloadMessage msg = new PayloadMessage(0, (networkWidth*networkWidth-1), dataToSend);
		//((AodvNetworkNode)networkNodes[0]).sendMessage(msg);
		
		
		do{
			
			
			if(networkLifetime % 1000 == 0){
				//every 10 seconds
				for(int id=0; id<networkNodes.length; id++){
					double random = Math.random();
					if(random <= sendProbability){
						
						//find random destination
						int randomDestination = (int)(Math.random()*networkNodes.length);
						
						PayloadMessage tmpMsg = new PayloadMessage(id , randomDestination, dataToSend);
						((AodvNetworkNode)networkNodes[id]).startSendingProcess(tmpMsg);
					}
				}
			}
			
			
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].generateRandomTransmissionLoad(sendProbability, networkNodes.length);
			}
			
			
			
			// TODO: performe 1 msec
			for(int id=0; id<networkNodes.length; id++){
				networkNodes[id].performAction();
			}
			
			networkLifetime++;
			
			
			
			if(networkLifetime % (3600000) == 0){
				simulatedHours++;
				System.out.println("Simulated hours: " + simulatedHours);
			}
			
			
			if(networkLifetime % (86400000) == 0){
				simulatedDays++;
				System.out.println("Simulated days: " + simulatedDays);
			}
		
		//}while(networkNodes[networkWidth*networkWidth-1].getNumberOfRecivedPayloadMessages() == 0);
		}while(allNodesAlive(networkNodes));//while(networkLifetime < 3600000);//
		
		
		
		//System.out.println("Network Lifetime:" + networkLifetime/1000/60/60/24 + " Tage bzw "+ networkLifetime/1000 + " Sekunden.");
		
		return networkLifetime;
	}






	public long speedAnalysis(int networkWidth, int sourceNodeId, int destinationNodeId) {
		
		networkLifetime = 0;

		NetworkGraph graph = new AodvNetworkGraph(networkWidth);
		NetworkNode networkNodes[] = graph.getNetworkNodes();
		for(int id=0; id<networkNodes.length; id++){
			networkNodes[id].setSimulator(this);
		}
		
		char dataToSend[] = {'H', 'E', 'L', 'O', ' ', 'W', 'O', 'R', 'L', 'D'}; 
		
		
		PayloadMessage msg = new PayloadMessage(0, (destinationNodeId), dataToSend);
		((AodvNetworkNode)networkNodes[sourceNodeId]).startSendingProcess(msg);
		
		
		do{
			// Performe 1 ms every iteration
			
			for(int id=0; id<networkNodes.length; id++){
				((AodvNetworkNode)networkNodes[id]).performAction();
			}
			
			networkLifetime++;
		
		}while(networkNodes[destinationNodeId].getNumberOfRecivedPayloadMessages() == 0);
		
		long transmissionTime = networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getEndTransmissionTime() - networkNodes[destinationNodeId].getLastRecivedPayloadMessage().getStartTransmissionTime();

		return transmissionTime;
	}






	






	@Override
	public long partitioningAnalysis(int networkWidth, double sendProbability) {
		// TODO Auto-generated method stub
		return 0;
	}






	@Override
	public long energyCostAnalysis(NetworkGraph graph, int networkWidth, int sourceNodeId, int destinationNodeId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
