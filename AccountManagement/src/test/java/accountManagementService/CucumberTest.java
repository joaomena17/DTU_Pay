package accountManagementService;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin="summary"
		, publish= false
		, tags = "not @DontRun"
		, features = "features"  // directory of the feature files
)
public class CucumberTest {
}