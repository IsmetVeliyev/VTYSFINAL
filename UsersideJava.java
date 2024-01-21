package userside.java;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


public class UsersideJava extends JFrame {
    private Socket socket;

    public UsersideJava() {
        initializeUI();
    }

    private void initializeUI() {
        getContentPane().setBackground(Color.GRAY);
        getContentPane().setForeground(Color.RED);
        setTitle("Client Management");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton adminButton = new JButton("OWNER");
        adminButton.setBackground(Color.LIGHT_GRAY);
        adminButton.setForeground(Color.BLACK);
        JButton workerButton = new JButton("WORKER");
        workerButton.setBackground(Color.LIGHT_GRAY);
        workerButton.setForeground(Color.BLACK);
        JButton SignAdminButton = new JButton("NEW OWNER SPACE");
        SignAdminButton.setBackground(Color.LIGHT_GRAY);
        SignAdminButton.setForeground(Color.BLACK);
        ImageIcon icon = new ImageIcon("C:\\Users\\user\\Downloads\\pngwing.com(3).png");
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);
        add(label);
        

        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectAndStartThread(1);
            }
        });

        workerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectAndStartThread(2);
            }
        });
        
        SignAdminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectAndStartThread(3);
            }
        });
        
     
        
     
        add(SignAdminButton);
        add(adminButton);
        add(workerButton);
       
        setVisible(true);
    }

    private void connectAndStartThread(int choice) {
        try {
            socket = new Socket("localhost", 5252);
            if (choice == 1) {
                Thread sender = new Thread(new Sender(socket));
                sender.start();
            } else if (choice == 2) {
                Thread sender = new Thread(new SenderWrk(socket));
                sender.start();
            }else if(choice ==3){
                Thread sender = new Thread(new SenderSign(socket));
                sender.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UsersideJava();
            }
        });
    }
    
    
}


class Sender implements Runnable {
    
    private Socket socket;
    private JFrame frame;
    private JTextField gmailField, passwordField, projectNameField, startDateField, endDateField;
    private JButton loginButton, createProjectButton;
    private JTextArea responseArea;
    private JTextField fullNameField, workerGmailField, positionField;
    private JButton addWorkerButton;
    private JTextField projectNameField1, workerNameField, missionField, missionStartField, missionEndField;
    private JButton addMissionButton;
    private JTextField workerIdField;
    private JButton deleteWorkerButton;
    private JButton getWorkerInfoButton;
    private JButton updateWorkerButton;
    private JTextField workernamefieldupdt, workergmailfield;
    private JButton getMissionInfButton;
    private JTextField nameMsnfield;
    private JButton refreshButton;
    private JPanel logPanel,CreatePPanel,CreateWPanel,AddMPanel,WorkerPanel,UpdateWorkerPanel,SitutationPanel;
    private  JTable jtp,jtw,jtm;
    private JScrollPane spp,spw,spm;
    public Sender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        frame = new JFrame("Sender - Admin Panel");
        frame.setSize(800, 900);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.getContentPane().setForeground(Color.WHITE);
        frame.setForeground(Color.RED);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        

        gmailField = new JTextField(10);
        passwordField = new JTextField(10);
        loginButton = new JButton("Login");
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setForeground(Color.BLACK);
        projectNameField1 = new JTextField(20);
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        createProjectButton = new JButton("Create Project");
        createProjectButton.setBackground(Color.LIGHT_GRAY);
        createProjectButton.setForeground(Color.BLACK);
        JPanel logPanel = new JPanel();
        logPanel.add(new JLabel("Gmail:"));
        logPanel.add(gmailField);
        logPanel.add(new JLabel("Password:"));
        logPanel.add(passwordField);
        logPanel.add(loginButton);
        frame.add(logPanel);
        CreatePPanel= new JPanel();
        CreatePPanel.add(new JLabel("Project Name:"));
        CreatePPanel.add(projectNameField1);
        CreatePPanel.add(new JLabel("Start Date:"));
        CreatePPanel.add(startDateField);
        CreatePPanel.add(new JLabel("End Date:"));
        CreatePPanel.add(endDateField);
        CreatePPanel.add(createProjectButton);
        frame.add(CreatePPanel);
        fullNameField = new JTextField(20);
        workerGmailField = new JTextField(20);
        positionField = new JTextField(20);
        CreateWPanel= new JPanel();
        addWorkerButton = new JButton("Add Worker");
        addWorkerButton.setBackground(Color.LIGHT_GRAY);
        addWorkerButton.setForeground(Color.BLACK);
        

