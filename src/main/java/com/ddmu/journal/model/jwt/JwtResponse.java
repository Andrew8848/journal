package com.ddmu.journal.model.jwt;

import com.ddmu.journal.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {

    private User user;
    private String jwtToken;

}
