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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author elfay
 */
public class Database {

    public Connection conn = null; 
    public Statement stat;
    public ResultSet result;

    private boolean is_connected = false;
    private String message = "";
    
    public Database(String server, String user, String pass, String db) {
        connect(server, user, pass, db);
    }

    public boolean is_connected() {
        return is_connected;
    }
    
    public String status() {
        return message;
    }
    
    private void connect(String server, String user, String pass, String db){
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
    
    public boolean insertKategori(String nama){
        String sql = "INSERT category (category_name) "
            + "VALUE ('" + nama + "')";
        try {
            stat = conn.createStatement();
            stat.execute(sql);
        } catch(SQLException e){
            return false;
        }
        return true;
    }

    public List<Product> getDataBarang() {
        List<Product> data = new ArrayList<>();
        int number = 0;
        String sql = "SELECT * FROM product JOIN category ON product_category_id=category_id";
        
        try {

            stat = conn.createStatement();
            result = stat.executeQuery(sql);
            
            while(result.next()){
                Product row = new Product();
                row.name = result.getString("product_name").toString();
                row.price = result.getString("product_price").toString();
                row.stock = result.getString("product_stock").toString();
                row.category_name = result.getString("category_name").toString();
                data.add(number, row);
                number++;
            }

            result.close();
            stat.close();

        } catch (SQLException e){
            this.message = "Failed : Query error!";
        }
        return data;
    }
    
    public List<Category> getDataKategori() {
        List<Category> data = new ArrayList<>();
        int number = 0;
        String sql = "SELECT * FROM category";
        
        try {

            stat = conn.createStatement();
            result = stat.executeQuery(sql);
            
            while(result.next()){
                Category row = new Category();
                row.id = result.getString("category_id").toString();
                row.name = result.getString("category_name").toString();
                data.add(number, row);
                number++;
            }

            result.close();
            stat.close();

        } catch (SQLException e){
            this.message = "Failed : Query error!";
        }
        return data;
    }
    
    public boolean updateKategori(String nama, String id){
        String sql = "UPDATE category SET category_name = '" + nama + "' "
            + "WHERE category_id = '" + id + "'";
        try {
            stat = conn.createStatement();
            stat.execute(sql);
        } catch(SQLException e){
            return false;
        }
        return true;
    }
    
    public boolean deleteKategori(String id){
        String sql = "DELETE FROM category WHERE category_id = '" + id + "'";
        try {
            stat = conn.createStatement();
            stat.execute(sql);
        } catch(SQLException e){
            return false;
        }
        return true;
    }
    
}
