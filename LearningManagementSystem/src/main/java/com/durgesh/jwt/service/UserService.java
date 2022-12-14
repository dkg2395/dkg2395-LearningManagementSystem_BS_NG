package com.durgesh.jwt.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.durgesh.jwt.dao.CourseRepository;
import com.durgesh.jwt.dao.RoleRepository;
import com.durgesh.jwt.dao.UserRepository;
import com.durgesh.jwt.entity.Course;
import com.durgesh.jwt.entity.Role;
import com.durgesh.jwt.entity.User;
import com.durgesh.jwt.exception.CourseDurationNotFoundException;
import com.durgesh.jwt.exception.CourseNameNotFoundException;
import com.durgesh.jwt.exception.PageNotFoundException;
import com.durgesh.jwt.exception.TechnologyNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private RoleRepository roleRepository;
    

	@Autowired
	private CourseRepository courseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleRepository.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.save(adminUser);


    }

    public User registerNewUser(User user) {
    Role role = roleRepository.findById("User").get();
        
       
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        return userRepository.save(user);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    
   
	public List<Course> getByTechnology(String technology) {
		
			List<Course> course = courseRepository.findByTechnology(technology);
			if(course.isEmpty()) {
				throw new TechnologyNotFoundException("technolog not found with : "+ technology);
			}
			
			return course;
		
	}


	public List<Course> getAllCourse() {
		
		return courseRepository.findAll();
	}

	
	public void deleteByCourseName(String courseName) {
		List<Course> CourseName = courseRepository.findByCourseName(courseName);
		
		if(CourseName.isEmpty()) {
			throw new  CourseNameNotFoundException(" CourseName not found with : "+ courseName);
		}else {
			courseRepository.deleteByCourseName(courseName);
			
		}
		
	}

	
	public List<Course> getByDurationRange(String technology,Double courseDuration) {
		List<Course> durationRange = courseRepository.findByDurationRange(technology, courseDuration);
		if(durationRange.isEmpty()) {
			throw new  CourseDurationNotFoundException(" CourseDuration not found");
		}
		
		return durationRange;
	}

	

	
	

	
}
