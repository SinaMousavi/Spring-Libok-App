package com.libok.springlibrarymallapp.user;

import com.libok.springlibrarymallapp.controller.UserController;
import com.libok.springlibrarymallapp.dto.UserDto;
import com.libok.springlibrarymallapp.model.user.User;
import com.libok.springlibrarymallapp.service.UserService;
import com.libok.springlibrarymallapp.verification.Token;
import com.libok.springlibrarymallapp.verification.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)

class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    Model model;

    @Mock
    TokenRepository tokenRepository;

    @Mock
    BindingResult bindingResult;

    @Mock
    User user;


    @InjectMocks
    UserController controller;

    List<UserDto> users = new ArrayList<>();

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        users.add(new UserDto());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void homeTest() {
        //given
        given(userService.findAll()).willReturn(users);

        //when
        String view = controller.home(model,null);

        //then
        then(userService).should().findAll();
        then(model).should().addAttribute(anyString(), any());
        assertThat("user").isEqualToIgnoringCase(view);
    }


    @Test
    void homeControllerTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user"));
    }
    @Test
    void homeControllerWithParamTest() throws Exception {
        mockMvc.perform(get("/users").param("text","test"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user"));
    }
    @Test
    void registerControllerTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("registerForm"));
    }

    @Test
    void addValidUserTest() throws Exception {
        mockMvc.perform(post("/register")
                    .param("firstname","John")
                    .param("lastname","Doe")
                    .param("email","mail@test.com")
                    .param("password","not empty"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerSuccess"));
    }

    @Test
    void addUserNotValidEmailTest() throws Exception {
        mockMvc.perform(post("/register")
                .param("firstname","John")
                .param("lastname","Doe")
                .param("email","mail")
                .param("password","not empty"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerForm"));

    }


    @Test
    void singupTest() {
        //given
        Token token = new Token();
        token.setAppUser(user);
        Optional<Token> optionalToken = Optional.of(token);
        given(tokenRepository.findByValue("test")).willReturn(optionalToken);

        //when
        String view = controller.signup("test");

        //then
        then(user).should().setEnabled(true);
        then(userService).should().save(user);
        assertThat("activationSuccess").isEqualToIgnoringCase(view);
    }

    @Test
    void singupControllerTest() throws Exception {
        mockMvc.perform(get("/token")
                .param("value","not empty"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void deleteUserTest() {
        //given
        Long userId = 2L;

        //when
        String view = controller.deleteUser(userId,model);

        //then
        then(userService).should().delete(userId);
        then(userService).should().findAll();
        then(model).should().addAttribute(anyString(), any());
        assertThat("redirect:/users").isEqualToIgnoringCase(view);
    }

    @Test
    void deleteUserControllerTest() throws Exception {
        mockMvc.perform(get("/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("redirect:/users"));

    }

    @Test
    void getUserTest() {
        //given
        Long userId = 2L;

        //when
        String view = controller.getUser(userId,model);

        //then
        then(userService).should().findById(userId);
        then(model).should().addAttribute(anyString(), any());
        assertThat("panel/user-edit").isEqualToIgnoringCase(view);
    }

    @Test
    void getUserControllerTest() throws Exception {
        mockMvc.perform(get("/user-edit")
                .param("userId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("userModel"))
                .andExpect(view().name("panel/user-edit"));
    }

    @Test
    void updateUserValidControllerTest() throws Exception {
        mockMvc.perform(post("/user-edit")
                .param("firstname","John")
                .param("lastname","Doe")
                .param("email","mail@ma.com")
                .param("password","not empty")
                .param("userId","2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("redirect:/users"));
    }
    @Test
    void updateUserNotValidControllerTest() throws Exception {
        mockMvc.perform(post("/user-edit")
                .param("firstname","John")
                .param("lastname","Doe")
                .param("email","mail")
                .param("password","not empty")
                .param("userId","2"))
                .andExpect(status().is4xxClientError());
    }
}