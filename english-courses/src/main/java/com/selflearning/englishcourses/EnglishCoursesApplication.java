package com.selflearning.englishcourses;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnglishCoursesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishCoursesApplication.class, args);
    }


//    private RegistrationTokenJpaRepository registrationTokenJpaRepository;
//
//    @Autowired
//    public void setRegistrationTokenJpaRepository(RegistrationTokenJpaRepository registrationTokenJpaRepository) {
//        this.registrationTokenJpaRepository = registrationTokenJpaRepository;
//    }

//    @Override
//    public void run(String... args) throws Exception {
//        RegistrationToken registrationToken = new RegistrationToken();
//        User user = new User();
//        user.setEmail("manhnd.695@gmail.com");
//        user.setUsername("admin");
//        user.setPassword(new BCryptPasswordEncoder().encode("123"));
//        user.setRoles(Arrays.asList(new Role("ADMIN")));
//        user.setFirstName("Mạnh");
//        user.setLastName("Nguyễn Đức");
//        user.setGender(true);
//        user.setEnabled(true);
//        user.setLocked(false);
//        registrationToken.setUser(user);
//        registrationToken.setActive(true);
//        registrationTokenJpaRepository.save(registrationToken);
//    }

}
