// Testing imports needed
import org.junit.jupiter.api.BeforeEach;

//import java.util.HashMap;
import com.unhandledexceptions.Model.MethodItem;
//import com.unhandledexceptions.Model.ParameterItem;

public class MethodItemTests {
    //private HashMap<String, ParameterItem> parameterMap;
    //private String type;
    //private String methodName;
    private MethodItem methodItem;
    //private ClassItem classMap;

    @BeforeEach
    public void setUp(){
        //parameterMap = new HashMap<String, ParameterItem>();
        MethodItem.addParameter(methodItem, "testerType", "testerName");
    }
    //test to see if add method works properly for a unique parameter
    //@Test
    //public void testAddParam(){
    //}
}
