package tf.detection.service;

import org.springframework.stereotype.Service;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.framework.MetaGraphDef;
import org.tensorflow.framework.SignatureDef;
import org.tensorflow.framework.TensorInfo;

import java.util.Map;
import java.util.StringJoiner;

@Service
public class ViewService {
    private final String[] detectionLabels;
    private final SavedModelBundle detectionModel;

    public ViewService(String[] detectionLabels, SavedModelBundle detectionModel) {
        this.detectionLabels = detectionLabels;
        this.detectionModel = detectionModel;
    }

    public String[] viewLabels() {
        return detectionLabels;
    }

    public String viewSignature() throws Exception {
        return getModelSignature(detectionModel);
    }

    private static String getModelSignature(SavedModelBundle model) throws Exception {
        MetaGraphDef m = MetaGraphDef.parseFrom(model.metaGraphDef());
        SignatureDef sig = m.getSignatureDefOrThrow("serving_default");
        StringJoiner output = new StringJoiner("\n");

        int numInputs = sig.getInputsCount();
        int i = 1;
        output.add("MODEL SIGNATURE")
                .add("Inputs:");
        for (Map.Entry<String, TensorInfo> entry : sig.getInputsMap().entrySet()) {
            TensorInfo t = entry.getValue();
            output.add(String.format(
                    "%d of %d: %-20s (Node name in graph: %-20s, type: %s)",
                    i++, numInputs, entry.getKey(), t.getName(), t.getDtype()));
        }

        int numOutputs = sig.getOutputsCount();
        i = 1;
        output.add("Outputs:");
        for (Map.Entry<String, TensorInfo> entry : sig.getOutputsMap().entrySet()) {
            TensorInfo t = entry.getValue();
            output.add(String.format(
                    "%d of %d: %-20s (Node name in graph: %-20s, type: %s)",
                    i++, numOutputs, entry.getKey(), t.getName(), t.getDtype()));
        }
        output.add("-----------------------------------------------");

        return output.toString();
    }
}