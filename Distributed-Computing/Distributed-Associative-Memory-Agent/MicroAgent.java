/*
 * The local agent for Neural Distributed Associatev Memory System
	>>> Documentation In Progress
	By Mina Rady (moc.liamg@ydaranim)
 
 * Initialize (N) //Initialize Store Locations
 * Search (X,T) // Search the addresses for X, returns addreses matching with C threshold
 * Store (X,T,C)// Store at addresses matching with T threshold with +C,-C limits 
 * 
 * String ID: ID of current Peer
	
 * */

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.text.html.CSS;
import javax.xml.bind.DatatypeConverter;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class MicroAgent  extends Thread {
	String [] args;
	MicroAgent (String [] args){
		this.args=args;
	}
	public  static void main(String[] args) {
		
	}
	public void run() {
		try {
			System.out.println("Initializing Socket");
			System.out.println(Arrays.toString(args));
			//MA.run();
			ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[2]));
			while (true) {
				
				Socket connectionSocket = welcomeSocket.accept();
				MicroAgentServer MA = new MicroAgentServer(args,connectionSocket);
				

				}
			}catch (Exception e) {e.printStackTrace();}	
	}
	
}
class MicroAgentServer extends Thread{
	public  String ID;
	public  int N,T,C, port;
	public  int X_size;
	public  byte[]r;
	public int [] search_result;
	public DataOutputStream outToClient;
	public BufferedReader inFromClient;
	public Socket clientSocket;
	public String [] args;
	MicroAgentServer(){}
	MicroAgentServer(String [] args){
		this.args=args;
	}
	MicroAgentServer(String [] args, Socket connectionSocket){
		
		this.args=args;
		try {
		inFromClient =new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		System.out.println("----inside MAS constructor");
		System.out.println(Arrays.toString(this.args));
		
		this.start();
		}catch (Exception e) {e.printStackTrace();}
	}
	public void run() {

		try 
		{
			System.out.println("----inside MAS runner");
			System.out.println(Arrays.toString(this.args));
			String clientSentence="";
			if (args[0] .equals( "initialize"))
			{
				System.out.println(args[0]);

				System.out.println(Arrays.toString(args));
				
				this.ID =args[1];
				this.port = Integer.parseInt(args[2]);
				this.N= Integer.parseInt(args[3]);
				this.X_size =  Integer.parseInt(args[4]);
				System.out.println(this.Initialize()); //Initialize Storage
			}
			while ((clientSentence = inFromClient.readLine())!=null)
			{   
				this.outToClient.writeBytes("--------inside MicroAgent\n");
				ProcessCommand(clientSentence);
				
			}

		}catch(Exception e) {e.printStackTrace();}
		
	}

	public void ProcessCommand(String CMD)
	{
		String [] args = CMD.split(" ");
		System.out.println(Arrays.toString(args));
		try {
		
		if (args[0] .equals( "run"))
		{
			//System.out.println(args[0]);

			System.out.println(Arrays.toString(args));
			
			this.ID =args[1];
			this.port = Integer.parseInt(args[2]);

		}	
		if (args[0] .equals( "store"))
		{
			this.ID =args[1];
			//this.X_size =  Integer.parseInt(args[2]);
			System.out.println("MicroAgent Store");
			System.out.println(args[2]);
			String content = args[2];// content = "1;-1;1;1"
			this.X_size = Integer.parseInt(args[3]);
		
			this.T =  Integer.parseInt(args[4]);
			this.C =  Integer.parseInt(args[5]);
			int [] content_int= StringToIntArrayFlex(content,"_");//{1,1, etc.}
			BitSet content_bin = BipolarToBin(content_int);
			List <String> res = this.Store(content_bin, this.T,this.C);
			
			for (int i=0;i<res.size();i++)
			{
				outToClient.writeBytes(res.get(i).trim());
				outToClient.writeBytes("_".trim());
				outToClient.flush();
			}
		}
		if (args[0] .equals( "search"))
		{
			this.ID =args[1];
			String content = args[2];// content = "1 -1 1 -1"
			this.X_size =  Integer.parseInt(args[3]);
			this.T =  Integer.parseInt(args[4]);
			this.search_result=new int [0];
			
			int [] content_int= StringToIntArrayFlex(content,"_");//{1,1, etc.}
			BitSet content_bin = BipolarToBin(content_int);
			int [] res =this.Search(content_bin, this.T);
			//outToClient.writeBytes("-------RESULT\n");
			this.outToClient.writeBytes("-------RESULT1\n");
			this.outToClient.writeBytes(Arrays.toString(res)+"\n");
			this.outToClient.writeBytes(IntArrayJoin(res,"_")+"\n");
			//this.outToClient.writeBytes(";".trim());
			this.outToClient.flush();
		}}catch (Exception e) {e.printStackTrace();}
	}
	      
