package tokenMicroservice;
import org.acme.*;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin="summary"
			   , publish= false
			   , features = "features"
				//glue={"TokenMicroservice.src.test.java.tokenMicroservice"}// directory of the feature files
			   )
public class CucumberTest {
}
