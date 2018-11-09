package io.github.spaceman1701.footing.annotation;

import com.google.auto.service.AutoService;
import io.github.spaceman1701.footing.api.FootingTest;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
public class FootingTestAnnotationProcessor extends AbstractProcessor {

    private final FootingTest test;

    public FootingTestAnnotationProcessor(final FootingTest test) {
        super();
        this.test = test;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        test.test(processingEnv, roundEnv);
        return false;
    }
}
