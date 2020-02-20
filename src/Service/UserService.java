package Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UTFDataFormatException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
import Model.User;
import Model.UserType;
import Model.Users;

@Path("/users")
public class UserService {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	public static int counterUser = 0;
	public static int idUser;
	public final static String DEFAULT_IMAGE_FOLDER = "WebProject/WebContent/images/";
	
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signUp(User user) throws IOException {
		for(User u : getUsers().getValues()) {
			if(u.getUsername().equals(user.getUsername())) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		
		if(counterUser == 0) {
			idUser = getUsers().getValues().size();
			counterUser = 1;
		}
		idUser++;
		System.out.println("USER " + idUser);
		Area area = getAreas().getArea(user.getArea().getName());
		User newUser = new User(idUser, UserType.VOLUNTEER, user.getUsername(), user.getPassword(), user.getName(), user.getSurname(),
				user.getPhoneNumber(), user.getEmail(), "jNNT4LE.jpg", area, false);
		getUsers().getUsers().put(user.getUsername(), newUser);
		
		try {
			System.out.println("TRY");
			writeToFile();
			System.out.println("UPISANO U FILE");
		} catch (IOException e) {
			System.out.println("EXCEPTION");
			e.printStackTrace();
		}
		System.out.println("RETURN OKEJ");
		return Response.status(Status.OK).entity(newUser).build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User u) {
		User user = getUsers().getUser(u.getUsername());
		
		if(user == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if(!user.getPassword().equals(u.getPassword())) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		request.getSession().setAttribute("user", user);
		return Response.status(Status.OK).entity(user).build();
	}
	
	@POST
	@Path("/changeActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeActivity(User u) {
		User user = null;
		for(User us : getUsers().getValues()) {
			if(us.getId() == u.getId()) {
				user = us;
			}
		}
		user.setBlocked(u.isBlocked());
		getUsers().getUsers().remove(u.getUsername());
		getUsers().getUsers().put(user.getUsername(), user);
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(user).build();
	}
	
	@GET
	@Path("/loggedUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sessionUser() {
		User user = (User) request.getSession().getAttribute("user");
		
		if(user == null) {
			return Response.status(Status.NOT_FOUND).build();	
		}
		return Response.status(Status.OK).entity(user).build();
	}

	@POST
	@Path("/changeProfile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeProfile(User u) {
		User user = null;
		for(User us : getUsers().getValues()) {
			if(us.getId() == u.getId()) {
				user = us;
			}
		}
		String username = user.getUsername();
		if(!u.getUsername().equals(user.getUsername())){
			if(getUsers().getUser(u.getUsername()) != null) {
				return Response.status(Status.BAD_REQUEST).build();
			}
		}
		
		user.setName(u.getName());
		user.setEmail(u.getEmail());
		user.setPassword(u.getPassword());
		user.setPhoneNumber(u.getPhoneNumber());
		user.setUsername(u.getUsername());
		user.setSurname(u.getSurname());
		if(u.getArea().getName() == null) {
			user.setArea(null);
		}else {
			user.setArea(getAreas().getArea(u.getArea().getName()));
		}
		getUsers().getUsers().remove(username);
		getUsers().getUsers().put(user.getUsername(), user);
		try {
			writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Response.status(Status.OK).entity(user).build();
	}
	
	@GET
	@Path("/volunteers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllVolunteers() {
		ArrayList<User> users = new ArrayList<User>();
		for(User u : getUsers().getValues()) {
			if(u.getUserType() == UserType.VOLUNTEER) {
				users.add(u);
			}
		}
		return Response.status(Status.OK).entity(users).build();
	}
	
	@GET
	@Path("/volunteers/{areaName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response users(@PathParam("areaName") String areaName) {
		ArrayList<User> users = new ArrayList<User>();
		for(User u : getUsers().getValues()) {
			if(u.getUserType() == UserType.VOLUNTEER) {
				if(u.getArea().getName().equals(areaName)) {
					users.add(u);
				}
			}
		}
		if(users.size() == 0) {
			return Response.status(Status.NOT_FOUND).entity(users).build();
		}
		return Response.status(Status.OK).entity(users).build();
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout() {
		
		request.getSession().invalidate();
		System.out.println("User: " + request.getSession().getAttribute("user").toString());
		return Response.status(Status.OK).build();
	}
	/*
	@POST
	@Path("api/uploadProfileImage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	//public Response uploadImage(@FormParam("file") File file) {
	public Response uploadFile(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) { 

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String fileLocation =  DEFAULT_IMAGE_FOLDER + contentDispositionHeader.getFileName();
        //saving file  
		try {  
		    FileOutputStream out = new FileOutputStream(new File(fileLocation));  
		    int read = 0;  
		    byte[] bytes = new byte[1024];  
		    out = new FileOutputStream(new File(fileLocation));  
		    while ((read = fileInputStream.read(bytes)) != -1) {  
		        out.write(bytes, 0, read);  
		    }  
		    out.flush();  
		    out.close();  
		} catch (IOException e) {e.printStackTrace();}
		return Response.status(Status.OK).entity(user).build();  
		        
	}
*/
	
	private void writeToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ctx.getRealPath("") + "/users.txt"), StandardCharsets.UTF_16));
		for(User u : getUsers().getValues()) {
			String id = String.valueOf(u.getId());
			String area = "0";
			if(u.getArea() != null)
				area = String.valueOf(u.getArea().getId());
			writer.write(id);
			writer.write(";");
			writer.write(u.getName());
			writer.write(";");
			writer.write(u.getUserType().toString());
			writer.write(";");
			writer.write(u.getUsername());
			writer.write(";");
			writer.write(u.getPassword());
			writer.write(";");
			writer.write(u.getName());
			writer.write(";");
			writer.write(u.getSurname());
			writer.write(";");
			writer.write(u.getPhoneNumber());
			writer.write(";");
			writer.write(u.getEmail());
			writer.write(";");
			writer.write(u.getPhoto());
			writer.write(";");
			writer.write(area);
			writer.write(";");
			if(u.isBlocked()) {
				writer.write("true");
			}else {
				writer.write("false");
			}
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
}