        CreateWPanel.add(new JLabel("Worker  Name:"));
        CreateWPanel.add(fullNameField);
        CreateWPanel.add(new JLabel("Worker Gmail:"));
        CreateWPanel.add(workerGmailField);
        CreateWPanel.add(new JLabel("Position:"));
        CreateWPanel.add(positionField);
        CreateWPanel.add(addWorkerButton);
        CreateWPanel.setPreferredSize(new Dimension(300, 200));
        frame.add(CreateWPanel);
        
        AddMPanel= new JPanel();
        projectNameField = new JTextField(20);
        workerNameField = new JTextField(20);
        missionField = new JTextField(20);
        missionStartField = new JTextField(20);
        missionEndField = new JTextField(20);
        addMissionButton = new JButton("Add Mission");
        addMissionButton.setBackground(Color.LIGHT_GRAY);
        addMissionButton.setForeground(Color.BLACK);
        AddMPanel.add(new JLabel("Project Name:"));
        AddMPanel.add(projectNameField);
        AddMPanel.add(new JLabel("Worker Name:"));
        AddMPanel.add(workerNameField);
        AddMPanel.add(new JLabel("Mission:"));
        AddMPanel.add(missionField);
        AddMPanel.add(new JLabel("Start Time:"));
        AddMPanel.add(missionStartField);
        AddMPanel.add(new JLabel("End Time:"));
        AddMPanel.add(missionEndField);
        AddMPanel.add(addMissionButton);
        AddMPanel.setPreferredSize(new Dimension(300, 200));
        frame.add(AddMPanel);
        
        workerIdField = new JTextField(20);
        deleteWorkerButton = new JButton("Delete Worker");
        deleteWorkerButton.setBackground(Color.LIGHT_GRAY);
        deleteWorkerButton.setForeground(Color.BLACK);
        WorkerPanel = new JPanel();
        WorkerPanel.add(new JLabel("Worker Name:"));
        WorkerPanel.add(workerIdField);
        WorkerPanel.add(deleteWorkerButton);
        getWorkerInfoButton = new JButton("Get Worker Info");
        getWorkerInfoButton.setBackground(Color.LIGHT_GRAY);
        getWorkerInfoButton.setForeground(Color.BLACK);
        WorkerPanel.add(getWorkerInfoButton);
        frame.add(WorkerPanel);
        
        UpdateWorkerPanel = new JPanel();
        updateWorkerButton = new JButton("Update worker gmail");
        updateWorkerButton.setBackground(Color.LIGHT_GRAY);
        updateWorkerButton.setForeground(Color.BLACK);
        workergmailfield = new JTextField(20);
        workernamefieldupdt = new JTextField(20);
        UpdateWorkerPanel.add(new JLabel("WorkerName:"));
        UpdateWorkerPanel.add(workernamefieldupdt);
        UpdateWorkerPanel.add(new JLabel("Worker new Gmail:"));
        UpdateWorkerPanel.add(workergmailfield);
        UpdateWorkerPanel.add(updateWorkerButton);
        frame.add(UpdateWorkerPanel);
        
        SitutationPanel = new JPanel();
        getMissionInfButton = new JButton("Get mission situation");
        getMissionInfButton.setBackground(Color.LIGHT_GRAY);
        getMissionInfButton.setForeground(Color.BLACK);
        nameMsnfield = new JTextField(20);
        SitutationPanel.add(new JLabel("Name"));
        SitutationPanel.add(nameMsnfield);
        SitutationPanel.add(getMissionInfButton);
        frame.add(SitutationPanel);
        
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(Color.LIGHT_GRAY);
        refreshButton.setForeground(Color.BLACK);
        frame.add(refreshButton);
        
