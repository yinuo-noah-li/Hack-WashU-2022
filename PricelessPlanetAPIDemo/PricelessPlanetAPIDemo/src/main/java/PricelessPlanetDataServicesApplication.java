import com.mastercard.developer.interceptors.OkHttpOAuth1Interceptor;
import com.mastercard.developer.utils.AuthenticationUtils;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.ImpactMetricsApi;
import org.openapitools.client.api.PlantingProjectsApi;
import org.openapitools.client.model.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.PrivateKey;

public class PricelessPlanetDataServicesApplication {

    public static void main(String[] args) throws Exception {

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

        ImpactMetric impactMetric = impactMetricsApi.getImpactMetrics(200.0f, "AED");
        //PlantingProjects plantingProjects = plantingProjectsApi.getPlantingProjects(0, 10, "-CreatedAt");

        System.out.println("Impact Metrics API response : " + impactMetric);
//        System.out.println("{\"trees\": " + impactMetric.getTrees() + "," + "\n" + "\"carbonSequestered\": " + impactMetric.getCarbonSequestered()+"}");

        try (PrintWriter p = new PrintWriter(new FileOutputStream("/Users/ericy/pricelessPlanetData.json", true))) {
            p.println("{\"trees\": " + impactMetric.getTrees() + "," + "\n" + "\"carbonSequestered\": " + impactMetric.getCarbonSequestered()+"}");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        //System.out.println("Planting Projects API response : " + plantingProjects);
    }
}