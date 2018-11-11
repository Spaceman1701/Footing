package io.github.spaceman1701.footing.api;

import io.github.spaceman1701.footing.annotation.TestEnvironment;

@FunctionalInterface
public interface FootingTest {
    void test(TestEnvironment env);
}
