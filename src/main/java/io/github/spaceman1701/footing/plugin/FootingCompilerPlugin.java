package io.github.spaceman1701.footing.plugin;

import com.google.auto.service.AutoService;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;

/**
 * The FootingCompilerPlugin adds the {@link io.github.spaceman1701.footing.annotation.RunFootingTest}
 * annotation to the root class in every source file. This ensures the
 * {@link io.github.spaceman1701.footing.annotation.FootingTestAnnotationProcessor}
 * will run.
 */
@AutoService(Plugin.class)
public class FootingCompilerPlugin implements Plugin {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void init(JavacTask task, String... args) {

    }
}
