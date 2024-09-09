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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        String userJSON = objectMapper.writeValueAsString(user);

        given(userRepository.findByUserName("testUser")).willReturn(Optional.empty());
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);

        mockMvc.perform(
                post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.state").value(user.getState()))
                .andExpect(jsonPath("$.zipcode").value(user.getZipcode()))
                .andExpect(jsonPath("$.peanutAllergy").value(user.getPeanutAllergy()))
                .andExpect(jsonPath("$.eggAllergy").value(user.getEggAllergy()))
                .andExpect(jsonPath("$.dairyAllergy").value(user.getDairyAllergy()));
    }

    @Test
    void UserController_GetUserByUserName_ReturnUser() throws Exception {
        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        mockMvc.perform(
                get("/user/" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.state").value(user.getState()))
                .andExpect(jsonPath("$.zipcode").value(user.getZipcode()))
                .andExpect(jsonPath("$.peanutAllergy").value(user.getPeanutAllergy()))
                .andExpect(jsonPath("$.eggAllergy").value(user.getEggAllergy()))
                .andExpect(jsonPath("$.dairyAllergy").value(user.getDairyAllergy()));
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

        String userDetails = objectMapper.writeValueAsString(updatedUser);

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(userRepository.save(Mockito.any(User.class))).willReturn(updatedUser);

        mockMvc.perform(
                put("/user/" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDetails)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(updatedUser.getUserName()))
                .andExpect(jsonPath("$.city").value(updatedUser.getCity()))
                .andExpect(jsonPath("$.state").value(updatedUser.getState()))
                .andExpect(jsonPath("$.zipcode").value(updatedUser.getZipcode()))
                .andExpect(jsonPath("$.peanutAllergy").value(updatedUser.getPeanutAllergy()))
                .andExpect(jsonPath("$.eggAllergy").value(updatedUser.getEggAllergy()))
                .andExpect(jsonPath("$.dairyAllergy").value(updatedUser.getDairyAllergy()));
    }

    @Test
    public void UserController_DeleteUser_ReturnUser() throws Exception {
        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        mockMvc.perform(
                delete("/user/" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.userName").value(user.getUserName()))
                .andExpect(jsonPath("$.city").value(user.getCity()))
                .andExpect(jsonPath("$.state").value(user.getState()))
                .andExpect(jsonPath("$.zipcode").value(user.getZipcode()))
                .andExpect(jsonPath("$.peanutAllergy").value(user.getPeanutAllergy()))
                .andExpect(jsonPath("$.eggAllergy").value(user.getEggAllergy()))
                .andExpect(jsonPath("$.dairyAllergy").value(user.getDairyAllergy()));
    }
}
