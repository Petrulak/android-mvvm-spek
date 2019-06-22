package com.petrulak.mvvm

import com.petrulak.mvvm.MyEnum.*
import com.petrulak.mvvm.feature.price.data.BitCoinPriceRepositoryType
import com.petrulak.mvvm.feature.price.data.source.BitCoinPriceLocalSource
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Skip
import org.spekframework.spek2.lifecycle.CachingMode
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals


object SpecificationTest : Spek({

    val someProperty by memoized { listOf("1", "2") }

    Feature("some feature") {

        Scenario("getting the list size") {

            var listSize: Int = -1

            When("getting a list size") {
                listSize = someProperty.size
            }

            Then("should equal 2") {
                assertEquals(listSize, 2)
            }
        }
    }

    val dynamicExclude = if (!BuildConfig.DEBUG) Skip.No else Skip.Yes("reason here")

    describe("some name", skip = dynamicExclude) {

        it("some assertion") {

        }
    }

    describe("some other name") {

        it("some other assertion", skip = dynamicExclude) {

        }

        it("some other assertion 2") {

        }
    }
})


object FlowTest : Spek({

    describe("Group 1") {

        before {
            println("Before G1")
        }

        context("Condition 1") {

            beforeEach {
                println("G1 C1 before test")
            }

            it("G1 C1 test 1") {
                println("G1 C1 test 1")
            }

            it("G1 C1 test 2") {
                println("G1 C1 test 1")
            }

        }

        context("Condition 2") {
            it("G1 C2 test 1") {
                println("G1 C2 test 1")
            }
        }

        after {
            println("After G1")
        }
    }

})


object HelloWorldTest : Spek({

    group("main group") {

        group("sub group 1") {

            val string = "abcde"

            test("size should be 5") {
                assertEquals(string.length, 5)
            }
        }


    }
})

enum class MyEnum { FIRST, SECOND, THIRD, OTHER }

fun thaBrain(input: Int) = when (input) {
    1    -> FIRST
    2    -> SECOND
    3    -> THIRD
    else -> OTHER
}


object ParametrizedTest : Spek({

    describe("thaBrain") {

        listOf(
            1 to FIRST,
            2 to SECOND,
            3 to THIRD,
            -1 to OTHER
        ).forEach {
            it("${it.first} : should map to proper value") {
                assertEquals(thaBrain(it.first), it.second)
            }
        }
    }
})

object HelloWorld : Spek({

    val mock by memoized { mock(BitCoinPriceRepositoryType::class.java) }

    group("A") {

        group("B") {

            test("test 1") {
                println("mock : $mock")
            }

            test("test 2") {
                println("mock : $mock")
            }
        }

        test("test 3") {
            println("mock : $mock")
        }
    }
})


object TestTest : Spek({

    val abc by memoized(CachingMode.GROUP) { BitCoinPriceLocalSource() }

    beforeEachTest {
        println("beforeEachTest")
    }

    afterEachTest {
        println("afterEachTest")
    }

    beforeGroup {
        println("beforeGroup")
    }

    afterGroup {
        println("afterGroup")
    }

    describe("test suite 1") {

        println("TS 1")

        context("test suite 1 : scenario 1") {

            println("TS 1 : scenario 1")

            it("test suite 1 : scenario 1 : assertions 1") {
                println("TS 1: scenario 1 : assertions 1" + abc)
            }

            it("test suite 1 : scenario 1 : assertions 2") {

                println("TS 1: scenario 1 : assertions 2" + abc)
            }
        }
    }

    describe("test suite 2") {

        println("TS 2")

        describe("test suite 2 : scenario 1") {

            println("TS 2 : scenario 1")

            it("test suite 2 : scenario 1 : assertions 1") {

                println("TS 2: scenario 1 : assertions 1" + abc)
            }

            it("test suite 2 : scenario 1 : assertions 2") {

                println("TS 2: scenario 1 : assertions 2" + abc)
            }
        }
    }

})

object Abc : Spek({


    beforeGroup {
        println("beforeGroup")
    }

    describe("registration") {

        println("describe : registration")


        context("user name is valid") {

            println("context : user name is valid")

            beforeEachTest {
                println("beforeEachTest")
            }

            it("should navigate to next screen") {
                println("should navigate to next screen")
            }

            it("should show a dialog") {
                println("hould show a dialog")
            }

            afterEachTest {
                println("afterEachTest")
            }
        }

        context("user name is NOT valid") {

            println("context : user name is  NOTvalid")


            it("should navigate to next screen") {
                println("should navigate to next screen")
            }

            it("should show a dialog") {
                println("hould show a dialog")
            }

        }

    }

    afterGroup {
        println("afterGroup")
    }
})