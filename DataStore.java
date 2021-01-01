import java.io.*;
import java.util.*;
import org.json.simple.JSONObject;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator; 
class DataStore{
	static Scanner sc=new Scanner(System.in);
	static File f;
	static RandomAccessFile fileAccess;
	static String path;
	static long currloc=0;
	static long limit=0;
	static Map<String,Long>keysData=new HashMap<>();
	static List<String> delKey=new ArrayList<>();
	static Map<String,Long> keytime=new HashMap<>();
	DataStore() throws IOException{
		System.out.println(f.exists()==true?"File created successfully":"File not created");
		path=f.getAbsolutePath();
		create();
	}
	private long getSize(Object object)  {
		try {
			  return ObjectSizeCalculator.getObjectSize(object); 
		}catch(Exception e) {
			  e.printStackTrace();
		}
	  return 0;
	}
	private Boolean isValueValid(JSONObject value) {
		long size = getSize(value);
		return (size != null && size<=16000)?true:false;
	}
	private boolean isKeyValid(String key) {
		return (key.length()<=32)?true:false;
	}
	protected synchronized void create() {
		System.out.println("Enter Key :");
		String key=sc.next();
		System.out.println("Enter Value :");
		String val=sc.next();
		JSONObject obj=new JSONObject();
		obj.put("name",val);
		System.out.println("Do you want to set TimeLimit to key");
		int time=0;
		if(sc.next().equals("yes")) {
			System.out.println("Enter KeyLimit :");
			time = sc.nextInt();
		}
		limit=time==0?0:(System.currentTimeMillis()+(time*1000));
		create(key,obj,limit);
	}
	protected static synchronized boolean timeValid(String key) {
        if(limit==0 || System.currentTimeMillis()<keytime.get(key)) {
			return true;
		}
        keysData.remove(key);
        keytime.remove(key);
        return false;
	}
	protected synchronized void create(String key, JSONObject value,long limit) {
		try {
			if(isKeyValid(key) && !keysData.containsKey(key) && isValueValid(value)) {
			    keytime.put(key,limit);
				keysData.put(key,(long)1);
			    write(key,value);
			}
			else {
				System.out.println("Invalid Key/Value\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	static synchronized void read(String key) throws IOException {
		if(keysData.containsKey(key) && !delKey.contains(key) && timeValid(key)) {
			RandomAccessFile file = new RandomAccessFile(path,"rw");
			file.seek(keysData.get(key));
		    System.out.println(file.readLine());
			file.close();
		}
		else {
			System.out.println("Key not found");
		}
	}
	
    void write(String key, JSONObject value) throws Exception {
    	fileAccess= new RandomAccessFile(path, "rw");
		fileAccess.seek(currloc);
		String data = key+":"+value+"\n";
		fileAccess.write(data.getBytes());
		long location=fileAccess.getFilePointer();
		keysData.put(key,currloc);	
		currloc=location;
		fileAccess.close();
	}
    static void delete(String key) throws IOException {
    	if(keysData.containsKey(key) && !delKey.contains(key)) {
    		delKey.add(key);
    		System.out.println("key deleted");
    	}
    	else {
    		System.out.println("key not found to delete");
    	}
    } 
}
