package org.gospel.backend.processing

import org.gospel.backend.processing.dto.ReadingsDto
import org.mockito.integrations.scalatest.MockitoFixture
import org.scalatest.FlatSpec

class ReferencesSpec extends FlatSpec with MockitoFixture {
    it should "return simple week references with fixed psalms references" in {
        // Arrange
        val defResult = new ReadingsDto()
        val refs = Seq("Romans 10:9-18", "Psalm  18(19):2-5", "Matthew 4:18-22")

        // Act
        val actual = References.extractReferences(refs)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(defResult).firsts === Seq("Romans 10:9-18"))
        assert (actual.getOrElse(defResult).seconds === Seq.empty)
        assert (actual.getOrElse(defResult).psalms === Seq("Psalm 19:2-5"))
        assert (actual.getOrElse(defResult).gospels === Seq("Matthew 4:18-22"))
    }

    it should "return complex week references with fixed psalms references" in {
        // Arrange
        val defResult = new ReadingsDto()
        val refs = Seq("Ref1", "Ref2", "Psalm  18(19)", "Psalm  19(20)", "Matthew 4", "Matthew 5")

        // Act
        val actual = References.extractReferences(refs)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(defResult).firsts === Seq("Ref1", "Ref2"))
        assert (actual.getOrElse(defResult).seconds === Seq.empty)
        assert (actual.getOrElse(defResult).psalms === Seq("Psalm 19", "Psalm 20"))
        assert (actual.getOrElse(defResult).gospels === Seq("Matthew 4", "Matthew 5"))
    }

    it should "return simple sunday references with fixed psalms references" in {
        // Arrange
        val defResult = new ReadingsDto()
        val refs = Seq("Romans 10:9-18", "Psalm 18(19):2-5", "Romans 11:1-2", "Matthew 4:18-22")

        // Act
        val actual = References.extractReferences(refs)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(defResult).firsts === Seq("Romans 10:9-18"))
        assert (actual.getOrElse(defResult).seconds === Seq("Romans 11:1-2"))
        assert (actual.getOrElse(defResult).psalms === Seq("Psalm 19:2-5"))
        assert (actual.getOrElse(defResult).gospels === Seq("Matthew 4:18-22"))
    }

    it should "return complex references with fixed psalm part" in {
        // Arrange
        val defResult = new ReadingsDto()
        val refs = Seq("Psalm 18(19)--Psalm 20", "Psalm 21", "Matthew 4")

        // Act
        val actual = References.extractReferences(refs)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(defResult).firsts === Seq.empty)
        assert (actual.getOrElse(defResult).seconds === Seq.empty)
        assert (actual.getOrElse(defResult).psalms === Seq("Psalm 19--Psalm 20", "Psalm 21"))
        assert (actual.getOrElse(defResult).gospels === Seq("Matthew 4"))
    }

    it should "return complex sunday references with fixed psalms references" in {
        // Arrange
        val defResult = new ReadingsDto()
        val refs = Seq("Ref1", "Ref2", "Psalm  18(19)", "Psalm  19(20)", "Ref3", "Ref4", "John 4", "Matthew 5")

        // Act
        val actual = References.extractReferences(refs)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(defResult).firsts === Seq("Ref1", "Ref2"))
        assert (actual.getOrElse(defResult).seconds === Seq("Ref3", "Ref4"))
        assert (actual.getOrElse(defResult).psalms === Seq("Psalm 19", "Psalm 20"))
        assert (actual.getOrElse(defResult).gospels === Seq("John 4", "Matthew 5"))
    }
}
