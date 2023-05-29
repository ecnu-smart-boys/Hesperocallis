package org.ecnusmartboys.application.dto.request.command;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DisableUserRequest {

    @NotNull
    String id;
}
