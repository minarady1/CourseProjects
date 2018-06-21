import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.util.Random;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;

import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;



import jade.wrapper.*;
public class MasterAgent extends GuiAgent{
	Random random = new Random();
	MasterAgentGui window;
	public static String ServerIP;
	public static int ServerPort;
	public static boolean attack = false;
	public int sample_counter =0;
		protected void setup() {
			try {
					Object[] args = getArguments();
					if (args.length>0)
					{
						//System.out.println("arg 0 ["+args[0].toString()+"]");
						//System.out.println("Launching: "+getLocalName());
						if (args[0].toString().equals("true"))
						{
							//System.out.println("will create gui");
							window = new MasterAgentGui();
							window.frame.setVisible(true);
							window.setAgent(this);
							ServerIP =window.textServerIP.getText();
							ServerPort= Integer.parseInt(window.textPort.getText());
						}
						 
					}
					addBehaviour(new TickerBehaviour(this, 1000) {
					protected void onTick() {
					// perform operation Y
					try {
						if (attack){
						//System.out.println("I am about to send in "+getLocalName());
						String sentence;
						String modifiedSentence;
						BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
						Socket clientSocket = new Socket(ServerIP,ServerPort );
						DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
						BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						int randomNumber = random.nextInt(39) + 1;
						sentence = Integer.toString(randomNumber);
						outToServer.writeBytes(sentence + '\n');
						modifiedSentence = inFromServer.readLine();
						//System.out.println("Response to "+getLocalName()+":" + modifiedSentence);
						clientSocket.close();
						//sample_counter=sample_counter+1;
						//if (sample_counter==50)
						//{
						//	sample_counter =0;
						//	System.out.println("Response to "+getLocalName()+":" + modifiedSentence);
						//}
						
						}
					}catch (Exception e){
						e.printStackTrace();
					};
					}
					} );
										

				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		@Override
	protected void onGuiEvent(GuiEvent arg0) {
		// TODO Auto-generated method stub
		//addBehaviour(new SendMessage());

	}
	
}
 class MasterAgentGui {

	public JFrame frame;
	public MasterAgent myAgent;
	private JTextField textNumAgents;
	private JTextField textAgentName;
	public JTextField textServerIP;
	public JTextField textPort;
	static int ArmyCount =0;
	static int AgentCount =0;
	/**
	 * Launch the application.
	 */
/* 	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MasterAgentGui window = new MasterAgentGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} */

	/**
	 * Create the application.
	 */
	public MasterAgentGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 656, 505);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textNumAgents = new JTextField();
		textNumAgents.setText("1000");
		textNumAgents.setBounds(55, 80, 400, 22);
		frame.getContentPane().add(textNumAgents);
		textNumAgents.setColumns(10);
		
		JLabel lblTextnumagents = new JLabel("Number of Agents");
		lblTextnumagents.setBounds(55, 51, 168, 29);
		frame.getContentPane().add(lblTextnumagents);
		
		textAgentName = new JTextField("Master");
		textAgentName.setBounds(55, 139, 400, 22);
		frame.getContentPane().add(textAgentName);
		textAgentName.setColumns(10);
		
		JLabel lblAgentname = new JLabel("Attacker AgentName");
		lblAgentname.setBounds(55, 115, 228, 16);
		frame.getContentPane().add(lblAgentname);
		
		textServerIP = new JTextField("FibServerLB-1204110643.us-west-2.elb.amazonaws.com");
		textServerIP.setBounds(55, 194, 400, 22);
		frame.getContentPane().add(textServerIP);
		textServerIP.setColumns(10);
		
		JLabel lblServerIp = new JLabel("Victim IP");
		lblServerIp.setBounds(55, 175, 134, 16);
		frame.getContentPane().add(lblServerIp);
		
		textPort = new JTextField("8090");
		textPort.setBounds(55, 248, 400, 22);
		frame.getContentPane().add(textPort);
		textPort.setColumns(10);
		
		JLabel lblPort = new JLabel("Victim Port");
		lblPort.setBounds(54, 229, 144, 16);
		frame.getContentPane().add(lblPort);
		
		JButton btnGenerateAgents = new JButton("Generate Agents");
		btnGenerateAgents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Place your code here
				myAgent.attack = true;
				jade.core.Runtime runtime = jade.core.Runtime.instance();
				//Create a Profile, where the launch arguments are stored
				Profile profile = new ProfileImpl();
				profile.setParameter(Profile.CONTAINER_NAME, "TestContainer");
				profile.setParameter(Profile.MAIN_HOST, "172.31.40.61");
				profile.setParameter(Profile.MAIN_PORT, "1099");
				//create a non-main agent container
				ContainerController container = runtime.createAgentContainer(profile);
				try {
					
					AgentCount= Integer.parseInt(textNumAgents.getText());
					for (int i =ArmyCount;i<ArmyCount+AgentCount;i++){
						AgentController ag = container.createNewAgent(textAgentName.getText()+new Integer(i).toString(), 
													  "MasterAgent", 
													  new Object[] {"false"});//arguments
												
						ag.start();
					}
					ArmyCount +=AgentCount;
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			}
		});
		btnGenerateAgents.setBounds(93, 308, 161, 25);
		frame.getContentPane().add(btnGenerateAgents);
	}
	 public void setAgent(MasterAgent a) {
        myAgent = a;
    }
	
}
