//Documentation In Progress
import java.io.*;
import java.net.*;
import java.util.*;
public class Angel {
	//public  String Prefix;
	//public  int N,T,C;
	//public  int X_size;
	public DataOutputStream outToClient;
	List<String> PeerIDs;
	List<Integer> ports;
	//public ProcessCommand PC;
	public Angel (String [] args){
		try 
		{

		PeerIDs = new ArrayList<String>();
		ports= new ArrayList<Integer>();
		String clientSentence;
		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
		System.out.println("Socket open at : " + args[0] );
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient =
			new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			this.outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			this.outToClient.writeBytes("welcome\n");
			while ((clientSentence = inFromClient.readLine())!=null)
			{
				System.out.println("Received: " + clientSentence);
				outToClient.writeBytes("OutToClientReceived: " + clientSentence+"\n");
				ProcessCommand PC = new ProcessCommand	(clientSentence);
				//PC.cmd=clientSentence;
				PC.run();
				//A.ProcessCommand(clientSentence);
				
			}
		}
		}catch(Exception e) {e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		Angel A= new Angel(args);
		
	}
	public class ProcessCommand extends Thread
	{
		String [] args;
		public String cmd;
		ProcessCommand(){
		}
		ProcessCommand(String CMD){
			try {
			this.args = CMD.split(" ");//
			System.out.println(Arrays.toString(args));
			outToClient.writeBytes("inside PC constructor");
			}catch (Exception e) {e.printStackTrace();};
		}
		public void run() {
		
		if (this.args[0].equals("initialize"))
		{
			
			System.out.println(args[0]);
			this.Initialize(
					Integer.parseInt(args[1]),//number of peers
					args[2].trim(), //prefix
					Integer.parseInt(args[3]), //port range start
					Integer.parseInt(args[4]), //N of storage elements
					Integer.parseInt(args[5]));//Storage cell size in bits
		}
		if (args[0].equals("run"))
		{
			this.AgentRun(
					Integer.parseInt(args[1]),//number of peers
					args[2].trim(), //prefix
					Integer.parseInt(args[3])); //port range start
					//Integer.parseInt(args[4]), //N of storage elements
					//Integer.parseInt(args[5]));//Storage cell size in bits
		}
		/*
		 * if command = search content T
		 * args[0]
		 * for every registered host
		 * 	compile command
		 *  open tcpclient with host,port,command
		 *  result on output
		 * 
		 * */
		if (this.args[0].equals("search"))
		{
			//System.out.println(args[0]);
			for (int i=0; i<PeerIDs.size();i++)
			{
			this.Search(
					PeerIDs.get(i),
					ports.get(i),
					args[1], //content
					args[2], //X_Size
					args[3]);//T
			}
		}
		if (this.args[0].equals("store"))
		{
			//System.out.println(args[0]);
			for (int i=0; i<PeerIDs.size();i++)
			{
			this.Store(
					PeerIDs.get(i),
					ports.get(i),
					args[1], //content
					args[2], //X_Size
					args[3],//T
					args[4]);//C
			}
		}
	}
	void Search (String ID, int port, String content, String X_size, String T)
	{
		String [] cmd = {"search",ID,content,X_size, T};
		TCPClient tp = new TCPClient("localhost",port,cmd);
		Thread TT= new Thread(tp);
		TT.start();
		//PeerRunner PR = new PeerRunner(ID,port,cmd);
		//PR.run();
	}
	void Store (String ID, int port, String content, String X_size, String T, String C)
	{
		
		String [] cmd = {"store",ID,content,X_size, T, C};
		try {
			outToClient.writeBytes("Inside stire in angel\n");
		outToClient.writeBytes(Arrays.toString(cmd)+"\n");
		}catch (Exception e) {e.printStackTrace();}
		TCPClient tp = new TCPClient("localhost",port,cmd);
		Thread TT= new Thread(tp);
		TT.start();
		//PeerRunner PR = new PeerRunner(ID,port,cmd);
		//PR.run();
	}
	void Initialize(int Peers,String Prefix,int port_range_start, int N, int X_size)
	{
		// initialize [ID] [port] [N] [X]
		//int port_range_start = 2000;
		for (int i =0; i<Peers; i++)
		{
			int port = port_range_start+i;
			String [] cmd = {"initialize", 
							Prefix+ "_"+Integer.toString(i),
							Integer.toString(port),
							Integer.toString(N),
							Integer.toString(X_size)}; 
			//System.out.println(Arrays.toString(cmd));
			MicroAgent MA = new MicroAgent(cmd);//client side
			Thread T = new Thread(MA);
			T.start();
			
		}
	}
	void AgentRun(int Peers,String Prefix,int port_range_start)
	{
		// initialize [ID] [port] [N] [X]
		//int port_range_start = 2000;
		for (int i =0; i<Peers; i++)
		{
			int port = port_range_start+i;
			String [] cmd = { "run", 
					Prefix+ "_"+Integer.toString(i),
					Integer.toString(port)};
			PeerIDs.add(Prefix+ "_"+Integer.toString(i));
			ports.add(port);
			PeerRunner PR = new PeerRunner(Prefix+ "_"+Integer.toString(i), port, cmd);
			PR.run();
			
		}
	}
	
}
	public class PeerRunner extends Thread{
		String ID;
		int port;
		String [] cmd;
		PeerRunner (String ID, int port, String [] cmd)
		{
			this.ID = ID;
			this.port=port;
			this.cmd=cmd;
		}
		public void run()
		{
			try {
				 
					MicroAgent MA = new MicroAgent(cmd);
					//MA.run();//client side
					//Thread MA_T = new Thread(MA);
					//MA_T.start();
					//System.out.println(Arrays.toString(cmd));
					//MicroAgent.main(cmd);//client side
			/*		Runtime rt = Runtime.getRuntime();
		           
		            Process proc = rt.exec(cmd);          
		                		           
		            InputStreamReader isr = new InputStreamReader(proc.getInputStream());
		            BufferedReader br = new BufferedReader(isr);
		            String line =null;
		            while ( (line = br.readLine()) != null)
		            {
		            	System.out.println(line);
		            	//os.write(line.getBytes());
		            	//textOutput.append("Response from: "+this.cmdString+": \n"+line+"\n");
		            	outToClient.writeBytes(line+"\n");
		            }*/
		}catch (Exception e) {e.printStackTrace();}
		}
	}
	
