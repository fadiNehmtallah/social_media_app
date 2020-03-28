package com.example.mongodb.controller;

import java.util.Comparator;
import java.util.List;

import com.example.mongodb.dao.Commentdao;
import com.example.mongodb.dao.Likedao;
import com.example.mongodb.dao.Notificationdao;
import com.example.mongodb.dao.Screamdao;
import com.example.mongodb.dao.Userdao;
import com.example.mongodb.jwt.JwtUtil;
import com.example.mongodb.objects.Comment;
import com.example.mongodb.objects.Like;
import com.example.mongodb.objects.Notifications;
import com.example.mongodb.objects.Screams;
import com.example.mongodb.objects.User;
import com.example.mongodb.service.SequenceGeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@RestController
@RequestMapping("/api")
public class ScreamController {
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
    SequenceGeneratorService seqGeneratorService;

    JwtUtil jwtUtil = new JwtUtil();

    @GetMapping("/screams")
    public JSONArray getAllScreams() throws ParseException {
        List<Screams> scream=screamdao.findAll();
        JSONObject screamJson = new JSONObject();
		JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONArray screamArray = new JSONArray();
        scream.sort(new Comparator<Screams>() {
			@Override
			public int compare(Screams o1, Screams o2) {
				if(o1.getCreatedAt().compareTo(o2.getCreatedAt()) > 0){
					return -1;
				}else
					return 1;
			}
			
		});
        String screamString;
        for(int i=0;i<scream.size();i++){
            screamString = "{screamId : "+scream.get(i).getScreamId()+","+
            "body : "+scream.get(i).getBody()+","+
            "userHandle : "+scream.get(i).getUserHandle()+","+
            "createdAt : "+scream.get(i).getCreatedAt()+","+
            "userImage : "+scream.get(i).getUserImage()+","+
            "likeCount : "+scream.get(i).getLikeCount()+","+
            "commentCount : "+scream.get(i).getCommentCount()+"}";
            screamJson = (JSONObject) parser.parse(screamString);
            screamArray.appendElement(screamJson);
        }
        return screamArray;
    }
    @PostMapping("/scream")
    public JSONObject addScream(@RequestBody JSONObject body,@RequestHeader(name = "Authorization") String token ) throws Exception {
        String jwt = token.substring(7);
        String userHandle = jwtUtil.extractUsername(jwt);
        User user = userdao.findByHandle(userHandle);
        if(body.get("body").toString().trim().equals("") ||  body.get("body") == null){
            throw new Exception("Scream");
        }
        String b = (String) body.get("body");

        String userImage = user.getImageUrl();
        long screamId = seqGeneratorService.generateSequence(Screams.SEQUENCE_NAME);
        Screams scream = new Screams(b,userHandle,userImage);
        scream.setScreamId(screamId);
        scream.setCommentCount(0);
        scream.setLikeCount(0);
        screamdao.save(scream);
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        String screamString = "{screamId : "+scream.getScreamId()+","+
        "body : "+scream.getBody()+","+
        "userHandle : "+scream.getUserHandle()+","+
        "createdAt : "+scream.getCreatedAt()+","+
        "userImage : "+scream.getUserImage()+","+
        "likeCount : "+scream.getLikeCount()+","+
        "commentCount : "+scream.getCommentCount()+"}";
        JSONObject screamJson = (JSONObject) parser.parse(screamString);
        return screamJson;
    }

