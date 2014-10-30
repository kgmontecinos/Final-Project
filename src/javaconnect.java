/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kgmontecinos92
 */
/*
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
*/
import java.sql.*;
import javax.swing.JOptionPane;
// import javax.swing.*;

public class javaconnect {
   public javaconnect(){
   }
    
    private int[][] repositionArray(int [][]toRepositionArray){
        int[][] toModify= toRepositionArray;
        int[][] modified= new int[12][24];
        for(int columns = 0;columns<12;columns++){
            for(int rows =0; rows<24;rows++){
                modified[columns][rows]=toModify[rows][columns];
            }
        }
        return modified;
    }
    
    private int[][] reverseRepositionArray(int [][]toRepositionArray){
        int[][] toModify= toRepositionArray;
        int[][] modified= new int[24][12];
        for(int rows =0; rows<12;rows++){
            for(int columns = 0;columns<24;columns++){
                modified[columns][rows]=toModify[rows][columns];
            }
        }
        return modified;
    }
    
    public static void main(String[] args) {
        /*
        String email = "kgmontecinos@gmail.com";
        String password = "Arnold#1";
        String fn = "arnold";
        String ln ="montecinos";
        String address = "2822 Juniper St";
        String city = "fairfax";
        String state = "VA";
        String zipcode= "22031";
        String ccNumber = "1234123412341234";
        String ccExpMonth = "08";
        String ccExpYear = "2015";
        String ccSecurityCode = "332";
        */
        
        
        try {
            
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database1.sqlite");
            
            String sql = " select * from auditorium15seats";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int [][] readFromDB = new int[24][12];
            for(int r=0; r<24; r++){
                rs.next();
                for (int c=0;c<12;c++){
                    if(c ==0){//if column is A
                        readFromDB[r][c]=(int)rs.getObject("A");
                    }
                    else if(c ==1){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("B");
                    }
                    else if(c ==2){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("C");
                    }
                    else if(c ==3){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("D");
                    }
                    else if(c ==4){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("E");
                    }
                    else if(c ==5){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("F");
                    }
                    else if(c ==6){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("G");
                    }
                    else if(c ==7){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("H");
                    }
                    else if(c ==8){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("I");
                    }
                    else if(c ==9){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("J");
                    }
                    else if(c ==10){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("K");
                    }
                    else if(c ==11){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("L");
                    }
                }
            }
            rs.close();
            pst.close();
            //print results
            
            for(int r=0;r<24;r++){
                for(int c=0;c<12;c++){
                    System.out.print(readFromDB[r][c]+",");
                }
                System.out.println();
            }
            
            int [][] readyArray = new javaconnect().repositionArray(readFromDB);
            
            //print results
            
            for(int r=0;r<12;r++){
                for(int c=0;c<24;c++){
                    System.out.print(readyArray[r][c]+",");
                }
                System.out.println();
            }
            
            int [][] reverseReadyArray = new javaconnect().reverseRepositionArray(readyArray);
            
            //print results
            
            for(int r=0;r<24;r++){
                for(int c=0;c<12;c++){
                    System.out.print(reverseReadyArray[r][c]+",");
                }
                System.out.println();
            }
            
            /*
            String [][] readFromDBTest = new String[4][2];
            for(int r=0; r<4; r++){
                rs.next();
                for (int c=0;c<2;c++){
                    if(c ==0){//if column is email
                        readFromDBTest[r][c]= (String)rs.getObject("email");
                    }
                    else if(c ==1){//if column is email
                        readFromDBTest[r][c]= (String)rs.getObject("firstname");
                    }
                    
                }
                
            }
            */
            
            /*
            for(int r=0; r<4; r++){
                for (int c=0;c<2;c++){
                    System.out.print(readFromDBTest[r][c]+",");   
                }
                System.out.println();
            }
            */
            /*
            while(rs.next()){
                
                System.out.print(rs.getObject("email")+",");
                System.out.println(rs.getObject("firstname"));
            }
            */
            
            /*
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database1.sqlite");
           
            
            String sql = "INSERT INTO mtmUSERACCOUNTS (EMAIL,PASSWORD,FIRSTNAME,LASTNAME,ADDRESS,CITY,STATE,ZIPCODE,CCN,CCNEXPMO,CCNEXPYR,CCNSC) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1,email);
            pst.setString(2,password);
            pst.setString(3,fn);
            pst.setString(4,ln);
            pst.setString(5,address);
            pst.setString(6,city);
            pst.setString(7,state);
            pst.setString(8,zipcode);
            pst.setString(9,ccNumber);
            pst.setString(10,ccExpMonth);
            pst.setString(11,ccExpYear);
            pst.setString(12,ccSecurityCode);
            
            pst.execute();
   
            JOptionPane.showMessageDialog(null, "Data added.");
            
            pst.close();
            
            if (conn != null) {
                System.out.println("Connected to the database");
                DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("url: " + dm.getURL());
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
                conn.close(); //close connection
            
            }
            else{
                System.out.println("Connection is null");
            }
            */
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
                    
    }
    
}
