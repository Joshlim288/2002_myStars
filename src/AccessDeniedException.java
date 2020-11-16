/**
 * Exception class for handling login failures
 * Does not have unique functionality, but the name allows for clearer program flow
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-11-16
 */
public class AccessDeniedException extends Exception{
    AccessDeniedException(String S){
        super(S);
    }
}