    @GetMapping(value = "/scream/{screamId}")
    public JSONObject getScream(@PathVariable(name = "screamId",required = true) long screamId) throws ParseException {
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject screamJson = getScreamdata(screamId);
        List<Comment> comments = commentdao.findByScreamId(screamId);

        comments.sort(new Comparator<Comment>() {
			@Override
			public int compare(Comment o1, Comment o2) {
				if(o1.getCreatedAt().compareTo(o2.getCreatedAt()) > 0){
					return -1;
				}else
					return 1;
			}
			
        });
        
        JSONArray commentsArray = new JSONArray();
        String commentsString ;
        JSONObject commentJson = new JSONObject();
        for(int i=0;i<comments.size();i++){
            commentsString = "{body :"+comments.get(i).getBody()+","+
            "createdAt :"+comments.get(i).getCreatedAt()+","+
            "userHandle :"+comments.get(i).getUserHandle()+","+
            "userImage :"+comments.get(i).getUserImage()+","+
            "screamId :"+comments.get(i).getScreamId()+",}";
            commentJson = (JSONObject) parser.parse(commentsString);
            commentsArray.appendElement(commentJson);
        }
        screamJson.put("comments",commentsArray);
        return screamJson;
        
    }
    @DeleteMapping(value ="/screamd/{screamId}")
    public JSONObject deleteScream(@PathVariable(name ="screamId",required = true) long screamId){
        JSONObject res = new JSONObject();
        try{
            screamdao.deleteByScreamId(screamId);
            res.put("message","Scream deled Successfully");
            List<Comment> cmnts = commentdao.findByScreamId(screamId);
            for(int i=0;i<cmnts.size();i++){
                commentdao.delete(cmnts.get(i));
            }
            List<Like> likkes = likedao.findByScreamId(screamId);
            for(int i=0;i<likkes.size();i++){
                likedao.delete(likkes.get(i));
            }
            List<Notifications> nots = notificationdao.findByScreamId(screamId);
            for(int i=0;i<nots.size();i++){
                notificationdao.delete(nots.get(i));
            }

            return res;
        }catch(Exception e){
            res.put("error",e);
            return res;
        }
    }
    @GetMapping(value ="/scream/{screamId}/unlike")
    public JSONObject unlikeScream(@RequestHeader(name = "Authorization") String token,
    @PathVariable(name = "screamId",required = true) long screamId)
    throws ParseException {
        Screams scream = screamdao.findByScreamId(screamId);
        JSONObject res = new JSONObject();
        String jwt = token.substring(7);
        String userHandle = jwtUtil.extractUsername(jwt);
        Like like = likedao.findByScreamIdAndUserHandle(screamId,userHandle);
        if( like == null){
            res.put("error","Scream Not liked");
            return res;
        }else{
            Notifications not = notificationdao.findByScreamIdAndSenderAndType(screamId,userHandle,"like");
            if(not != null){
                notificationdao.delete(not);
            }
            likedao.delete(like);
            scream.setLikeCount(scream.getLikeCount()-1);
            screamdao.save(scream);
            res = getScreamdata(screamId);
            return res;
        }
        
    }

    @GetMapping(value ="/scream/{screamId}/like")
    public JSONObject likeScream(@RequestHeader(name = "Authorization") String token,
    @PathVariable(name = "screamId",required = true) long screamId)
    throws ParseException {
        Screams scream = screamdao.findByScreamId(screamId);
        JSONObject res = new JSONObject();
        String jwt = token.substring(7);
        String userHandle = jwtUtil.extractUsername(jwt);
        Like like = likedao.findByScreamIdAndUserHandle(screamId,userHandle);
        
        if( like != null){
            res.put("error","Scream already liked");
            return res;
        }else{
            like = new Like(userHandle,screamId);
            if(!scream.getUserHandle().equals(userHandle)){
                Notifications nots = new Notifications(screamId, scream.getUserHandle(), userHandle, "like");
                notificationdao.save(nots);
                }
            likedao.save(like);
            scream.setLikeCount(scream.getLikeCount()+1);
            screamdao.save(scream);
            res = getScreamdata(screamId);
            return res;
        }
        
    }
    @PostMapping(value = "/scream/{screamId}/comment")
    public JSONObject commentScream(
        @RequestBody JSONObject bodyJson,
        @RequestHeader(name = "Authorization") String token,
        @PathVariable(name = "screamId",required = true) long screamId)
            throws Exception {
        Screams scream = screamdao.findByScreamId(screamId);
        String jwt = token.substring(7);
        String userHandle = jwtUtil.extractUsername(jwt);
        User us = userdao.findByHandle(userHandle);
        String userImage = us.getImageUrl();
        String body =(String) bodyJson.get("body");
        if(body == null || body.trim().equals("")){
            throw new Exception("Comment");
        }
        Comment cmnt = new Comment(screamId,userHandle,userImage,body);
        commentdao.save(cmnt);
        scream.setCommentCount(scream.getCommentCount()+1);
        screamdao.save(scream);
        String comment = "{body :"+cmnt.getBody()+","+
        "createdAt :"+cmnt.getCreatedAt()+","+
        "screamId :"+cmnt.getScreamId()+","+
        "userHandle :"+cmnt.getUserHandle()+","+
        "userImage :"+cmnt.getUserImage()+","+
        "commentCount :"+scream.getCommentCount()+"}";
        if(!userHandle.equals(scream.getUserHandle())){
            Notifications nots = new Notifications(screamId, scream.getUserHandle(), userHandle, "comment");
            notificationdao.save(nots);
        }
        
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        JSONObject jsonObject = new JSONObject();
        jsonObject = (JSONObject) parser.parse(comment);
        return jsonObject;
    }
    

    public JSONObject getScreamdata(long screamId) throws ParseException {
        JSONObject screamJson = new JSONObject();
        JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        Screams scream = screamdao.findByScreamId(screamId);
        String screamString = "{screamId : "+scream.getScreamId()+","+
        "body : "+scream.getBody()+","+
        "userHandle : "+scream.getUserHandle()+","+
        "createdAt : "+scream.getCreatedAt()+","+
        "userImage : "+scream.getUserImage()+","+
        "likeCount : "+scream.getLikeCount()+","+
        "commentCount : "+scream.getCommentCount()+"}";
         screamJson = (JSONObject) parser.parse(screamString);
        return screamJson;
    }

}