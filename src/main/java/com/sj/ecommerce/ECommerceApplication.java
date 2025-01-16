package com.sj.ecommerce;

import com.sj.ecommerce.common.Functions;
import com.sj.ecommerce.entity.Role;
import com.sj.ecommerce.repository.RoleRepository;
import com.sj.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sj.ecommerce.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository repository;
	@Value("${normal.role.id}")
	private String role_normal_id;
	@Value("${admin.role.id}")
	private String role_admin_id;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("abcd"));
/*
		try {

			Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_user = Role.builder().roleId(role_normal_id).roleName("ROLE_USER").build();

			User adminUser = User.builder()
					.id(Functions.randomIdGenerator())
					.name("Admin User")
					.email("admin@gmail.com")
					.password(passwordEncoder.encode("admin@123")) // Use your password encoder here
					.phone("1234567890")
					.address("Admin Street")
					.city("Admin City")
					.state("Admin State")
					.country("Admin Country")
					.zip("123456")
					.status("ACTIVE")
					.createdAt(LocalDateTime.now())
					.roles(Set.of(role_admin, role_user))  // Assign Role objects
					.build();

			User normalUser = User.builder()
					.id(Functions.randomIdGenerator())
					.name("Sanjay Vishwakarma")
					.email("sjroot9@gmail.com")
					.password(passwordEncoder.encode("test@123")) // Use your password encoder here
					.phone("9876543210")
					.address("Normal Street")
					.city("Normal City")
					.state("Normal State")
					.country("Normal Country")
					.zip("654321")
					.status("ACTIVE")
					.createdAt(LocalDateTime.now())
					.roles(Set.of(role_user))  // Assign Role objects

					.build();

			repository.save(role_admin);
			repository.save(role_user);


			userRepository.save(adminUser);
			userRepository.save(normalUser);

		} catch (Exception e) {
			e.printStackTrace();
		}


 */
	}


}
