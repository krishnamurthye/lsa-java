package com.pulsar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulsar.models.AddressType;
import com.pulsar.models.UserType;
import com.pulsar.payload.request.LoginRequest;
import com.pulsar.payload.request.PreLoginRequest;
import com.pulsar.payload.request.ProfileRequest;
import com.pulsar.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest extends EmbeddedMongoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignup() throws Exception {

        String userName = "testing";
        String email = "test@test.com";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setMobileNumber(987654321L);
        signupRequest.setUsername(userName);
        signupRequest.setUserType(UserType.LSA.getType());

        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))

        );

        // Validate the response
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        PreLoginRequest preLoginRequest = new PreLoginRequest();
        preLoginRequest.setUsername(userName);

        result = mockMvc.perform(post("/api/auth/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(preLoginRequest))
        );

        // Validate the response
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(userName);
        loginRequest.setPassword("0000");

        result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        // Validate the response
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.roles").isArray())
//                .andExpect(jsonPath("$.roles").value()
                .andExpect(jsonPath("$.username").value(userName))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));


        String jsonResponse = result.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setAddressType(AddressType.HOME);
        profileRequest.setCity("AD");
        profileRequest.setPostalCode("502000");
        profileRequest.setStreetAddress("test street");

        result = mockMvc.perform(post("/api/LSAPProfile/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jsonNode.get("accessToken").textValue())
                .content(objectMapper.writeValueAsString(profileRequest))
        );

        // Validate the response
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSignupIncorrectType() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("testAdmin@test.com");
        signupRequest.setMobileNumber(987654321L);
        signupRequest.setUsername("testingAdmin");
        signupRequest.setUserType(UserType.ADMIN.getType());

        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        );

        // Validate the response
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSignupIncorrectTypeCouncillor() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("testCouncillor@test.com");
        signupRequest.setMobileNumber(987654321L);
        signupRequest.setUsername("testingCouncillor");
        signupRequest.setUserType(UserType.COUNCILLOR.getType());

        ResultActions result = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        );

        // Validate the response
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
