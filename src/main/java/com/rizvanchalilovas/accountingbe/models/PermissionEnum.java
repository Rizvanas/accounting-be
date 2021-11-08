package com.rizvanchalilovas.accountingbe.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PermissionEnum {
    @JsonProperty("ceo:read")
    CEO_READ,
    @JsonProperty("ceo:write")
    CEO_WRITE,
    @JsonProperty("admin:read")
    ADMIN_READ,
    @JsonProperty("admin:write")
    ADMIN_WRITE,
    @JsonProperty("employee:read")
    EMPLOYEE_READ,
    @JsonProperty("employee:write")
    EMPLOYEE_WRITE,
    @JsonProperty("guest:read")
    GUEST_READ,
    @JsonProperty("guest:write")
    GUEST_WRITE;

    public static PermissionEnum fromPermission(Permission p) {
        switch (p.getName()) {
            case "ceo:read":
                return CEO_READ;
            case "ceo:write":
                return CEO_WRITE;
            case "admin:read":
                return ADMIN_READ;
            case "admin:write":
                return ADMIN_WRITE;
            case "employee:read":
                return EMPLOYEE_READ;
            case "employee:write":
                return EMPLOYEE_WRITE;
            case "guest:read":
                return GUEST_READ;
            case "guest:write":
                return GUEST_WRITE;
            default:
                return null;
        }
    }
}
