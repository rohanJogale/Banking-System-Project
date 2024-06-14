import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User{
        private Connection con;
        private Scanner sc;

        public User(Connection con , Scanner sc){
                this.con = con;
                this.sc = sc;
        };

        // register class
        public void register(){
                sc.nextLine();
                System.out.println("Full Name : ");
                String full_name = sc.nextLine();
                System.out.println("Email : ");
                String email = sc.nextLine();
                System.out.println("Password : ");
                String password = sc.nextLine();
                if(user_exist(email)){
                        System.out.println("User Already Exist for this Email Address!!");
                        return;
                }
                String resister_query = "insert into user(full_name,email,password)values(?,?,?)";
                try{
                        PreparedStatement ps = con.prepareStatement(resister_query);
                        ps.setString(1,full_name);
                        ps.setString(2,email);
                        ps.setString(3,password);
                        int affectsRows = ps.executeUpdate();
                        if (affectsRows > 0){
                                System.out.println("Registration Successful !!");
                        } else {
                                System.out.println("Registration Failed ! ");
                        }
                } catch ( SQLException e){
                        System.out.println(e);
                }
        }
        //login class
        public String login(){
                sc.nextLine();
                System.out.println("Email : ");
                String email = sc.nextLine();
                System.out.println("Password : ");
                String password = sc.nextLine();
                String login_query = "select * from user where email = ? and password = ?";
                try {
                        PreparedStatement ps = con.prepareStatement(login_query);
                        ps.setString(1,email);
                        ps.setString(2,password);
                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                                return email;
                        } else {
                                return null;
                        }
                } catch (SQLException e){
                        System.out.println(e);
                }
                return null;


        }

        public boolean user_exist(String email){
                String user_exist = "select * from user where email = ?";
                try{
                        PreparedStatement ps = con.prepareStatement(user_exist);
                        ps.setString(1,email);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()){
                                return true;
                        } else {
                                return false;
                        }
                }catch (SQLException e){
                        System.out.println(e);
                }
                return false;
        }
}
