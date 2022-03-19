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

package dev.kobalt.csv2i18n.web.download

import dev.kobalt.csv2i18n.web.convert.ConvertRepository
import dev.kobalt.csv2i18n.web.extension.pageArticle
import dev.kobalt.csv2i18n.web.extension.pageMarkdown
import dev.kobalt.csv2i18n.web.extension.respondHtmlContent
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*
import java.io.File

fun Route.downloadRoute() {
    route("download/") {
        get {
            call.respondHtmlContent(
                title = DownloadRepository.pageTitle,
                description = DownloadRepository.pageSubtitle
            ) {
                pageArticle(
                    DownloadRepository.pageTitle,
                    DownloadRepository.pageSubtitle
                ) {
                    pageMarkdown(DownloadRepository.pageContent)
                }
            }
        }
        static {
            file("csv2i18n.jar", File(ConvertRepository.jarPath!!))
        }
    }
}
