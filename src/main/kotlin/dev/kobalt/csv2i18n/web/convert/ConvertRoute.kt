/*
 * dev.kobalt.csv2i18n
 * Copyright (C) 2022 Tom.K
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.kobalt.csv2i18n.web.convert

import dev.kobalt.csv2i18n.web.extension.*
import dev.kobalt.csv2i18n.web.html.LimitedSizeInputStream
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import java.io.ByteArrayOutputStream


fun Route.convertRoute() {
    route("convert/") {
        get {
            call.respondHtmlContent(
                title = ConvertRepository.pageTitle,
                description = ConvertRepository.pageSubtitle
            ) {
                pageArticle(
                    ConvertRepository.pageTitle,
                    ConvertRepository.pageSubtitle
                ) {
                    h3 { text("File Form") }
                    form {
                        method = FormMethod.post
                        encType = FormEncType.multipartFormData
                        name = "messagebox"
                        div {
                            pageInputFile("File", "input")
                            br { }
                            pageInputSubmit("Begin conversion", "submit")
                        }
                    }
                    h3 { text("Text Form") }
                    form {
                        method = FormMethod.post
                        encType = FormEncType.multipartFormData
                        name = "messagebox"
                        div {
                            pageInputTextBox("Text", "input")
                            br { }
                            pageInputSubmit("Begin conversion", "submit")
                        }
                    }
                    pageMarkdown(ConvertRepository.pageContent)
                }
            }
        }
        post {
            runCatching {
                val data = when (val part = call.receiveMultipart().readAllParts().find { it.name == "input" }) {
                    is PartData.FileItem -> LimitedSizeInputStream(part.streamProvider(), 500 * 1024).readBytes()
                        .decodeToString()
                    is PartData.FormItem -> part.value.takeIf { it.length < 500 * 1024 } ?: throw Exception()
                    else -> throw Exception()
                }
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "output.zip")
                        .toString()
                )
                call.respondBytes(
                    contentType = ContentType.Application.Zip,
                    status = HttpStatusCode.OK,
                    bytes = ByteArrayOutputStream().use {
                        ConvertRepository.submit(data, it)
                        it.toByteArray()
                    }
                )
            }.getOrElse {
                call.respondHtmlContent(
                    title = ConvertRepository.pageTitle,
                    description = ConvertRepository.pageSubtitle
                ) {
                    pageArticle(
                        ConvertRepository.pageTitle,
                        ConvertRepository.pageSubtitle
                    ) {
                        h3 { text("Failure") }
                        p { text("Conversion process was not successful.") }
                    }
                }
            }
        }
    }
}

