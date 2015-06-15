package databaseFirstConnectionApp;

import java.sql.*;
import java.text.DateFormat;
import java.text.NumberFormat;

public class DatabaseFirstConnectionApp {
	public static void main(String[] args){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","password");
			Statement stmt = con.createStatement();
			
			//Signature Uses (Order Number, Statement)			
			printInvoice(new Integer("1"), stmt);
			
			System.out.println();
			
			//Signature Uses (Customer Number, Statement)
			printAllInvoice(new Integer("7"), stmt);
			
			con.close();	
		} catch (Exception e){
			System.out.println(e);
		}//END try catch
	}//END main
	
	public static void printAllInvoice(Integer orderNumber, Statement stmt){
		printUserInfo2(orderNumber, stmt);
		printInvoices(orderNumber, stmt);
	}//END printInvoice
	
	
	public static void printInvoice(Integer orderNumber, Statement stmt){
		printOrderInfo(orderNumber, stmt);
		printUserInfo(orderNumber, stmt);
		printOrderItems(orderNumber, stmt);
	}//END printInvoice
	
	public static void printOrderInfo(Integer orderNumber, Statement stmt){
		try{
			ResultSet rs = stmt.executeQuery("SELECT ORDER_TIMESTAMP "
											+ "FROM TESTUSER.DEMO_ORDERS "
											+ "INNER JOIN TESTUSER.DEMO_CUSTOMERS "
											+ "ON DEMO_ORDERS.CUSTOMER_ID = DEMO_CUSTOMERS.CUSTOMER_ID " 
											+ "WHERE DEMO_ORDERS.ORDER_ID = "
											+ orderNumber.toString()
											);
			while(rs.next())
				System.out.println(DateFormat.getDateInstance().format(rs.getDate(1)) + " - - - - - - - - - - - - - - - - Order: " + orderNumber.toString());
		}catch (Exception e){
			System.out.println(e);
		}//END try catch
	}//END printOrderInfo
		
	public static void printUserInfo(Integer orderNumber, Statement stmt){
		try{
			ResultSet rs = stmt.executeQuery("SELECT CUST_FIRST_NAME, CUST_LAST_NAME, CUST_STREET_ADDRESS1, CUST_CITY, CUST_STATE "
											+ "FROM TESTUSER.DEMO_ORDERS "
											+ "INNER JOIN TESTUSER.DEMO_CUSTOMERS "
											+ "ON DEMO_ORDERS.CUSTOMER_ID = DEMO_CUSTOMERS.CUSTOMER_ID " 
											+ "WHERE DEMO_ORDERS.ORDER_ID = "
											+ orderNumber.toString()
											);
			while(rs.next())
				System.out.println("\n"+rs.getString(1) +" "
						+rs.getString(2)+"\n"
						+rs.getString(3)+"\n"
						+rs.getString(4)+" "
						+rs.getString(5));				
		}catch (Exception e){
			System.out.println(e);
		}//END 
	}//END printUserInfo
	
	public static void printOrderItems(Integer orderNumber, Statement stmt){
		try{
			System.out.println("\nQty\t\tDescription\t\tAmount\n--------------------------------------------------");
			
			ResultSet rs = stmt.executeQuery("SELECT QUANTITY , UNIT_PRICE, PRODUCT_NAME "
					+ "FROM TESTUSER.DEMO_ORDER_ITEMS "
					+ "INNER JOIN TESTUSER.DEMO_PRODUCT_INFO "
					+ "ON DEMO_ORDER_ITEMS.PRODUCT_ID = DEMO_PRODUCT_INFO.PRODUCT_ID "
					+ "WHERE DEMO_ORDER_ITEMS.ORDER_ID = "
					+ orderNumber.toString()
					);

			NumberFormat currency = NumberFormat.getCurrencyInstance();
			int total = 0;
			while(rs.next()){
				int temp = (rs.getInt(1) * rs.getInt(2));
				if(rs.getString(3).length() < 7)
					System.out.println(rs.getString(1) + "\t\t" + rs.getString(3)  + "\t\t\t" + currency.format(temp));
				else 
					System.out.println(rs.getString(1) + "\t\t" + rs.getString(3)  + "\t\t" + currency.format(temp));
				total += temp;
			}//END while
			System.out.println("\t\t\tTotal:\t      " + currency.format(total));
		}catch (Exception e){
			System.out.println(e);
		}//END try catch
	}//END getOrder
	
	public static void printUserInfo2(Integer cusNumber, Statement stmt){
		try{
			ResultSet rs = stmt.executeQuery("SELECT CUST_FIRST_NAME, CUST_LAST_NAME, CUST_STREET_ADDRESS1, CUST_CITY, CUST_STATE "
											+ "FROM TESTUSER.DEMO_ORDERS "
											+ "INNER JOIN TESTUSER.DEMO_CUSTOMERS "
											+ "ON DEMO_ORDERS.CUSTOMER_ID = DEMO_CUSTOMERS.CUSTOMER_ID " 
											+ "WHERE DEMO_ORDERS.CUSTOMER_ID = "
											+ cusNumber.toString()
											);
			rs.next();
			System.out.println("\n"+rs.getString(1) +" "
					+rs.getString(2)+"\n"
					+rs.getString(3)+"\n"
					+rs.getString(4)+" "
					+rs.getString(5));				
		}catch (Exception e){
			System.out.println(e);
		}//END 
	}//END printUserInfo
	
	public static void printInvoices(Integer cusNum, Statement stmt){
		try{
			System.out.println("\nDate\t\tOrder ID\t\tTotal\n--------------------------------------------------");
			
			ResultSet rs = stmt.executeQuery("SELECT ORDER_TIMESTAMP, ORDER_ID, ORDER_TOTAL "
					+ "FROM TESTUSER.DEMO_ORDERS "
					+ "WHERE DEMO_ORDERS.CUSTOMER_ID = "
					+ cusNum.toString()
					);

			NumberFormat currency = NumberFormat.getCurrencyInstance();
			int total = 0;
			while(rs.next()){
				int temp = rs.getInt(3);
				System.out.println(DateFormat.getDateInstance().format(rs.getDate(1)) + "\t" + rs.getString(2)  + "\t\t\t" + currency.format(temp));				
				total += temp;
			}
			System.out.println("\t\t\tTotal:\t\t" + currency.format(total));			
		}catch (Exception e){
			System.out.println(e);
		}//END try catch
	}//END getOrder
	
}//END main
