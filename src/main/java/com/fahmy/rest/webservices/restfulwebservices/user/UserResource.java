package com.fahmy.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;



@RestController()
@Tag(name = "User", description = "the social app API")
public class UserResource {
	
	@Autowired
	private UserDaoService userDaoService;
	
	//retrieve all users
	@Operation(summary = "Find All users", description = "Get all users in database", tags = { "User" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))) })	
	@GetMapping(value="/users",produces = { "application/json", "application/xml" })
	public List<User> retrieveAllUsers(){
		return userDaoService.findAll();
		
	}
	
//	@GetMapping(value="/users/{id}/posts", produces = {"application/json", "application/xml"}
//	)
//	public List<Post> retrieveUserPosts(@PathVariable int id){
//		return userDaoService.findUserPosts(id);
//	}
	
	
	//retrieve userbyID
	@Operation(summary = "Find user by ID", description = "Returns a single user", tags = { "User" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation",
                content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found") })
	@GetMapping(value="/users/{id}", produces = { "application/json", "application/xml" })
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user = userDaoService.findOne(id);
		if(user==null) {
			throw new UserNotFoundException("id-"+ id);
		}
		//"all-users", SERVER_PATH + "/users"
		//retrieveAllUsers
		EntityModel<User> resource =
				new EntityModel<User>(user);
		WebMvcLinkBuilder linkTo =
				linkTo(methodOn(this.getClass()).retrieveAllUsers());
		resource.add(linkTo.withRel("all-users"));
		//HATEOAS
		
		return resource;
	}
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = userDaoService.deleteById(id);
		if(user==null) 
			throw new UserNotFoundException("id-"+ id);
	}
	
//	@PostMapping("/users/{id}/posts")
//	public ResponseEntity<Object> createPost(@PathVariable int id ,
//			@RequestBody Post post){
//				post.setPostDate(new Date());
//				Post savedPost = userDaoService.saveUserPost(id, post);
//				URI location=
//						ServletUriComponentsBuilder
//						.fromCurrentRequestUri()
//						.path("{post_id}")
//						.buildAndExpand(savedPost.getId()).toUri();
//				return ResponseEntity.created(location).build();
//		
//	}
//	@GetMapping("/users/{id}/posts/{post_id}")
//	public Post getUserPost(@PathVariable("id") int userId ,@PathVariable("post_id") int postId) {
//		return userDaoService.findPostById(userId, postId);
//	}
//	
	
	@Operation(summary = "Add a new user", description = "", tags = { "User" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "User created",
                content = @Content(schema = @Schema(implementation = User.class))), 
        @ApiResponse(responseCode = "400", description = "Invalid input")})	
	@PostMapping(value="/users",produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userDaoService.save(user);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	

}