	String Initialize()
	{
		String msg ="";
		//Create Storage Directory: storage
		File theDir = new File("/home/ubuntu/Exam/storage/");
		File theFile = new File("/home/ubuntu/Exam/storage/"+ID + ".storage");
		boolean result1 = true;
		
		PrintWriter writer;
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + theDir.getName());
		     result1 = false;

		    try{
		        theDir.mkdir();
		        result1 = true;
		    } 
		    catch(SecurityException se){

		        //handle it
		    }        
		    if(result1) {    
		        System.out.println("Directory created");  
		    }
		}
		     
	
	    if (result1)
	    {
		try {	
				writer = new PrintWriter(theFile, "UTF-8");
				for (int i =0;i<N;i++)
				{
					// file i
					byte [] Rand = GenRandomBytes(X_size/8);
					//Encodeer
					String encoded = DatatypeConverter.printHexBinary(Rand);
					while (encoded.contains(","))
					{
						Rand = GenRandomBytes(X_size/8);
						encoded = DatatypeConverter.printHexBinary(Rand);
					}
					
					writer.println(encoded+",");
					
					//Create N random addresses of Size X
					//Store X locations at the file
				
				}
				 msg = Integer.toString(N) + " Storage locations have been successfully created in "+theFile.getName() ;
				writer.close();
			} catch (Exception e) {e.printStackTrace();}
		}
		return msg;
	}
	
	//Store item in areas with T threshold in address comparison
	
		/*
		 * Open File
		 * Get all lines
		 * For each line:
		 * 	Split by ,
		 * 	Compare First cell(addr) with item
		 * 	if hamming distance < Search_T
		 * 		Sum with search_result storage
		 * */
		public int [] Search (BitSet item, int Search_T) {
			String msg="";
			PositionSumResult result = new PositionSumResult(0);
			try {
			System.out.println("Will search: ");
			PrintBits(item);
			
			File theFile = new File("/home/ubuntu/Exam/storage/"+ID + ".storage");
	        FileReader fileReader = new FileReader(theFile);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        List<String> lines = new ArrayList<String>();
	        String line = null;
	        int counter =0;
	        
	        while ((line = bufferedReader.readLine()) != null) {
	        	counter++;
	            String [] row= line.split(",");
	            byte [] addr = row[0].getBytes();
	            System.out.println(row[0]);
	            if (row.length>=1)
	            {
	            	int d = this.HammingDistance(BitSet.valueOf(addr),item);

	            	PrintBits(BitSet.valueOf(DatatypeConverter.parseHexBinary(row[0])));
	            	System.out.println(d);
	            	
	            	if (d<=T)
	            	{
	            		// if nothing was stored, skip

	            		//Otherwise, DO POSITIONWISE SUMMATION!
        				System.out.println("found match at "+counter+ " with distance "+ d);

	            		if (row.length==2)
	            		{
	            			int [] record= StringToIntArray(row[1]);
	            			
	            			if (this.search_result.length==0)
	            			{	System.out.println("first result at "+counter);
	            				this.search_result= record;
	            			
	            			}
	            			else 
	            			{
		            			int [] item_int = BinToBipolar (item);
	
	
		            			if (item_int.length == this.search_result.length)
		            			{
		            				result = PositionSum(this.search_result,item_int, false);
	
			            			if (result.valid) {
			            				//String newline = row[0]+","+ Arrays.toString(res).replaceAll(",", "\t");
			            				System.out.println("result summed");
			            				System.out.println(Arrays.toString(result.result));
			            			}else {
			            				System.out.println(result.msg+" at record : "+ record);
			            			}
		            			}
	            			}
	            		}

	            	}
	            }
	        	
	            //System.out.println(line);
	        }
	        bufferedReader.close();
			}catch (Exception e) {e.printStackTrace();}
			System.out.println("Final Result");
	        return result.result;
			
		}

	//Store item in areas with T threshold in address comparison
	
	/*
	 * Open File
	 * Get all lines
	 * For each line:
	 * 	Split by ,
	 * 	Compare First cell with item
	 * 	if hamming distance < T
	 * 		Store item in record.
	 * */
	public List <String> Store (BitSet item, int T, int C) {
		String msg="";
		List <String> msg_list = new ArrayList <String>();
		try {
		System.out.println("Will store: ");
		PrintBits(item);
		System.out.println("------------- ");
		File theFile = new File("/home/ubuntu/Exam/storage/"+ID + ".storage");
        FileReader fileReader = new FileReader(theFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        int record =0;
        while ((line = bufferedReader.readLine()) != null) {
        	record++;
            String [] row= line.split(",");
            byte [] addr = row[0].getBytes();
            System.out.println(row[0]);
            if (row.length>=1)
            {
            	int d = this.HammingDistance(BitSet.valueOf(addr),item);

            	PrintBits(BitSet.valueOf(DatatypeConverter.parseHexBinary(row[0])));
            	System.out.println(d);
            	
            	if (d<=T)
            	{
            		// if nothing was stored, convert and store
            		int [] res = BinToBipolar (item);
            		if (row.length==1)
            		{
            			String newline = line+ IntArrayToString(res);
            			line=newline;
        				System.out.println(" Item inserted at : "+ record);
        				msg_list.add("Item inserted at : "+ record+"\n");

            		}

            		//Otherwise, DO POSITIONWISE SUMMATION!
            		if (row.length==2)
            		{
            			int [] content_int= StringToIntArray(row[1]);
            			int [] item_int = BinToBipolar (item);
            			if (item_int.length == content_int.length)
            			{
            				PositionSumResult result = PositionSum(content_int,item_int,true);

	            			if (result.valid) {
	            				//String newline = row[0]+","+ Arrays.toString(res).replaceAll(",", "\t");
	            				System.out.println("Will Write Sum result");
	            				msg_list.add("Will Write Sum result");
	            				System.out.println(Arrays.toString(result.result));
	            				String newline = row[0]+","+IntArrayJoin(result.result," ");
	            				line=newline;
	            				System.out.println("Final Line");
	            				System.out.println(line);
	            				System.out.println(" Item Summed at : "+ record);
	            				msg_list.add(" Item Summed at : "+ record+"\n");
	            			}else {
	            				System.out.println(result.msg+" at record : "+ record);
	            				msg_list.add(result.msg+" at record : "+ record+"\n");
	            				
	            			}
            			}
            		}

            	}
            }
        	lines.add(line);
            //System.out.println(line);
        }
        bufferedReader.close();
        FileWriter writer = new FileWriter(theFile, false); 
       
        writer.write(ArrayJoin(lines,"\n"));
        writer.close();
		}catch (Exception e) {e.printStackTrace();}
        return msg_list;
		
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
	public String IntArrayJoin (int [] l, String del)
	{
		String res = "";
		for (int i =0;i<l.length;i++)
		{

			res += Integer.toString(l[i]);
			if (i!=l.length-1)
				res+=del;
		}
		return res;
	}

	public  int HammingDistance(BitSet x, BitSet y) {
		//System.out.println(this.X_size);
		//System.out.println(this.X_size);
		System.out.println("-----X------");
		PrintBits(x);
		System.out.println("-----END X------");
		System.out.println("-----Y------");
		PrintBits(y);
		System.out.println("-----END Y------");
       // if (x.size() != y.size())
         //   throw new IllegalArgumentException(String.format("BitSets have different length: x[%d], y[%d]", x.size(), y.size()));

        int dist = 0;
        for (int i = 0; i < this.X_size; i++) {
            if (x.get(i) != y.get(i))
                dist++;
        }

        return dist;
    }
	 public   byte[] GenRandomBytes(int size) {
		 	byte[] r = new byte[size]; //10000/4
			Random random = new Random();
			random.nextBytes(r);
			//System.out.println("old bits");
			//System.out.flush();
			//PrintBits(BitSet.valueOf(r));
	 	     return r;
	 	   }
	 public  String BitsToString (BitSet b)
	 {
		 String encoded = "";
		 byte [] bb = b.toByteArray();

		 /*for (int i = 0; i <b.length(); i++) {
	        encoded +=(b.get(i)?"1":"0");
	      }*/
		 encoded = DatatypeConverter.printBase64Binary(bb);
		 System.out.println("encoded string");

		 System.out.println(encoded);
		 return encoded;
	 }
	 public  BitSet StringToBits (int Size , String s64)
	 {
		 String encoded = "";
		 BitSet b = new BitSet (Size);
		 b= BitSet.valueOf(s64.getBytes());
		 System.out.println("decoded string");
		 PrintBits(b);
		/* for (int i = 0; i <b.length(); i++) {
	        encoded +=(b.get(i)?"1":"0");
	      }
	     
		 //encoded = DatatypeConverter.printBase64Binary(bb);
		 

		 System.out.println(encoded); */
		 return b;
	 }
	 public String IntArrayToString (int [] c)
	 {
		 String d = "";
		 for (int i =0;i<c.length;i++)
		 {
			d+= Integer.toString(c[i])+" ";
		 }
		 return d;
	 }
	 public int [] StringToIntArray (String c)
	 {
		 String [] vals = c.split(" ");
		 int []d = new int [vals.length];
		 for (int i =0;i<d.length;i++)
		 {
			 d[i]= Integer.parseInt(vals[i]);
		 }
		 return d;
	 }
	 public int [] StringToIntArrayFlex (String c, String del)
	 {
		 String [] vals = c.split(del);
		 int []d = new int [vals.length];
		 for (int i =0;i<d.length;i++)
		 {
			 d[i]= Integer.parseInt(vals[i]);
		 }
		 return d;
	 }
	 public PositionSumResult PositionSum(int [] a , int [] b, boolean constrained)
	 {
		System.out.println("Will Sum");
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(b));
		PositionSumResult result =  new PositionSumResult (a.length);
		 int [] res = new int [a.length];
		 int sum;
		 boolean valid = true;
		 for (int i =0;i<a.length;i++)
		 {
			 sum =a[i]+b[i];
			 if (!constrained || (sum<= this.C && sum >=this.C*-1))
			 {
				 res [i]=sum;
			 }else {
				 result.valid=false;
				 result.msg="Storage Sum Exceeded";
				 return result;
				 
			 }
		 }
		 System.out.println("Sum result");
		 System.out.println(Arrays.toString(result.result));
		 result.valid=true;
		 result.result= res;
		 return result;
	 }
	 public BitSet BipolarToBin (int [] t)
	 {
		 BitSet B = new BitSet(t.length);
		 for (int i =0;i<t.length;i++)
		 {
			 if (t[i]==1)
				 B.set(i);
		 }
		 return B;
	 }
	 public  int [] BinToBipolar (BitSet b)
	 {
		System.out.println("BinToBipolar");
		System.out.println(X_size);
		int [] res = new int [ this.X_size];
		System.out.println(res.length);
		for (int i = 0; i < this.X_size; i++) {
		        res[i] =(b.get(i)? 1:-1);
		      }		 
		 return res;
	 }
	 public int [] ReduceBipolar(int []b )
	 {
		 int [] result = new int [b.length];
		 for (int i =0;i<b.length;i++)
		 {
			 result[i]=(b[i]>=0?1:-1);
		 }
		 return result;
	 }
	 public  void PrintBits (BitSet b)
	 {
		 String encoded ="";
		 for (int i = 0; i <this.X_size; i++) {
		        encoded +=(b.get(i)?"1":"0");
		      }
			System.out.flush();

		 System.out.println(encoded);
		 System.out.flush();

	 }
	 public  void PrintBitsFlexible (BitSet b,int size)
	 {
		 String encoded ="";
		 for (int i = 0; i <size; i++) {
		        encoded +=(b.get(i)?"1":"0");
		      }
			System.out.flush();

		 System.out.println(encoded);
		 System.out.flush();

	 }

}
class PositionSumResult{
	int [] result ;
	boolean valid;
	String msg;
	PositionSumResult(int length){
		
		result = new int [length];
	}
}


