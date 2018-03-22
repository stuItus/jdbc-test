package jdbc_home;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	static Connection conn; 
	
	public static void main(String[] args) throws SQLException {
		
		String url = "jdbc:mysql://localhost:3306/human_db?autoReconnect=true&useSSL=false";
		String username = "root";
		String password = "***;
		conn = DriverManager.getConnection(url, username, password);
		System.out.println("Connected? " + !conn.isClosed());
		
		createTableHuman();
		
		int id = 0;
		String firstName = null;
		String lastName = null;
		int age = 0;
		String hobby = null; 
		boolean run = true;
		int key = 0;
		Scanner sc = new Scanner(System.in);
		while(run) {
			System.out.println("Choose from below:");
			System.out.println("1 - add human to DB");
			System.out.println("2 - delete human from DB by ID");
			System.out.println("3 - print list of humans from DB");
			System.out.println("4 - find human by ID");
			System.out.println("5 - fill in DB by random data");
			System.out.println("6 - EXIT");
			if (sc.hasNext()) {
				key = sc.nextInt();
			} if(key > 0 && key < 7) {
				switch (key) {
				case 1:
					System.out.println("Enter first name:");
					if (sc.hasNext()) {
						firstName = sc.next();
					}
					System.out.println("Enter last name:");
					if (sc.hasNext()) {
						lastName = sc.next();
					}
					System.out.println("Enter age");
					if (sc.hasNext()) {
						age = sc.nextInt();
					}
					System.out.println("And finally hobby");
					if (sc.hasNext()) {
						hobby = sc.next();
					}
					addHuman(firstName, lastName, age, hobby);
					break;
				case 2:
					System.out.println("Enter ID for deleting:");
					if (sc.hasNext()) {
						id = sc.nextInt();
					}
					delete(id);
					break;
				case 3:
					printHumans();
					break;
				case 4:
					System.out.println("Enter ID for finding:");
					if (sc.hasNext()) {
						id = sc.nextInt();
					}
					find(id);
					break;
				case 5:
					System.out.println("How many random entries do you want to add?");
					int num = 0;
					if (sc.hasNext()) {
						num = sc.nextInt();
					}
					for (int i = 0; i < num; i++) {
						int rand = (int) (Math.random()*100);
						int rand1 = (int) (Math.random()*26+65);
						int rand2 = (int) (Math.random()*26+65);
						int rand3 = (int) (Math.random()*26+65);
						String randC1 = Character.toString ((char) rand1);
						String randC2 = Character.toString ((char) rand2);
						String randC3 = Character.toString ((char) rand3);
						
						randomFillIn(randC1, randC2, rand, randC3);
					}
					break;
				case 6:
					run = false;
					break;
				default:
					break;
				}
			} else {
				System.out.println("You've entered a wrong number! Please try again)");
			}
		}
		sc.close();
	}
	
	static void createTableHuman() throws SQLException {
		String dropQuery = "DROP TABLE IF EXISTS human;";
		String query = "CREATE TABLE human("
				+ "id INT PRIMARY KEY AUTO_INCREMENT, "
				+ "first_name VARCHAR(100), "
				+ "last_name VARCHAR(100), "
				+ "age INT, "
				+ "hobby VARCHAR(100) "
				+ ");";
		Statement st = conn.createStatement();
		st.execute(dropQuery);
		st.execute(query);
		st.close();
	}
	
	static void addHuman(String firstName, String lastName, int age, String hobby) throws SQLException {
		String query = "INSERT INTO human(first_name, last_name, age, hobby) VALUES(?, ?, ?, ?)";
		
		PreparedStatement pst = conn.prepareStatement(query);
		pst.setString(1, firstName);
		pst.setString(2, lastName);
		pst.setInt(3, age);
		pst.setString(4, hobby);
		pst.executeUpdate();
		pst.close();
	}
	
	static void delete(int id) throws SQLException {
		String query = "DELETE FROM human WHERE ID = " + id;
		
		Statement st = conn.createStatement();
		st.execute(query);
		st.close();
	}
	
	static void printHumans() throws SQLException {
		String query = "SELECT * FROM human";
		
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		List<String> humans = new ArrayList<>();
		
		while (rs.next()) {
			humans.add(
					"ID: " + rs.getInt("id") + "\t| "
					+ "First Name: " + rs.getString("first_name") + "\t| " 
					+ "Last Name: " + rs.getString("last_name") + "\t| " 
					+ "Age: " + rs.getInt("age") + "\t| "
					+ "Hobby: " + rs.getString("hobby")
					);
		}
		
		humans.forEach( h -> System.out.println(h));
	}
		
	static void find(int id) throws SQLException {
		String query = "SELECT * FROM human WHERE ID = " + id;
		
		PreparedStatement pst = conn.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		
		List<String> humans = new ArrayList<>();
		
		while (rs.next()) {
			humans.add(
					"ID: " + rs.getInt("id") + "\t| "
					+ "First Name: " + rs.getString("first_name") + "\t| " 
					+ "Last Name: " + rs.getString("last_name") + "\t| " 
					+ "Age: " + rs.getInt("age") + "\t| "
					+ "Hobby: " + rs.getString("hobby")
					);
		}
		
		humans.forEach( h -> System.out.println(h));
		rs.close();
		pst.close();
	}

	static void randomFillIn(String randC1, String randC2, int rand, String randC3) throws SQLException {
		String query = "INSERT INTO human(first_name, last_name, age, hobby) VALUES(?, ?, ?, ?)";
		PreparedStatement pst = conn.prepareStatement(query);
		
		pst.setString(1, randC1);
		pst.setString(2, randC2);
		pst.setInt(3, rand);
		pst.setString(4, randC3);
		pst.executeUpdate();
		pst.close();
	}
}
