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

package dev.kobalt.csv2i18n.web.legal

import dev.kobalt.csv2i18n.web.resource.ResourceRepository

object LegalRepository {

    val pageTitle = "Legal"
    val pageSubtitle = "To make sure things are fair as much as possible."
    val pageContent = ResourceRepository.getText("legal.md")!!
    val pageRoute = "legal/"

}