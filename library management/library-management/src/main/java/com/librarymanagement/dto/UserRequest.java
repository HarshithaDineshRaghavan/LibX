package com.librarymanagement.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {

    private Integer userId;

    private String name;

    private String userName;
    private String email;
    private String password;

    private Integer roleId;
    List<BorrowRequest> Borrows = new ArrayList<>();
    private String membership;

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

}
