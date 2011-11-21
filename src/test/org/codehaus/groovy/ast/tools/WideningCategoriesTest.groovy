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
package org.codehaus.groovy.ast.tools

import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.GenericsTestCase
import static org.codehaus.groovy.ast.tools.WideningCategories.*
import static org.codehaus.groovy.ast.ClassHelper.*

class WideningCategoriesTest extends GenericsTestCase {

    void testBuildCommonTypeWithNullClassNode() {
        ClassNode a = null
        ClassNode b = make(Serializable)
        assert lowestUpperBound(a,b) == null
        assert lowestUpperBound(b,a) == null
    }

    void testBuildCommonTypeWithObjectClassNode() {
        ClassNode a = OBJECT_TYPE
        ClassNode b = make(Serializable)
        assert lowestUpperBound(a,b) == OBJECT_TYPE
        assert lowestUpperBound(b,a) == OBJECT_TYPE
    }

    void testBuildCommonTypeWithVoidClassNode() {
        ClassNode a = VOID_TYPE
        ClassNode b = VOID_TYPE
        assert lowestUpperBound(a,b) == VOID_TYPE
        assert lowestUpperBound(b,a) == VOID_TYPE
    }

    void testBuildCommonTypeWithVoidClassNodeAndAnyNode() {
        ClassNode a = VOID_TYPE
        ClassNode b = make(Set)
        assert lowestUpperBound(a,b) == OBJECT_TYPE
        assert lowestUpperBound(b,a) == OBJECT_TYPE
    }

    void testBuildCommonTypeWithIdenticalInterfaces() {
        ClassNode a = make(Serializable)
        ClassNode b = make(Serializable)
        assert lowestUpperBound(a,b) == make(Serializable)
    }

    void testBuildCommonTypeWithOneInterfaceInheritsFromOther() {
        ClassNode a = make(Set)
        ClassNode b = make(SortedSet)
        assert lowestUpperBound(a,b) == make(Set)
        assert lowestUpperBound(b,a) == make(Set)
    }

    void testBuildCommonTypeWithTwoIncompatibleInterfaces() {
        ClassNode a = make(Set)
        ClassNode b = make(Map)
        assert lowestUpperBound(a,b) == OBJECT_TYPE
        assert lowestUpperBound(b,a) == OBJECT_TYPE
    }

    void testBuildCommonTypeWithOneClassAndOneImplementedInterface() {
        ClassNode a = make(Set)
        ClassNode b = make(HashSet)
        assert lowestUpperBound(a,b) == make(Set)
        assert lowestUpperBound(b,a) == make(Set)
    }

    void testBuildCommonTypeWithOneClassAndNoImplementedInterface() {
        ClassNode a = make(Map)
        ClassNode b = make(HashSet)
        assert lowestUpperBound(a,b) == OBJECT_TYPE
        assert lowestUpperBound(b,a) == OBJECT_TYPE
    }

    void testBuildCommonTypeWithTwoClassesWithoutSuperClass() {
        ClassNode a = make(ClassA)
        ClassNode b = make(ClassB)
        assert lowestUpperBound(a,b) == make(GroovyObject) // GroovyObject because Groovy classes implicitely implement GroovyObject
        assert lowestUpperBound(b,a) == make(GroovyObject)
    }

    void testBuildCommonTypeWithIdenticalPrimitiveTypes() {
        [int_TYPE, long_TYPE, short_TYPE, boolean_TYPE, float_TYPE, double_TYPE].each {
            ClassNode a = it
            ClassNode b = it
            assert lowestUpperBound(a,b) == it
            assert lowestUpperBound(b,a) == it
        }
    }

    void testBuildCommonTypeWithPrimitiveTypeAndItsBoxedVersion() {
        [int_TYPE, long_TYPE, short_TYPE, boolean_TYPE, float_TYPE, double_TYPE].each {
            ClassNode a = it
            ClassNode b = getWrapper(it)
            assert lowestUpperBound(a,b) == getWrapper(it)
            assert lowestUpperBound(b,a) == getWrapper(it)
        }
    }


    void testBuildCommonTypeWithTwoIdenticalClasses() {
        ClassNode a = make(HashSet)
        ClassNode b = make(HashSet)
        assert lowestUpperBound(a,b) == make(HashSet)
        assert lowestUpperBound(b,a) == make(HashSet)
    }

    void testBuildCommonTypeWithOneClassInheritsFromAnother() {
        ClassNode a = make(HashSet)
        ClassNode b = make(LinkedHashSet)
        assert lowestUpperBound(a,b) == make(HashSet)
        assert lowestUpperBound(b,a) == make(HashSet)
    }

