package io.github.spaceman1701.footing.api;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import io.github.spaceman1701.footing.annotation.FootingTestAnnotationProcessor;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

public class FootingCompiler {

    private Compiler compiler;

    private FootingCompiler(Compiler compiler) {
        this.compiler = compiler;
    }

    public static FootingCompiler compiler() {
        return new FootingCompiler(Compiler.javac());
    }

    public FootingCompiler withProcessors(Processor... processors) {
        return new FootingCompiler(compiler.withProcessors(processors));
    }

    public FootingCompiler withProcessors(Iterable<? extends Processor> processors) {
        return new FootingCompiler(compiler.withProcessors(processors));
    }

    public FootingCompiler withTest(FootingTest test) {
        FootingTestAnnotationProcessor proc = new FootingTestAnnotationProcessor(test);
        return new FootingCompiler(compiler.withProcessors(proc));
    }

    public Compilation compile(Iterable<? extends JavaFileObject> javaFileObjects) {
        return compiler.compile(javaFileObjects);
    }

    public Compilation compile(JavaFileObject... javaFileObjects) {
        try {
            return compiler.compile(javaFileObjects);
        } catch (RuntimeException re) {
            if (re.getCause() instanceof AssertionError) {
                throw (AssertionError) re.getCause();
            } else {
                throw re;
            }
        }
    }
}