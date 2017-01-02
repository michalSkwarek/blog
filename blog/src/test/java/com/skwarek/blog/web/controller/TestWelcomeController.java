package com.skwarek.blog.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by Michal on 01/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWelcomeController {

    private WelcomeController welcomeController;

    private MockMvc mockMvc;

    private String text = "Hello world";

    @Before
    public void setUp() {
        this.welcomeController = new WelcomeController();

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.welcomeController)
                .build();
    }

    @Test
    public void testSendingHelloWorldToView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("welcomeText"))
                .andExpect(model().attribute("welcomeText", text))
                .andExpect(view().name("index"));
    }
}
