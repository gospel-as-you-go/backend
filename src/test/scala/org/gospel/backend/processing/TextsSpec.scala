package org.gospel.backend.processing

import com.themillhousegroup.scoup.Scoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.parser.Tag
import org.jsoup.select.Elements
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar
import org.mockito.integrations.scalatest.MockitoFixture
import org.scalatest.{FlatSpec, FunSuite}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}


class TextsSpec extends FlatSpec with MockitoFixture {
    private implicit val context: ExecutionContextExecutor = ExecutionContext.global

    it should "return an empty string when nothing is found" in {
        // Arrange
        val scoup = mock[Scoup]
        val document = mock[Document]
        val version = "SVX"
        val elements = new Elements()
        scoup.parse(*, *, *) shouldReturn Future {document}
        document.select("p>span.text") shouldReturn elements

        // Act
        val actual = Texts.findTextByRef("Act 1", version)(scoup)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(null).length === 0)
    }

    it should "return a text containing extracted verses" in {
        // Arrange
        val text = "Dopo ciò, vidi un altro angelo discendere dal cielo con grande potere e la terra fu illuminata dal suo splendore."
        val scoup = mock[Scoup]
        val version = "SVX"
        val document = Scoup.parseHTML(s"""
           |<html>
           | <head></head>
           | <body>
           |  <p><span class="text Rev-18-1"><span class="chapternum">18&nbsp;</span>$text</span></p>
           | </body>
           |</html>""".stripMargin)

        scoup.parse(*, *, *) shouldReturn Future {document}

        // Act
        val actual = Texts.findTextByRef("Act 1", version)(scoup)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse("").contains(text))
    }

    it should "return nothing when text node is empty" in {
        // Arrange
        val scoup = mock[Scoup]
        val version = "SVX"
        val document = Scoup.parseHTML(s"""
                                          |<html>
                                          | <head></head>
                                          | <body>
                                          |  <p>
                                          |     <span class="text Rev-18-1"></span>
                                          |     <span class="text Rev-18-2"></span>
                                          |     <span class="text Rev-18-3"></span>
                                          |     <span class="text Rev-18-4"></span>
                                          |     <span class="text Rev-18-5"></span>
                                          |  </p>
                                          | </body>
                                          |</html>""".stripMargin)

        scoup.parse(*, *, *) shouldReturn Future {document}

        // Act
        val actual = Texts.findTextByRef("Act 1", version)(scoup)

        // Assert
        assert (actual.isRight)
        assert (actual.getOrElse(null).contains(""))
    }
}
