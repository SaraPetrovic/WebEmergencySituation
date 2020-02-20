package Service;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import Model.Area;
import Model.Areas;
import Model.Comment;
import Model.EmergencySituation;
import Model.EmergencySituations;
import Model.Urgency;
import Model.User;
import Model.Users;

@Path("/emergencies")
public class EmergencySituationService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	public static int counterEs = 0;
	public static int idEs;
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(EmergencySituation emergencySituation) {
		
		//AKO JE LOGOVANI USER BLOKIRANI VOLONTER -> ERROR
		User user = (User) request.getSession().getAttribute("user");
		if(user == null || user.isBlocked() == true) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		if(counterEs == 0) {
			idEs = getEmergencies().getValues().size();
			counterEs = 1;
		}
		idEs++;
		System.out.println("ES " + idEs);
		Area area = getAreas().getArea(emergencySituation.getArea().getName());
		
		User volunteer = null;
		if(emergencySituation.getVolunteer() != null || emergencySituation.getVolunteer().getId() != 0) {
			for(User u: getUsers().getValues()) {
				if(u.getId() == emergencySituation.getVolunteer().getId()) {
					volunteer = u;
				}
			}
		}
		
		Urgency urgency = emergencySituation.getUrgency();
		System.out.println("BOJAA:" + urgency);	
		
		Date d = Calendar.getInstance(Locale.getDefault()).getTime();
		System.out.println("DATUM: " +  d);
		EmergencySituation emergency = new EmergencySituation(idEs, emergencySituation.getPlaceName(), emergencySituation.getTownship(),
				emergencySituation.getDescription(), d, emergencySituation.getAddress(), area, urgency, emergencySituation.getPhoto(), true, volunteer);
		
		getEmergencies().getEmergencySituations().put(idEs, emergency);
		
		return Response.status(Status.OK).entity(emergency).build();
	}
	
	@GET
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		ArrayList<EmergencySituation> list = new ArrayList<EmergencySituation>();
		for(EmergencySituation e : getEmergencies().getValues()) {
			if(e.isActive() == true) {
				list.add(e);
			}
		}
		return Response.status(Status.OK).entity(list).build();
	}
	
	@POST
	@Path("/changeActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeActivity(EmergencySituation em) {
		EmergencySituation e = null;
		for(EmergencySituation ems : getEmergencies().getValues()) {
			if(ems.getId() == em.getId()) {
				e = ems;
			}
		}
		e.setActive(em.isActive());
		getEmergencies().getEmergencySituations().remove(em.getId());
		getEmergencies().getEmergencySituations().put(e.getId(), e);
		try {
			writeToFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return Response.status(Status.OK).entity(e).build();
	}
	
	@POST
	@Path("/changeVolunteer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeVolunteer(EmergencySituation em) {
		EmergencySituation e = null;
		for(EmergencySituation ems : getEmergencies().getValues()) {
			if(ems.getId() == em.getId()) {
				e = ems;
			}
		}
		for(User u: getUsers().getValues()) {
			if(u.getId() == em.getVolunteer().getId()) {
				e.setVolunteer(u);
			}
		}
		getEmergencies().getEmergencySituations().remove(em.getId());
		getEmergencies().getEmergencySituations().put(e.getId(), e);
		try {
			writeToFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return Response.status(Status.OK).entity(e).build();
	}
	
	@GET
	@Path("/filter/{criterion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filter(@PathParam("criterion") String criterion) {
		
		String criterion1 = criterion.split(",")[0];
		String criterion2 = criterion.split(",")[1];
		
		ArrayList<EmergencySituation> emergencies = filter(criterion1, criterion2);
		
		return Response.status(Status.OK).entity(emergencies).build();
	}
	
	@GET
	@Path("/search/{criterion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("criterion") String criterion) {
		
		String criterion1 = criterion.split(",")[0];
		String criterion2 = criterion.split(",")[1];
		
		Collection<EmergencySituation> collection = getEmergencies().getValues();
		ArrayList<EmergencySituation> ems = new ArrayList<EmergencySituation>(collection);
		
		ArrayList<EmergencySituation> emergencies = search(criterion1, criterion2, ems);
		
		return Response.status(Status.OK).entity(emergencies).build();
	}
	
	@GET
	@Path("/filterSearch/{criterion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterSearch(@PathParam("criterion") String criterion) {
	
		String criterion1 = criterion.split(",")[0];
		String criterion2 = criterion.split(",")[1];
		String criterion3 = criterion.split(",")[2];
		String criterion4 = criterion.split(",")[3];
		
		ArrayList<EmergencySituation> emergencySituations = filter(criterion1, criterion2);
		ArrayList<EmergencySituation> ems = search(criterion3, criterion4, emergencySituations);
		
		return Response.status(Status.OK).entity(ems).build();
	}
	
	
	public ArrayList<EmergencySituation> filter(String criterion1, String criterion2) {
		
		ArrayList<EmergencySituation> emergencies;
		
		if(criterion1.equals("date")) {
			//najnovije
			Collection<EmergencySituation> collection = getEmergencies().getValues();
			emergencies = new ArrayList<EmergencySituation>(collection);
			Collections.sort(emergencies, EmergencySituation.EmergencySituationComparator);
			Collections.reverse(emergencies);
		}
		else if(criterion1.equals("urgency")) {
			emergencies = new ArrayList<EmergencySituation>();
			for(EmergencySituation em : getEmergencies().getValues()) {
				if(em.getUrgency().toString().equals(criterion2.toUpperCase())) {
					emergencies.add(em);
				}
			}
		}
		else{ //criterion1 == "area"
			emergencies = new ArrayList<EmergencySituation>();
			for(EmergencySituation em : getEmergencies().getValues()) {
				if(em.getArea().getId() == Integer.parseInt(criterion2)) {
					emergencies.add(em);
				}
			}
		}
		
		return emergencies;
	}
	
	public ArrayList<EmergencySituation> search(String criterion1, String criterion2, ArrayList<EmergencySituation> emergencySituations) {
		
		ArrayList<EmergencySituation> emergencies = new ArrayList<EmergencySituation>();
		
		if(criterion1.equals("area")) {
			for(EmergencySituation em : emergencySituations) {
				if(em.getArea().getName().toLowerCase().contains(criterion2.toLowerCase())) {
					emergencies.add(em);
				}
			}
		}else if(criterion1.equals("township")) {
			for(EmergencySituation em : emergencySituations) {
				if(em.getTownship().toLowerCase().contains(criterion2.toLowerCase())) {
					emergencies.add(em);
				}
			}
		}else if(criterion1.equals("description")) {
			for(EmergencySituation em : emergencySituations) {
				if(em.getDescription().toLowerCase().contains(criterion2.toLowerCase())) {
					emergencies.add(em);
				}
			}
		}else if(criterion1.equals("volunteer")) {
			for(EmergencySituation em : emergencySituations) {
				if(em.getVolunteer().getName().toLowerCase().contains(criterion2.toLowerCase()) || 
						em.getVolunteer().getSurname().toLowerCase().contains(criterion2.toLowerCase()) || 
						em.getVolunteer().getUsername().toLowerCase().contains(criterion2.toLowerCase())) {
					emergencies.add(em);
				}
			}
		}else { //criterion1.equals("withoutvolunteer")
			for(EmergencySituation em : emergencySituations) {
				if(em.getVolunteer().getId() == 0) {
					emergencies.add(em);
				}
			}
		}
		return emergencies;
	}
	
	private void writeToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ctx.getRealPath("") + "/emergencySituations.txt"), StandardCharsets.UTF_16));
		for(EmergencySituation e : getEmergencies().getValues()) {
			writer.write(String.valueOf(e.getId()) + ";" + e.getPlaceName() + ";" + e.getTownship() + ";" + e.getDescription() + ";" + String.valueOf(e.getDatetime()) + ";" + e.getAddress() + ";" + String.valueOf(e.getArea().getId() + ";" + String.valueOf(e.getUrgency() + ";" + e.getPhoto() + ";" + String.valueOf(e.isActive() + ";" + String.valueOf(e.getVolunteer().getId())))));
			writer.newLine();
		}
		writer.close();
	}
	
	private Users getUsers() {
		Users users = (Users) ctx.getAttribute("users");
		if(users == null) {
			users = new Users(ctx.getRealPath(""), getAreas());
			ctx.setAttribute("users", users);
		}
		return users;
	}

	private Areas getAreas() {
		Areas areas = (Areas) ctx.getAttribute("areas");
		if(areas == null) {
			areas = new Areas(ctx.getRealPath(""));
			ctx.setAttribute("areas", areas);
		}
		return areas;
	}
	
	private EmergencySituations getEmergencies() {
		EmergencySituations emergencies = (EmergencySituations) ctx.getAttribute("emergencies");
		if(emergencies == null) {
			emergencies = new EmergencySituations(ctx.getRealPath(""), getUsers(), getAreas());
			ctx.setAttribute("emergencies", emergencies);
		}
		return emergencies;
	}
}
