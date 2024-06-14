import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Banking_App {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "rohan1175";

    public static void main(String args[]) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch ( Exception e){
            System.out.println(e);
        }

        try {
            Connection con = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con,sc);
            Accounts accounts = new Accounts(con,sc);
            AccountManager accountmanager = new AccountManager(con,sc);

            String email;
            long account_number;

            while(true){
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter Your Choice : ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        System.out.println("\033[H\033[2J");
                        System.out.flush();
                        break;
                    case 2:
                        email = user.login();
                        if (email!= null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if (!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if (sc.nextInt() == 1){
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully .");
                                    System.out.println("Your Account Number is : "+ account_number);
                                } else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while(choice2 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter Your Choice : ");
                                choice2 = sc.nextInt();
                                switch(choice2){
                                    case 1:
                                        accountmanager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountmanager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountmanager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountmanager.get_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice !");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password !!");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM !!");
                        System.out.println("Existing System !");
                        return;
                    default:
                        System.out.println("Enter Valid Choice !");
                        break;
                }
            }

        }catch ( Exception e){
            System.out.println(e);
        }
    }
}
