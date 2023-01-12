package paymentservice;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin="summary"
			   , publish= false
			   , features = "features"  // directory of the feature files
			   )
public class CucumberTest {
}
