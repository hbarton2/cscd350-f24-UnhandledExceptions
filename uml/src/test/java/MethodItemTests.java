// Testing imports needed
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import com.unhandledexceptions.Model.ClassItem;
import com.unhandledexceptions.Model.MethodItem;
import com.unhandledexceptions.Model.ParameterItem;
import com.unhandledexceptions.Model.RelationshipItem;

public class MethodItemTests {
    private HashMap<String, ParameterItem> parameterMap;
    private String type;
    private String methodName;
    private MethodItem methodItem;
    //private ClassItem classMap;

    @BeforeEach
    public void setUp(){
        parameterMap = new HashMap<String, ParameterItem>();
        MethodItem.addParameter(methodItem, "testerType", "testerName");
    }
    //test to see if add method works properly for a unique parameter
    //@Test
    //public void testAddParam(){
    //}
}
