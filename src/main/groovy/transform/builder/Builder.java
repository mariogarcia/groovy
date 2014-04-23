/*
 * Copyright 2008-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovy.transform.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import static org.codehaus.groovy.transform.BuilderASTTransformation.BuilderStrategy;

/**
 * The {@code @Builder} AST transformation is used to help write classes that can be created using _fluent_ api calls.
 * The transform supports multiple building strategies to cover a range of cases and there are a number
 * of configuration options to customize the building process. If you're an AST hacker, you can also define your
 * own strategy class. The following strategies are bundled with Groovy:
 * <ul>
 *     <li><b>SimpleStrategy</b> for creating chained setters</li>
 *     <li><b>ExternalStrategy</b> where you annotate an explicit builder class while leaving some buildee class being built untouched</li>
 *     <li><b>DefaultStrategy</b> which creates a nested helper class for instance creation</li>
 * </ul>
 *
 * Note that Groovy provides other built-in mechanisms for easy creation of objects, e.g. the named-args constructor:
 * <pre>
 * new Person(firstName: "Robert", lastName: "Lewandowski", age: 21)
 * </pre>
 * or the with statement:
 * <pre>
 * new Person().with {
 *     firstName = "Robert"
 *     lastName = "Lewandowski"
 *     age = 21
 * }
 * </pre>
 * so you might not find value in using the builder transform at all. But if you need Java integration or in some cases improved type safety, the {@code @Builder} transform might prove very useful.
 *
 * @author Marcin Grzejszczak
 * @author Paul King
 * @see groovy.transform.builder.SimpleStrategy
 * @see groovy.transform.builder.ExternalStrategy
 * @see groovy.transform.builder.DefaultStrategy
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
@GroovyASTTransformationClass("org.codehaus.groovy.transform.BuilderASTTransformation")
public @interface Builder {

    /**
     * A class for which builder methods should be created
     */
    Class forClass();

    /**
     * A class capturing the builder strategy
     */
    Class<? extends BuilderStrategy> builderStrategy() default DefaultStrategy.class;

    /**
     * The prefix to use when creating the setter methods.
     * Default is determined by the strategy which might use "" or "set" but you can choose your own, e.g. "with".
     * If non-empty the first letter of the property will be capitalized before being appended to the prefix.
     */
    String prefix();

    /**
     * For strategies which create a builder helper class, the class name to use for the helper class.
     * Not used if using {@code forClass} since in such cases the builder class is explicitly supplied.
     * Default is determined by the strategy, e.g. <em>TargetClass</em> + "Builder" or <em>TargetClass</em> + "Initializer".
     */
    String builderClassName();

    /**
     * For strategies which create a builder helper class that creates the instance, the method name to call to create the instance.
     * Default is determined by the strategy, e.g. <em>build</em> or <em>create</em>.
     */
    String buildMethodName();

    /**
     * The method name to use for a builder factory method in the source class for easy access of the
     * builder helper class for strategies which create such a helper class.
     * Must not be used if using {@code forClass}.
     * Default is determined by the strategy, e.g. <em>builder</em> or <em>createInitializer</em>.
     */
    String builderMethodName();

    /**
     * List of field and/or property names to exclude from generated builder methods.
     * Must not be used if 'includes' is used. For convenience, a String with comma separated names
     * can be used in addition to an array (using Groovy's literal list notation) of String values.
     */
    String[] excludes() default {};

    /**
     * List of field and/or property names to include within the generated builder methods.
     * Must not be used if 'excludes' is used. For convenience, a String with comma separated names
     * can be used in addition to an array (using Groovy's literal list notation) of String values.
     */
    String[] includes() default {};
}
