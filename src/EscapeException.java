/**
 * Exception class that is thrown when user wishes to abort method execution.<p>
 * Does not have unique functionality from other Exceptions, but the name allows for clearer program flow
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.4
 * @since 1.4
 */
public class EscapeException extends Exception{
    /**
     * @param S contains message that is to be shown, should include reason exception was thrown
     */
    public EscapeException(String S){
        super(S);
    }
}
