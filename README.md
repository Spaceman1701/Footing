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

You can write assertions in your compile-time tests. When these assertions
cause a build failure, Footing intercepts them and rethrows them properly.
This means assertion errors in Footing tests look exactly like assertion
errors in normal tests. 

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

**This library is super unfinished right now** See the unit tests for 
examples of the current API.

The library is relatively simple and has good test coverage. About 95%
of lines are covered right now. 

If you really want to use it for something, it should be fine. Just don't
expect the API to remain stable.