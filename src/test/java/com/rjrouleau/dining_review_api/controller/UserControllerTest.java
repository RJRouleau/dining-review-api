package com.rjrouleau.dining_review_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjrouleau.dining_review_api.model.User;
import com.rjrouleau.dining_review_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;



@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void init(){
        user = User.builder()
                .userName("testUser")
                .city("Fremont")
                .state("California")
                .zipcode("00005")
                .peanutAllergy(false)
                .eggAllergy(false)
                .dairyAllergy(false)
                .build();
    }

    @Test
    void UserController_CreateUser_ReturnUser() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName("testUser")).willReturn(Optional.empty());
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
        Mockito.verify(userRepository, Mockito.times(1)).findByUserName("testUser");
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void UserController_CreateUser_ReturnNameTaken() throws Exception {
        String invalidUser = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName(Mockito.anyString())).willReturn(Optional.of(user));

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUser)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is taken. Please choose a unique username."));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }

    @Test
    void UserController_GetUserByUserName_ReturnUser() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        mockMvc.perform(
                get("/user/" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(user.getUserName());
    }

    @Test
    void UserController_GetUserByUserName_ReturnNotFound() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName(Mockito.anyString())).willReturn(Optional.empty());

        mockMvc.perform(
                        get("/user/" + user.getUserName())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(user.getUserName());
    }

    @Test
    void UserController_UpdateUser_ReturnUser() throws Exception {
        User updatedUser = User.builder()
                .userName("testUser")
                .city("San Jose")
                .state("California")
                .zipcode("00005")
                .peanutAllergy(false)
                .eggAllergy(false)
                .dairyAllergy(false)
                .build();

        String expectedJson = objectMapper.writeValueAsString(updatedUser);

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(userRepository.save(Mockito.any(User.class))).willReturn(updatedUser);

        mockMvc.perform(
                put("/user/" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedJson)
        )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(user.getUserName());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void UserController_UpdateUser_ReturnNotFound() throws Exception {
        String invalidUser = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.empty());

        mockMvc.perform(
                        put("/user/" + user.getUserName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidUser)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(user.getUserName());
        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    public void UserController_DeleteUser_ReturnUser() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName(Mockito.anyString())).willReturn(Optional.of(user));

        mockMvc.perform(
                delete("/user/" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andExpect(content().json(expectedJson));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(user.getUserName());
    }

    @Test
    public void UserController_DeleteUser_ReturnNotFound() throws Exception {
        String invalidUser = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName(Mockito.anyString())).willReturn(Optional.empty());

        mockMvc.perform(
                        delete("/user/" + user.getUserName())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found."));

        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(user.getUserName());
    }
}
