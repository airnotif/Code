import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
import java.net.ServerSocket;
import java.io.BufferedInputStream;
import java.net.*;
import java.io.*;
public class Serveur extends JFrame implements ActionListener
{
    private JPanel panel;
    private JButton sendRequest;
    private JLabel label;
    private List<Data> DataToCompare = new ArrayList<Data>();
    private Connection con;
    public Serveur()
    {
        super("Serveur");       
        setSize(400, 200);
        this.setLayout(new FlowLayout());
        label = new JLabel("");
        add(label);
        sendRequest = new JButton("Send");
        sendRequest.addActionListener(this);
        add(sendRequest);
        setVisible(true);        
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");  
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/system?autoReconnect=true&useSSL=false","root","folio109");
            System.out.println("Database connected!");
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        this.main();
    }

    public void actionPerformed(ActionEvent e)
    {

        if(e.getSource() == sendRequest)
        {

            try
            {
                Socket s = new Socket("192.168.219.231",7801);
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                pw.write("Coucou");
                pw.flush();
                pw.close();
                s.close();
            }
            catch (UnknownHostException ea) {
                ea.printStackTrace();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

    }

    public void main()
    {
        final File dossier = new File("DossierPartage");
        NewConnection();
        boolean copieMachine;
        String query;
        String query3;
        Statement st;
        Statement st3;
        ResultSet rs;
        ResultSet rs3;
        PreparedStatement pstmt;
        String sqlUpdate = "UPDATE machine "
            + "SET date_machine = ? "
            + ",programme_machine = ? "
            + ", status_system_machine = ? "
            + ",status_process_machine = ? "
            + ",nogo_machine = ? "
            + ",session_machine = ? "
            + "WHERE id_machine = ?";
        while(true)
        {
            for (final File fileEntry : dossier.listFiles()) {
                xmlDecoding xml = new xmlDecoding(fileEntry);                
                DataToCompare.add(xml.getData());
            }
            query = "SELECT * FROM machine";
            query3 = "SELECT * FROM utilisateur";
            try
            {
                for (int k=0;k<DataToCompare.size();k++)
                {
                    copieMachine=false;
                    st = con.createStatement();
                    pstmt = con.prepareStatement(sqlUpdate);
                    rs = st.executeQuery(query);
                    st3 = con.createStatement();
                    rs3 = st3.executeQuery(query3);                    
                    while (rs.next())
                    {
                        if(rs.getString("id_machine").equals(DataToCompare.get(k).getStation()))
                        {
                            if(isMoreRecent(DataToCompare.get(k).getDate(),rs.getString("date_machine"))==true)
                            {
                                if(DataToCompare.get(k).getProcessStatus().equals(rs.getString("status_process_machine"))== false)
                                {                                    
                                    System.out.println(DataToCompare.get(k).getProcessStatus());
                                    while(rs3.next())
                                    {
                                        System.out.println("rechercher user");
                                    }

                                }
                                pstmt.setString(1, DataToCompare.get(k).getDate());
                                pstmt.setString(2, DataToCompare.get(k).getProgram());
                                pstmt.setString(3, DataToCompare.get(k).getSystemStatus());
                                pstmt.setString(4, DataToCompare.get(k).getProcessStatus());
                                pstmt.setInt(5, Integer.parseInt(DataToCompare.get(k).getNogo()));
                                pstmt.setString(6, DataToCompare.get(k).getSession());
                                pstmt.setString(7, DataToCompare.get(k).getStation());
                                pstmt.executeUpdate(); 

                            }

                            copieMachine=true;
                            break;
                        } 
                    }
                    if (copieMachine==false)
                    {
                        String query2 = "INSERT INTO machine (id_machine,date_machine,programme_machine,status_system_machine,status_process_machine,nogo_machine,session_machine) VALUES('"+DataToCompare.get(k).getStation()+"','"+DataToCompare.get(k).getDate()+"','"+DataToCompare.get(k).getProgram()+"','"+DataToCompare.get(k).getSystemStatus()+"','"+DataToCompare.get(k).getProcessStatus()+"','"+DataToCompare.get(k).getNogo()+"','"+DataToCompare.get(k).getSession()+"')";
                        int rs2 = st.executeUpdate(query2);
                    }

                    st.close();
                    st3.close();
                    pstmt.close();
                }
            }
            catch (Exception e)
            {
                throw new IllegalStateException("bug comparaison ou bug insertion dans MySQL", e);
            }
        }
    }

    public boolean isMoreRecent(String p1,String p2)
    {
        if(Integer.parseInt(Character.toString(p1.charAt(6))+Character.toString(p1.charAt(7))+Character.toString(p1.charAt(8))+Character.toString(p1.charAt(9)))>Integer.parseInt(Character.toString(p2.charAt(6))+Character.toString(p2.charAt(7))+Character.toString(p2.charAt(8))+Character.toString(p2.charAt(9))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(3))+Character.toString(p1.charAt(4)))>Integer.parseInt(Character.toString(p2.charAt(3))+Character.toString(p2.charAt(4))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(0))+Character.toString(p1.charAt(1)))>Integer.parseInt(Character.toString(p2.charAt(0))+Character.toString(p2.charAt(1))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(11))+Character.toString(p1.charAt(12)))>Integer.parseInt(Character.toString(p2.charAt(11))+Character.toString(p2.charAt(12))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(14))+Character.toString(p1.charAt(15)))>Integer.parseInt(Character.toString(p2.charAt(14))+Character.toString(p2.charAt(15))))
            return true;
        if(Integer.parseInt(Character.toString(p1.charAt(17))+Character.toString(p1.charAt(18)))>=Integer.parseInt(Character.toString(p2.charAt(17))+Character.toString(p2.charAt(18))))
            return true;
        return false;
    }

    public boolean connexion(String identifiant,String ip)
    {
        try{
            boolean trouvee=false;
            Statement st4 = con.createStatement();
            ResultSet rs4 = st4.executeQuery("SELECT * FROM utilisateur");
            String sqlUpdate = "UPDATE utilisateur "
                + "SET adresseIp_utilisateur = ? "
                + "WHERE identifiant_utilisateur = ?";
            while (rs4.next())
            {
                if(rs4.getString("identifiant_utilisateur").equals(identifiant))
                {
                    trouvee=true;
                    System.out.println("connexion ok");
                    if(rs4.getString("adresseIp_utilisateur").equals(ip))
                    { 

                    }
                    else
                    {
                        PreparedStatement pstmt = con.prepareStatement(sqlUpdate);
                        pstmt.setString(1,ip);
                        pstmt.setString(2,identifiant);
                        pstmt.executeUpdate(); 
                    }
                    return true;
                }
            }
            if(trouvee==false)
            {
                System.out.println("identifiant introuvable");  
            }

        }
        catch (Exception e)
        {
            throw new IllegalStateException("bug query", e);
        }
        return false;

    }

    public  ArrayList<ArrayList<String>> listeMachine()
    {
        try{
            ArrayList< ArrayList<String>> liste_machine=new ArrayList< ArrayList<String>>();
            ArrayList<String> liste_donnee=new ArrayList<String>();
            boolean trouvee=false;
            Statement st4 = con.createStatement();
            ResultSet rs4 = st4.executeQuery("SELECT * FROM machine");
            liste_donnee.add(Boolean.toString(true));
            liste_machine.add(liste_donnee);
            while (rs4.next())
            {

                liste_donnee=new ArrayList<String>();
                liste_donnee.add(rs4.getString("id_machine"));
                liste_donnee.add(rs4.getString("status_system_machine"));
                liste_donnee.add(rs4.getString("status_process_machine"));
                liste_donnee.add(rs4.getString("nogo_machine"));
                liste_donnee.add(rs4.getString("session_machine"));
                liste_machine.add(liste_donnee);
                
            }
            liste_donnee.add(Boolean.toString(false));
            liste_machine.add(liste_donnee);
            return liste_machine;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("bug query", e);
        }

    }

    public  ArrayList<ArrayList<String>> listeMachineUtilisateur(String identifiant)
    {
        try{
            ArrayList<String> liste_machine_utilisateur=new ArrayList<String>();
            ArrayList<String> liste_donnee=new ArrayList<String>();
            ArrayList<ArrayList<String>> liste_machine=new ArrayList<ArrayList<String>>();
            boolean trouvee=false;
            Statement st4 = con.createStatement();
            ResultSet rs4 = st4.executeQuery("SELECT * FROM machine_utilisateur");
            while (rs4.next())
            {
                if(rs4.getString("identifiant_utilisateur").equals(identifiant))
                {
                    liste_machine_utilisateur.add(rs4.getString("id_machine"));
                }
            }
            System.out.println(liste_machine_utilisateur);
            for (String machine : liste_machine_utilisateur) { 		      
                rs4 = st4.executeQuery("SELECT * FROM machine");	
                while (rs4.next())
                {
                    if(trouvee==false)
                    {
                        trouvee=true;
                        liste_donnee.add(Boolean.toString(true));
                        liste_machine.add(liste_donnee);
                    }
                    if(rs4.getString("id_machine").equals(machine))
                    {
                        liste_donnee=new ArrayList<String>();
                        liste_donnee.add(rs4.getString("id_machine"));
                        liste_donnee.add(rs4.getString("status_system_machine"));
                        liste_donnee.add(rs4.getString("status_process_machine"));
                        liste_donnee.add(rs4.getString("nogo_machine"));
                        liste_donnee.add(rs4.getString("session_machine"));
                        liste_machine.add(liste_donnee);

                    }
                }
                return(liste_machine);
            }
            liste_donnee.add(Boolean.toString(false));
            liste_machine.add(liste_donnee);
            return liste_machine;
        }
        catch (Exception e)
        {
            throw new IllegalStateException("bug query", e);
        }

    }

    public boolean abonnement(String identifiant, String machine)
    {
        try{
            boolean copieMachine=false;
            Statement st4 = con.createStatement();
            ResultSet rs6 = st4.executeQuery("SELECT COUNT(*) FROM historique_abonnement");
            rs6.next();
            int taille=rs6.getInt(1);
            ResultSet rs4 = st4.executeQuery("SELECT * FROM utilisateur");

            while (rs4.next())
            {
                if(rs4.getString("identifiant_utilisateur").equals(identifiant))
                {
                    String utilisateur=rs4.getString("identifiant_utilisateur");
                    ResultSet rs5 = st4.executeQuery("SELECT * FROM machine_utilisateur");
                    while(rs5.next())
                    {
                        if(utilisateur.equals(rs5.getString("identifiant_utilisateur")) && rs5.getString("id_machine").equals(machine))
                        {                       
                            copieMachine=true;
                            break;
                        }
                    }
                    if (copieMachine==false)
                    {
                        st4.executeUpdate("INSERT INTO machine_utilisateur (id_abonnement,identifiant_utilisateur,id_machine) VALUES('"+taille+"','"+utilisateur+"','"+machine+"')");
                        st4.executeUpdate("INSERT INTO historique_abonnement (id_abonnement,identifiant_utilisateur,id_machine,is_abonnement) VALUES('"+taille+"','"+utilisateur+"','"+machine+"','"+1+"')");
                    }

                    return true;
                }
            } 
            st4.close();
        }
        catch (Exception e)
        {
            throw new IllegalStateException("bug query", e);
        }
        return false;
    }

    public boolean desabonnement(String identifiant, String machine)
    {
        try{
            Statement st4 = con.createStatement();
            ResultSet rs4 = st4.executeQuery("SELECT * FROM utilisateur");

            while (rs4.next())
            {
                if(rs4.getString("adresseIp_utilisateur").equals(identifiant))
                {
                    String utilisateur=rs4.getString("identifiant_utilisateur");
                    ResultSet rs5 = st4.executeQuery("SELECT * FROM machine_utilisateur");
                    while(rs5.next())
                    {

                        if(utilisateur.equals(rs5.getString("identifiant_utilisateur")) && rs5.getString("id_machine").equals(machine))
                        {    
                            ResultSet rs6 = st4.executeQuery("SELECT COUNT(*) FROM historique_abonnement");
                            rs6.next();
                            int taille=rs6.getInt(1);
                            st4.executeUpdate("INSERT INTO historique_abonnement (id_abonnement,identifiant_utilisateur,id_machine,is_abonnement) VALUES('"+taille+"','"+utilisateur+"','"+machine+"','"+0+"')");
                            PreparedStatement prst4 = con.prepareStatement("DELETE FROM machine_utilisateur WHERE identifiant_utilisateur = ? AND id_machine = ? ");
                            prst4.setString(1,utilisateur);
                            prst4.setString(2,machine);
                            prst4.executeUpdate(); 
                            break;
                        }
                    }
                    return true;
                }
            } 
            st4.close();
        }
        catch (Exception e)
        {
            throw new IllegalStateException("bug query", e);
        }
        return false;
    }

    public void NewConnection(){
        try{
            int port = 5555;
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Server launch");
            while (true) 
            {
                Socket socketClient= socketServeur.accept();
                //System.out.println("Connexion avec : "+socketClient.getInetAddress());
                CommunicationThread CT = new CommunicationThread(socketClient);
            }

        }
        catch (Exception e){
            System.out.println("Erreur newconnection()");
        }
    }

    public class CommunicationThread extends Thread {
        Socket socketClient;
        public CommunicationThread(Socket socketClient){
            this.socketClient=socketClient;
            this.start();
            System.out.println("yo");
        }

        public void run(){
            //System.out.println("run "+this.socketClient.getInetAddress());
            traitements();
        }

        public void traitements(){
            ArrayList<String> message=new  ArrayList<String>();
            ArrayList<String> out=new  ArrayList<String>();
            ArrayList< ArrayList<String>> machineOut;
            try{
                //BufferedReader in=new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
                //PrintStream out=new PrintStream(socketClient.getOutputStream());
                ObjectInputStream objectInput = new ObjectInputStream(socketClient.getInputStream());
                ObjectOutputStream objectOutput = new ObjectOutputStream(socketClient.getOutputStream());
                System.out.println("traitements "+this.socketClient.getInetAddress());
                while( (objectInput) != null)
                {
                    try
                    {             
                        message =(ArrayList<String>) objectInput.readObject();
                        System.out.println(message);
                        break;
                    }
                    catch (Exception e){
                        System.out.println("Erreur traitements()");
                    }
                }
                if (message.get(0).equals("0"))
                {
                    System.out.println("ok");
                    String Ip="'"+this.socketClient.getInetAddress()+"'";
                    String Id=message.get(1);
                    out.add(Boolean.toString(connexion(Id,Ip)));
                    objectOutput.writeObject(out);
                    //System.out.println(message+" connect� IP: "+this.socketClient.getInetAddress());
                    //out.println("Bonjour "+message+" Envoi DATA");
                }

                if (message.get(0).equals("1"))
                {
                    //String Id=message.get(1);
                    machineOut=(listeMachine());
                    objectOutput.writeObject(machineOut);
                }
                if (message.get(0).equals("2"))
                {
                    String Id=message.get(1);
                    machineOut=(listeMachineUtilisateur(Id));
                    objectOutput.writeObject(machineOut);
                }
                if (message.get(0).equals("3"))
                {
                    String Id=message.get(1);
                    String Machine=message.get(3);
                    out.add(Boolean.toString(abonnement(Id,Machine)));
                    objectOutput.writeObject(out);
                }
                if (message.get(0).equals("4"))
                {
                    String Id=message.get(1);
                    String Machine=message.get(3);
                    out.add(Boolean.toString(desabonnement(Id,Machine)));
                    objectOutput.writeObject(out);
                }
                System.out.println("Ferme");
                socketClient.close();
            } catch (Exception e){
                System.out.println("Erreur traitements()");
            }
        }  
    }
}

