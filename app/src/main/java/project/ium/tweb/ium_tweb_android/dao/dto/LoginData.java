package project.ium.tweb.ium_tweb_android.dao.dto;

import java.util.Objects;

public class LoginData {

    private Boolean loggedIn;
    private String username;
    private Boolean isAdmin;

    public LoginData(Boolean loggedIn, String username, Boolean isAdmin) {
        this.loggedIn = loggedIn;
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public LoginData() {}

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "loggedIn=" + loggedIn +
                ", username='" + username + '\'' +
                ", isAdmin='" + isAdmin + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginData loginData = (LoginData) o;
        return Objects.equals(loggedIn, loginData.loggedIn) &&
                Objects.equals(username, loginData.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(loggedIn, username);
    }

    public boolean identical(LoginData ld) {
        if (this == ld) return true;
        if (ld == null) return false;
        return Objects.equals(loggedIn, ld.loggedIn) &&
                Objects.equals(username, ld.username) &&
                Objects.equals(isAdmin, ld.isAdmin);
    }
}
