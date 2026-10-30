package com.foodflow.foodflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public List<String> healthCheck() {

        List<String> statuses = new ArrayList<>();

        statuses.add("Status: UP");
        statuses.add("Application: FoodFlow");

        return statuses;
    }
}
