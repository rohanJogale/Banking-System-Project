import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con , Scanner sc) {
        this.con = con;
        this.sc = sc;
    };


    public void credit_money(long account_number ) throws SQLException{
        sc.nextLine();
        System.out.println("Enter Amount : ");
        double amonut = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (account_number != 0){
                PreparedStatement ps = con.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                ps.setLong(1,account_number);
                ps.setString(2,security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()){
                    String credit_query = "update accounts set balance = balance + ? where account_number = ?";
                    ps = con.prepareStatement(credit_query);
                    ps.setDouble(1,amonut);
                    ps.setLong(2,account_number);
                    int rowsAffect = ps.executeUpdate();
                    if (rowsAffect>0){
                        System.out.println("Rs. " +amonut+ " Credited Successfully .");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed !");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid Security Pin !");
                }
            }
        }catch(SQLException e){
            System.out.println(e);
        }
        con.setAutoCommit(true);
    }




    public void debit_money(long account_number ) throws SQLException {
        sc.nextLine();
        System.out.println("Enter Amount : ");
        double amonut = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement ps = con.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if(amonut<=current_balance){
                        String credit_query = "update accounts set balance = balance - ? where account_number = ?";
                        ps = con.prepareStatement(credit_query);
                        ps.setDouble(1,amonut);
                        ps.setLong(2,account_number);
                        int rowsAffect = ps.executeUpdate();
                        if(rowsAffect>0){
                            System.out.println("Rs. " +amonut+ " Debited Successfully .");
                            con.commit();
                            con.setAutoCommit(true);
                        }else {
                            System.out.println("Transaction Failed !!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance !");
                    }
                } else {
                    System.out.println("Invalid Pin !");
                }
            }
        }catch ( SQLException e){
            System.out.println(e);
        }
        con.setAutoCommit(true);
    }




    public void get_balance(long account_number){
        sc.nextLine();
        System.out.println("Enter  Security Pin : ");
        String security_pin = sc.nextLine();
        try {
            PreparedStatement ps = con.prepareStatement("select balance from accounts where account_number = ? and security_pin = ?");
            ps.setLong(1,account_number);
            ps.setString(2,security_pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Balance : " + balance);
            } else {
                System.out.println("Invalid Pin !");
            }
        } catch ( SQLException e){
            System.out.println(e);
        }
    }




    public void transfer_money(long sender_account_number) throws SQLException{
         sc.nextLine();
        System.out.println("Enter Receiver Account Number : ");
        long receiver_account_number = sc.nextLong();
        System.out.println("Enter Amount : ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security Pin : ");
        String security_pin = sc.nextLine();
        try {
            con.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0){
                PreparedStatement ps = con.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                ps.setLong(1,sender_account_number);
                ps.setString(2,security_pin);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if (amount<= current_balance){
                        String debit_query = "update accounts set balance = balance - ? where account_number = ?";
                        String credit_query = "update accounts set balance = balance + ? where account_number = ?";
                        PreparedStatement creditPs = con.prepareStatement(credit_query);
                        PreparedStatement debitPs = con.prepareStatement(debit_query);
                        creditPs.setDouble(1,amount);
                        creditPs.setLong(2,receiver_account_number);
                        debitPs.setDouble(1,amount);
                        debitPs.setLong(2,sender_account_number);
                        int rowsAffect1 = creditPs.executeUpdate();
                        int rowsAffect2 = debitPs.executeUpdate();
                        if (rowsAffect1 > 0 && rowsAffect2 > 0) {
                            System.out.println("Transaction Successful .");
                            System.out.println("Rs. " +amount+ " Transferred Successfully .");
                            con.commit();
                            con.setAutoCommit(true);
                        }else {
                            System.out.println("Transaction Failed !");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance !");
                    }
                } else {
                    System.out.println("Invalid Security Pin !");
                }
            }
        }catch ( SQLException e){
            System.out.println(e);
        }
        con.setAutoCommit(true);
    }
}
