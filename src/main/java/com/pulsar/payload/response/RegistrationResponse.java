package com.pulsar.payload.response;


public class RegistrationResponse {

    private MessageResponse messageResponse;
    private JwtResponse jwtResponse;

    public RegistrationResponse(MessageResponse messageResponse, JwtResponse jwtResponse) {
        this.messageResponse = messageResponse;
        this.jwtResponse = jwtResponse;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }

    public void setMessageResponse(MessageResponse messageResponse) {
        this.messageResponse = messageResponse;
    }

    public JwtResponse getJwtResponse() {
        return jwtResponse;
    }

    public void setJwtResponse(JwtResponse jwtResponse) {
        this.jwtResponse = jwtResponse;
    }
}
