import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.sql.* ;

import static java.lang.System.exit;

public class MYSQL {
    static Scanner scan = new Scanner(System.in);
    static String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) {
        Connection conn;
        Statement stmt;

        try {
            Class.forName(DRIVER);

            System.out.print("Enter database name, user, and password here:");
            String DB = "jdbc:mysql://localhost:3306/" + scan.next();
            String user = scan.next();
            String pass = scan.next();
            System.out.println("Connecting to database...");

            conn = DriverManager.getConnection(DB, user, pass);
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            if (!conn.isClosed())
            {
                while (true)
                    Menus.main_menu(stmt);
            }
            else
                System.out.println("\nError while connecting to database.\nContact IT department");

            stmt.close();
            conn.close();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
        System.out.println("\nGoodbye!");
    }
    static class Books {
        static int id; //Primary Key
        static String name;
        static String auth;
        static String price;
        static String qty;
        static String sql;
        static ResultSet rs;

        public static void add(Statement stmt) throws SQLException {
            int idnum = 0;
            sql = "SELECT id FROM books ORDER BY id DESC LIMIT 1";
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                idnum = rs.getInt(1) + 1;
            }
            System.out.print("Enter the Name of the book:");
            name = scan.nextLine();
            System.out.print("Enter the Author:");
            auth = scan.next();
            System.out.print("Enter the Price of the book:");
            price = scan.next();
            System.out.print("Enter the Quantity received:");
            qty = scan.next();
            sql = "INSERT INTO books VALUES('" + idnum + "','" + auth + "','" + price + "')";
            stmt.execute(sql);
            System.out.println("\nBook entered successfully!\n");
        }
        public static void update_price(Statement stmt) throws SQLException{
            char choice;
            System.out.println("Enter the id of the book for update in price:");
            id = scan.nextInt();
            sql = "SELECT name, price FROM books WHERE id = " + id + ";";
            rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                System.out.printf("\nThe Name of the book is: %25s", rs.getString("name"));
                System.out.printf("\nThe current price of the book is: %25s", rs.getString("price"));
            }
            System.out.print("Do you want to update the price [y/n]:");
            choice = scan.next().charAt(0);
            if (choice == 121 || choice  == 89)
            {
                System.out.print("Enter the new price: ");
                price = scan.next();
                sql = "UPDATE books SET price = " + price + " WHERE id = " + ";";
                int update = stmt.executeUpdate(sql);
                if (update != 0)
                    System.out.println("\n\nBook price updated successfully\n");
                else
                    System.out.println("\n\nEntry ERROR!\n");
            }
            else
                System.out.println("No changes made!\n");
        }
        public static void search(Statement stmt) throws SQLException {
            System.out.print("Enter book id number for details:");
            id = scan.nextInt();
            sql = "SELECT * FROM books WHERE id = " + id + ";";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.printf("Details of book id number:%19d", rs.getInt("id"));
                System.out.printf("\nName of the book:%31s", rs.getString("name"));
                System.out.printf("\nAuthor of the book:%30s", rs.getString("auth"));
                System.out.printf("\nPrice of the book:%28s", rs.getString("price"));
                System.out.printf("\nInventory count:%29s\n", rs.getString("qty"));
            }
        }
        public static void update(Statement stmt) throws SQLException {
            int[] b_id = new int[20];
            int[] qty = new int[20];
            int i = 0, max;
            sql = "SELECT book_id,qty FROM purchases WHERE received = 'T' and inv IS NULL;";
            rs = stmt.executeQuery(sql);
            sql = "UPDATE purchases SET inv = 1 WHERE received = 'T' and inv IS NULL;";
            stmt.executeQuery(sql);
            while(rs.next())
            {
                b_id[i] = rs.getInt(1);
                qty[i] = rs.getInt(2);
                i++;
            }
            max = i;
            for (int j = 0; j <= max; j++)
            {
                sql = "UPDATE books SET qty = " + qty[i] + " WHERE id = " + b_id[i] + ";";
                stmt.executeUpdate(sql);
            }
            System.out.println("\nThe orders received have been updated.\n");
        }
        public static void display(Statement stmt) throws SQLException {
            int i = 0;
            sql = "SELECT * FROM books;";
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                System.out.printf("\nBook id number:%20d", ++i);
                System.out.printf("\nName of book:%20s", rs.getString(2));
                System.out.printf("\nName of author:%20s", rs.getString(3));
                System.out.printf("\nPrice:%20s", rs.getString(4));
                System.out.printf("\nQuantity:%20s\n", rs.getString(5));
            }
        }
    }
    static class Employees {
        static int id;      //Primary Key
        static String name;
        static String addr_line1;
        static String addr_line2;
        static String addr_city;
        static String addr_state;
        static String phn;
        static Date date_of_joining;
        static String salary;
        static String mgr_status;   //check(T or F) def f
        static String sql;
        static ResultSet rs;
        static String peso = "$";

        public static void add_emp(Statement stmt) throws SQLException {
            int idnum = 0;
            rs = stmt.executeQuery("SELECT id FROM employees ORDER BY id DESC LIMIT 1;");
            while (rs.next()) {
                idnum = rs.getInt(1) + 1;
            }
            System.out.print("Enter your ID for verification");
            id = scan.nextInt();
            sql = "SELECT mgr_status FROM employees WHERE id  = " + id + ";";
            rs = stmt.executeQuery(sql);
            if (!rs.next())
            {
                System.out.println("\nEmployee not found!\n\n");
            }
            else {
                mgr_status = rs.getString("mgr_status");
                if (Objects.equals(mgr_status, "F"))
                {
                    System.out.println("\nYou don't have manager rights!\n");
                    return;
                }
            }
            System.out.println("Enter the details of the new employee");
            System.out.print("Enter the name of the employee: ");
            name = scan.next();
            System.out.print("\nEnter the address (in 3 lines): ");
            addr_line1 = scan.next();
            addr_line2 = scan.next();
            addr_city = scan.next();
            System.out.print("\nEnter State: ");
            addr_state = scan.next();
            System.out.print("\nEnter phone number(no spaces or dashes): ");
            phn = scan.next();
            System.out.print("\nEnter the salary: ");
            salary = scan.next();
            sql = "INSERT INTO employees (id,name,addr_line1,addr_line2,addr_city,addr_state,phn,date_of_joining,salary) VALUES ('" + idnum + "','" + name + "','" + addr_line1 + "','" + addr_line2 + "','" + addr_city + "','" + addr_state + "','" + phn + "',CURDATE(),'" + salary + "');";
            stmt.execute(sql);
            System.out.println("\n\nEmployee added successfully!\n\n");
        }
        public static void assign_mgr_stat(Statement stmt) throws SQLException {
            System.out.print("Enter your ID for verification: ");
            id = scan.nextInt();
            sql = "SELECT mgr_status FROM employees WHERE id  = " + id + ";";
            rs = stmt.executeQuery(sql);
            if (!rs.next())
            {
                System.out.println("\nEmployee not found!\n\n");
                return;
            }
            else
            {
                mgr_status = rs.getString("mgr_status");
                if (Objects.equals(mgr_status, "F"))
                {
                    System.out.println("\nYou dont have manager rights!\n");
                }
                System.out.print("\nEnter the employee ID to grant manager status: ");
                id = scan.nextInt();
                sql = "UPDATE employees SET mgr_status = 'T' WHERE id = " + id + ";";
                stmt.executeUpdate(sql);
                System.out.println("\n\nManager status granted\n");
            }
        }
        public static void search_emp(Statement stmt) throws SQLException {
            System.out.print("Enter the id of the employee you seek: ");
            id = scan.nextInt();
            sql = "SELECT * FROM employees WHERE id  = " + id + ";";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                System.out.println("Employee details");
                System.out.printf("\nName:%25s", rs.getString("name"));
                System.out.printf("\nAddress:%25s %s, %s, %s", rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
                System.out.printf("\nContact no.:%25s", rs.getString("phn"));
                System.out.printf("\nDate of Joining:%25s", rs.getString("date_of_joining"));
                System.out.printf("\nSalary:%25s%s", peso, rs.getString("salary"));
            }
            else {
                System.out.println("No employee found!\n");
            }
        }
        public static void display (Statement stmt) throws SQLException {
            int i = 0;
            sql = "SELECT * FROM employees;";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                do {
                    System.out.println("Details of Employee No." + ++i);
                    System.out.printf("Name:%25s", rs.getString("name"));
                    System.out.printf("\nAddress:%25s %s, %s, %s", rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
                    System.out.printf("\nContact no.:%25s", rs.getString("phn"));
                    System.out.printf("\nDate of Joining:%25s", rs.getString("date_of_joining"));
                    System.out.printf("\nSalary:%25s%s\n\n", peso, rs.getString("salary"));
                } while (rs.next());
                }
            else {
                System.out.println("Employees not found!\n");
            }
        }
        public static void update_sal(Statement stmt) throws SQLException {
            System.out.print("Enter the id of the employee who's salary will be updated: ");
            id = scan.nextInt();
            System.out.print("\nEnter the updated salary: ");
            salary = scan.next();
            sql = "UPDATE employees SET salary = '" + salary + "' WHERE id = " + id + ";";
            stmt.executeUpdate(sql);
            System.out.print("\n\nSalary updated successfully\n\n");
        }
    }
    static class Members  {
        static int id;
        static String name;
        static String addr_line1;
        static String addr_line2;
        static String addr_city;
        static String addr_state;
        static String phn;
        static Date beg_date;
        static Date end_date;
        static String valid;
        static ResultSet rs;
        static String sql;

        public static void add_mem(Statement stmt) throws SQLException {
            int idnum = 0;
            rs = stmt.executeQuery("SELECT id FROM members ORDER BY id DESC LIMIT 1;");
            while (rs.next())
            {
                idnum = (rs.getInt(1)) + 1;
            }
            System.out.print("Enter the name of the member: ");
            name = scan.next();
            System.out.print("\nEnter phone no.: ");
            phn = scan.next();
            System.out.print("\nEnter address (in 3 lines)");
            addr_line1 = scan.next();
            addr_line2 = scan.next();
            addr_city = scan.next();
            System.out.print("\nEnter the name of the state: ");
            addr_state = scan.next();
            sql = "INSERT INTO members(id,name,addr_line1,addr_line2,addr_city,addr_state,phn,beg_date,end_date) VALUES(" + idnum + ",'" + name + "','" +
                    addr_line1 + "','" + addr_line2 + "','" + addr_city + "','" + addr_state + "','" + phn + "',curdate(),Date_add(curdate(), INTERVAL 1 YEAR))";
            stmt.execute(sql);
            //Fetching member id
            sql = "SELECT id FROM members WHERE phn = '" + phn + "';";
            rs = stmt.executeQuery(sql);
            System.out.print("\n\n");
            while (rs.next())
            {
                System.out.println("Member added successfully\nMember ID: " + rs.getInt("id"));
                stmt.execute("UPDATE members SET valid = 'valid';");
            }
            System.out.print("\n\n");
        }
        public static void refresh(Statement stmt) throws SQLException {
            sql = "UPDATE members SET valid = 'invalid' WHERE end_date <= curdate();";
            stmt.executeUpdate(sql);
        }
        public static void  search_mem(Statement stmt) throws SQLException {
            System.out.print("Enter member ID: ");
            id = scan.nextInt();
            sql = "SELECT * FROM members WHERE id = " + id + ";";
            rs = stmt.executeQuery(sql);
            if (rs.next())
            {
                System.out.println("Member Details");
                System.out.printf("Name:%25s", rs.getString(2));
                System.out.printf("\nAddress:%25s  %s, %s, %s", rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
                System.out.printf("\nContact no.:%25s", rs.getString(7));
                System.out.printf("\nMembership begin date:%25s", rs.getString(8));
                System.out.printf("\nMembership end date:%25s", rs.getString(9));
                System.out.printf("\nMembership status:%25s", rs.getString(10));
            }
            else
            {
                System.out.println("No Member Found!\n");
            }
        }
    }
    static class Purchases {
        static int ord_id;			//Primary Key
        static int book_id;		//FK ref (books)
        static int sup_id;			//FK ref (suppliers)
        static int qty;
        static Date dt_ordered;
        static int eta;
        static char received;		// Check(T or C or F) def (F)
        static int inv;
        static ResultSet rs;
        static String sql;

        public static void new_ord(Statement stmt) throws SQLException {
            System.out.print("Enter the book ID: ");
            book_id = scan.nextInt();
            System.out.print("\nEnter Supplier ID: ");
            sup_id = scan.nextInt();
            System.out.print("\nEnter the Quantity: ");
            qty = scan.nextInt();
            System.out.print("\nEstimated expected Delivery (in days): ");
            eta = scan.nextInt();

            sql = "Insert into purchases(book_id, sup_id, qty, dt_ordered, eta) values(" + book_id + "," + sup_id + "," + qty +
                    "," + "curdate(), Date_add(curdate(), INTERVAL " + eta + " DAY))";
            stmt.execute(sql);
            System.out.println("New order added!");
        }
        public static void mark_received(Statement stmt) throws SQLException {
            System.out.println("Enter the order id for order received:");
            ord_id = scan.nextInt();
            sql = "UPDATE purchases SET received = 'T' WHERE ord_id  = " + ord_id;
            stmt.execute(sql);
            System.out.println("Received marked successfully\n");
        }
        public static void mar_cancel(Statement stmt) throws SQLException {
            System.out.println("Enter the order id for order cancellation");
            ord_id = scan.nextInt();
            sql = "UPDATE purchases SET received = 'C' WHERE ord_id = " + ord_id;
            stmt.execute(sql);
            System.out.println("Cancelled field marked successfully\n");
        }
        public static void view(Statement stmt) throws SQLException {
            int c;
            System.out.println("Select an Option");
            System.out.println("1. View orders not received");
            System.out.println("2. View orders cancelled");
            System.out.println("3. View orders received");
            System.out.println("Enter your choice:");
            c = scan.nextInt();
            if (c == 1)
                received = 'F';
            else if (c == 2)
                received = 'C';
            else if (c == 3)
                received = 'T';
            else
                return;
            sql = "SELECT * FROM purchases WHERE received = '" + received + "';";
            rs = stmt.executeQuery(sql);
            if (c == 1)
                System.out.println("\nOrders not recieved are");
            else if (c == 2)
                System.out.println("\nOrders cancelled are");
            else if (c == 3)
                System.out.println("\nOrders received are");
            while (rs.next())
            {
                System.out.printf("\nOrder ID:%28d", rs.getInt(1));
                System.out.printf("\nBook ID:%28d", rs.getInt(2));
                System.out.printf("\nSupplier ID:%28d", rs.getInt(3));
                System.out.printf("\nQuantity:%28d",rs.getInt(4));
                System.out.printf("\nDate Ordered:%28s",rs.getString(5));
                System.out.printf("\nEstimated delivery date:%28s",rs.getString(6));
                System.out.print("\n\n");
            }
            System.out.print("\n\n");
        }
    }
    static class Sales {
        static int invoice_id;		//Primary key
        static int member_id;		//FK ref member(id)
        static int book_id;		//FK ref books(id)
        static int qty;
        static int amount;
        Date date_s;
        static String sql;
        static ResultSet rs;

        public static void add(Statement stmt) throws SQLException {
            System.out.print("Enter member ID: ");
            member_id = scan.nextInt();
            System.out.print("\nEnter the book ID: ");
            book_id = scan.nextInt();
            System.out.print("Enter the quantity: ");
            qty = scan.nextInt();
            sql = "SELECT price*" + qty + " FROM books WHERE id = " + book_id;
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                System.out.println("The bill amount: $" + rs.getInt(1));
                amount = rs.getInt(1);
            }
            else
            {
                System.out.println("Book ID invalid!");
                return;
            }
            sql = "INSERT INTO sales(member_id,book_id,qty,amount,date_s) VALUES(" + member_id + "," + book_id + "," + qty + "," + amount + ",curdate());";
            stmt.execute(sql);
            //fetching invoice id now
            sql = "SELECT invoice_id FROM sales WHERE member_id = " + member_id + " AND book_id = " + book_id + " AND qty = " + qty + " AND date_s = curdate();";
            rs = stmt.executeQuery(sql);
            if (rs.next())
                System.out.println("The invoice id for the bill is " + rs.getInt(1) + "\n");
            else
                System.out.println("The entered details may be wrong\nPlease try again\n");
        }
        public static void find_total_sales(Statement stmt) throws SQLException {
            sql = "SELECT SUM(amount) FROM sales WHERE YEAR(date_s) = YEAR(curdate())";
            rs =  stmt.executeQuery(sql);
            if(rs.next())
                System.out.println("Total sales this year: $" + rs.getInt(1) + '\n');
        }
    }
    static class Suppliers {
        static int id;				//Primary Key
        static String name;
        static String phn;
        static String addr_line1;
        static String addr_line2;
        static String addr_city;
        static String addr_state;
        static String sql;
        static ResultSet rs;

        public static void add_sup(Statement stmt) throws SQLException {
            int idnum = 0;
            rs = stmt.executeQuery("SELECT id FROM suppliers ORDER BY id DESC LIMIT 1;");
            while (rs.next())
            {
                idnum = (rs.getInt(1)) + 1;
            }
            System.out.print("Enter the supplier name: ");
            name = scan.next();
            System.out.print("\nEnter Phone no.: ");
            phn = scan.next();
            System.out.print("\nEnter the address (in 3 lines): ");
            addr_line1 = scan.next();
            addr_line2 = scan.next();
            addr_city = scan.next();
            System.out.print("\nEnter state: ");
            addr_state = scan.next();
            sql = "INSERT INTO suppliers VALUES('" + idnum + "','" + name + "'," + phn + ",'" + addr_line1 + "','" + addr_line2 + "','" + addr_city + "','" + addr_state + "');";
            if (stmt.execute(sql))
                System.out.println("\n\nSupplier record inserted successfully\n");
            else
                System.out.println("\n\nEntry error!\n");
        }
        public static void remove_supplier(Statement stmt) throws SQLException {
            System.out.print("Enter the id of the supplier you wish to remove: ");
            id = scan.nextInt();
            sql = "DELETE FROM suppliers WHERE id = " + id;
            stmt.execute(sql);
            System.out.println("Supplier removed");
        }
        public static void search_id(Statement stmt) throws SQLException {
            System.out.print("Enter the id of the supplier who's details you seek: ");
            id = scan.nextInt();
            sql = "SELECT * FROM suppliers WHERE id = " + id;
            rs = stmt.executeQuery(sql);
            if(rs.next())
            {
                System.out.printf("Details of Supplier Id:%25d", rs.getInt(1));
                System.out.printf("\nName:%28s", rs.getString(2));
                System.out.printf("\nPhone no.:%28s", rs.getString(3));
                System.out.printf("\nAddress Line 1:%28s", rs.getString(4));
                System.out.printf("\nAddress Line 2:%28s",rs.getString(5));
                System.out.printf("\nCity:%28s", rs.getString(6));
                System.out.printf("\nState:%28s", rs.getString(7));
            }
            else
                System.out.println("No supplier found!\n");
        }
    }
    static class Menus {
        static int c;

        public static void main_menu(Statement stmt)
        {
            try {
                System.out.println("*************************************************");
                System.out.println("         BOOKSHOP MANGEMENT SYSTEM");
                System.out.println("*************************************************");
                System.out.println("   1. BOOKS");
                System.out.println("   2. SUPPLIERS");
                System.out.println("   3. PURCHASES");
                System.out.println("   4. EMPLOYEES");
                System.out.println("   5. MEMBERS");
                System.out.println("   6. SALES");
                System.out.println("   7. EXIT\n\n");
                System.out.print("Enter Your Choice: ");
                while(true) {
                    try {
                        System.out.println("Please enter a number: ");
                        c = scan.nextInt();
                        break;
                    }
                    catch(InputMismatchException | NumberFormatException ex ) {
                        scan.next();
                    }
                }
               // c = scan.nextInt();
                switch (c) {
                    case 1:
                        book_menu(stmt);
                        break;
                    case 2:
                        sup_menu(stmt);
                        break;
                    case 3:
                        pur_menu(stmt);
                        break;
                    case 4:
                        emp_menu(stmt);
                        break;
                    case 5:
                        mem_menu(stmt);
                        break;
                    case 6:
                        sal_menu(stmt);
                        break;
                    case 7:
                        System.out.println("Sayonara!");
                        exit(0);
                    default:
                        System.out.println("Out of range, try again\n");
                        break;
                }
                return;
            }
            catch (SQLException se)
            {
                System.err.println("Exception found: " + se);
            }
        }
        public static void book_menu(Statement stmt) throws SQLException {
                System.out.println("*************************************************");
                System.out.println("                  BOOK MENU");
                System.out.println("*************************************************");
                System.out.println("   1. ADD");
                System.out.println("   2. UPDATE PRICE");
                System.out.println("   3. SEARCH");
                System.out.println("   4. UPDATE STATUS");
                System.out.println("   5. DISPLAY ALL");
                System.out.println("   6. RETURN TO MAIN MENU\n\n");
                System.out.print("Enter Your Choice: ");
            while(true) {
                try {
                    System.out.println("Please enter a number: ");
                    c = scan.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    scan.next();
                }
            }
                System.out.println();
                switch (c)
                {
                    case 1:
                        Books.add(stmt);
                        break;
                    case 2:
                        Books.update_price(stmt);
                        break;
                    case 3:
                        Books.search(stmt);
                        break;
                    case 4:
                        Books.update(stmt);
                        break;
                    case 5:
                        Books.display(stmt);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Out of range, try again\n");
                        break;
                }
        }
        public static void sup_menu(Statement stmt) throws SQLException {
            System.out.println("*************************************************");
            System.out.println("                SUPPLIER MENU");
            System.out.println("*************************************************");
            System.out.println("   1. ADD");
            System.out.println("   2. REMOVE");
            System.out.println("   3. SEARCH");
            System.out.println("   4. RETURN TO MAIN MENU\n\n");
            System.out.println("Enter Your Choice: ");
            while(true) {
                try {
                    System.out.println("Please enter a number: ");
                    c = scan.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    scan.next();
                }
            }
            System.out.println();
            switch (c)
            {
                case 1:
                    Suppliers.add_sup(stmt);
                    break;
                case 2:
                    Suppliers.remove_supplier(stmt);
                    break;
                case 3:
                    Suppliers.search_id(stmt);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Out of range, try again\n");
                    break;
            }
        }
        public static void pur_menu(Statement stmt) throws SQLException {
            System.out.println("*************************************************");
            System.out.println("                PURCHASES MENU");
            System.out.println("*************************************************");
            System.out.println("   1. New Order");
            System.out.println("   2. View All");
            System.out.println("   3. Cancel Order");
            System.out.println("   4. Recieved Order");
            System.out.println("   5. RETURN TO MAIN MENU\n\n");
            System.out.println("Enter Your Choice: ");
            while(true) {
                try {
                    System.out.println("Please enter a number: ");
                    c = scan.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    scan.next();
                }
            }
            System.out.println();
            switch (c)
            {
                case 1:
                    Purchases.new_ord(stmt);
                    break;
                case 2:
                    Purchases.view(stmt);
                    break;
                case 3:
                    Purchases.mar_cancel(stmt);
                    break;
                case 4:
                    Purchases.mark_received(stmt);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Out of range, try again\n");
                    break;
            }
        }
        public static void emp_menu(Statement stmt) throws SQLException {
            System.out.println("*************************************************");
            System.out.println("                 EMPLOYEE MENU");
            System.out.println("*************************************************");
            System.out.println("   1. New Employee");
            System.out.println("   2. Search Employee");
            System.out.println("   3. Assign Manager");
            System.out.println("   4. View All");
            System.out.println("   5. Update Salary");
            System.out.println("   6. RETURN TO MAIN MENU\n\n");
            System.out.println("Enter Your Choice: ");
            while(true) {
                try {
                    System.out.println("Please enter a number: ");
                    c = scan.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    scan.next();
                }
            }
            System.out.println();
            switch (c)
            {
                case 1:
                    Employees.add_emp(stmt);
                    break;
                case 2:
                    Employees.search_emp(stmt);
                    break;
                case 3:
                    Employees.assign_mgr_stat(stmt);
                    break;
                case 4:
                    Employees.display(stmt);
                    break;
                case 5:
                    Employees.update_sal(stmt);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Out of range, try again\n");
                    break;
            }
        }
        public static void mem_menu(Statement stmt) throws SQLException {
            Members.refresh(stmt);
            System.out.println("*************************************************");
            System.out.println("                 MEMBERS MENU");
            System.out.println("*************************************************");
            System.out.println("   1. New Member");
            System.out.println("   2. Search Member" );
            System.out.println("   3. RETURN TO MAIN MENU\n\n");
            System.out.println("Enter Your Choice: ");
            while(true) {
                try {
                    System.out.println("Please enter a number: ");
                    c = scan.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    scan.next();
                }
            }
            System.out.println();
            switch (c)
            {
                case 1:
                    Members.add_mem(stmt);
                    break;
                case 2:
                    Members.search_mem(stmt);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Out of range, try again\n");
                    break;
            }
        }
        public static void sal_menu(Statement stmt) throws SQLException {
            System.out.println("*************************************************");
            System.out.println("                 SALES MENU");
            System.out.println("*************************************************");
            System.out.println("   1. Add New Bill");
            System.out.println("   2. Total Sales Of The Year");
            System.out.println("   3. RETURN TO MAIN MENU\n\n");
            System.out.println("Enter Your Choice: ");
            while(true) {
                try {
                    System.out.println("Please enter a number: ");
                    c = scan.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    scan.next();
                }
            }
            System.out.println();
            switch (c)
            {
                case 1:
                    Sales.add(stmt);
                    break;
                case 2:
                    Sales.find_total_sales(stmt);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Wrong Input\nInvalid input");
                    break;
            }
        }
    }
}



