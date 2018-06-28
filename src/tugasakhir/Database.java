/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasakhir;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Database {

    public Connection conn = null;
    public Statement stat;
    public ResultSet result;
    public String message = "";
    
    public Database(String server, String user, String pass, String db) {
        connect(server, user, pass, db);
    }

    public String status() {
        return message;
    }
    
    void connect(String server, String user, String pass, String db){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            message = "Failed to load Driver";
            return;
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+server+":3306/" + db, user, pass);
        } catch(SQLException e){
            int error_code = e.getErrorCode();
            if (error_code == 1049) {
                message = "Unknown database " + db;
            } else if(error_code == 1045) {
                message = "Wrong Username or Password";
            } else {
                message = "Failed connect to server";
            }
            return;
        }
        message = "Database connect successfully";
    }
    
    public boolean insertBarang(String nama, String harga, String stok, String kategori){
        String sql = "INSERT product (product_name, product_price, product_stock, product_category) "
            + "VALUE ('" + nama + "', " + harga + ", " + stok + ", " + kategori + ")";
        try {
            stat = conn.createStatement();
            stat.execute(sql);
        } catch(SQLException e){
            return false;
        }
        return true;
    }
    
    public String getOne(String tabel, String kolom, String kunci, String cari){
        String result_data = "";
        String sql = "SELECT " + kolom + " FROMs " + tabel + " WHERE " + kunci + " = '" + cari + "'";
        
        try {
            stat = conn.createStatement();
            result = stat.executeQuery(sql);
        
            if(result.next()){
                System.out.println(result.getString(kolom));
                result_data = result.getString(kolom).toString();
            } else {
                JOptionPane.showMessageDialog(null, "Data not found.");
            }

            result.close();
            stat.close();

        } catch (SQLException e){
            this.message = "Failed : Query error!";
        }
        
        return result_data;
    }
    
}
