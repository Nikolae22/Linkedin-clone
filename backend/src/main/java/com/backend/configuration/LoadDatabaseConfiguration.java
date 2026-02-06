package com.backend.configuration;

import com.backend.features.authentication.model.AuthenticationUser;
import com.backend.features.authentication.repository.AuthenticationUserRepository;
import com.backend.features.authentication.utility.Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LoadDatabaseConfiguration {

    private final Encoder encoder;

//    @Bean
//    public CommandLineRunner initDatabase(AuthenticationUserRepository authenticationUserRepository){
//        return args -> {
//            AuthenticationUser authenticationUser=new AuthenticationUser(
//                  1L , "we@we.com",encoder.encode("password"));
//            authenticationUserRepository.save(authenticationUser);
//        };
//    }
}
