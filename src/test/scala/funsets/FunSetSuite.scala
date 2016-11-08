package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = union(s1, s2)
    val s5 = union(union(s1, s2), s3)
    val s6 = singletonSet(-10)
    val s7 = singletonSet(-20)
    val s8 = union(s6, s7)
    val s9 = union(s8, s5)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      printSet(s2)
      assert(contains(s2, 2), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      printSet(s)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains only elements from both set") {
    new TestSets {
      val s = intersect(s1, s4)
      printSet(s)
      assert(contains(s, 1), "Intersect 1")
      assert(!contains(s, 2), "Intersect 2")
    }
  }

  test("diff contains only elements from first set that's not in second set") {
    new TestSets {
      val t = diff(s5, s4)
      val s = diff(s4, s5)
      printSet(t)
      printSet(s)
      assert(contains(t, 3), "Diff 3")
      assert(!contains(t, 2), "Diff 2")
      assert(!contains(s, 3), "Diff 3")
    }
  }

  test("filter contains elements satisfy the filter predicate") {
    new TestSets {
      val t = filter(s4, {x: Int => x == 1})
      val t2 = filter(s9, {x: Int => x < 0})
      printSet(t)
      printSet(t2)
      assert(contains(t, 1), "filter 1")
      assert(!contains(t, 2), "filter 2")
      assert(contains(t2, -10), "filter 10")
      assert(!contains(t2, 3), "filter 3")
    }
  }

  test("forall contains all elements satisfy the filter predicate") {
    new TestSets {
      assert(forall(s5, {x: Int => x > 0}), "forall > 0")
      assert(forall(s5, {x: Int => x < 5}), "forall < 5")
      assert(!forall(s5, {x: Int => x < 3}), "forall < 3")
      assert(!forall(s5, {x: Int => contains(s5, 10)}), "forall 10")
    }
  }

  test("exist contains at least an element satisfy the filter predicate") {
    new TestSets {
      assert(exists(s4, {x: Int => x < 5}), "exist < 5")
      assert(exists(s5, {x: Int => x == 1}), "exist 1")
      assert(!exists(s5, {x: Int => x == -1}), "exist 1")
      assert(exists(s9, {x: Int => x < 0}), "exist  < 0")
    }
  }

  test("map contains elements satisfy the filter predicate") {
    new TestSets {
      val m = map(s5, {x: Int => x - 10})
      val m2 = map(s5, {x: Int => x - 1002})
      val m3 = map(s9, {x: Int => x * 1000})
      printSet(m)
      printSet(m2)
      printSet(m3)
      assert(contains(m, -9), "map -9")
      assert(contains(m, -7), "map -7")
      assert(contains(m2, -1000), "map -1000")
      assert(contains(m2, -1001), "map -1001")
      assert(contains(m3, 2000), "map 2000")
      assert(!contains(m3, 0), "map 0")
    }
  }
}
