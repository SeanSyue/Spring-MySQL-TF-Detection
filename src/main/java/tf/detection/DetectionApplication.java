package tf.detection;

import com.google.protobuf.TextFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tensorflow.SavedModelBundle;
import tf.detection.protos.StringIntLabelMapOuterClass;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class DetectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DetectionApplication.class, args);
    }

    @Bean
    public SavedModelBundle loadDetectionModel(@Value("${tf.modelPath}") String tfModelPath) throws IOException {
        String detectionModelPath = ClassLoader.getSystemResource(tfModelPath).getPath();
        return SavedModelBundle.load(detectionModelPath, "serve");
    }

    @Bean
    public String[] loadDetectionLabels(@Value("${tf.labelPath}") String labelsPath) throws Exception {
        String detectionLabelPath = ClassLoader.getSystemResource(labelsPath).getPath();
        return loadLabels(detectionLabelPath);
    }

    @Bean
    public URL getInferenceFilePath(@Value("${tf.testImagePath}") String inferenceFilePath) {
        return ClassLoader.getSystemResource(inferenceFilePath);
    }

    private static String[] loadLabels(String filename) throws Exception {
        String text = Files.readString(Paths.get(filename));
        StringIntLabelMapOuterClass.StringIntLabelMap.Builder builder = StringIntLabelMapOuterClass.StringIntLabelMap.newBuilder();
        TextFormat.merge(text, builder);
        StringIntLabelMapOuterClass.StringIntLabelMap proto = builder.build();
        int maxId = 0;
        for (StringIntLabelMapOuterClass.StringIntLabelMapItem item : proto.getItemList()) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }
        String[] ret = new String[maxId + 1];
        for (StringIntLabelMapOuterClass.StringIntLabelMapItem item : proto.getItemList()) {
            ret[item.getId()] = item.getDisplayName();
        }
        return ret;
    }


}
