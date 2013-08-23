package jhc;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PropertyConfigurator;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 23/08/2013
 */
public class PlatformRunner {

    static {
        Properties p = new Properties();
        p.setProperty("log4j.rootLogger", "DEBUG,console");
        p.setProperty("log4j.appender.console", ConsoleAppender.class.getName());
        p.setProperty("log4j.appender.console.Target","System.out");
        p.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
        PropertyConfigurator.configure(p);
    }

    public static void main(String args[]) {
        PlatformManager pm = PlatformLocator.factory.createPlatformManager();

        System.setProperty(
                "org.vertx.logger-delegate-factory-class-name",
                "org.vertx.java.core.logging.impl.Log4jLogDelegateFactory"
        );
        JsonObject conf = new JsonObject().putString("foo", "wibble");

        pm.deployModuleFromClasspath("com.mycompany~my-module~1.0.0-final", conf, 1,
                getClasspath(),
                new AsyncResultHandler<String>() {
                    public void handle(AsyncResult<String> asyncResult) {
                        if (asyncResult.succeeded()) {
                            System.out.println("Deployment ID is " + asyncResult.result());
                        } else {
                            System.out.println("Deployment failed, ID is " + asyncResult.result());
                            asyncResult.cause().printStackTrace();
                        }
                    }
                });


        try {
            Thread.sleep(5000);
            pm.vertx().eventBus().send("ping-address","!");
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException("", e);
        }
        System.out.println("?");
    }

    private static URL[] getClasspath() {
        try {
            return new URL[]{
                    new URL("file:src/main/resources/"),
                    new URL("file:out/production/vertx-gradle-template")};
        } catch (MalformedURLException e) {
            throw new RuntimeException("", e);
        }
    }
}
