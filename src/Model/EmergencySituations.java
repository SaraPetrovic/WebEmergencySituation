package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class EmergencySituations {

	private HashMap<Integer, EmergencySituation> emergencySituations =  new HashMap<Integer, EmergencySituation>();
	
	public EmergencySituations() {
		
	}
	public EmergencySituations(String path, Users users, Areas areas) {
		BufferedReader in = null;
		try {
			File file = new File(path + "/emergencySituations.txt");
			System.out.println(file.getCanonicalPath());
			in = new BufferedReader(new FileReader(file));
			readEmergencySituations(in, users, areas);
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
	
	public void readEmergencySituations(BufferedReader in, Users users, Areas areas){
		String line, id = "", placeName = "", township = "", desc = "", dateTime = "",
				location = "", areaId = "", urgency = "", photo = "", active = "", volunteerId = "";
		
		StringTokenizer st;
		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.startsWith("#"))
					continue;
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					id = st.nextToken();
					placeName = st.nextToken().trim();
					township = st.nextToken().trim();
					desc = st.nextToken().trim();
					dateTime = st.nextToken().trim();
					location = st.nextToken().trim();
					areaId = st.nextToken().trim();
					urgency = st.nextToken().trim();
					photo = st.nextToken().trim();
					active = st.nextToken().trim();
					volunteerId = st.nextToken().trim();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = sdf.parse(dateTime);
				
				Area area = null;
				for(Area a : areas.getValues()) {
					if(a.getId() == Integer.parseInt(areaId)) {
						area = a;
					}
				}
				
				Urgency color = null;
				if(urgency.toLowerCase().equals("blue")){
					color = Urgency.BLUE;
				}else if(urgency.toLowerCase().equals("orange")){
					color = Urgency.ORANGE;
				}else{
					color = Urgency.RED;
				}
				User volunteer = null;
				for(User u : users.getValues()) {
					if(u.getId() == Integer.parseInt(volunteerId)) {
						volunteer = u;
					}
				}
				EmergencySituation es = new EmergencySituation(Integer.parseInt(id), placeName, township, desc, date,
						location, area, color, photo, Boolean.parseBoolean(active), volunteer);
				emergencySituations.put(Integer.parseInt(id), es);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public Collection<EmergencySituation> getValues(){
		return emergencySituations.values();
	}
	
	public EmergencySituation getEmergencySituation(int id) {
		return emergencySituations.get(id);
	}
	
	public HashMap<Integer, EmergencySituation> getEmergencySituations() {
		return emergencySituations;
	}
}
