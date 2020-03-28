package com.example.mongodb.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import com.example.mongodb.dao.Commentdao;
import com.example.mongodb.dao.Likedao;
import com.example.mongodb.dao.Notificationdao;
import com.example.mongodb.dao.Screamdao;
import com.example.mongodb.dao.Userdao;
import com.example.mongodb.jwt.JwtUtil;
import com.example.mongodb.model.AuthenticateResponse;
import com.example.mongodb.objects.Comment;
import com.example.mongodb.objects.Like;
import com.example.mongodb.objects.Notifications;
import com.example.mongodb.objects.Screams;
import com.example.mongodb.objects.User;
import com.example.mongodb.service.CustomUserDetailsService;
import com.example.mongodb.service.SequenceGeneratorService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	Userdao userdao;

	@Autowired
	Likedao likedao;

	@Autowired
	Notificationdao notificationdao;

	@Autowired
	Screamdao screamdao;

	@Autowired
	Commentdao commentdao;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	SequenceGeneratorService seqGeneratorService;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
    private PasswordEncoder passwordEncoder;

	private JwtUtil jwtTokenUtil = new JwtUtil();

	@PostMapping("/signup")
	public ResponseEntity<?> create(@RequestBody JSONObject userMode) throws Exception {
		String h = (String) userMode.get("handle");
		String p = (String) userMode.get("password");
		String confirmPassword = (String) userMode.get("confirmPassword");
		String email = (String) userMode.get("email");
		String pass = passwordEncoder.encode(p);
		User userModel = new User(h, pass, pass, email);
		userModel.setUserId(seqGeneratorService.generateSequence(User.SEQUENCE_NAME));
		User u1 = userdao.findByHandle(userModel.getHandle());

		if (userModel.getEmail().trim().equals("") || userModel.getEmail() == null) {
			throw new Exception("EmailEmpty");

		}
		if (userModel.getPassword().trim().equals("")) {
			throw new Exception("PasswordEmpty");

		}
		if (userModel.getConfirmPassword().trim().equals("")) {
			throw new Exception("ConfirmPasswordEmpty");

		}
		if (userModel.getHandle().trim().equals("")) {
			throw new Exception("HandleEmpty");

		}
		if (!p.equals(confirmPassword)) {
			throw new Exception("PasswordMatch");
		}
		String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		if (!userModel.getEmail().matches(regex)) {
			throw new Exception("EmailValid");
		}
		if (u1 != null) {
			throw new Exception("HandleTaken");
		}
		u1 = userdao.findByEmail(userModel.getEmail());
		if (u1 != null) {
			throw new Exception("EmailTaken");
		}

		userdao.save(userModel);

		final UserDetails userDetails = userDetailsService.loadUserByUsername(h);
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticateResponse(token));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JSONObject userModel) throws Exception {
		String handle = (String) userModel.get("handle");
		String password = (String) userModel.get("password");
		
		
		if (handle.trim().equals("") || handle == null) {
			throw new Exception("HEmpty");
		}
		if (password.trim().equals("") || password == null) {
			throw new Exception("PEmpty");
		}
		try {
			authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(handle,password)));
		} catch (Exception e) {
			throw new Exception("Incorrect");
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(handle);
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticateResponse(token));
	}

	// to be checked later Parameter Issue
	@PostMapping("/user")
	public JSONObject update(@RequestHeader(name = "Authorization") String token, @RequestBody JSONObject modifiedUser)
			throws ParseException {
		String jwt = token.substring(7);
		String handle = jwtTokenUtil.extractUsername(jwt);
		String bio = (String) modifiedUser.get("bio");
		String location = (String) modifiedUser.get("location");
		String website = (String) modifiedUser.get("website");
		List<User> a = userdao.findAll();
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).getHandle().equals(handle)) {
				User b = a.get(i);
				if (!bio.trim().equals(""))
					b.setBio(bio);

				if (!location.trim().equals(""))
					b.setLocation(location);
				if (!website.trim().equals(""))
					b.setWebsite(website);
				userdao.save(b);
				JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
				JSONObject json = (JSONObject) parser.parse("{message :Details added successfully}");
				return json;
			}
		}
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject json = (JSONObject) parser.parse("{error :error adding details}");
		return json;
	}

	@GetMapping("/user")
	public JSONObject getAuthenticatedUser(@RequestHeader(name = "Authorization") String token) throws ParseException {
		String jwt = token.substring(7);
		String handle = jwtTokenUtil.extractUsername(jwt);
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);

		JSONObject parsLike = new JSONObject();
		JSONArray likesarray = new JSONArray();
		List<Like> likes = likedao.findByUserHandle(handle);
		JSONObject json = getUserdata(handle);
		String likesString;

		for (int i = 0; i < likes.size(); i++) {

			likesString = "{userHandle :" + likes.get(i).getUserHandle() + ", screamId : " + likes.get(i).getScreamId()
					+ "}";
			parsLike = (JSONObject) parser.parse(likesString);
			likesarray.appendElement(parsLike);

		}
		JSONObject jall = new JSONObject();
		jall.put("credentials", json);
		jall.put("likes", likesarray);
		// Notifications
		List<Notifications> nots = notificationdao.findByRecipient(handle);
		nots.sort(new Comparator<Notifications>() {
			@Override
			public int compare(Notifications o1, Notifications o2) {
				if (o1.getCreatedAt().compareTo(o2.getCreatedAt()) > 0) {
					return -1;
				} else
					return 1;
			}

		});

		String notsString;
		JSONObject parseNots = new JSONObject();
		JSONArray notsArray = new JSONArray();
		for (int i = 0; i < 10 && i < nots.size(); i++) {
			notsString = "{recipient: " + nots.get(i).getRecipient() + ",sender: " + nots.get(i).getSender()
					+ ",createdAt : " + nots.get(i).getCreatedAt().toString() + ",screamId : "
					+ nots.get(i).getScreamId() + ",type: " + nots.get(i).getType() + "," + "read:"
					+ nots.get(i).getRead() + "," + "notificationId: " + nots.get(i).getNotificationId() + "}";
			parseNots = (JSONObject) parser.parse(notsString);
			notsArray.appendElement(parseNots);
		}
		jall.put("notifications", notsArray);
		return jall;

	}

	@GetMapping(value = "/user/{handle}")
	public JSONObject getUserDetails(@PathVariable(name = "handle", required = true) String handle)
			throws ParseException {
		JSONObject json = getUserdata(handle);
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
		JSONObject jall = new JSONObject();

		List<Screams> screams = screamdao.findByUserHandle(handle);
		screams.sort(new Comparator<Screams>() {
			@Override
			public int compare(Screams o1, Screams o2) {
				if (o1.getCreatedAt().compareTo(o2.getCreatedAt()) > 0) {
					return -1;
				} else
					return 1;
			}

		});
		JSONObject parseScream = new JSONObject();
		JSONArray screamArray = new JSONArray();
		String screamsString;
		for (int i = 0; i < screams.size(); i++) {
			screamsString = "{body: " + screams.get(i).getBody() + ",userHandle: " + screams.get(i).getUserHandle()
					+ ",createdAt : " + screams.get(i).getCreatedAt().toString() + ",userImage : "
					+ screams.get(i).getUserImage() + ",likeCount: " + screams.get(i).getLikeCount() + ","
					+ "commentCount:" + screams.get(i).getCommentCount() + "," + "screamId: "
					+ screams.get(i).getScreamId() + "}";
			parseScream = (JSONObject) parser.parse(screamsString);
			screamArray.appendElement(parseScream);
		}
		jall.put("user", json);
		jall.put("screams", screamArray);

		return jall;
	}

	@PostMapping("/notifications")
	public JSONObject markNotificationsRead(@RequestBody List<ObjectId> notificationsIds) {
		JSONObject j = new JSONObject();
		Notifications not;

		try {
			for (int i = 0; i < notificationsIds.size(); i++) {
				not = notificationdao.findByNotificationsId(notificationsIds.get(i));
				not.setRead(true);
				notificationdao.save(not);
			}
			j.put("message", "Notifications Marked Read");
		} catch (Exception e) {
			j.put("errors", "Can't update notification");
		}

		return j;

	}

	@PostMapping(value = "/user/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadFile(@RequestBody MultipartFile file,
	@RequestHeader(name = "Authorization") String token) throws Exception {
		if(file.getSize() > 1048576*6){
			throw new Exception("Maximum file size is 2MB");
		}
		try{
			String jwt = token.substring(7);
			String handle = jwtTokenUtil.extractUsername(jwt);
			User user = userdao.findByHandle(handle);
			File path = new File("C:\\Users\\user\\Desktop\\info app\\userImages\\"+user.getHandle());
			if(!path.exists()){
				path.mkdirs();
			}
			File convertFile = new File(path.getAbsolutePath()+"\\" +file.getOriginalFilename());
			FileUtils.cleanDirectory(path);
			convertFile.createNewFile();
			FileOutputStream fout = new FileOutputStream(convertFile);
			fout.write(file.getBytes());
			fout.close();
			
			String imageUrl = "http://127.0.0.1:8877/" +user.getHandle()+"//"+file.getOriginalFilename()+"?static=1";
			user.setImageUrl(imageUrl);
			List<Comment> cmnts = commentdao.findByUserHandle(handle);
			for(int i=0;i<cmnts.size();i++){
				cmnts.get(i).setUserImage(imageUrl);
				commentdao.save(cmnts.get(i));
			}
			List<Screams> scream = screamdao.findByUserHandle(handle);
			for(int i=0;i<scream.size();i++){
				scream.get(i).setUserImage(imageUrl);
				screamdao.save(scream.get(i));
			}
			userdao.save(user);
			return  ResponseEntity.ok("image uploaded successfully");
	}catch(Exception e){
		throw new Exception("Problem : "+e);
	}
	}

	



	public JSONObject getUserdata(String handle){
		JSONObject json = new JSONObject();

		
		User user = userdao.findByHandle(handle);
		json.put("createdAt", user.getCreatedAt());
		json.put("location", user.getLocation());
		json.put("website", user.getWebsite());
		json.put("bio", user.getBio());
		json.put("handle", user.getHandle());
		json.put("userId", user.getUserId());
		json.put("email", user.getEmail());
		json.put("imageUrl", user.getImageUrl());

		return json;
	}

	
}
	
		