        responseArea = new JTextArea(5, 30);
        responseArea.setEditable(false);
        frame.add(new JScrollPane(responseArea));

       
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    
                login();
            }
        });

        getMissionInfButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               getWorkerMissionInf();
            }
        
        });
        updateWorkerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                uptadeWorker(); 
            }
            
        });
        getWorkerInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getWorkerInfo();
            }
        });


        deleteWorkerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteWorker();
            }
        });

        createProjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createProject();
            }
        });

        addWorkerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addWorker();
            }
        });
        
       addMissionButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               addMission();
    }});
       
       refreshButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               refreshPrjctWrk();
           }
           
    });

        
       
       
       
        frame.setVisible(true);
    }

     private void login() {
         
    String gmail = gmailField.getText();
    String password = passwordField.getText();
    try {
        BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ob.write("$ " + gmail + " " + password);
        ob.newLine();
        ob.flush();
        String response = in.readLine();
        System.out.println(response);
        if(response.equals("+")){
            responseArea.setText(GetInfosFromServer().toString());
            String[] pBaslik = {"PROJECT NAME","START TIME","FINISH TIME"};
            String[] wBaslik = {"WORKER NAME","WORKER GMAIL"};
            jtp = new JTable(ListToArray(GetInfosFromServer()),pBaslik);
            jtp.setBounds(30,40,200,300);
            spp = new JScrollPane(jtp);
            frame.add(spp);
            jtw = new JTable(ListToArray(GetInfosFromServer()),wBaslik);
            jtw.setBounds(30,40,200,300);
            spw = new JScrollPane(jtw);
            frame.add(spw);

       jtp.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jtp.getSelectedRow();
                    if (selectedRow != -1) { 
                        projectNameField.setText(jtp.getValueAt(selectedRow, 0).toString());

                    }
                }
            }
        });

       jtw.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = jtw.getSelectedRow();
                    if (selectedRow != -1) { 
                       workerNameField.setText(jtw.getValueAt(selectedRow, 0).toString());

                    }
                }
            }
        });
           JOptionPane.showMessageDialog(frame, "Giriş yapıldı", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        }else
            JOptionPane.showMessageDialog(frame, "Giriş yapılamadı", "Hata", JOptionPane.ERROR_MESSAGE);
                
    } catch (IOException e) {
        e.printStackTrace();
        responseArea.setText("Error: " + e.getMessage());
    }

     }
     
    public  ArrayList<String> GetInfosFromServer(){
        ArrayList<String> info = new ArrayList<>();
        
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response ="response";
            while(!response.equals("OK")){
                response = in.readLine(); 
                if(!response.equals("OK"))
                info.add(response);
            } 
          
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return info;
    }
    

    private  void createProject() {
    String projectName = projectNameField1.getText();
    String startDate = startDateField.getText();
    String endDate = endDateField.getText();

    try {
        BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(projectName);
        ob.write("p&~78 "+ projectName + " " + startDate + " " + endDate);
        ob.newLine();
        ob.flush();

        String response = in.readLine();
        responseArea.setText(response);
    } catch (IOException e) {
        e.printStackTrace();
        responseArea.setText("Error: " + e.getMessage());
    }
    
    
    
    
}
    
    private void addMission() {
    String projectName = projectNameField.getText();
    String workerName = workerNameField.getText();
    String mission = missionField.getText();
    String startTime = missionStartField.getText();
    String endTime = missionEndField.getText();
    
    

    try {
        BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ob.write("m>,?[]" + " " + projectName + " " + workerName + " " + mission + " " + startTime + " " + endTime);
        ob.newLine();
        ob.flush();

        String response = in.readLine();
        responseArea.setText(response);
    } catch (IOException e) {
        e.printStackTrace();
        responseArea.setText("Error: " + e.getMessage());
    }
}
    private void addWorker() {
    String fullName = fullNameField.getText();
    String workerGmail = workerGmailField.getText();
    String position = positionField.getText();

    try {
        BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ob.write("w$#/8" + " " + fullName + " " + workerGmail + " " + position);
        ob.newLine();
        ob.flush();

        String response = in.readLine();
        responseArea.setText(response);
    } catch (IOException e) {
        e.printStackTrace();
        responseArea.setText("Error: " + e.getMessage());
    }
}
    
    
    private void deleteWorker() {
    String workerId = workerIdField.getText();

    try {
        BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        ob.write("dm<>&89." + " " + workerId);
        ob.newLine();
        ob.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        responseArea.setText(response);
    } catch (IOException e) {
        e.printStackTrace();
        responseArea.setText("Error: " + e.getMessage());
    } 
}
    private void uptadeWorker(){
        try{
           String workername= workernamefieldupdt.getText();
            String workergmail  = workergmailfield.getText();
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ob.write("<.l:P" + " " +workername+ " "+workergmail);
            ob.newLine();
            ob.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            responseArea.setText(response);
            
        }catch(Exception e){
            
        }
        
    }
    private void getWorkerInfo() {
    String workerId = workerIdField.getText();
    try {
        
        ArrayList<String> info = new ArrayList<String>();
        BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ob.write("wp&*()3u8" + " " + workerId);
        ob.newLine();
        ob.flush();
       responseArea.setText(GetInfosFromServer().toString());
    } catch (Exception e) {
        e.printStackTrace();
        responseArea.setText("Error: " + e.getMessage());
    }
}
    private void getWorkerMissionInf(){
        try{
            String name = nameMsnfield.getText();
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
            ob.write("!@#$%"+" "+name);
            ob.newLine();
            ob.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            responseArea.setText(response);
        }catch(Exception e){
            e.printStackTrace();
        }
            
        
    }
    
    private void refreshPrjctWrk(){
        try{
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
            ob.write("@rf~s#*h");
            ob.newLine();
            ob.flush();
            String[] basp = {"PROJECT NAME","START TIME","FINISH TIME"};
            String[] basw = {"WORKER NAME","WORKER GMAIL"};
            jtp.removeAll();
            jtp.setModel(new DefaultTableModel(ListToArray(GetInfosFromServer()), basp));
            jtw.removeAll();
            jtw.setModel(new DefaultTableModel(ListToArray(GetInfosFromServer()),basw));
            frame.revalidate();
            frame.repaint();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public String[][] ListToArray(ArrayList<String> List){
        String array[][] = new String[100][100];
        for(int i= 0; i<List.size();i++){
            String temp[] =List.get(i).split(" ");
            for(int j=0;j<temp.length;j++){
                array[i][j] = temp[j];
            }
        }
        
        return array;  
    }
        
        
}



class SenderWrk implements Runnable {
    private Socket socket;
    private JFrame frame;
    private JTextArea responseArea;
    private JTextField fullNameField, gmailField, missionField;
    private JPanel wrkPanel,UpdatePanel;

    public SenderWrk(Socket socket) {
        this.socket = socket;
    }

    
    public void run() {
        frame = new JFrame("Worker Login");
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setBackground(Color.GRAY);
        frame.getContentPane().setForeground(Color.WHITE);
        wrkPanel = new JPanel();
        wrkPanel.setPreferredSize(new Dimension(300, 150));

        fullNameField = new JTextField(20);
        gmailField = new JTextField(20);
        missionField = new JTextField(20);
        responseArea = new JTextArea(5, 20);
        responseArea.setEditable(false);

        wrkPanel.add(new JLabel("Full Name:"));
        wrkPanel.add(fullNameField);
        wrkPanel.add(new JLabel("Gmail:"));
        wrkPanel.add(gmailField);
        
        JButton loginButton = new JButton("Log In");
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setForeground(Color.BLACK);
        wrkPanel.add(loginButton);
        loginButton.setBackground(Color.LIGHT_GRAY);
        loginButton.setForeground(Color.BLACK);
        frame.add(wrkPanel);
        
        UpdatePanel = new JPanel();
        UpdatePanel.setPreferredSize(new Dimension(300, 150));
        UpdatePanel.add(new JLabel("Mission"));
        UpdatePanel.add(missionField);
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.LIGHT_GRAY);
        submitButton.setForeground(Color.BLACK);
        UpdatePanel.add(submitButton);
        frame.add(UpdatePanel);
        frame.add(new JScrollPane(responseArea));
        

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitMission();
            }
        });

        frame.setVisible(true);
    }
    
    
        private void login() {
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            ob.write("!" + " " + fullNameField.getText() + " " + gmailField.getText());
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            
            responseArea.setText(response);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 
      

        private void submitMission() {
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            ob.write("+ "+missionField.getText());
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            responseArea.setText(response);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}


class SenderSign implements Runnable{
    private Socket socket;
    private JTextField gmailField,passwordField,nameField, surnameField;
    private JFrame frame;
    private JTextArea responseArea;
    public SenderSign(Socket socket){
        this.socket=socket;
    }
    
    public void run(){
        frame = new JFrame("User sign in");
        frame.getContentPane().setBackground(Color.GRAY);
        frame.getContentPane().setForeground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout()); 
        gmailField = new JTextField(10);
        passwordField = new JTextField(10);
        nameField = new JTextField(10);
        surnameField = new JTextField(10);
        responseArea = new JTextArea(5, 20);
        responseArea.setEditable(false);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setLayout(new FlowLayout());
        JLabel gmaillable = new JLabel("Gmail:");
        gmaillable.setForeground(Color.WHITE);
        frame.add(gmaillable);
        frame.add(gmailField);
        JLabel passwordlable = new JLabel("password:");
        passwordlable.setForeground(Color.WHITE);
        frame.add(passwordlable);
        frame.add(passwordField);
        JLabel namelable = new JLabel("name:");
        namelable.setForeground(Color.WHITE);
        frame.add(namelable);
        frame.add(nameField);
        JLabel surnamelable = new JLabel("surname:");
        surnamelable.setForeground(Color.WHITE);
        frame.add(surnamelable);
        frame.add(surnameField);
        
        JButton SignButton = new JButton("Sign In");
        SignButton.setBackground(Color.LIGHT_GRAY);
        SignButton.setForeground(Color.BLACK);
        frame.add(SignButton);
        frame.add(new JScrollPane(responseArea));
        ImageIcon icon = new ImageIcon("C:\\Users\\user\\Downloads\\pngwing.com(1).png");
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(scaledIcon);
        frame.add(label);
        
       SignButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Sign();
            frame.dispose();
        }
    });
       frame.setVisible(true);
    }
    
    private void Sign(){
        try{
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Please type gmail password name surname sequentially"); 
            ob.write("# "+ gmailField.getText() + " " +passwordField.getText() + " " +nameField.getText() + " " + surnameField.getText());
            ob.newLine();
            ob.flush();
            String response = in.readLine();
            responseArea.setText(response);
            socket.close();        
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
}


