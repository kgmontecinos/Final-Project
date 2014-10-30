
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kgmontecinos92
 */
public class StringValidationTool {
    StringValidationTool(){
    }
    
    //method checks if string contains '@' char for email address
    public static boolean validateEmailAddress(String toValidate){
        if(toValidate.indexOf('@') == -1)
            return false;
        else
            return true;
    }
    //method checks if string contains numbers only, if it finds any other character it returns false
    public static boolean validateNumbers(String toValidate) {
        char [] nums = toValidate.toCharArray();
        boolean isNumber = false;
        for(int i = 0; i < nums.length; i++){
            if ((nums[i] < '0') || (nums[i] > '9')){ 
                isNumber = false;
                break;
            }
            else
                isNumber = true;
        }
        return isNumber;

    }   
    //method checks if string contains letters only
    public static boolean validateLetters(String toValidate) {

        String regx = "^[\\p{L} .'-]+$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(toValidate);
        return matcher.find();

    }   
    //method checks if valid password
    public static boolean validatePassword(String toValidate) {
        /**(?=.*[0-9]) a digit must occur at least once
        (?=.*[a-z]) a lower case letter must occur at least once
        (?=.*[A-Z]) an upper case letter must occur at least once
        (?=.*[@#$%^&+=]) a special character must occur at least once
        (?=\\S+$) no whitespace allowed in the entire string
        .{8,} at least 8 characters*/
        String regx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return toValidate.matches(regx);

    }   
    
    
    public static void main(String [] args){
        StringValidationTool VTool = new StringValidationTool();
        System.out.println(VTool.validateLetters("arnoldd"));
    }
    
}
