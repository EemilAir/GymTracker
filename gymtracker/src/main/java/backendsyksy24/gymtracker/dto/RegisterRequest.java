package backendsyksy24.gymtracker.dto;

import jakarta.validation.constraints.*;

//Data Transfer Object for RegisterRequest, encapsulating user registration details.
public class RegisterRequest {

    @NotEmpty
    @Size(min = 4, max = 20)
    private String username = "";

    // Strong password check is done in the controller
    @NotEmpty
    @Size(min = 8, max = 30)
    private String password = "";;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPasswordHash(String password) {
        this.password = password;
    }
}
