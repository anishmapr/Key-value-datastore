import java.io.*;
import java.util.*;
import org.json.simple.JSONObject;
public class Main extends Data{
	Main() throws IOException {
		super();
	}
	public static void userOption() throws IOException {
		System.out.print("1.Create 2.Read 3.Delete\n");
		int option=sc.nextInt();
		switch(option) {
		case 1:
			new Data();
			break;
		case 2:
			System.out.println("Enter key to read :");
			String key=sc.next();
			read(key);
			break;
		case 3:
			System.out.println("Enter key to delete :");
			String k=sc.next();
			delete(k);
			break;
		default:
			System.out.println("Invalid Option");
		}
    }
	public static void main(String[] args) throws Exception {
		f=new File("database.db");
		f.createNewFile();
		f.delete();
		int val=1;
		do{
		userOption();
		System.out.println("Press 0 to exit and 1 to continue");
		val=sc.nextInt();
	    }while(val!=0);
		f.deleteOnExit();
		System.out.println("Successfully exit");
	}
}
