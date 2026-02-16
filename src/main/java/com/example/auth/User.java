package com.example.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private String role;  // member/teamLeader/zoneLeader/admin
    private String zone;  // zone1/zone2/zone3/zone4

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
}

