package io.github.spaceman1701.footing.api;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

@FunctionalInterface
public interface FootingTest {
    void test(ProcessingEnvironment procEnv, RoundEnvironment roundEnv);
}
