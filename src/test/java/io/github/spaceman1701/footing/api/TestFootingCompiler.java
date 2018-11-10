package io.github.spaceman1701.footing.api;

import com.google.testing.compile.Compilation;
import com.squareup.javapoet.*;
import io.github.spaceman1701.footing.annotation.RunFootingTest;
import org.junit.Assert;
import org.junit.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.util.Set;

import static com.google.testing.compile.CompilationSubject.assertThat;

public class TestFootingCompiler {

    @Test
    public void testCompileNoTests() {
        TypeSpec testType = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(TypeName.VOID)
                    .addParameter(ArrayTypeName.get(String[].class), "args")
                    .addStatement("System.out.println($S)", "Hello, World")
                    .build()
                )
                .build();

        JavaFile file = JavaFile.builder("com.foo", testType)
                .build();

        JavaFileObject fileObject = file.toJavaFileObject();


        Compilation c = FootingCompiler.compiler()
                .compile(fileObject);

        assertThat(c).succeededWithoutWarnings();
    }

    @Test
    public void testCompileFailNoTests() {
        TypeSpec testType = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.methodBuilder("main")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.VOID)
                        .addParameter(ArrayTypeName.get(String[].class), "args")
                        .addStatement("System.out.println($L)", "Hello, World") //$L means no quotes.. should -> failed build
                        .build()
                )
                .build();

        JavaFile file = JavaFile.builder("com.foo", testType)
                .build();

        JavaFileObject fileObject = file.toJavaFileObject();


        Compilation c = FootingCompiler.compiler()
                .compile(fileObject);

        assertThat(c).failed();
    }


    @Test
    public void testCompileWithSimpleTest() {
        TypeSpec testType = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.methodBuilder("main")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.VOID)
                        .addParameter(ArrayTypeName.get(String[].class), "args")
                        .addStatement("System.out.println($S)", "Hello, World")
                        .build()
                )
                .addAnnotation(RunFootingTest.class)
                .build();
        JavaFile file = JavaFile.builder("com.foo", testType)
                .build();

        JavaFileObject fileObject = file.toJavaFileObject();

        Compilation c = FootingCompiler.compiler()
                .withTest(((procEnv, roundEnv) -> {
                    if (roundEnv.processingOver()) {
                        return;
                    }
                    Set<? extends Element> roots = roundEnv.getRootElements();

                    Assert.assertFalse(roots.isEmpty());
                }))
                .compile(fileObject);

        assertThat(c).succeededWithoutWarnings();
    }

    @Test(expected = AssertionError.class)
    public void testCompileWithAssertionError() {
        TypeSpec testType = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.methodBuilder("main")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.VOID)
                        .addParameter(ArrayTypeName.get(String[].class), "args")
                        .addStatement("System.out.println($S)", "Hello, World")
                        .build()
                )
                .addAnnotation(RunFootingTest.class)
                .build();
        JavaFile file = JavaFile.builder("com.foo", testType)
                .build();

        JavaFileObject fileObject = file.toJavaFileObject();

        Compilation c = FootingCompiler.compiler()
                .withTest(((procEnv, roundEnv) -> {
                    if (roundEnv.processingOver()) {
                        return;
                    }
                    Set<? extends Element> roots = roundEnv.getRootElements();

                    Assert.assertTrue(roots.isEmpty());
                }))
                .compile(fileObject);
    }
}
