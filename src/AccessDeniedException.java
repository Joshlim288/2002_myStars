/**
 * Exception class that is thrown when login fails for any reason
 * Does not have unique functionality from other Exceptions, but the name allows for clearer program flow
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-11-16
 */
public class AccessDeniedException extends Exception{
    /**
     * @param S contains message that is to be shown, should include reason exception was thrown
     */
    AccessDeniedException(String S){
        super(S);
    }
}
