# Footing
A unit-testing tool for annotation processors.

## Why Does This Exist

There are only a few tools for testing annotation processors
right now. None of them are very good. All of them require you
to run your complete annotation processor on complete source files.
This makes it impossible to do proper unit testing where each component
of the processor is tested individually. For complex processors, this
can be very problematic. 

Footing solves this problem by being an annotation processor. It 
allows you to interact with all your annotation processing code 
inside the annotation processing environment. This means there's
no need to mock any complex interfaces like `Types` or `TypeElement`.
By using the Java Compiler API (like you would with `compile-test` 
from Google), you can run individual unit tests on your compile-time code.

Footing allows you do this by taking advantage of `FunctionInterface` allowing
tests to be written as lambdas.

Example:

```java
class TestAPartOfAProcessor {
    
    @Test
    public void testAMethodOrSomething() {
        JavaFileObjects sources = getSomeListOfJavaFileObjects();
        
        FootingTest.compile(sources)
            .andRun((processingEnv, roundEnv) -> {                
                List<TypeMirror> theMirrors = //get some TypeMirrors
                
                APartOfAProcessor toTest = new APartOfAProcessor(theMirrors);
                
                boolean result = toTest.theMethod(processingEnv.getTypeUtils());
                
                Assert.assertTrue(result);
            });
        
    }
}
```

**This library is super unfinished right now**