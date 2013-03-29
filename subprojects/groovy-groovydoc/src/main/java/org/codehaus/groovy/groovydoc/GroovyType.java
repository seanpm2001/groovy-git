/*
 * Copyright 2003-2009 the original author or authors.
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
package org.codehaus.groovy.groovydoc;

public interface GroovyType {
//    GroovyAnnotationTypeDoc asAnnotationTypeDoc();
//
//    GroovyClassDoc asClassDoc();
//
//    GroovyParameterizedType asParameterizedType();
//
//    GroovyTypeVariable asTypeVariable();
//
//    GroovyWildcardType asWildcardType();
//
//    String dimension();

    boolean isPrimitive();

    /**
     * The qualified name of this type excluding any dimension information.
     * For example, a two dimensional array of {@code String} returns "{@code java.lang.String}".
     *
     * @return The qualified name of this type excluding any dimension information.
     */
    String qualifiedTypeName();

    /**
     * The unqualified name of this type excluding any dimension or nesting information.
     * For example, the class {@code Outer.Inner} returns "{@code Inner}".
     *
     * @return The unqualified name of this type excluding any dimension or nesting information.
     */
    String simpleTypeName();

    /**
     * The unqualified name of this type excluding any dimension information.
     * For example, a two dimensional array of {@code String} returns "{@code String}".
     *
     * @return The unqualified name of this type excluding any dimension information.
     */
    String typeName();

    /**
     * The qualified name including any dimension information.
     * For example, a two dimensional array of String returns
     * "{@code java.lang.String[][]}", and the parameterized type
     * {@code List&lt;Integer&gt;} returns "{@code java.util.List&lt;java.lang.Integer&gt;}".
     *
     * @return The qualified name including any dimension information.
     */
    String toString();
}
