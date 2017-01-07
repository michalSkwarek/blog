package com.skwarek.blog.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Michal on 05/01/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestLoginController {

    private static final String VIEWS_LOGIN_FORM = "registration/login";

    private LoginController loginController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.loginController = new LoginController();

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.loginController)
                .build();
    }

    @Test
    public void initLoginForm_ShouldRenderLoginView() throws Exception {
        mockMvc.perform(get("/accounts/login"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(forwardedUrl(VIEWS_LOGIN_FORM))
                .andExpect(view().name(VIEWS_LOGIN_FORM));
    }

    @Test
    public void failedUsernameOrPassword_ShouldHasError() throws Exception {
        mockMvc.perform(get("/accounts/login")
                .param("error", "true")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", is(true)))
                .andExpect(forwardedUrl(VIEWS_LOGIN_FORM))
                .andExpect(view().name(VIEWS_LOGIN_FORM));
    }
}
