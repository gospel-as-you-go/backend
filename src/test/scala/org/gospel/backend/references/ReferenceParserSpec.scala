package org.gospel.backend.references

import org.mockito.integrations.scalatest.MockitoFixture
import org.scalatest.FlatSpec

import scala.collection.SortedMap

class ReferenceParserSpec extends FlatSpec with MockitoFixture {

    it should "parse mixed bible references inter alia with intervals of more than 2 chapters" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "isaiah 52 :7,12 -- 53: 2; 53:5-7; 54:15--55:4 "

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Isaiah", SortedMap(
            (52,List(7, 12, 13, 14, 15)),
            (53,List(1, 2, 5, 6, 7)),
            (54,List(15, 16, 17)),
            (55,List(1, 2, 3, 4))
        )))
    }

    it should "parse bible references with intervals of more than 2 chapters" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "isaiah 52 :7,12 -- 55: 4"

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Isaiah", SortedMap(
            (52,List(7, 12, 13, 14, 15)),
            (53,List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
            (54,List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)),
            (55,List(1, 2, 3, 4))
        )))
    }

    it should "parse very simple bible reference with chapter and verse" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "Romans 10:9"

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Romans", SortedMap(10 -> List(9))))
    }
    it should "parse very simple bible reference with multiple words in book title" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "1 john 1:5"

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("1 John", SortedMap(1 -> List(5))))
    }

    it should "parse very simple bible references with few chapters and verses" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "Romans 10:3; 10:4; 12:5"

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Romans", SortedMap(
            10 -> List(3, 4),
            12 -> List(5),
        )))
    }

    it should "parse bible references with few chapters and sequences of verses" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "Romans 10:3,4, 5, 6; 10:7,  8; 12:5"

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Romans", SortedMap(
            10 -> List(3, 4, 5, 6, 7, 8),
            12 -> List(5),
        )))
    }

    it should "parse bible references with intervals of verses" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "Romans 10:3-6; 10:7 - 8; 12:5"

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Romans", SortedMap(
            10 -> List(3, 4, 5, 6, 7, 8),
            12 -> List(5),
        )))
    }

    it should "parse jumbled bible reference" in {
        // Arrange
        val sut = new ReferenceParser
        val reference = "  roManS   10:9  "

        // Act
        val actual = sut.parse(reference)

        // Assert
        assert(actual.isRight)
        assert(actual.toList.head === Reference("Romans", SortedMap(10 -> List(9))))
    }
}
