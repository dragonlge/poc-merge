package com.poc.merge_poc.rest;

import com.poc.merge_poc.entity.LoggedRequest;
import com.poc.merge_poc.repository.LoggedRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@RestController
@RequestMapping("/api/merge/webhook")
public class WebhookController {

    private final LoggedRequestRepository repository;

    public WebhookController(LoggedRequestRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH,
            RequestMethod.OPTIONS,
            RequestMethod.HEAD
    })
    public ResponseEntity<String> logAndStore(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.append(name).append(": ").append(request.getHeader(name)).append("\n");
        }

        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line).append("\n");
            }
        }

        // Log to console
        System.out.println("------ Incoming Request ------");
        System.out.println("Method: " + method);
        System.out.println("URI: " + uri);
        System.out.println("-- Headers --\n" + headers);
        System.out.println("-- Body --\n" + body);

        // Save to database
        LoggedRequest logged = LoggedRequest.builder()
                .method(method)
                .uri(uri)
                .headers(headers.toString())
                .body(body.toString())
                .build();

        repository.save(logged);

        return ResponseEntity.ok("Request logged and stored.");
    }

}
