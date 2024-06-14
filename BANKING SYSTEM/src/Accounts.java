import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private Scanner sc;

    public Accounts(Connection con , Scanner sc) {
        this.con = con;
        this.sc = sc;
    };

    public long open_account(String email){
        if (!account_exist(email)) {
            String open_account_query = "insert into accounts( account_number , full_name , email , balance , security_pin )values (?,?,?,?,?)";
            sc.nextLine();
            System.out.println("Enter Full Name : ");
            String full_name = sc.nextLine();
            System.out.println("Enter Initial Amount : ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.println("Enter Security pin : ");
            String security_pin = sc.nextLine();
            try{
                long account_number = generateAccount_number();
                PreparedStatement ps = con.prepareStatement(open_account_query);
                ps.setLong(1,account_number);
                ps.setString(2,full_name);
                ps.setString(3,email);
                ps.setDouble(4,balance);
                ps.setString(5,security_pin);
                int rowsAffect = ps.executeUpdate();
                if (rowsAffect>0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account Creation Failed !!");
                }
            }catch (SQLException e){
                System.out.println(e);
            }
        }
        throw new RuntimeException("Account Already Exist .");
    }





    public long getAccount_number(String email){
        String query = "select account_number from accounts where email = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getLong("account_number");
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        throw new RuntimeException( "Account Does Not Exist !!");
    }






    private long generateAccount_number(){
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select account_number from accounts order by account_number desc limit 1 ");
            if (rs.next()){
                long last_account_number = rs.getLong("account_number");
                return last_account_number+1;
            } else {
                return 10000100;
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return 10000100;
    }






    public boolean account_exist(String email){
        String query = "select account_number from accounts where email = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else {
                return false;
            }
        }catch ( SQLException e){
            System.out.println(e);
        }
        return false;
    }
}