    void testBuildCommonTypeWithTwoInterfacesSharingOneParent() {
        ClassNode a = make(InterfaceCA)
        ClassNode b = make(InterfaceDA)
        assert lowestUpperBound(a,b) == make(InterfaceA)
        assert lowestUpperBound(b,a) == make(InterfaceA)
    }

    void testBuildCommonTypeWithTwoInterfacesSharingTwoParents() {
        ClassNode a = make(InterfaceCAB)
        ClassNode b = make(InterfaceDAB)
        assert lowestUpperBound(a,b).interfaces as Set == [make(InterfaceA), make(InterfaceB)] as Set
        assert lowestUpperBound(b,a).interfaces as Set == [make(InterfaceA), make(InterfaceB)] as Set
    }

    void testBuildCommonTypeWithTwoInterfacesSharingTwoParentsAndOneDifferent() {
        ClassNode a = make(InterfaceCAB)
        ClassNode b = make(InterfaceDABE)
        assert lowestUpperBound(a,b).interfaces as Set == [make(InterfaceA), make(InterfaceB)] as Set
        assert lowestUpperBound(b,a).interfaces as Set == [make(InterfaceA), make(InterfaceB)] as Set
    }

    void testBuildCommonTypeFromTwoClassesInDifferentBranches() {
        ClassNode a = make(ClassA1)
        ClassNode b = make(ClassB1)
        assert lowestUpperBound(a,b) == make(GroovyObject)
        assert lowestUpperBound(b,a) == make(GroovyObject)
    }

    void testBuildCommonTypeFromTwoClassesInDifferentBranchesAndOneCommonInterface() {
        ClassNode a = make(ClassA1_Serializable)
        ClassNode b = make(ClassB1_Serializable)
        assert lowestUpperBound(a,b).interfaces as Set == [make(Serializable), make(GroovyObject)] as Set
        assert lowestUpperBound(b,a).interfaces as Set == [make(Serializable), make(GroovyObject)] as Set
    }

    void testBuildCommonTypeFromTwoClassesWithCommonSuperClassAndOneCommonInterface() {
        ClassNode a = make(BottomA)
        ClassNode b = make(BottomB)
        ClassNode type = lowestUpperBound(a, b)
        assert type.name =~ /.*Top/
        assert type.superClass == make(Top) // includes interface GroovyObject
        assert type.interfaces as Set == [make(Serializable)] as Set // extra interface
        type = lowestUpperBound(b, a)
        assert type.name =~ /.*Top/
        assert type.superClass == make(Top)
        assert type.interfaces as Set == [make(Serializable)] as Set
    }

    void testStringWithGString() {
        ClassNode a = make(String)
        ClassNode b = make(GString)
        ClassNode type = lowestUpperBound(a,b)
        assert type.interfaces as Set == [make(CharSequence), make(Comparable), make(Serializable)] as Set
    }

    void testCommonAssignableType() {
        def typeA = extractTypesFromCode('LinkedList type').type
        def typeB = extractTypesFromCode('List type').type
        def superType = lowestUpperBound(typeA, typeB)
        assert superType == make(List)
    }

    void testCommonAssignableType2() {
        def typeA = extractTypesFromCode('LinkedHashSet type').type
        def typeB = extractTypesFromCode('List type').type
        def superType = lowestUpperBound(typeA, typeB)
        assert superType == make(Collection)
    }

    void testCommonAssignableTypeWithGenerics() {
        def typeA = extractTypesFromCode('LinkedHashSet<String> type').type
        def typeB = extractTypesFromCode('List<String> type').type
        def superType = lowestUpperBound(typeA, typeB)
        assert superType == make(Collection)
    }

    // ---------- Classes and Interfaces used in this unit test ----------------
    private static interface InterfaceA {}
    private static interface InterfaceB {}
    private static interface InterfaceE {}
    private static interface InterfaceCA extends InterfaceA {}
    private static interface InterfaceDA extends InterfaceA {}
    private static interface InterfaceCAB extends InterfaceA, InterfaceB {}
    private static interface InterfaceDAB extends InterfaceA, InterfaceB {}
    private static interface InterfaceDABE extends InterfaceA, InterfaceB, InterfaceE {}

    private static class ClassA {}
    private static class ClassB {}
    private static class ClassA1 extends ClassA {}
    private static class ClassB1 extends ClassB {}
    private static class ClassA1_Serializable extends ClassA implements Serializable {}
    private static class ClassB1_Serializable extends ClassB implements Serializable {}

    private static class Top {}
    private static class BottomA extends Top implements Serializable {}
    private static class BottomB extends Top implements Serializable {}

}
