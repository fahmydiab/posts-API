package com.fahmy.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
public class UserJPAResource {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	//retrieve all users
	@Operation(summary = "Find All users", description = "Get all users in database", tags = { "User" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation", 
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))) })	
	@GetMapping(value="/jpa/users",produces = { "application/json", "application/xml" })
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
		
	}
	
	//retrieve userbyID
	@Operation(summary = "Find user by ID", description = "Returns a single user", tags = { "User" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "successful operation",
                content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found") })
	@GetMapping(value="/jpa/users/{id}", produces = { "application/json", "application/xml" })
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id-"+ id);
		}
		
		
		EntityModel<User> resource =
				new EntityModel<User>(user.get());
		
		WebMvcLinkBuilder linkTo =
				linkTo(methodOn(this.getClass()).retrieveAllUsers());
		resource.add(linkTo.withRel("all-users"));
		//HATEOAS
		
		return resource;
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
		
	}
	
	@Operation(summary = "Add a new user", description = "", tags = { "User" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "User created",
                content = @Content(schema = @Schema(implementation = User.class))), 
        @ApiResponse(responseCode = "400", description = "Invalid input")})	
	@PostMapping(value="/jpa/users",produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		
		User savedUser = userRepository.save(user);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveAllUserPosts(@PathVariable int id){
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent())
			throw new UserNotFoundException("id-"+id);
		
		return userOptional.get().getPosts();
	}
	
	@PostMapping(value="/jpa/users/{id}/posts",produces = { "application/json", "application/xml" })
	public ResponseEntity<Object> createPost(@Valid @PathVariable int id,
			@RequestBody Post post) {
		
		Optional<User> userOptional = userRepository.findById(id);
		
		if(!userOptional.isPresent())
			throw new UserNotFoundException("id-"+id);
		
		User user = userOptional.get();

		post.setUser(user);
		
		postRepository.save(post);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(post.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}

}
