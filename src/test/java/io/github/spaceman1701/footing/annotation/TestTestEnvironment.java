package io.github.spaceman1701.footing.annotation;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.*;
import io.github.spaceman1701.footing.api.FootingCompiler;
import org.junit.Assert;
import org.junit.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestTestEnvironment {

    private JavaFileObject getSimpleTestType() {
        TypeSpec type = TypeSpec.classBuilder("Test")
                .addAnnotation(RunFootingTest.class)
                .build();

        return JavaFile.builder("foo", type).build().toJavaFileObject();
    }

    private MethodSpec getSimpleVoidMethod(String name, ParameterSpec... parameterSpecs) {
        return MethodSpec.methodBuilder(name)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addParameters(ImmutableList.copyOf(parameterSpecs))
                .build();
    }

    private JavaFileObject getSimpleTestTypeWithMethods() {
        TypeSpec type = TypeSpec.classBuilder("Test")
                .addAnnotation(RunFootingTest.class)
                .addMethod(getSimpleVoidMethod("foo"))
                .addMethod(getSimpleVoidMethod("bar"))
                .addMethod(getSimpleVoidMethod("foo", ParameterSpec.builder(Object.class, "aParam").build()))
                .build();

        return JavaFile.builder("foo", type).build().toJavaFileObject();
    }

    @Test
    public void testGetTypeByName() {

        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestType());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            TypeElement e = env.getTypeByName("foo.Test");
            Assert.assertNotNull(e);

            TypeElement notReal = env.getTypeByName("this.is.not.a.real.Type");
            Assert.assertNull(notReal);
        });
    }

    @Test
    public void testRequireTypeByName() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestType());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            TypeElement e = env.requireTypeByName("foo.Test");
            Assert.assertNotNull(e);
        });
    }

    @Test(expected = AssertionError.class)
    public void testRequireTypeByNameFail() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestType());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            TypeElement e = env.requireTypeByName("this.is.not.a.real.Type");
            Assert.assertNotNull(e);
        });
    }

    @Test
    public void testGetMethodsByName() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            Set<ExecutableElement> fooSet = env.getMethodsByName("foo.Test", "foo");
            Assert.assertEquals(2, fooSet.size());

            Set<ExecutableElement> barSet = env.getMethodsByName("foo.Test", "bar");
            Assert.assertEquals(1, barSet.size());

            Set<ExecutableElement> emptySet = env.getMethodsByName("foo.Test", "meh");
            Assert.assertTrue(emptySet.isEmpty());

            Set<ExecutableElement> emptySet2 = env.getMethodsByName("foo.NotARealClass", "foo");
            Assert.assertTrue(emptySet.isEmpty());
        });
    }

    @Test
    public void testRequireMethodByNameSingle() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }



            ExecutableElement barElement = env.requireMethodByName("foo.Test", "bar");
            Assert.assertNotNull(barElement);
            Assert.assertEquals(barElement.getSimpleName().toString(), "bar");
        });
    }

    @Test(expected = AssertionError.class)
    public void testRequireMethodByNameMulti() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            ExecutableElement barElement = env.requireMethodByName("foo.Test", "foo");
        });
    }

    @Test(expected = AssertionError.class)
    public void testRequireMethodByNameNotRealMethod() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            ExecutableElement barElement = env.requireMethodByName("foo.Test", "notRealMethod");
        });
    }

    @Test(expected = AssertionError.class)
    public void testRequireMethodByNameNotRealClass() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            ExecutableElement barElement = env.requireMethodByName("foo.NotRealClass", "foo");
        });
    }

    @Test
    public void testGetByName() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            Set<? extends Element> elements = env.getByName("foo.Test#foo");
            Assert.assertEquals(2, elements.size());

            Set<? extends Element> classElements = env.getByName("foo.Test");
            Assert.assertEquals(1, classElements.size());
        });
    }

    @Test(expected = RuntimeException.class)
    public void testGetByNameBadSyntax() {
        List<JavaFileObject> objects = new ArrayList<>();
        objects.add(getSimpleTestTypeWithMethods());

        FootingCompiler.compileAndRun(objects, env -> {
            if (env.processingOver()) {
                return;
            }

            Set<? extends Element> elements = env.getByName("foo.#Test#foo");
            Assert.assertEquals(2, elements.size());
        });
    }
}
