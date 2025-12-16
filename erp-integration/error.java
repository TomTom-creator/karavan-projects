import org.apache.camel.BindToRegistry;
import org.apache.camel.Configuration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Configuration
@BindToRegistry("error")
public class error implements Processor {

    public void process(Exchange exchange) throws Exception {
        String correlationId = 
            exchange.getIn().getHeader("correlationId", String.class);

        Exception exception = 
            exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        String errorMessage = 
            exception != null ? exception.getMessage() : "Unknown error";

        // ✅ Fixed: Added backslashes (\") to escape the quotes
        // ✅ Fixed: Handled the .replace() quote escaping as well
        String errorJson = "{"
            + "\"correlationId\":\"" + correlationId + "\","
            + "\"status\":\"ERROR\","
            + "\"error\":\"" + errorMessage.replace("\"", "'") + "\""
            + "}";

        exchange.getIn().setBody(errorJson);
        exchange.getIn().setHeader("Content-Type", "application/json");
    }
}