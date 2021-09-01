package com.saic.demos.venus.cucumber;

import com.saic.demos.venus.VenusApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = VenusApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
