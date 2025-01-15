package com.sj.ecommerce.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "roles")
public class Role {

    @Id
    private String roleId; // MongoDB's unique identifier
    private String roleName; // Example: "ROLE_USER", "ROLE_ADMIN"

    public Role(String roleUser) {
    }
}
