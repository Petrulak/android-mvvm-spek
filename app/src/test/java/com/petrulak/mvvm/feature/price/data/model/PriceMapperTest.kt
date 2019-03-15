package com.petrulak.mvvm.feature.price.data.model

import com.petrulak.mvvm.common.Mapper.Companion.EMPTY_STRING
import com.petrulak.mvvm.common.Mapper.Companion.INVALID_FLOAT
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

object PriceMapperTest : Spek({

    describe("PriceMapper") {

        val mapper by memoized { PriceMapper() }

        describe("map") {

            context("source values are not null") {

                val source = PriceDto.newMock()
                val mapped = mapper.map(source)

                it("should map to proper values") {
                    assertEquals(mapped.code, source.code)
                    assertEquals(mapped.description, source.description)
                    assertEquals(mapped.rate, source.rate)
                    assertEquals(mapped.symbol, source.symbol)
                }
            }

            context("source values are null") {

                val original = PriceDto.newNullMock()
                val mapped = mapper.map(original)

                it("should fallback to defaults") {
                    assertEquals(mapped.code, EMPTY_STRING)
                    assertEquals(mapped.description, EMPTY_STRING)
                    assertEquals(mapped.rate, INVALID_FLOAT)
                    assertEquals(mapped.symbol, EMPTY_STRING)
                }
            }
        }
    }
})