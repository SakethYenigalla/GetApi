package com.example.getApi.controller;

import com.example.getApi.entity.Person;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class GetApiController {
    @Autowired
    private WebClient.Builder webClientBuilder;
    public static final String USER_SERVICE = "userService";

    @GetMapping
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "fallbackForGetData")
    @Retry(name = USER_SERVICE)
    public ResponseEntity<List<Person>> getPersons(@PathVariable(name = "ageFilter", required = false) Integer ageFilter) {
        //String data=new String();
        List<Person> people = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/persons")
                .retrieve()
                .bodyToFlux(Person.class)
                .collectList()
                .block();
        if (ageFilter != null && !CollectionUtils.isEmpty(people)) {
            people = people.stream()
                    .filter(Person -> Person.getAge() == ageFilter)
                    .collect(Collectors.toList());
        }
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    public ResponseEntity<String> fallbackForGetData(Exception e) {
        System.out.println("Fallback triggered. Unable to retrieve data at the moment.");
        return new ResponseEntity<>("Fallback: Unable to retrieve data at the moment", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/Download")
    public ResponseEntity<byte[]> downloadData() {
        List<Person> persons = webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/api/persons")
                .retrieve()
                .bodyToFlux(Person.class)
                .collectList()
                .block();
        String csvContent = convertPersonsToCsv(persons);

        // Convert CSV content to bytes
        byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("data", "data.csv");
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    private String convertPersonsToCsv(List<Person> persons) {
        // Example: Simple conversion to CSV (you might want to use a CSV library for more complex cases)
        StringBuilder csvCon = new StringBuilder();
        csvCon.append("ID, Name,age\n");
        for (Person person : persons) {
            csvCon.append(person.getId()).append(',')
                    .append(person.getName()).append(',')
                    .append(person.getAge()).append("\n");
        }
        return csvCon.toString();
    }

}
