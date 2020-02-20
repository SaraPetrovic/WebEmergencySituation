	package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class Comments {
	
	private ArrayList<Comment> comments = new ArrayList<Comment>();
	
	public Comments() {
		
	}
	public Comments(String path, EmergencySituations emergencies, Users users) {
		BufferedReader in = null;
		try {
			File file = new File(path + "/comments.txt");
			in = new BufferedReader(new FileReader(file));
			readComments(in, emergencies, users);
		}catch(Exception e) {
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
	
	public void readComments(BufferedReader in, EmergencySituations emergencies, Users users) {
		String line, id = "", text = "", date = "", username = "", emergencyId = "";
		StringTokenizer st;
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim();
					text = st.nextToken().trim();
					date = st.nextToken().trim();
					username = st.nextToken().trim();
					emergencyId = st.nextToken().trim();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date dateTime = sdf.parse(date);
				User user = users.getUser(username);
				EmergencySituation emergency = emergencies.getEmergencySituation(Integer.parseInt(emergencyId));
				Comment comment = new Comment(Integer.parseInt(id), text, dateTime, user, emergency);
				comments.add(comment);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}
	
}
