package com.vbatecan.portfolio_manager.models.output;

import com.vbatecan.portfolio_manager.models.dto.UserDTO;

public record LoginSuccessfulResponse( String token, UserDTO username, Long expiration ) {
}
