package com.fahmy.rest.webservices.restfulwebservices.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	private static int userCount = 3;
	private static List<User> users
	=new ArrayList<>();
//	private static List<Post> posts
//	=new ArrayList<>();
	
	static {
		users.add(new User(1, "Adam", new Date()));
		users.add(new User(2, "Fahmy", new Date()));
		users.add(new User(3, "Diab", new Date()));

	}
	
	public List<User> findAll(){
		return users;
	}

//	public Post saveUserPost(int id,Post post) {
//		User user = findOne(id);
//		Integer postCounter = user.getPostCounter();
//		if(post.getId()==null) {
//			post.setId(++postCounter);
//		}
//		posts.add(post);
//		return post;
//	}
//	
//	public List<Post> findUserPosts(int id) {
//		User user = findOne(id);
//		return user.getPosts();
//	}
//	
//	public Post findPostById(int userId,int postId) {
//		findOne(userId);
//		for(Post post: posts) {
//			if(post.getId() == postId) {
//				return post;
//			}
//		}
//		return null;
//	}
	
	public User save(User user) {
		if(user.getId()==null) {
			user.setId(++userCount);
		}
		users.add(user);
		return user;
	}
	
	public User findOne(int id) {
		for(User user : users) {
			if(user.getId()==id) {
//				posts=user.getPosts();
				return user;
			}
		}
		return null;
	}
	
	public User deleteById(int id) {
		Iterator<User> iterator = users.iterator();
		while(iterator.hasNext()) {
			User user = iterator.next();
			if(user.getId() == id) {
				iterator.remove();
				return user;
			}
		}
		return null;
	}
}
