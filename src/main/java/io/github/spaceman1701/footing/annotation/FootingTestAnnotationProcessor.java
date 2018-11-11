package io.github.spaceman1701.footing.annotation;

import com.google.auto.service.AutoService;
import io.github.spaceman1701.footing.api.FootingTest;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("io.github.spaceman1701.footing.annotation.RunFootingTest")
public class FootingTestAnnotationProcessor extends AbstractProcessor {

    private final FootingTest test;

    public FootingTestAnnotationProcessor(final FootingTest test) {
        super();
        this.test = test;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TestEnvironment env = new TestEnvironment(processingEnv, roundEnv);
        test.test(env);
        return false;
    }
}
