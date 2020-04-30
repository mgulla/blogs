package com.example.blogs.serviceimpl;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogs.model.Blog;

import reactor.core.publisher.Mono;

/**
 * Although the requirement was to concentrate on creating asynchronous API
 * calls, I believe it this could be further enhanced by creating a Semaphore
 * for each newly constructed Blog so the map might actually be:
 * HashMap<Integer, BlogAndSemaphore>. When attempting to find the blog, you
 * would first check to see if you could acquire the semaphore. If someone is
 * attempting to (update) during the POST, then the semaphore would be locked.
 * When the update is completed then you would release the lock.
 *
 */
@RestController
public class BlogsImpl {

	private HashMap<Integer, Blog> blogMap = new HashMap<>();

	@GetMapping("/blogs/{id}")
	public Mono<Blog> findBlog(@PathVariable(name = "id") Integer id) {
		if (blogMap.containsKey(id)) {
			return Mono.just(blogMap.get(id));
		} else {
			return Mono.error(new Throwable("Blog [" + id + "] not found."));
		}
	}

	@PostMapping("/blogs")
	public Mono<Blog> createBlog(@RequestBody(required = true) Blog blog) {
		blogMap.put(blog.getId(), blog);
		return Mono.just(blog);
	}

}
