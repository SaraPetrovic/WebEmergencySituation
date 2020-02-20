package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Users {

	private HashMap<String, User> users = new HashMap<String, User>();
	
	public Users() {
		
	}
	public Users(String path, Areas areas) {
		BufferedReader in = null;
		try {
			File file = new File(path + "/users.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			readUsers(in, areas);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if ( in != null ) {
				try {
					in.close();
				}
				catch (Exception e) { }
			}
		}
	}

	public void readUsers(BufferedReader in, Areas areas) {
		StringTokenizer st;
		String line, id = "", type = "", username = "", pass = "", name = "", surname = "", number = "", email = "", photo = "", areaId = "";
		String blocked = "";
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.startsWith("#"))
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					id = st.nextToken().trim();
					type = st.nextToken().trim();
					username = st.nextToken().trim();
					pass = st.nextToken().trim();
					name = st.nextToken().trim();
					surname = st.nextToken().trim();
					number = st.nextToken().trim();
					email = st.nextToken().trim();
					photo = st.nextToken().trim();
					areaId = st.nextToken().trim();
					blocked = st.nextToken().trim();
				}
				Area area = null;
				for(Area a : areas.getValues()) {
					if(a.getId() == Integer.parseInt(areaId)) {
						area = a;
					}
				}

				User user = new User(Integer.parseInt(id), username, pass, name, surname, number, email, photo, area, Boolean.parseBoolean(blocked));
				if(("VOLUNTEER").equals(type.toUpperCase())) {
					user.setUserType(UserType.VOLUNTEER);
				}else {
					user.setUserType(UserType.ADMIN);
				}
				users.put(username, user);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public HashMap<String, User> getUsers(){
		return users;
	}
	public Collection<User> getValues(){
		return users.values();
	}
	
	public User getUser(String username) {
		return users.get(username);
	}
}