	class TCPClient extends Thread{
		String host,CMD;
		int port;
		
		TCPClient (String host, int port, String [] CMD) {
			try {
			outToClient.writeBytes("inside TCP constructor\n");
			}catch (Exception e) {e.printStackTrace();}
			this.host=host;
			this.port = port;
			this.CMD=ArrayJoin(Arrays.asList(CMD)," ");
		}
		  public void run () {
				 
				  
				  try {
				  //BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					 outToClient.writeBytes("running TCP\n");
					 outToClient.writeBytes(CMD +"\n");
				  Socket clientSocket = new Socket(this.host, this.port);
				  
				  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				  BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				  
				  outToServer.writeBytes(CMD + '\n');
				  String result = null;
				  
				  while ((result = inFromServer.readLine())!=null) {
				  
				  //System.out.println("FROM SERVER: " + modifiedSentence);
				  outToClient.writeBytes(result.trim()+"\n");
				  }
				  //
				  clientSocket.close();
				  
				  }catch (Exception ex) {ex.printStackTrace();}
		 }
		  public String ArrayJoin (List <String> l, String del)
			{
				String res = "";
				for (int i =0;i<l.size();i++)
				{

					res += l.get(i);
					if (i!=l.size()-1)
						res+=del;
				}
				return res;
			}
		
		}
}

