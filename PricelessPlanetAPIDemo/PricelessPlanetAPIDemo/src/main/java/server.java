import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ImpactMetricsApi;
import org.openapitools.client.api.PlantingProjectsApi;
import org.openapitools.client.model.*;

import java.security.PrivateKey;

public class server {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        Context context = tomcat.addContext("", null);

        Tomcat.addServlet(context, "func1", new HttpServlet() {
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                Object response = func1(req.getParameter("param1"), req.getParameter("param2"));

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(resp.getWriter(), response);
            }
        });
        context.addServletMappingDecoded("/func1", "func1");

        tomcat.start();
        tomcat.getServer().await();
    }
    private static ImpactMetric func1(String p1, String p2) {
        try {
            float f = Float.parseFloat(p1);

            String consumerKey = "uc-qQoIL7HDYCta9AomqFS43DGI-cdftHSsta466fb400633!313173e368a749d588af4f16321f026c0000000000000000";
            String signingKeyFilePath = "/Users/ericy/IdeaProjects/PricelessPlanetAPIDemo/src/main/resources/Priceless_Planet_Demo-sandbox.p12";
            String encryptionCertPath = "/Users/derekhumphreys/IdeaProjects/pricelessplanetdataservicesdemo/src/main/resources/PricelessPlanetDataServicesDemo-sandbox.pem";
            String signingKeyAlias = "keyalias";
            String signingKeyPassword = "keystorepassword";
            PrivateKey signingKey = AuthenticationUtils.loadSigningKey(signingKeyFilePath, signingKeyAlias, signingKeyPassword);

        /*
        Certificate encryptionCertificate = EncryptionUtils.loadEncryptionCertificate(encryptionCertPath);

        FieldLevelEncryptionConfig config = FieldLevelEncryptionConfigBuilder
                .aFieldLevelEncryptionConfig()
                .withEncryptionCertificate(encryptionCertificate)
                .withEncryptionPath("$", "$")
                .withEncryptedValueFieldName("encryptedData")
                .withEncryptedKeyFieldName("encryptedKey")
                .withOaepPaddingDigestAlgorithmFieldName("oaepHashingAlgorithm")
                .withOaepPaddingDigestAlgorithm("SHA-256")
                .withEncryptionKeyFingerprintFieldName("publicKeyFingerprint")
                .withIvFieldName("iv")
                .withFieldValueEncoding(FieldLevelEncryptionConfig.FieldValueEncoding.HEX)
                .build();
        */

            ApiClient client = new ApiClient();

            client.setBasePath("https://sandbox.api.mastercard.com/priceless-planet-coalition");

            client.setDebugging(false);
            client.setHttpClient(client.getHttpClient()
                    .newBuilder()
                    //.addInterceptor(new OkHttpFieldLevelEncryptionInterceptor(config))
                    .addInterceptor(new OkHttpOAuth1Interceptor(consumerKey, signingKey))
                    .build());

            ImpactMetricsApi impactMetricsApi = new ImpactMetricsApi(client);
            PlantingProjectsApi plantingProjectsApi = new PlantingProjectsApi(client);

            ImpactMetric impactMetric = impactMetricsApi.getImpactMetrics(f, p2);

            return impactMetric;
        } catch (Exception E) {System.out.println("Error");
        }
        return null;
    }

}