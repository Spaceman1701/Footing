package io.github.spaceman1701.footing.annotation;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

public final class TestEnvironment {

    private ProcessingEnvironment procEnv;
    private RoundEnvironment roundEnv;

    TestEnvironment(ProcessingEnvironment procEnv, RoundEnvironment roundEnv) {
        this.procEnv = procEnv;
        this.roundEnv = roundEnv;
    }

    public boolean processingOver() {
        return roundEnv.processingOver();
    }

    public ProcessingEnvironment getProcessingEnvironment() {
        return procEnv;
    }

    public RoundEnvironment getRoundEnvironment() {
        return roundEnv;
    }

    /**
     * Return an {@link Element} by it's name. Name format as follows:
     *
     * the.package.ClassName#MethodName
     * @param name the name of the element
     * @return a collection of all matching elements
     */
    public Set<? extends Element> getByName(String name) {
        String[] nameParts = name.split("#");
        if (nameParts.length == 1) {
            TypeElement result = getTypeByName(name);
            Set<Element> elements = new HashSet<>();
            if (result != null) {
                elements.add(result);
            }
            return elements;
        } else if (nameParts.length == 2) {
            return getMethodsByName(nameParts[0], nameParts[1]);
        } else {
            throw new IllegalArgumentException("invalid name format");
        }
    }

    public Set<ExecutableElement> getMethodsByName(String className, String methodName) {
        TypeElement type = getTypeByName(className);
        if (type == null) {
            return new HashSet<>();
        }

        Set<ExecutableElement> elements = new HashSet<>();
        for (Element e : type.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD) {
                if (e.getSimpleName().toString().equals(methodName)) {
                    elements.add((ExecutableElement) e);
                }
            }
        }

        return elements;
    }

    public TypeElement getTypeByName(String name) {
        return procEnv.getElementUtils().getTypeElement(name);
    }

    public TypeElement requireTypeByName(String name) {
        TypeElement element = getTypeByName(name);
        if (element == null) {
            throw new AssertionError(name + " was required but not found");
        }
        return element;
    }

    /**
     * returns <bold>the only method</bold> by the given name or throws an {@link AssertionError}
     * <br>
     * If no method exists by the given name or more than one exists, this method will throw.
     * @param className the fully qualified name of the containing class
     * @param methodName the simple name of the method
     * @return an executable element that represents the method
     */
    public ExecutableElement requireMethodByName(String className, String methodName) {
        Set<ExecutableElement> potentialElements = getMethodsByName(className, methodName);
        if (potentialElements.size() != 1) {
            throw new AssertionError("Required single method: "
                    + className + "#" + methodName + " but found " + potentialElements.size());
        }

        return potentialElements.iterator().next();
    }
}
