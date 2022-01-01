package com.jpr.maintenance.database.service;

import com.jpr.maintenance.database.model.UserEntity;
import com.jpr.maintenance.database.repository.UserRepository;
import com.jpr.maintenance.graphql.model.FindUserInput;
import com.jpr.maintenance.graphql.model.UserOutput;
import com.jpr.maintenance.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Mono<UserOutput> findByUserName(final FindUserInput input) {
        return ValidationService.validate(input)
            .flatMap(i -> userRepository.findByUserName(i.username()))
            .map(UserOutput::of);
    }



    public static void main(String... args) {
        List<Map<String,Object>> data = List.of(
            Map.of("user.id", 1, "um.id", 1, "ump.id", 1),
            Map.of("user.id", 1, "um.id", 1, "ump.id", 2),
            Map.of("user.id", 1, "um.id", 1, "ump.id", 3),
            Map.of("user.id", 1, "um.id", 2, "ump.id", 4),
            Map.of("user.id", 1, "um.id", 2, "ump.id", 5)
        );

        var result = data
            .stream()
            .collect(
                Collectors.groupingBy(m -> m.get("user.id"),
                Collectors
                    .mapping(m2 -> Map.of("um.id", m2.get("um.id"), "ump.id", m2.get("ump.id")),
                        Collectors.toList()))
            );

        log.info(result.toString());

        var result2 = data
            .stream()
            .collect(
                Collectors.groupingBy(m -> m.get("user.id"),
                    Collectors.groupingBy(m2 -> m2.get("um.id"),
                        Collectors.mapping(m3 -> m3.get("ump.id"), Collectors.toList()))
                )
            );

        log.info(result2.toString());
    }

    public Mono<UserOutput> save(final UserEntity user) {
        return userRepository.saveUser(user)
            .map(UserOutput::of);
    }

    public Mono<Boolean> deleteById(final Long id) {
        return userRepository.removeById(id).map(i -> i == 1);
    }
}
