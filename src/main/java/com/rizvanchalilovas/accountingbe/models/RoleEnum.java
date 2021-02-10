package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleEnum {
    @JsonProperty("ceo")
    CEO,

    @JsonProperty("admin")
    ADMIN,

    @JsonProperty("employee")
    EMPLOYEE,

    @JsonProperty("guest")
    GUEST
}
