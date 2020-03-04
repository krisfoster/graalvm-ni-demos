package oracle.monkeybusiness;


import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;
import oracle.Configuration;

public class ConfigureAtBuildTimeFeature implements Feature {

    public void beforeAnalysis(BeforeAnalysisAccess access) {
        System.out.println(">>>> beforeAnalysis");
		RuntimeClassInitialization.initializeAtBuildTime(Configuration.class.getPackage().getName());
        System.out.println(">>>> Registered Configuration as being built at build time");
        try {
            System.out.println(">>>> Adding to singletons");
            ImageSingletons.add(Configuration.class, Configuration.loadFromFile());
        } catch (Throwable ex) {
            throw new RuntimeException("native-image build-time configuration failed", ex);
        }
    }
}
