package poe_part1;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Login {

    private String registeredUsername = "";
    private String registeredPassword = "";
    private String registeredFirstName = "";
    private String registeredLastName = "";

    public void setFirstName(String firstName) {
        this.registeredFirstName = firstName;
    }

    public void setLastName(String lastName) {
        this.registeredLastName = lastName;
    }

    public boolean checkUserName(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }

        return hasCapital && hasDigit && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }
        // Regex source: https://www.regexlib.com/REDetails.aspx?regexp_id=726
        String regex = "\\+27[0-9]{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public String registerUser(String username, String password, String cellPhone) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.registeredUsername = username;
        this.registeredPassword = password;

        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    public boolean loginUser(String username, String password) {
        return this.registeredUsername.equals(username) && this.registeredPassword.equals(password);
    }

    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Welcome " + registeredFirstName + " " + registeredLastName + ", it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}