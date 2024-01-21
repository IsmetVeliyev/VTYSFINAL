package vtys;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static vtys.VTYS.con;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class VTYS {
    protected static Connection con;
    static long pid =0;
    static long wid =0;
    static long mid =0;
            

    
    public static void main(String[] args) {
        try{
            createConnection();
            ServerSocket serversoc = new ServerSocket(5252);
            while(true){
                Thread th = new Thread(new ClientHandlermng(serversoc.accept()));
                th.start();
            }
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    
    public static void createConnection() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vtys", "root", "password");
        System.out.println("Database Connection Success");     
    }
}
   


class ClientHandlermng implements Runnable{
     Socket socket;
    
    public ClientHandlermng(Socket socket){
        this.socket=socket;
    }
    
    public void run(){
        try{
            boolean signal = true;
            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String opr = in.readLine();
            if(opr.contains("$")){
                String rsp;
                String ugmail;
                String gp[] = opr.split(" ");
                ugmail = gp[1];
                rsp=User.Authentication(gp[1],gp[2]);
                System.out.println(rsp);
                pw.write(rsp);
                pw.newLine(); 
                pw.flush();
                
                if(!rsp.equals("+"))
                    signal=false;
                ArrayList<String> timeContList = Project.projeZamanKont(ugmail);
                sendList(timeContList);
                 ArrayList<String> proInf =Project.projects(ugmail);
                 sendList(proInf);
                ArrayList<String> workers = Worker.workers(ugmail);
                sendList(workers);
                while(signal){
                String sysmanage = in.readLine();
                    if(sysmanage.contains("p&~78")){
                        String projparam[] = sysmanage.split(" ");
                        System.out.println(projparam[1]);            
                        rsp = new Project(projparam[1],projparam[2],projparam[3],ugmail).projectAdd();
                        pw.write(rsp);
                        pw.newLine();
                        pw.flush();
                    }else if(sysmanage.contains("w$#/8")){
                        String workparam[] = sysmanage.split(" ");
                        System.out.println(workparam[1]);
                        String response = new Worker(workparam[1],workparam[2],workparam[3]).workerAdd(ugmail);  /////
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    }else if(sysmanage.contains("m>,?[]")){
                        String missionparam[] = sysmanage.split(" ");          
                        String response = new Mission(missionparam[1],missionparam[2],missionparam[3],missionparam[4],missionparam[5],ugmail).missionAdd();
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    }else if(sysmanage.contains("dm<>&89.")){
                        String di[] = sysmanage.split(" ");
                        String response = Worker.DeleteWorker(di[1], ugmail);         /////
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    }else if(sysmanage.contains("wp&*()3u8")){
                        String wp[] = sysmanage.split(" ");
                        ArrayList <String> list = Worker.workerinfo(wp[1],ugmail);
                      for(int i=0;i<list.size();i++){
                          pw.write(list.get(i));
                         pw.newLine();
                          pw.flush();
                        }
                        pw.write("OK");
                        pw.newLine();
                        pw.flush();
                    }else if(sysmanage.contains("<.l:P")){
                        String wp[] = sysmanage.split(" ");
                        pw.write(Worker.UpdateWorkerCondition(wp[1],wp[2],ugmail));   
                        pw.newLine();         //...
                        pw.flush();
                        
                    }else if(sysmanage.contains("!@#$%")){
                        String nm[] = sysmanage.split(" ");
                        System.out.println(nm[1]);
                        pw.write(Worker.WorkerMissionInf(nm[1],ugmail));
                        pw.newLine();
                        pw.flush();
                    }else if(sysmanage.contains("@rf~s#*h")){
                        proInf =Project.projects(ugmail);
                        sendList(proInf);
                        workers = Worker.workers(ugmail);
                        sendList(workers);  
                    }
                            
                        
                   
                }
                         
                  
            }else if(opr.contains("#")){
                String param[] = opr.split(" ");
                String rsp= new User(param[1],param[2],param[3],param[4]).AddUser();
                System.out.println(rsp);
                pw.write(rsp);
                pw.newLine();
                pw.flush();
                
            }
            
            if(opr.contains("!")){
                String wrkprm[] = opr.split(" ");
                String response = Worker.AutoHenction(wrkprm[1],wrkprm[2]);  ///////
                pw.write(response);
                pw.newLine();
                pw.flush();
                
                if(response.contains("+")){
                    while(true){
                        String task=in.readLine();
                        System.out.println(task.split(" ")[1]);
                        response =Mission.UpdateCondition(task.split(" ")[1],wrkprm[2]);
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    }
                }
            }
 
                
        }
            
            
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
   public void sendList(ArrayList<String> list){
       try{
           BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  
           for(int i=0;i<list.size();i++){
               bf.write(list.get(i));
               bf.newLine();
               bf.flush();
           }
           bf.write("OK");
           bf.newLine();
           bf.flush();
           
       }catch(Exception e){
           e.printStackTrace();
       }
   
       }
}

class User{
    private String gmail;
    private String password;
    private String name;
    private String surname;
    
    public User(String gmail,String password,String name,String surname){
        this.gmail=gmail;
        this.password = password;
        this.name=name;
        this.surname=surname;
    }
    
    public String AddUser() throws Exception{
        
        try{
            PreparedStatement psmt = VTYS.con.prepareStatement("INSERT INTO users VALUES(?,?,?,?)");
            psmt.setString(1,this.gmail);
            psmt.setString(2,this.password);
            psmt.setString(3,this.name);
            psmt.setString(4,this.surname);
            psmt.execute();
            psmt.close();
            
        }catch(Exception e){
            return "this gmail has been used";
            
        }
        return "Operation successfully done";
        
    }
    
    public static String Authentication(String gmail,String password){
        try{
           PreparedStatement psmt = VTYS.con.prepareStatement("SELECT * FROM users WHERE gmail= ?");
           psmt.setString(1,gmail);
           ResultSet rs = psmt.executeQuery();
           rs.next();  
        
           if(password.equals(rs.getString("password"))){
               
               return "+";
          }
        }catch(Exception e){
            return "Not Logged";
        }
        
     return " " ;
}
    
}


class Project{
    private String projectname;
    private String startTime;
    private String finishTime;
    private String gmail;
    
    public Project(String projectname,String startTime,String finishTime,String gmail){
        this.projectname = projectname;
        this.startTime=startTime;
        this.finishTime=finishTime;
        this.gmail=gmail;
    }
    
    
    public String projectAdd(){
        try{
            PreparedStatement psmt = VTYS.con.prepareStatement("INSERT INTO projects VALUES(?,?,?,?,?)");
            psmt.setString(1,"pid"+(++VTYS.pid));
            psmt.setString(2,this.projectname);
            psmt.setString(3,this.startTime);
            psmt.setString(4,this.finishTime);
            psmt.setString(5,this.gmail);
            psmt.execute();
            psmt.close();
        }catch(Exception e){
            e.printStackTrace();
            return "Problem occcured";
        }
        return "Project added";
    }
    
    
    public static ArrayList<String> projects (String gmail) throws Exception{
      ArrayList<String> projectList  = new ArrayList<>();
      PreparedStatement psmt  = VTYS.con.prepareStatement("SELECT * FROM projects WHERE gmail=?");
      psmt.setString(1,gmail);
      ResultSet rs = psmt.executeQuery();
      while(rs.next()){
          projectList.add(rs.getString("projectname")+" "+rs.getString("startTime")+" "+rs.getString("finishTime"));
      }
      psmt.close();
      return projectList;
    }
    
    public static ArrayList<String>  projeZamanKont (String gmail)throws Exception{
        ArrayList<String> List = new ArrayList<>();
        PreparedStatement psmt = VTYS.con.prepareStatement("SELECT p.idprojects,p.finishTime,p.projectname,p.startTime FROM projects p JOIN users s ON p.gmail = s.gmail WHERE p.gmail=? && STR_TO_DATE(finishTime, '%d.%m.%Y') < STR_TO_DATE(?, '%d.%m.%Y')");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDateTime = now.format(formatter);
        psmt.setString(1,gmail);
        psmt.setString(2, formattedDateTime);
        ResultSet rs = psmt.executeQuery();
        System.out.println(formattedDateTime);
        while(rs.next()){
            System.out.println(rs.getString("idprojects"));
            
           PreparedStatement psmt1 = VTYS.con.prepareStatement("SELECT iddurum FROM missions WHERE idprojects=?");
           psmt1.setString(1,rs.getString("idprojects"));
         ResultSet rs1 = psmt1.executeQuery();
         while(rs1.next()){
               if( rs1.getInt("iddurum")==2){
                   
               }else{
                  PreparedStatement psmt2 = VTYS.con.prepareStatement("UPDATE projects SET finishTime =? WHERE idprojects =? ");
                  String paramfinish[] = rs.getString("finishTime").split("\\.");
                  System.out.println(formattedDateTime);
                  String paramform[] = formattedDateTime.split("\\.");
                  int subtraction = Integer.parseInt(paramform[2])-Integer.parseInt(paramfinish[2]);
                  paramfinish[2] = String.valueOf(Integer.parseInt(paramfinish[2])+(subtraction)+1);
                  String newtime = String.join(".",paramfinish);
                  System.out.println(newtime);
                  psmt2.setString(1,newtime);
                 psmt2.setString(2,rs.getString("idprojects"));
                  psmt2.execute();
                   List.add("gecikme: "+subtraction +" "+rs.getString("projectname")+" 1 yil uzatildi >==<");
                   break;
              }
                  
                  
    }
            psmt1.close();
        }
       psmt.close();
       return List;
    }  
}
    
class Mission{
    
    private String idmissions;
    private String idprojects;
    private String idWorkers;
    private String mission;
    private String missionstart;
    private String missionfinish;
    private int iddurum;
    
    
    public Mission(String projectname,String workername,String mission,String missionstart,String missionfinish,String ugmail){
        this.idprojects=getIdforUser(projectname,"pid",ugmail);
        this.idWorkers = getIdforUser(workername,"wid",ugmail);
        this.mission = mission;
        this.missionstart = missionstart;
        try{
            if(isValidDate(missionfinish)){
           this.missionfinish=comprasion(getProjectFinishTime(this.idprojects),missionfinish);
            this.iddurum=3;
            }
            else
                this.iddurum=1;
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
      private static boolean isValidDate(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);  

        try {
            Date date = dateFormat.parse(input);
            return true;
        } catch (ParseException e) {
            
            return false;
        }
    }
    
    public String missionAdd() {
        try{
            PreparedStatement psmt = VTYS.con.prepareStatement("INSERT INTO missions VALUES(?,?,?,?,?,?,?)");
            psmt.setString(1,"mid" +(++VTYS.mid));
            psmt.setString(2,this.idprojects);
            psmt.setString(3,this.idWorkers);
            psmt.setString(4,this.mission);
            psmt.setString(5,this.missionstart);
            psmt.setString(6, this.missionfinish);
            psmt.setInt(7,this.iddurum);
            psmt.execute();
            psmt.close();
        }catch(Exception e){
            e.printStackTrace();
            return "not succcess operation";
            
        }
        return "Mission based";
        
    }
    
   public  String getProjectFinishTime(String id) throws Exception{
       System.out.print(id);
       PreparedStatement psmt =  VTYS.con.prepareStatement("SELECT * FROM projects WHERE idprojects=?");
       psmt.setString(1,id);
       ResultSet rs = psmt.executeQuery();
       rs.next();
       return rs.getString("finishTime");
   }
    
   public  String comprasion (String ProjectFinishTime,String missionfinishtime)throws Exception{
       String projecttime[] = ProjectFinishTime.split("\\.");
       String missiontime[] = missionfinishtime.split("\\.");
       int projtime = Integer.parseInt(projecttime[0]) + Integer.parseInt(projecttime[1])*30+Integer.parseInt(projecttime[2])*365;
       int mistime=Integer.parseInt(missiontime[0]) + Integer.parseInt(missiontime[1])*30+Integer.parseInt(missiontime[2])*365;
       System.out.println(projtime);
       System.out.println(mistime);
       if(projtime>mistime){
           return missionfinishtime;
       }
       
       return ProjectFinishTime;
   }
 
    public static String getIdforUser(String name,String opr,String gmail){
        try{
           if(opr.equals("pid")){
               PreparedStatement psmt = VTYS.con.prepareStatement("SELECT p.idprojects FROM projects p WHERE projectname=? AND gmail =? ");
               psmt.setString(1,name);
               psmt.setString(2,gmail);
               ResultSet rs = psmt.executeQuery();
               rs.next();
               return rs.getString("idprojects");
           }else if(opr.equals("wid")){
               PreparedStatement psmt = VTYS.con.prepareStatement("SELECT w.idWorkers FROM workers w WHERE workerName=? AND gmail =? ");
               psmt.setString(1,name);
               psmt.setString(2, gmail);
               ResultSet rs = psmt.executeQuery();
               rs.next();
               return rs.getString("idWorkers");
               
           }
        }catch(Exception e){
            e.printStackTrace();
            return("<>");
        }
           return ("><");
    }
    
    public static String getIdforWorker(String missionname,String workergmail){
        try{
            PreparedStatement psmt = VTYS.con.prepareStatement("SELECT m.idmissions,m.idWorkers  FROM missions m  JOIN workers w ON m.idWorkers = w.idWorkers WHERE  mission=? AND workergmail=?");
            psmt.setString(1,missionname);
            psmt.setString(2, workergmail);
            ResultSet rs= psmt.executeQuery();
            rs.next();
            return  rs.getString("idmissions")+ " " +rs.getString("idWorkers");
          
        }catch(Exception e){
            e.printStackTrace();
        }
        return"jkdl";
    }
    
   
       public static String UpdateCondition(String mission,String workergmail){
       try{
           PreparedStatement psmt = con.prepareStatement("UPDATE missions SET iddurum =? WHERE idworkers=? AND idmissions =? ");
           String ids = getIdforWorker(mission,workergmail);
           psmt.setInt(1, 2);
           System.out.println(ids);
           psmt.setString(2,ids.split(" ")[1]);
           psmt.setString(3,ids.split(" ")[0]);
           psmt.execute();
       }catch(Exception e){
           e.printStackTrace();
           return "Guncelleme yapilmadi";
           
       }
       return "Guncelleme yapildi";
   }
       ////////////////////////////////////////////////////////////////////

    
}

class Worker{
    private  String fullName ;
    private String workergmail;
    private String position;
    public Worker(String fullName,String workergmail,String position){
        this.fullName = fullName;
        this.workergmail=workergmail;
        this.position = position;
        System.out.println("pozisyon:" +position);
    }
    
    public String workerAdd(String ugmail){
        try{
           
            PreparedStatement psmt = VTYS.con.prepareStatement("INSERT INTO workers VALUES(?,?,?,?,?)");
            psmt.setString(1,"wid"+(++VTYS.wid));
            psmt.setString(2,ugmail);
            psmt.setString(3,this.fullName);
            psmt.setString(4,this.workergmail);
            switch(this.position){
                case "Freshman":{
                    psmt.setInt(5, 1);
                    break;
                }
                case "Sophomore":{
                  psmt.setInt(5, 2);
                    break;  
                }
                case "Junior":{
                    psmt.setInt(5,3);
                    break; 
                }
                case "Senior":{
                    psmt.setInt(5,4);
                    break;
                }
                default:{
                    psmt.setInt(5,0);
                    break;
                }
                
            }
            psmt.execute();
            psmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Worker added";
    }
    
    
    public static ArrayList workers (String ugmail) throws Exception{
      ArrayList<String> workers = new ArrayList<>();
      PreparedStatement psmt = VTYS.con.prepareStatement("SELECT * FROM users s JOIN workers w ON s.gmail=w.gmail WHERE s.gmail=?");
      psmt.setString(1, ugmail);
      ResultSet rs = psmt.executeQuery();
      while(rs.next()){
          workers.add(rs.getString("WorkerName")+ " " + rs.getString("workergmail"));
      }
      return workers;
    }
    
    public static ArrayList<String> workerinfo(String name,String gmail) throws Exception{
      String wid = Mission.getIdforUser(name,"wid",gmail);
      System.out.println(wid);
      ArrayList <String> workerinf = new ArrayList();
       PreparedStatement psmt = VTYS.con.prepareStatement("SELECT * FROM pozisyon p JOIN workers w ON p.idpozisyon = w.idpozisyon JOIN missions m ON m.idWorkers = w.idWorkers JOIN projects pr ON pr.idprojects = m.idprojects JOIN durum d ON m.iddurum=d.iddurum WHERE w.idWorkers =? AND w.gmail=?");
       psmt.setString(1,wid);
       psmt.setString(2,gmail);
       ResultSet rs = psmt.executeQuery();
       while(rs.next()){
           workerinf.add(rs.getString("projectname")+" "+rs.getString("pozisyoncol")+" "+rs.getString("mission")+" "+rs.getString("missionstart")+" "+rs.getString("missionfinish")+" "+rs.getString("durumcol")+ ">--<");
       }
       psmt.close();
       
       return workerinf;
    }
    
    public static String DeleteWorker(String name,String gmail){
        try{
            String wid = Mission.getIdforUser(name,"wid",gmail);
            PreparedStatement psmt = VTYS.con.prepareStatement("DELETE FROM workers WHERE idWorkers=? AND gmail=?");
            psmt.setString(1,wid);
            psmt.setString(2,gmail);
            psmt.execute();
            psmt.close();

            PreparedStatement psmt1 = VTYS.con.prepareStatement("UPDATE missions SET idWorkers = ? WHERE idWorkers = ?");
            psmt1.setNull(1,java.sql.Types.VARCHAR);
            psmt1.setString(2, wid);
            psmt1.execute();
            psmt1.close();     
 
        }catch(Exception e){
            return "not success";
        }
        
        return "deletion success";   
   }
    
    public static String AutoHenction(String name,String workergmail){
        try{
            PreparedStatement psmt = VTYS.con.prepareStatement("SELECT * FROM workers WHERE workergmail=?");
            psmt.setString(1,workergmail);
            ResultSet rs = psmt.executeQuery();
            rs.next();
            if(rs.getString("WorkerName").equals(name)){
                return "Logged+";
            }
        }catch(Exception e){
            e.printStackTrace();
            return "Not Logged";
           
        }
        return "Not Logged";
    }
    
    public static String UpdateWorkerCondition(String workername,String workergmail,String ugmail ){
        try{
            PreparedStatement psmt = VTYS.con.prepareStatement("UPDATE workers SET workergmail=? WHERE idWorkers=?");
            psmt.setString(1, workergmail);
            psmt.setString(2,Mission.getIdforUser(workername,"wid",ugmail));
            psmt.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return "Gmail Updated";
   }
    
    
   public static String WorkerMissionInf(String name,String ugmail){
       int tamamlandi=0,tamamlanmadi=0;
       try{
           PreparedStatement psmt = VTYS.con.prepareStatement("SELECT * FROM workers w JOIN missions m ON  w.idWorkers=m.idWorkers WHERE (STR_TO_DATE(missionfinish, '%d.%m.%Y') < STR_TO_DATE(?, '%d.%m.%Y')  OR m.iddurum=?)AND w.gmail=? AND w.workerName=?");
           LocalDateTime now = LocalDateTime.now();
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
           String formattedDateTime = now.format(formatter);
           psmt.setString(1, formattedDateTime);
           psmt.setInt(2,2);
           psmt.setString(3, ugmail);
           psmt.setString(4, name);
           ResultSet rs = psmt.executeQuery();
           
           while(rs.next()){
               int durum = rs.getInt("iddurum");
               System.out.println(durum+1);
               if(durum==2){
                   tamamlandi = tamamlandi +1;  
               }else if(durum==3){
                   tamamlanmadi = tamamlanmadi +1;
               }
           }
       }catch(Exception e){
           e.printStackTrace();
       }
       
        return("Tamamlanmis gorev sayisi :"+ tamamlandi +"Tamamlanmamis gorev sayisi : "+tamamlanmadi);
 
   }
}
